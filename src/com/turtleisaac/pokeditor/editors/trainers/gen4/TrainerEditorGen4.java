package com.turtleisaac.pokeditor.editors.trainers.gen4;

import com.jackhack96.dspre.handlers.gen4.text.MessageFile;
import com.jackhack96.jNdstool.io.jBinaryStream;
import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.ArrayModifier;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.BitStream;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.util.*;

public class TrainerEditorGen4
{
    private Project project;
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

    public TrainerEditorGen4(Project project, String projectPath, Game baseRom) throws IOException
    {
        this.project= project;
        this.projectPath= projectPath;
        this.baseRom= baseRom;
        dataPath= projectPath;
        resourcePath= projectPath;
        System.out.println(projectPath);
        if(projectPath.endsWith(File.separator + "data"))
        {
            resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
            resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator));
        }
        resourcePath+= File.separator + "Program Files" + File.separator;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                moveData= TextEditor.getBank(project,588);
                itemData= TextEditor.getBank(project,344);
                abilityData= TextEditor.getBank(project,552);
                trainerNames= TextEditor.getBank(project,559);
                trainerClassData= TextEditor.getBank(project,560);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                moveData= TextEditor.getBank(project,647);
                itemData= TextEditor.getBank(project,392);
                abilityData= TextEditor.getBank(project,610);
                trainerNames= TextEditor.getBank(project,618);
                trainerClassData= TextEditor.getBank(project,619);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                moveData= TextEditor.getBank(project,750);
                itemData= TextEditor.getBank(project,222);
                abilityData= TextEditor.getBank(project,720);
                trainerNames= TextEditor.getBank(project,729);
                trainerClassData= TextEditor.getBank(project,730);
                break;
        }
    }

    public TrainerReturnGen4 trainersToSheets(String trainerDataDir, String trainerPokemonDir)
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
    }


    public void sheetsToTrainers(Object[][] trainerCsv, Object[][] pokemonCsv, String outputDir) throws IOException
    {
        String outputPath= dataPath + File.separator + outputDir;

        trainerCsv= ArrayModifier.trim(trainerCsv,1,0);
        pokemonCsv= ArrayModifier.trim(pokemonCsv,1,2);

        String dataName;
        String pokemonName;

        switch (baseRom)
        {
            case HeartGold:
            case SoulSilver:
                dataName= "5_";
                pokemonName= "6_";
                break;

            case Platinum:
                dataName= "trdata";
                pokemonName= "trpoke";
                break;

            default:
                throw new RuntimeException("Game not supported yet");
        }


        new File(outputPath + File.separator + dataName).mkdir();
        new File(outputPath + File.separator + pokemonName).mkdir();

        ArrayList<TrainerDataGen4> trainerData= new ArrayList<>();
        ArrayList<ArrayList<TrainerPokemonData>> teamData= new ArrayList<>();
        for(int i= 0; i < trainerCsv.length; i++)
        {
            trainerData.add(parseTrainerData(trainerCsv[i]));
            teamData.add(parseTrainerTeam(pokemonCsv[i],trainerData.get(i).getNumPokemon()));
        }

        BinaryWriter writer;
        for(int i= 0; i < trainerData.size(); i++)
        {
            System.out.println("Writing " + i);
            TrainerDataGen4 trainer= trainerData.get(i);
            ArrayList<TrainerPokemonData> team= teamData.get(i);

            writer= new BinaryWriter(outputPath + File.separator + dataName + File.separator + i + ".bin");

            writer.writeBytes(trainer.getFlag(),trainer.getTrainerClass(),trainer.getBattleType(),trainer.getNumPokemon());
            writer.writeShort((short) trainer.getItem1());
            writer.writeShort((short) trainer.getItem2());
            writer.writeShort((short) trainer.getItem3());
            writer.writeShort((short) trainer.getItem4());
            writer.writeInt((int) trainer.getAI());
            writer.writeBytes(i == 0 ? 0 : trainer.getBattleType2(),trainer.getUnknown1(),trainer.getUnknown2(),trainer.getUnknown3());

            writer= new BinaryWriter(outputPath + File.separator + pokemonName + File.separator + i + ".bin");

            for(int x= 0; x < team.size(); x++)
            {
                TrainerPokemonData pokemon= team.get(x);

                writer.writeBytes(pokemon.getIvs(),pokemon.getAbility());
                writer.writeShort((short) pokemon.getLevel());
                writer.writeShort((short)( ( (pokemon.getAltForm() & 0x7) << 10) | (pokemon.getPokemon() & 0x3ff) ) );

                if(trainer.getFlag() == 2 || trainer.getFlag() == 3)
                {
                    writer.writeShort((short) pokemon.getItem());
                }

                if(trainer.getFlag() == 1 || trainer.getFlag() == 3)
                {
                    writer.writeShort((short) pokemon.getMove1());
                    writer.writeShort((short) pokemon.getMove2());
                    writer.writeShort((short) pokemon.getMove3());
                    writer.writeShort((short) pokemon.getMove4());
                }

                writer.writeShort(pokemon.getBallCapsule());
            }

            if(team.size() == 0)
            {
                writer.writeByteNumTimes(0,8);
            }

            if(new File(outputPath + File.separator + pokemonName + File.separator + i + ".bin").length() % 4 != 0)
                writer.writeByteNumTimes(0,2);

            writer.close();
        }
    }

    public ArrayList<TrainerPokemonData> parseTrainerTeam(Object[] arr, int num)
    {
        ArrayList<TrainerPokemonData> ret= new ArrayList<>();
        for(int i= 0; i < num; i++)
        {
            ret.add(parseTrainerPokemon(Arrays.copyOfRange(arr,(i*11),(i+1)*11)));
        }
        return ret;
    }

    public TrainerPokemonData parseTrainerPokemon(Object[] arr)
    {
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
                return (short) (arr[25].equals("Single Battle") ? 0 : 2);
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

    private static int getSpecies(String pokemon)
    {
        for(int i= 0; i < nameData.length; i++)
        {
            if(pokemon.equals(nameData[i]))
            {
                return i;
            }
        }

        if(pokemon.equals(""))
            return 0;

        int species;
        try
        {
            species= Integer.parseInt(pokemon);
        }
        catch (NumberFormatException ignored)
        {
            throw new RuntimeException("Invalid pokemon entered: " + pokemon);
        }

        return species;
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
