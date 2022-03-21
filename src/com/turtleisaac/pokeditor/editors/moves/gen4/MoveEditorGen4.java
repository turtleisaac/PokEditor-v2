package com.turtleisaac.pokeditor.editors.moves.gen4;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Project;

import java.io.*;
import java.util.*;

public class MoveEditorGen4
{
//    public static void main(String[] args) throws IOException
//    {
//        MoveEditorGen4 moveEditor= new MoveEditorGen4();
//        moveEditor.movesToCsv("waza_tbl");
//    }

    private Project project;
    private String projectPath;
    private String dataPath;
    private static String resourcePath;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "???", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] categories= {"Physical","Special","Status"};
    private static String[] contestCategories= {"1","2","3","4","5","6","7","8","9","10","11","12"};
    private static String[] nameData;
    private static String[] moveData;
    private static String[] effects;
    private static String[] flags;
    private static String[] targets;



    public MoveEditorGen4(Project project, String projectPath) throws IOException
    {
        this.project= project;
        this.projectPath= projectPath;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                moveData= TextEditor.getBank(project,588);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                moveData= TextEditor.getBank(project,647);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                moveData= TextEditor.getBank(project,750);
                break;
        }


        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "Effects.txt"));
        String line;
        ArrayList<String> effectList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            effectList.add(line);
        }
        effects= effectList.toArray(new String[0]);
        reader.close();


        flags= new String[500];
        contestCategories= new String[500];
        for(int i= 0; i < flags.length; i++)
        {
            flags[i]= "" + i;
            contestCategories[i]= flags[i];
        }

        targets= new String[1025];
        targets[0]= "One opponent";
        targets[1]= "Automatic";
        targets[2]= "Random";
        targets[4]= "Both opponents";
        targets[8]= "Both opponents and ally";
        targets[16]= "User";
        targets[32]= "User's side of field";
        targets[64]= "Entire field";
        targets[128]= "Opponent's side of field";
        targets[256]= "Automatic (fails if there is no ally)";
        targets[512]= "User or ally";
        targets[1024]= "One opponent (fails if target faints)";
    }


    public Object[][] movesToSheet(String moveDir) throws IOException
    {
        dataPath= moveDir;

        Buffer buffer;
        ArrayList<MoveDataGen4> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        if(files.length > moveData.length)
            moveData= ArrayModifier.accommodateLength(moveData,files.length);

        for (File file : files)
        {
            buffer = new Buffer(file.toString());

            int effect = buffer.readUInt16();
            int category = buffer.readByte();
            int power = buffer.readByte();

            int type = buffer.readByte();
            int accuracy = buffer.readByte();
            int pp = buffer.readByte();
            int additionalEffect = buffer.readByte();

            int range = buffer.readUInt16();
            byte priority = buffer.readBytes(1)[0];
            int flag = buffer.readByte();

            int contestEffect = buffer.readByte();
            int contestType = buffer.readByte();

            dataList.add(new MoveDataGen4() {
                @Override
                public int getEffect() {
                    return effect;
                }

                @Override
                public short getCategory() {
                    return (short) category;
                }

                @Override
                public short getPower() {
                    return (short) power;
                }

                @Override
                public short getType() {
                    return (short) type;
                }

                @Override
                public short getAccuracy() {
                    return (short) accuracy;
                }

                @Override
                public short getPp() {
                    return (short) pp;
                }

                @Override
                public short getAdditionalEffect() {
                    return (short) additionalEffect;
                }

                @Override
                public int getRange() {
                    return range;
                }

                @Override
                public byte getPriority() {
                    return priority;
                }

                @Override
                public short getFlag() {
                    return (short) flag;
                }

                @Override
                public short getContestEffect() {
                    return (short) contestEffect;
                }

                @Override
                public short getContestType() {
                    return (short) contestType;
                }
            });

            buffer.close();
        }

        String[][] moveTable= new String[dataList.size()][30];
        for(int i= 0; i < dataList.size(); i++)
        {
            System.out.println(moveData[i] + ": " + i);
            MoveDataGen4 move= dataList.get(i);
            String[] line= new String[30];
            Arrays.fill(line,"");

            int idx= 0;
            line[idx++]= "" + effects[move.getEffect()];
            line[idx++]= categories[move.getCategory()];
            line[idx++]= "" + move.getPower();
            System.out.println("Additional Effect: " + effects[move.getEffect()]);
            System.out.println("Category: " + categories[move.getCategory()]);
            System.out.println("Power: " + move.getPower());

            line[idx++]= typeArr[move.getType()];
            line[idx++]= "" + move.getAccuracy();
//            System.out.println("Type: " + typeArr[move.getType()]);
            System.out.println("Accuracy: " + move.getAccuracy());

            line[idx++]= "" + move.getPp();
            line[idx++]= "" + move.getAdditionalEffect();
//            System.out.println("PP: " + move.getPp());
            System.out.println("Additional Effect Chance: " + move.getAdditionalEffect());

            line[idx++]= targets[move.getRange()];
//            short target= (short) move.getRange();
//            for(int x= 0; x < 12; x++)
//            {
//                if(x == 0)
//                    line[idx++]= Boolean.toString(((target >> x) & 0x1) == 1);
//                else
//                    line[idx++]= Boolean.toString((target & 0x1) == 1);
//            }

            line[idx++]= "" + move.getPriority();
            byte flag= (byte)move.getFlag();
            for(int x= 0; x < 8; x++)
            {
                line[idx++]= Boolean.toString(((flag >> x) & 0x1) == 1);
            }
            System.out.println("Target(s): " + move.getRange());
            System.out.println("Target(s): " + targets[move.getRange()]);
//            System.out.println("Priority: " + move.getPriority());
//            System.out.println("Flag: " + move.getFlag());

            line[idx++]= "" + move.getContestEffect();
            line[idx]= "" + move.getContestType();
//            System.out.println("Contest Effect: " + move.getContestEffect());
//            System.out.println("Contest Type: " + move.getContestType());

            moveTable[i]= line;
            System.out.println();
        }

        ArrayProcessor processor= new ArrayProcessor();
//        processor.append("ID Number,Name,Additional Effect,Category,Power,Type,Accuracy,PP,Additional Effect Chance (%),Target: One opponent (1),Target: Automatic (1),Target: Random,Target: Both opponents,Target: Both opponents and ally,Target: User,Target: User's side of field,Target: Entire field,Target: Opponent's side of field,Target: Automatic (2),Target: User or ally,Target: One opponent (2),Priority,Contact Move,Blocked by Protect,Reflected by Magic Coat, Affected by Snatch,Affected by Mirror Move,Triggers Kings Rock,Hide HP Bars,Remove Target's Shadow,Contest Effect,Contest Type");
        processor.append("ID Number,Name,Additional Effect,Category,Power,Type,Accuracy,PP,Additional Effect Chance (%),Target(s),Priority,Contact Move,Blocked by Protect,Reflected by Magic Coat, Affected by Snatch,Affected by Mirror Move,Triggers Kings Rock,Hide HP Bars,Remove Target's Shadow,Contest Effect,Contest Type");
        processor.newLine();
        String line;
        for(int row= 0; row < dataList.size(); row++)
        {
            line= row + "," + moveData[row] + ",";
            for(int col= 0; col < moveTable[0].length; col++)
            {
                line+= moveTable[row][col] + ",";
            }
            processor.append(line);
            processor.newLine();
        }

        return processor.getTable();
    }


    public void sheetToMoves(Object[][] moveSheet, String outputDir, String predictedOutputNarc) throws IOException
    {
        String outputPath= outputDir;

        if(!new File(outputPath).exists() && !new File(outputPath).mkdir())
        {
            throw new RuntimeException("Could not create output directory. Check write permissions");
        }
        outputPath+= File.separator;

        Object[] nameColumn= Arrays.copyOfRange(ArrayModifier.getColumn(moveSheet,1),1,moveSheet.length);
        moveSheet= ArrayModifier.trim(moveSheet,1,2);
        BinaryWriter writer;
        BitStream bitStream;

        //TODO add checks for out of bounds to all values (make new method in buffer which takes in the max value and returns 0 when the read value is greater.

        for(int i= 0; i < moveSheet.length; i++)
        {
            Object[] arr= moveSheet[i];
            initializeIndex(arr);
            writer= new BinaryWriter(outputPath + i + ".bin");

            System.out.println("Move: " + i + ", " + Arrays.toString(arr));
            String effect= next();
            String category= next();
            writer.writeShort(getEffect(effect)); //additional effect
            writer.writeByte(getCategory(category)); //category (physical, special, status)
            writer.writeByte((byte)Short.parseShort(next())); //power

            writer.writeByte(getType(next())); //type
            writer.writeByte((byte)Short.parseShort(next())); //accuracy
            writer.writeByte((byte)Short.parseShort(next())); //PP
            writer.writeByte((byte)Short.parseShort(next())); //additional effect chance (out of 100)

//            bitStream= new BitStream();
//            boolean target;
//            for(int x= 0; x < 12; x++)
//            {
//                target= Boolean.parseBoolean(next());
//                bitStream.append(target);
//            }
//            writer.write(bitStream.toBytes()); //targets
            writer.writeShort((short)getTargets(next()));
            writer.writeByte(Byte.parseByte(next())); //priority

            bitStream= new BitStream();
            boolean flag;
//            System.out.print(i + ": [");
            for(int x= 0; x < 8; x++)
            {
                flag= Boolean.parseBoolean(next());
//                System.out.print(flag + ", ");
                bitStream.append(flag);
            }
//            System.out.println("]");
//            System.out.println("BitStream " + i + ": " + bitStream.toBytes()[0]);
            writer.writeByte(bitStream.toBytes()[0]); //flags

            writer.writeByte((byte)Short.parseShort(next())); //contest effect (???)
            writer.writeByte((byte)Short.parseShort(next())); //contest type (???)
            writer.writeBytes(0,0);
        }


        int moveNameBank;
        switch(project.getBaseRom())
        {
            case Platinum:
                moveNameBank= 647;
                break;

            case HeartGold:
            case SoulSilver:
                moveNameBank= 750;
                break;

            default:
                throw new RuntimeException("Invalid base rom: " + project.getBaseRom());
        }

        boolean canTrim= true;
        if(new File(predictedOutputNarc).exists())
        {
            int numOriginalFiles= Narctowl.getNumFiles(predictedOutputNarc);

            if(moveSheet.length > numOriginalFiles)
            {
                canTrim= false;
            }
        }

        TextEditor.writeBank(project,nameColumn,moveNameBank,canTrim);
    }





    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(MoveEditorGen4::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private int arrIdx;
    private Object[] input;

    private void initializeIndex(Object[] arr)
    {
        arrIdx= 0;
        input= arr;
    }

    private String next()
    {
        try
        {
            return (String) input[arrIdx++];
        }
        catch (IndexOutOfBoundsException e)
        {
            return "";
        }
    }

    private static byte getType(String type)
    {
        for(int i= 0; i < typeArr.length; i++)
        {
            if(type.equals(typeArr[i]))
            {
                return (byte) i;
            }
        }

        throw new RuntimeException("Invalid type entered: " + type);
    }

    private static int getMove(String move)
    {
        for(int i= 0; i < moveData.length; i++)
        {
            if(move.equals(moveData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid move entered: " + move);
    }

    private static short getEffect(String effect)
    {
        for(int i= 0; i < effects.length; i++)
        {
            String str= effects[i];
            effect= effect.replaceAll("×","x");
            effect= effect.replaceAll("é","e");
            str= str.replaceAll("×","x");
            str= str.replaceAll("é","e");

            if(effect.equals(str))
            {
                return (short) i;
            }
            else if(effect.equalsIgnoreCase("Priority +1"))
            {
                return 103;
            }
        }
        throw new RuntimeException("Invalid effect entered: " + effect);
    }

    private static byte getCategory(String category)
    {
        for(int i= 0; i < categories.length; i++)
        {
            if(category.equals(categories[i]))
            {
                return (byte) i;
            }
        }
        throw new RuntimeException("Invalid category entered: " + category);
    }

    private static int getTargets(String target)
    {
        for(int i= 0; i < targets.length; i++)
        {
            if(target.equals(targets[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid target(s) entered: " + target);
    }
}
