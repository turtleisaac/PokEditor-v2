package com.turtleisaac.pokeditor.editors.scripts.gen4;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;
import java.io.*;
import java.util.*;

public class ScriptEditorGen4
{
    public static void main(String[] args) throws IOException
    {
        ScriptEditorGen4 scriptEditor= new ScriptEditorGen4("cpue");
//        scriptEditor.scriptsToCsv("scr_seqHG");
        scriptEditor.readScriptTable("arm9.bin");
    }

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
    private ScriptCommands scriptCommands;
    private final String gameCode;

    private static final String black= "\u001b[30m";
    private static final String red= "\u001b[31m";
    private static final String green= "\u001b[32m";
    private static final String yellow= "\u001b[33m";
    private static final String blue= "\u001b[34m";
    private static final String magenta= "\u001b[35m";
    private static final String cyan= "\u001b[36m";
    private static final String white= "\u001b[37m";
    private static final String reset= "\u001b[0m";



    public ScriptEditorGen4(String gameCode) throws IOException {
        String entryPath = resourcePath + "EntryData.txt";
        String movePath = resourcePath + "MoveList.txt";

        this.gameCode= gameCode;

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

        scriptCommands= new ScriptCommands(gameCode);
    }

    public void scriptsToCsv(String scriptsDir) throws IOException {
        dataPath += scriptsDir;

        Buffer buffer;
        ArrayList<ScriptData> dataList = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for (int i= 0; i < files.length; i++)
        {
            file= files[i];
//            BLZCoder blzCoder= new BLZCoder("-d",file.getPath());
            System.out.println(cyan + "\nScript file " + i + ":\n" + reset);
            buffer = new Buffer(file.toString());
            ArrayList<Long> scriptOffsets= new ArrayList<>();

            long num;
            try
            {
                while((num= buffer.readUIntS()) != 0xfd13)
                {
                    num|= buffer.readUIntS() << 16;
                    scriptOffsets.add(num + scriptOffsets.size()*4 + 0x4);
//                    System.out.println("    0x" + Integer.toHexString((int)num));
                }

                for(int x= 0; x < scriptOffsets.size(); x++)
                {
                    buffer.close();
                    buffer = new Buffer(file.toString());
                    buffer.skipTo(scriptOffsets.get(x));
                    int commandIdx;

                    while((commandIdx= buffer.readUIntS()) != 0x2)
                    {
                        ScriptData command= scriptCommands.getScript(commandIdx);
//                        System.out.print(yellow + "(0x" + Integer.toHexString(commandIdx) + ") " + reset);
                        System.out.print("  " + yellow + command.getName() + reset + " ");
                        for(int val : command.getParamBytes())
                        {
                            switch (val)
                            {
                                case 1 :
                                    System.out.print(blue + "0x" + Integer.toHexString(buffer.readUShortB()) + reset + " ");
                                    break;

                                case 2 :
                                    System.out.print(blue + "0x" + Integer.toHexString(buffer.readUIntS()) + reset + " ");
                                    break;

                                case 4 :
                                    System.out.print(blue + "0x" + Long.toHexString(buffer.readUIntI()) + reset + " ");
                                    break;

                                default:
                                    break;
                            }
                        }
                        System.out.println();
                    }
                    System.out.println("  " + red + scriptCommands.getScript(commandIdx).getName() + reset + "\n");
                }
            }
            catch (RuntimeException e)
            {
                System.err.println("Error: Level Script?");
            }



        }
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "scriptsData.csv"));
//        writer.write("ID Number,Name,\n"); //header in spreadsheet output
//        String line;
//        for (int row = 0; row < dataList.size(); row++) {
//            line = row + "," + scriptsData[row] + ",";
//            for (int col = 0; col < scriptsTable[0].length; col++) {
//                line += scriptsTable[row][col] + ",";
//            }
//            line += "\n";
//            writer.write(line);
//        }
//        writer.close();
    }


    public void csvTo(String scriptsCsv, String outputDir) throws IOException {
        String scriptsPath = path + scriptsCsv;
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

        CsvReader csvReader = new CsvReader(scriptsPath);
        BinaryWriter writer;
        for (int i = 0; i < csvReader.length(); i++) {
            initializeIndex(csvReader.next());
            writer = new BinaryWriter(outputPath + i + ".bin");

            writer.close();
        }

    }



    public void readScriptTable(String arm9)
    {
        int offsetTablePointer;

        switch(gameCode.substring(0,3).toLowerCase())
        {
            case "cpu":
                offsetTablePointer= 0x03eaf0;
                break;

            case "ada":
                offsetTablePointer= 0x00;
                break;

            case "apa":
                offsetTablePointer= 0x01;
                break;

            case "ipk":
                offsetTablePointer= 0x02;
                break;

            case "ipg":
                offsetTablePointer= 0x03;

            default:
                throw new RuntimeException("Unsupported game");
        }

        Buffer buffer= new Buffer(path + arm9);
        buffer.skipTo(offsetTablePointer);

        int offsetTableAddress= buffer.readInt() & 0xffffff;
        buffer.skipTo(offsetTableAddress);

        int[][] scriptTable= new int[839][2];

        for(int i= 0; i < scriptTable.length; i++)
        {
            scriptTable[i][0]= (buffer.readInt() & 0xffffff) + i*4;
            scriptTable[i][1]= i;
        }

        Arrays.sort(scriptTable, Comparator.comparingDouble(o -> o[0]));

        for(int i= 0; i < 839; i++)
        {
            System.out.println("\nScript " + scriptTable[i][1] + ": " + scriptCommands.getScript(i).getName());
            System.out.println("    Position: 0x" + Integer.toHexString(scriptTable[i][0]));
            System.out.println("    Length: 0x" + Integer.toHexString(scriptTable[i+1][0] - scriptTable[i][0]));
        }
    }

    private void sort(File[] arr) {
        Arrays.sort(arr, Comparator.comparingInt(ScriptEditorGen4::fileToInt));
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
