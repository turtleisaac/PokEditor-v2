package com.turtleisaac.pokeditor.editors.trainers.gen4;

import com.turtleisaac.pokeditor.framework.BitStream;
import com.turtleisaac.pokeditor.framework.Buffer;
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
        resourcePath= projectPath;
        System.out.println(projectPath);
        if(projectPath.endsWith("/data"))
        {
            resourcePath= projectPath.substring(0,projectPath.lastIndexOf("/"));
            resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf("/"));
        }
        resourcePath+= File.separator + "Program Files" + File.separator;
        System.out.println(resourcePath);


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

    public TrainerReturnGen4 trainersToCsv(String trainerDataDir, String trainerPokemonDir) throws IOException
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

            short flag= buffer.readUShort8();
            short trainerClass= buffer.readUShort8();
            short battleType= buffer.readUShort8();
            short numPokemon= buffer.readUShort8();

            int item1= buffer.readUInt16();
            int item2= buffer.readUInt16();
            int item3= buffer.readUInt16();
            int item4= buffer.readUInt16();

            long ai= buffer.readUInt32();
            short battleType2= buffer.readUShort8();
            short unknown1= buffer.readUShort8();
            short unknown2= buffer.readUShort8();
            short unknown3= buffer.readUShort8();

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
            System.out.println(trainerData.getTrainerClass());
            if(flag == 0) //no defined moveset and no defined held items
            {
                for(int x= 0; x < numPokemon; x++)
                {
                    try
                    {
                        short ivs= (short)buffer.readByte();
                        byte abilitySlot= (byte)buffer.readByte();
                        short level= buffer.readShort();

                        int thisPokemon= buffer.readUInt16();
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

                        int thisPokemon= buffer.readUInt16();
                        int species= thisPokemon & 0x3ff;
                        int altForm= thisPokemon >> 10;

                        int move1= buffer.readUInt16();
                        int move2= buffer.readUInt16();
                        int move3= buffer.readUInt16();
                        int move4= buffer.readUInt16();

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

                        int thisPokemon= buffer.readUInt16();
                        int species= thisPokemon & 0x3ff;
                        int altForm= thisPokemon >> 10;

                        int item= buffer.readUInt16();

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

                        int thisPokemon= buffer.readUInt16();
                        int species= thisPokemon & 0x3ff;
                        int altForm= thisPokemon >> 10;

                        int item= buffer.readUInt16();

                        int move1= buffer.readUInt16();
                        int move2= buffer.readUInt16();
                        int move3= buffer.readUInt16();
                        int move4= buffer.readUInt16();



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

                        System.out.println(nameData[species] + ":");
                        System.out.println("    Species Ability #: " + abilitySlot);
                        System.out.println("    IVs: " + ivs);
                        System.out.println("    Level: " + level);
                        System.out.println("    Alternate Form #: " + altForm);
                        System.out.println("    Item: " + itemData[item]);
                        System.out.println("    Moves: " + moveData[move1] + ", " + moveData[move2] + ", " + moveData[move3] + ", " + moveData[move4]);
                        System.out.println("    Ball Seal:  " + ballSeal);

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


        String[][] trainerDataTable= new String[trainerDataList.size()+1][];
        trainerDataTable[0]= "ID Number,Name,Moves,Held Item,Trainer Class,Battle Type,Number of Pokemon,Item 1,Item 2,Item 3,Item 4,Prioritize Effectiveness,Evaluate Attacks,Expert,Prioritize Status,Risky Attacks,Prioritize Damage,Partner,Double Battle,Prioritize Healing,Utilize Weather,Harassment,Roaming Pokemon,Safari Zone,Catching Demo,Battle Type,Unknown 1,Unknown 2,Unknown 3".split(",");
        for(int i= 0; i < trainerDataList.size(); i++)
        {
            int idx= 0;
            TrainerDataGen4 trainerData= trainerDataList.get(i);
            String[] thisTrainer= new String[29];
            Arrays.fill(thisTrainer,"");

            thisTrainer[idx++]= "" + i;
            thisTrainer[idx++]= trainerNames[i];
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
            trainerDataTable[i+1]= thisTrainer;
        }

        Object[][] trainerPokemonTable= new Object[trainerDataList.size()+1][];
        StringBuilder header= new StringBuilder("ID Number,Name,");
        String pokemonHeader= "Difficulty Value,Ability Number,Level,Species,Form Number,Held Item,Move 1,Move 2,Move 3,Move 4,Ball Seal,";
        for(int i= 0; i < 6; i++)
        {
            header.append(pokemonHeader);
        }
        String[] headerArr= header.toString().split(",");
        trainerPokemonTable[0]= new String[68];
        Arrays.fill(trainerPokemonTable[0],"");
        System.arraycopy(headerArr,0,trainerPokemonTable[0],0,headerArr.length);
//        System.out.println("0: " + Arrays.toString(trainerPokemonTable[0]));
        for(int i= 0; i < trainerPokemonList.size(); i++)
        {
            ArrayList<TrainerPokemonData> thisTeam= trainerPokemonList.get(i);
            TrainerDataGen4 trainerData= trainerDataList.get(i);
            String[] thisTrainer= new String[68];
            Arrays.fill(thisTrainer,"");
            int idx= 0;
            thisTrainer[idx++]= "" + i;
            thisTrainer[idx++]= trainerNames[i];
            for(int x= 0; x < thisTeam.size(); x++)
            {
                TrainerPokemonData pokemon= thisTeam.get(x);
                thisTrainer[idx++]= "" + pokemon.getIvs();
                thisTrainer[idx++]= "" + pokemon.getAbility();
                thisTrainer[idx++]= "" + pokemon.getLevel();
                thisTrainer[idx++]= nameData[pokemon.getPokemon()];
                thisTrainer[idx++]= "" + pokemon.getAltForm();
                thisTrainer[idx++]= itemData[pokemon.getItem()];
                thisTrainer[idx++]= moveData[pokemon.getMove1()];
                thisTrainer[idx++]= moveData[pokemon.getMove2()];
                thisTrainer[idx++]= moveData[pokemon.getMove3()];
                thisTrainer[idx++]= moveData[pokemon.getMove4()];
                thisTrainer[idx++]= "" + pokemon.getBallCapsule();
            }
//            System.out.println();
//            System.out.println(i + ": " + Arrays.toString(thisTrainer));
            trainerPokemonTable[i+1]= thisTrainer;
        }

        return new TrainerReturnGen4()
        {
            @Override
            public Object[][] getTrainerData()
            {
                return trainerDataTable;
            }

            @Override
            public Object[][] getTrainerPokemon()
            {
                return trainerPokemonTable;
            }
        };


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

    public ArrayList<TrainerPokemonData> parseTrainerTeam(Object[] arr, int num)
    {
        System.out.println(Arrays.toString(arr));

        ArrayList<TrainerPokemonData> ret= new ArrayList<>();
        for(int i= 0; i < num; i++)
        {
            ret.add(parseTrainerPokemon(Arrays.copyOfRange(arr,(i*11),(i+1)*11)));
        }
        return ret;
    }

    public TrainerPokemonData parseTrainerPokemon(Object[] arr)
    {
        System.out.println(Arrays.toString(arr));
        return new TrainerPokemonData()
        {
            @Override
            public short getIvs()
            {
                return (short) Integer.parseInt((String) arr[0]);
            }

            @Override
            public short getAbility()
            {
                return (short) Integer.parseInt((String) arr[1]);
            }

            @Override
            public int getLevel()
            {
                return Integer.parseInt((String) arr[2]);
            }

            @Override
            public int getPokemon()
            {
                return getSpecies((String) arr[3]);
            }

            @Override
            public int getAltForm()
            {
                return (Integer.parseInt((String) arr[4]));
            }

            @Override
            public int getItem()
            {
                return TrainerEditorGen4.getItem((String) arr[5]);
            }

            @Override
            public int getMove1()
            {
                return getMove((String) arr[6]);
            }

            @Override
            public int getMove2()
            {
                return getMove((String) arr[7]);
            }

            @Override
            public int getMove3()
            {
                return getMove((String) arr[8]);
            }

            @Override
            public int getMove4()
            {
                return getMove((String) arr[9]);
            }

            @Override
            public short getBallCapsule()
            {
                return (short) Integer.parseInt((String) arr[10]);
            }
        };
    }

    public String[] createTrainerTeamRow(ArrayList<TrainerPokemonData> team)
    {
        String[] ret= new String[66];
        for(int i= 0; i < team.size(); i++)
        {
            TrainerPokemonData pokemon= team.get(i);
            System.arraycopy(createTrainerTeamPokemon(pokemon),0,ret,i*11,11);
        }
        return ret;
    }

    public String[] createTrainerTeamPokemon(TrainerPokemonData pokemon)
    {
        String[] arr= new String[11];
        int idx= 0;

        arr[idx++]= "" + pokemon.getIvs();
        arr[idx++]= "" + pokemon.getAbility();
        arr[idx++]= "" + pokemon.getLevel();
        arr[idx++]= nameData[pokemon.getPokemon()];
        arr[idx++]= "" + pokemon.getAltForm();
        arr[idx++]= itemData[pokemon.getItem()];
        arr[idx++]= moveData[pokemon.getMove1()];
        arr[idx++]= moveData[pokemon.getMove2()];
        arr[idx++]= moveData[pokemon.getMove3()];
        arr[idx++]= moveData[pokemon.getMove4()];
        arr[idx]= "" + pokemon.getBallCapsule();

        return arr;
    }

    public TrainerDataGen4 parseTrainerData(Object[] arr)
    {
        return new TrainerDataGen4()
        {
            @Override
            public short getFlag()
            {
                return (short) ((Boolean.parseBoolean((String) arr[2]) ? 1 : 0) + (Boolean.parseBoolean((String) arr[3]) ? 2 : 0));
            }

            @Override
            public short getTrainerClass()
            {
                return (short) TrainerEditorGen4.getTrainerClass((String) arr[4]);
            }

            @Override
            public short getBattleType()
            {
                return (short) Integer.parseInt((String) arr[5]);
            }

            @Override
            public short getNumPokemon()
            {
                return (short) Integer.parseInt((String) arr[6]);
            }

            @Override
            public int getItem1()
            {
                return getItem((String) arr[7]);
            }

            @Override
            public int getItem2()
            {
                return getItem((String) arr[8]);
            }

            @Override
            public int getItem3()
            {
                return getItem((String) arr[9]);
            }

            @Override
            public int getItem4()
            {
                return getItem((String) arr[10]);
            }

            @Override
            public long getAI()
            {
                BitStream bitStream= new BitStream();
                for(int i= 0; i < 14; i++)
                    bitStream.append(Boolean.parseBoolean((String) arr[i+11]));
                return Long.parseLong(bitStream.toString(),2);
            }

            @Override
            public short getBattleType2()
            {
                return (short) (arr[25].equals("Single Battle") ? 0 : 1);
            }

            @Override
            public short getUnknown1()
            {
                return (short) Integer.parseInt((String) arr[26]);
            }

            @Override
            public short getUnknown2()
            {
                return (short) Integer.parseInt((String) arr[27]);
            }

            @Override
            public short getUnknown3()
            {
                return (short) Integer.parseInt((String) arr[28]);
            }
        };
    }

    public String[] createTrainerDataRow(TrainerDataGen4 trainerData)
    {
        String[] thisTrainer= new String[27];
        int idx= 0;

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

        return thisTrainer;
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

    private static int getSpecies(String species) {
        for (int i = 0; i < nameData.length; i++) {
            if (species.equals(nameData[i])) {
                return i;
            }
        }
        throw new RuntimeException("Invalid species entered: " + species);
    }

    private static int getItem(String item) {
        for (int i = 0; i < itemData.length; i++) {
            if (item.equals(itemData[i])) {
                return i;
            }
        }
        throw new RuntimeException("Invalid item entered: " + item);
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

    private static int getTrainerClass(String trainerClass) {
        for (int i = 0; i < trainerClassData.length; i++) {
            if (trainerClass.equals(trainerClassData[i])) {
                return i;
            }
        }
        throw new RuntimeException("Invalid trainer class entered: " + trainerClass);
    }

    private boolean isNotFull(int[] arr)
    {
        return arr[arr.length-1] == 0;
    }
}
