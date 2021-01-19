package com.turtleisaac.pokeditor.editors.trainers.gen4;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;

import java.io.*;
import java.util.*;

public class TrainerEditorGen4
{
    public static void main(String[] args) throws IOException
    {
        TrainerEditorGen4 trainerEditor= new TrainerEditorGen4();
        trainerEditor.trainersToCsv("trdata_pl","trpoke_pl");
    }

    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath = path;
    private static String resourcePath = path + "Program Files" + File.separator;
    private static final String[] typeArr = {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fire", "Water", "Grass", "Electric", "Psychic", "Ice", "Dragon", "Dark", "Fairy"};
    private static final String[] categories = {"Physical", "Special", "Status"};
    private static final String[] statusArr = {"None", "Sleep", "Poison", "Burn", "Freeze", "Paralysis", "Confusion", "Infatuation"};
    private static String game;
    private static String[] nameData;
    private static String[] moveData;
    private static String[] effects;
    private static String[] flags;
    private static String[] targets;
    private static String[] tmData;
    private static String[] itemData;
    private static String[] abilityData;
    private static String[] tmNameData;

    public TrainerEditorGen4() throws IOException
    {
        Scanner scanner= new Scanner(System.in);
        System.out.println("Diamond, Pearl, Platinum, HeartGold, or SoulSilver?");
        game= scanner.nextLine().toLowerCase();


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

        reader = new BufferedReader(new FileReader(resourcePath + "TmList.txt"));
        ArrayList<String> tmList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            tmList.add(line);
        }
        tmData = tmList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(resourcePath + "ItemList.txt"));
        ArrayList<String> itemList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            itemList.add(line);
        }
        itemData = itemList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(resourcePath + "AbilityList.txt"));
        ArrayList<String> abilityList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            abilityList.add(line);
        }
        abilityData = abilityList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(resourcePath + "TmNameList.txt"));
        ArrayList<String> tmNameList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            tmNameList.add(line);
        }
        tmNameData = tmNameList.toArray(new String[0]);
        reader.close();

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

    public void trainersToCsv(String trainerDir, String trainerPokemonDir) throws IOException {
        dataPath += trainerDir;

        Buffer buffer;
        ArrayList<TrainerDataGen4> dataList = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        for (File file : files)
        {
            buffer = new Buffer(file.toString());

            short flag= buffer.readUShortB();
            short trainerClass= buffer.readUShortB();
            short battleType= buffer.readUShortB();
            short numPokemon= buffer.readUShortB();

            int item1= buffer.readUIntS();
            int item2= buffer.readUIntS();
            int item3= buffer.readUIntS();
            int item4= buffer.readUIntS();

            long ai= buffer.readUIntI();
            short battleType2= buffer.readUShortB();
            short unknown1= buffer.readUShortB();
            short unknown2= buffer.readUShortB();
            short unknown3= buffer.readUShortB();

            dataList.add(new TrainerDataGen4() {
                @Override
                public short getFlag() {
                    return flag;
                }

                @Override
                public short getTrainerClass() {
                    return trainerClass;
                }

                @Override
                public short getBattleType() {
                    return battleType;
                }

                @Override
                public short getNumPokemon() {
                    return numPokemon;
                }

                @Override
                public int getItem1() {
                    return item1;
                }

                @Override
                public int getItem2() {
                    return item2;
                }

                @Override
                public int getItem3() {
                    return item3;
                }

                @Override
                public int getItem4() {
                    return item4;
                }

                @Override
                public long getAI() {
                    return ai;
                }

                @Override
                public short getBattleType2() {
                    return battleType2;
                }

                @Override
                public short getUnknown1() {
                    return unknown1;
                }

                @Override
                public short getUnknown2() {
                    return unknown2;
                }

                @Override
                public short getUnknown3() {
                    return unknown3;
                }
            });
        }

        dataPath= path + trainerPokemonDir;

        ArrayList<ArrayList<TrainerPokemonData>> trainerPokemonData= new ArrayList<>();

        List<File> fileList2 = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList2.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files2 = fileList2.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files2); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for(int i= 0; i < files2.length; i++)
        {
            ArrayList<TrainerPokemonData> thisTrainer= new ArrayList<>();
            System.out.print(i + ": ");
            file= files2[i];
            buffer= new Buffer(file.toString());
            TrainerDataGen4 trainerData= dataList.get(i);
            short flag= trainerData.getFlag();
            short numPokemon= trainerData.getNumPokemon();


            System.out.println(numPokemon + " Pokemon, Flag: " + flag);
            if(flag == 0) //no defined moveset and no defined held items
            {
                for(int x= 0; x < numPokemon; x++)
                {
                    try
                    {
                        short ivs= (short)buffer.readByte();
                        byte abilitySlot= (byte)buffer.readByte();
                        short level= buffer.readShort();

                        int thisPokemon= buffer.readUIntS();
                        int species= thisPokemon & 0x3ff;
                        int altForm= thisPokemon >> 10;

                        short ballSeal= 0;
//                        if(buffer.getLength() % 6 != 0 || buffer.getLength() % 6 == 2)
//                        {
//                            ballSeal= (short)buffer.readByte();
//                        }
                        switch (game)
                        {
                            case "platinum" :
                            case "heartgold" :
                            case "soulsilver" :
                                ballSeal= buffer.readShort();
                                break;

                            case "diamond" :
                            case "pearl" :
                                ballSeal= (short)buffer.readByte();
                                break;
                        }


                        System.out.println(nameData[species] + ":");
                        System.out.println("    Species Ability #: " + abilitySlot);
                        System.out.println("    IVs: " + ivs);
                        System.out.println("    Level: " + level);
                        System.out.println("    Alternate Form #: " + altForm);
                        System.out.println("    Ball Seal: " + ballSeal);

                        short finalBallSeal = ballSeal;
                        thisTrainer.add(new TrainerPokemonData() {
                            @Override
                            public short getIvs() {
                                return ivs;
                            }

                            @Override
                            public short getAbility() {
                                return abilitySlot;
                            }

                            @Override
                            public int getLevel() {
                                return level;
                            }

                            @Override
                            public int getPokemon() {
                                return species;
                            }

                            @Override
                            public int getAltForm() {
                                return altForm;
                            }

                            @Override
                            public short getBallCapsule() {
                                return finalBallSeal;
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println("PROBLEM");
                    }

                }
            }
            else if(flag == 1) //defined moveset
            {
                for(int x= 0; x < numPokemon; x++)
                {
                    try
                    {
                        short ivs= (short)buffer.readByte();
                        byte abilitySlot= (byte)buffer.readByte();
                        short level= buffer.readShort();

                        int thisPokemon= buffer.readUIntS();
                        int species= thisPokemon & 0x3ff;
                        int altForm= thisPokemon >> 10;

                        int move1= buffer.readUIntS();
                        int move2= buffer.readUIntS();
                        int move3= buffer.readUIntS();
                        int move4= buffer.readUIntS();

                        short ballSeal= 0;
                        switch (game)
                        {
                            case "platinum" :
                            case "heartgold" :
                            case "soulsilver" :
                                ballSeal= buffer.readShort();
                                break;

                            case "diamond" :
                            case "pearl" :
                                ballSeal= (short)buffer.readByte();
                                break;
                        }



                        System.out.println(nameData[species] + ":");
                        System.out.println("    Species Ability #: " + abilitySlot);
                        System.out.println("    IVs: " + ivs);
                        System.out.println("    Level: " + level);
                        System.out.println("    Alternate Form #: " + altForm);
                        System.out.println("    Moves: " + moveData[move1] + ", " + moveData[move2] + ", " + moveData[move3] + ", " + moveData[move4]);
                        System.out.println("    Ball Seal: " + ballSeal);

                        short finalBallSeal = ballSeal;
                        thisTrainer.add(new TrainerPokemonData.TrainerPokemonData1() {
                            @Override
                            public short getIvs() {
                                return ivs;
                            }

                            @Override
                            public short getAbility() {
                                return abilitySlot;
                            }

                            @Override
                            public int getLevel() {
                                return level;
                            }

                            @Override
                            public int getPokemon() {
                                return species;
                            }

                            @Override
                            public int getAltForm() {
                                return altForm;
                            }

                            @Override
                            public int getMove1() {
                                return move1;
                            }

                            @Override
                            public int getMove2() {
                                return move2;
                            }

                            @Override
                            public int getMove3() {
                                return move3;
                            }

                            @Override
                            public int getMove4() {
                                return move4;
                            }

                            @Override
                            public short getBallCapsule() {
                                return finalBallSeal;
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println("PROBLEM");
                    }

                }
            }
            else if(flag == 2) //defined held items
            {
                for(int x= 0; x < numPokemon; x++)
                {
                    try
                    {
                        short ivs= (short)buffer.readByte();
                        byte abilitySlot= (byte)buffer.readByte();
                        short level= buffer.readShort();

                        int thisPokemon= buffer.readUIntS();
                        int species= thisPokemon & 0x3ff;
                        int altForm= thisPokemon >> 10;

                        int item= buffer.readUIntS();

                        short ballSeal= 0;
                        switch (game)
                        {
                            case "platinum" :
                            case "heartgold" :
                            case "soulsilver" :
                                ballSeal= buffer.readShort();
                                break;

                            case "diamond" :
                            case "pearl" :
                                ballSeal= (short)buffer.readByte();
                                break;
                        }

                        System.out.println(nameData[species] + ":");
                        System.out.println("    Species Ability #: " + abilitySlot);
                        System.out.println("    IVs: " + ivs);
                        System.out.println("    Level: " + level);
                        System.out.println("    Alternate Form #: " + altForm);
                        System.out.println("    Item: " + itemData[item]);
                        System.out.println("    Ball Seal: " + ballSeal);

                        short finalBallSeal = ballSeal;
                        thisTrainer.add(new TrainerPokemonData.TrainerPokemonData2() {
                            @Override
                            public short getIvs() {
                                return ivs;
                            }

                            @Override
                            public short getAbility() {
                                return abilitySlot;
                            }

                            @Override
                            public int getLevel() {
                                return level;
                            }

                            @Override
                            public int getPokemon() {
                                return species;
                            }

                            @Override
                            public int getAltForm() {
                                return altForm;
                            }

                            @Override
                            public int getItem() {
                                return item;
                            }

                            @Override
                            public short getBallCapsule() {
                                return finalBallSeal;
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println("PROBLEM");
                    }
                }
            }
            else if(flag == 3) //defined moveset and defined held items
            {
                for(int x= 0; x < numPokemon; x++)
                {
                    try
                    {
                        short ivs= (short)buffer.readByte();
                        byte abilitySlot= (byte)buffer.readByte();
                        short level= buffer.readShort();

                        int thisPokemon= buffer.readUIntS();
                        int species= thisPokemon & 0x3ff;
                        int altForm= thisPokemon >> 10;

                        int item= buffer.readUIntS();

                        int move1= buffer.readUIntS();
                        int move2= buffer.readUIntS();
                        int move3= buffer.readUIntS();
                        int move4= buffer.readUIntS();



                        short ballSeal= 0;
                        switch (game)
                        {
                            case "platinum" :
                            case "heartgold" :
                            case "soulsilver" :
                                ballSeal= buffer.readShort();
                                break;

                            case "diamond" :
                            case "pearl" :
                                ballSeal= (short)buffer.readByte();
                                break;
                        }

                        System.out.println(nameData[species] + ":");
                        System.out.println("    Species Ability #: " + abilitySlot);
                        System.out.println("    IVs: " + ivs);
                        System.out.println("    Level: " + level);
                        System.out.println("    Alternate Form #: " + altForm);
                        System.out.println("    Item: " + itemData[item]);
                        System.out.println("    Moves: " + moveData[move1] + ", " + moveData[move2] + ", " + moveData[move3] + ", " + moveData[move4]);
                        System.out.println("    Ball Seal:  " + ballSeal);

                        short finalBallSeal = ballSeal;
                        thisTrainer.add(new TrainerPokemonData.TrainerPokemonData3() {
                            @Override
                            public short getIvs() {
                                return ivs;
                            }

                            @Override
                            public short getAbility() {
                                return abilitySlot;
                            }

                            @Override
                            public int getLevel() {
                                return level;
                            }

                            @Override
                            public int getPokemon() {
                                return species;
                            }

                            @Override
                            public int getAltForm() {
                                return altForm;
                            }

                            @Override
                            public int getItem() {
                                return item;
                            }

                            @Override
                            public int getMove1() {
                                return move1;
                            }

                            @Override
                            public int getMove2() {
                                return move2;
                            }

                            @Override
                            public int getMove3() {
                                return move3;
                            }

                            @Override
                            public int getMove4() {
                                return move4;
                            }

                            @Override
                            public short getBallCapsule() {
                                return finalBallSeal;
                            }
                        });
                    }
                    catch (Exception e)
                    {
                        System.out.println("PROBLEM");
                    }

                }
            }
            else
            {
                throw new RuntimeException("Invalid file type declaration");
            }
            System.out.println();

            trainerPokemonData.add(thisTrainer);
        }


//        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "trainerData.csv"));
//        writer.write("ID Number,Name,\n"); //header in spreadsheet output
//        String line;
//        for (int row = 0; row < dataList.size(); row++) {
//            line = row + "," + trainerData[row] + ",";
//            for (int col = 0; col < trainerTable[0].length; col++) {
//                line += trainerTable[row][col] + ",";
//            }
//            line += "\n";
//            writer.write(line);
//        }
//        writer.close();
    }


    public void csvToTrainers(String trainerCsv, String outputDir) throws IOException {
        String trainerPath = path + trainerCsv;
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

        CsvReader csvReader = new CsvReader(trainerPath);
        BinaryWriter writer;
        for (int i = 0; i < csvReader.length(); i++) {
            initializeIndex(csvReader.next());
            writer = new BinaryWriter(outputPath + i + ".bin");

            writer.close();
        }

    }

    private void sort(File arr[]) {
        Arrays.sort(arr, Comparator.comparingInt(TrainerEditorGen4::fileToInt));
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

    private boolean isNotFull(int[] arr)
    {
        return arr[arr.length-1] == 0;
    }
}
