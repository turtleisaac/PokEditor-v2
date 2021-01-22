package com.turtleisaac.pokeditor.editors.trainers.gen4;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;
import com.turtleisaac.pokeditor.project.Game;

import java.io.*;
import java.util.*;

public class TrainerEditorGen4
{
    private String projectPath;
    private String dataPath;
    private static String resourcePath;
    private static String[] nameData;
    private static String[] moveData;
    private static String[] itemData;
    private static String[] abilityData;
    private static String[] trainerNames;
    private static String[] trainerClassData;

    private Game baseRom;

    public TrainerEditorGen4(String projectPath, Game baseRom) throws IOException
    {
        this.projectPath= projectPath;
        this.baseRom= baseRom;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf("/"));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf("/")) + File.separator + "Program Files" + File.separator;

        String entryPath = resourcePath + "EntryData.txt";
        String movePath = resourcePath + "MoveList.txt";
        String itemPath= resourcePath;
        String classPath= resourcePath;
        String trainerNamePath= resourcePath;

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

        switch(baseRom)
        {
            case Diamond:
            case Pearl:
                itemPath+= "ItemListDP.txt";
                trainerNamePath+= "TrainerNamesDP.txt";
                classPath+= "TrainerClassesDP.txt";
                break;

            case Platinum:
                itemPath+= "ItemListPt.txt";
                trainerNamePath+= "TrainerNamesPt.txt";
                classPath+= "TrainerClassesPt.txt";
                break;

            case HeartGold:
            case SoulSilver:
                itemPath+= "ItemListJohto.txt";
                break;
        }

        reader = new BufferedReader(new FileReader(itemPath));
        ArrayList<String> itemList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            itemList.add(line);
        }
        itemData = itemList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(trainerNamePath));
        ArrayList<String> trainerNameList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            trainerNameList.add(line);
        }
        trainerNames= trainerNameList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(classPath));
        ArrayList<String> trainerClassList = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            trainerClassList.add(line);
        }
        trainerClassData= trainerClassList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "AbilityList.txt"));
        ArrayList<String> abilityList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            abilityList.add(line);
        }
        abilityData= abilityList.toArray(new String[0]);
        reader.close();
    }

    public void trainersToCsv(String trainerDataDir, String trainerPokemonDir) throws IOException
    {
        dataPath+= trainerDataDir;

        Buffer buffer;
        ArrayList<TrainerDataGen4> trainerDataList = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        int max= Integer.MIN_VALUE;
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

            trainerDataList.add(new TrainerDataGen4() {
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

//            System.out.println("Trainer Class: " + trainerClass);
            max= Math.max(trainerClass,max);
        }
        System.out.println("Highest trainer class value: " + max);

        dataPath= projectPath + trainerPokemonDir;

        ArrayList<ArrayList<TrainerPokemonData>> trainerPokemonList= new ArrayList<>();

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
            TrainerDataGen4 trainerData= trainerDataList.get(i);
            short flag= trainerData.getFlag();
            short numPokemon= trainerData.getNumPokemon();


            System.out.println(trainerClassData[trainerData.getTrainerClass()] + " " + trainerNames[i]);
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
                        switch (baseRom)
                        {
                            case Platinum:
                            case HeartGold:
                            case SoulSilver:
                                ballSeal= buffer.readShort();
                                break;

                            case Diamond:
                            case Pearl:
                                ballSeal= (short)buffer.readByte();
                                break;
                        }


//                        System.out.println(nameData[species] + ":");
//                        System.out.println("    Species Ability #: " + abilitySlot);
//                        System.out.println("    IVs: " + ivs);
//                        System.out.println("    Level: " + level);
//                        System.out.println("    Alternate Form #: " + altForm);
//                        System.out.println("    Ball Seal: " + ballSeal);

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
                            public int getItem()
                            {
                                return 0;
                            }

                            @Override
                            public int getMove1()
                            {
                                return 0;
                            }

                            @Override
                            public int getMove2()
                            {
                                return 0;
                            }

                            @Override
                            public int getMove3()
                            {
                                return 0;
                            }

                            @Override
                            public int getMove4()
                            {
                                return 0;
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
                        switch (baseRom)
                        {
                            case Platinum:
                            case HeartGold:
                            case SoulSilver:
                                ballSeal= buffer.readShort();
                                break;

                            case Diamond:
                            case Pearl:
                                ballSeal= (short)buffer.readByte();
                                break;
                        }



//                        System.out.println(nameData[species] + ":");
//                        System.out.println("    Species Ability #: " + abilitySlot);
//                        System.out.println("    IVs: " + ivs);
//                        System.out.println("    Level: " + level);
//                        System.out.println("    Alternate Form #: " + altForm);
//                        System.out.println("    Moves: " + moveData[move1] + ", " + moveData[move2] + ", " + moveData[move3] + ", " + moveData[move4]);
//                        System.out.println("    Ball Seal: " + ballSeal);

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
                            public int getItem()
                            {
                                return 0;
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
                        switch (baseRom)
                        {
                            case Platinum:
                            case HeartGold:
                            case SoulSilver:
                                ballSeal= buffer.readShort();
                                break;

                            case Diamond:
                            case Pearl:
                                ballSeal= (short)buffer.readByte();
                                break;
                        }

//                        System.out.println(nameData[species] + ":");
//                        System.out.println("    Species Ability #: " + abilitySlot);
//                        System.out.println("    IVs: " + ivs);
//                        System.out.println("    Level: " + level);
//                        System.out.println("    Alternate Form #: " + altForm);
//                        System.out.println("    Item: " + itemData[item]);
//                        System.out.println("    Ball Seal: " + ballSeal);

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
                            public int getItem() {
                                return item;
                            }

                            @Override
                            public int getMove1()
                            {
                                return 0;
                            }

                            @Override
                            public int getMove2()
                            {
                                return 0;
                            }

                            @Override
                            public int getMove3()
                            {
                                return 0;
                            }

                            @Override
                            public int getMove4()
                            {
                                return 0;
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
                        switch (baseRom)
                        {
                            case Platinum:
                            case HeartGold:
                            case SoulSilver:
                                ballSeal= buffer.readShort();
                                break;

                            case Diamond:
                            case Pearl:
                                ballSeal= (short)buffer.readByte();
                                break;
                        }

//                        System.out.println(nameData[species] + ":");
//                        System.out.println("    Species Ability #: " + abilitySlot);
//                        System.out.println("    IVs: " + ivs);
//                        System.out.println("    Level: " + level);
//                        System.out.println("    Alternate Form #: " + altForm);
//                        System.out.println("    Item: " + itemData[item]);
//                        System.out.println("    Moves: " + moveData[move1] + ", " + moveData[move2] + ", " + moveData[move3] + ", " + moveData[move4]);
//                        System.out.println("    Ball Seal:  " + ballSeal);

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

            trainerPokemonList.add(thisTrainer);
        }


        String[][] trainerDataTable= new String[trainerDataList.size()][];
        for(int i= 0; i < trainerDataList.size(); i++)
        {
            int idx= 0;
            TrainerDataGen4 trainerData= trainerDataList.get(i);
            String[] thisTrainer= new String[27];
            Arrays.fill(thisTrainer,"");

            thisTrainer[idx++]= Boolean.toString((trainerData.getFlag() & 0x1) == 1); //defined moveset
            thisTrainer[idx++]= Boolean.toString(((trainerData.getFlag() >> 1) & 0x1) == 1); //defined held items
            thisTrainer[idx++]= trainerClassData[trainerData.getTrainerClass()];
            thisTrainer[idx++]= "" + trainerData.getBattleType();
            thisTrainer[idx++]= "" + trainerData.getNumPokemon();
            thisTrainer[idx++]= itemData[trainerData.getItem1()];
            thisTrainer[idx++]= itemData[trainerData.getItem2()];
            thisTrainer[idx++]= itemData[trainerData.getItem3()];
            thisTrainer[idx++]= itemData[trainerData.getItem4()];

            for(int x= 0; x < 14; x++)
            {
                thisTrainer[idx++]= Boolean.toString(((trainerData.getAI() >> x) & 0x1) == 1);
            }

            thisTrainer[idx++]= trainerData.getBattleType2() == 0 ? "Single Battle" : "Double Battle";
            thisTrainer[idx++]= "" + trainerData.getUnknown1();
            thisTrainer[idx++]= "" + trainerData.getUnknown2();
            thisTrainer[idx]= "" + trainerData.getUnknown3();

//            System.out.println(i + ": " + Arrays.toString(thisTrainer));
            trainerDataTable[i]= thisTrainer;
        }

        String[][] trainerPokemonTable= new String[trainerDataList.size()][];
        for(int i= 0; i < trainerPokemonList.size(); i++)
        {
            ArrayList<TrainerPokemonData> thisTeam= trainerPokemonList.get(i);
            TrainerDataGen4 trainerData= trainerDataList.get(i);
            String[] thisTrainer= new String[150];
            Arrays.fill(thisTrainer,"");

            System.out.println(i + ": ");
            int idx= 0;
            for(int x= 0; x < thisTeam.size(); x++)
            {
                TrainerPokemonData pokemon= thisTeam.get(x);
                System.out.println("    " + Integer.toBinaryString(pokemon.getIvs()) + " (" + pokemon.getIvs() + ")");
                for(int y= 0; y < 8; y++)
                {
                    thisTrainer[idx++]= Boolean.toString(((pokemon.getIvs() >> y) & 0x1) == 1);
                }
                thisTrainer[idx++]= abilityData[pokemon.getAbility()];
                thisTrainer[idx++]= "" + pokemon.getLevel();
                thisTrainer[idx++]= nameData[pokemon.getPokemon()];
                thisTrainer[idx++]= "" + pokemon.getAltForm();
                thisTrainer[idx++]= itemData[pokemon.getItem()];
                thisTrainer[idx++]= moveData[pokemon.getMove1()];
                thisTrainer[idx++]= moveData[pokemon.getMove2()];
                thisTrainer[idx++]= moveData[pokemon.getMove3()];
                thisTrainer[idx++]= moveData[pokemon.getMove4()];
                for(int y= 0; y < 8; y++)
                {
                    thisTrainer[idx++]= Boolean.toString(((pokemon.getBallCapsule() >> y) & 0x1) == 1);
                }
            }
            System.out.println();
//            System.out.println(i + ": " + Arrays.toString(thisTrainer));
            trainerPokemonTable[i]= thisTrainer;
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


//    public void csvToTrainers(String trainerCsv, String outputDir) throws IOException {
//        String trainerPath = path + trainerCsv;
//        String outputPath;
//
//        if (outputDir.contains("Recompile")) {
//            outputPath = path + "temp" + File.separator + outputDir;
//        } else {
//            outputPath = path + File.separator + outputDir;
//        }
//
//        if (!new File(outputPath).exists() && !new File(outputPath).mkdir()) {
//            throw new RuntimeException("Could not create output directory. Check write permissions");
//        }
//        outputPath += File.separator;
//
//        CsvReader csvReader = new CsvReader(trainerPath);
//        BinaryWriter writer;
//        for (int i = 0; i < csvReader.length(); i++) {
//            initializeIndex(csvReader.next());
//            writer = new BinaryWriter(outputPath + i + ".bin");
//
//            writer.close();
//        }
//
//    }

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

    private static int getMove(String move) {
        for (int i = 0; i < moveData.length; i++) {
            if (move.equals(moveData[i])) {
                return i;
            }
        }
        throw new RuntimeException("Invalid move entered: " + move);
    }

    private static int getAbility(String ability)
    {
        for(int i= 0; i < abilityData.length; i++)
        {
            if(ability.equals(abilityData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid ability entered: " + ability);
    }

    private boolean isNotFull(int[] arr)
    {
        return arr[arr.length-1] == 0;
    }
}
