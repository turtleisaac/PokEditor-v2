package com.turtleisaac.pokeditor.editors.pokemart;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;

import java.io.*;
import java.util.*;

public class MartEditor
{
    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath = path;
    private static String resourcePath = path + "Program Files" + File.separator;
    private static final String[] typeArr = {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fire", "Water", "Grass", "Electric", "Psychic", "Ice", "Dragon", "Dark", "Fairy"};
    private static final String[] categories = {"Physical", "Special", "Status"};
    private static final String[] statusArr = {"None", "Sleep", "Poison", "Burn", "Freeze", "Paralysis", "Confusion", "Infatuation"};
    private static String[] nameData;
    private static String[] moveData;
    private static String[] effects;
    private static String[] flags;
    private static String[] targets;

    public MartEditor() throws IOException
    {
        String entryPath = resourcePath + "EntryData.txt";
        String movePath = resourcePath + "MoveList.txt";


        BufferedReader reader = new BufferedReader(new FileReader(entryPath));
        ArrayList<String> nameList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            nameList.add(line);
        }
        nameData = nameList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(movePath));
        ArrayList<String> moveList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            moveList.add(line);
        }
        moveData = moveList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(resourcePath + "Effects.txt"));
        ArrayList<String> effectList = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            effectList.add(line);
        }
        effects = effectList.toArray(new String[0]);
        reader.close();


        flags = new String[500];
        for (int i = 0; i < flags.length; i++) {
            flags[i] = "" + i;
        }

        targets = new String[0x81];
        targets[0] = "One opponent";
        targets[1] = "Automatic";
        targets[2] = "Random";
        targets[4] = "Both opponents";
        targets[8] = "Both opponents and ally";
        targets[16] = "User";
        targets[32] = "User's side of field";
        targets[64] = "Entire field";
        targets[128] = "Opponent's side of field";
    }

    public void martToCsv(String martDir) throws IOException {
        dataPath += martDir;

        Buffer buffer;
        ArrayList<MartData> dataList = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        for (File file : files)
        {
            buffer = new Buffer(file.toString());

            dataList.add(new MartData() {
                @Override
                public int test() {
                    return 0;
                }
            });
        }


//        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "martData.csv"));
//        writer.write("ID Number,Name,\n"); //header in spreadsheet output
//        String line;
//        for (int row = 0; row < dataList.size(); row++) {
//            line = row + "," + martData[row] + ",";
//            for (int col = 0; col < martTable[0].length; col++) {
//                line += martTable[row][col] + ",";
//            }
//            line += "\n";
//            writer.write(line);
//        }
//        writer.close();
    }


    public void csvToMart(String martCsv, String outputDir) throws IOException {
        String martPath = path + martCsv;
        String outputPath;

        if (outputDir.contains("Recompile")) {
            outputPath = path + "temp" + File.separator + outputDir;
        } else {
            outputPath = path + File.separator + outputDir;
        }

        if (!new File(outputPath).exists() && !new File(outputPath).mkdir()) {
            throw new RuntimeException("Could not create output directory. Check write permissions");
        }
        outputPath += File.separator;

        CsvReader csvReader = new CsvReader(martPath);
        BinaryWriter writer;
        for (int i = 0; i < csvReader.length(); i++) {
            initializeIndex(csvReader.next());
            writer = new BinaryWriter(outputPath + i + ".bin");

            writer.close();
        }

    }

    private void sort(File arr[]) {
        Arrays.sort(arr, Comparator.comparingInt(MartEditor::fileToInt));
    }

    private static int fileToInt(File f) {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private int arrIdx;
    private String[] input;

    private void initializeIndex(String[] arr) {
        arrIdx = 0;
        input = arr;
    }

    private String next() {
        try {
            return input[arrIdx++];
        } catch (IndexOutOfBoundsException e) {
            return "";
        }
    }

    private static byte getType(String type) {
        for (int i = 0; i < typeArr.length; i++) {
            if (type.equals(typeArr[i])) {
                return (byte) i;
            }
        }

        throw new RuntimeException("Invalid type entered: " + type);
    }

    private static int getMove(String move) {
        for (int i = 0; i < moveData.length; i++) {
            if (move.equals(moveData[i])) {
                return i;
            }
        }
        throw new RuntimeException("Invalid move entered: " + move);
    }

    private static short getEffect(String effect) {
        for (int i = 0; i < effects.length; i++) {
            if (effect.equals(effects[i])) {
                return (short) i;
            }
        }
        throw new RuntimeException("Invalid effect entered: " + effect);
    }

    private static byte getCategory(String category) {
        for (int i = 0; i < categories.length; i++) {
            if (category.equals(categories[i])) {
                return (byte) i;
            }
        }
        throw new RuntimeException("Invalid category entered: " + category);
    }

    private static short getTargets(String target) {
        for (int i = 0; i < targets.length; i++) {
            if (target.equals(targets[i])) {
                return (byte) i;
            }
        }
        throw new RuntimeException("Invalid target(s) entered: " + target);
    }


}
