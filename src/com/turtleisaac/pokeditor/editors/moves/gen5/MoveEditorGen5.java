package com.turtleisaac.pokeditor.editors.moves.gen5;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.BitStream;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;

import java.io.*;
import java.util.*;

public class MoveEditorGen5
{
    public static void main(String[] args) throws IOException
    {
        MoveEditorGen5 moveEditor= new MoveEditorGen5();
        moveEditor.movesToCsv("a021W2");
        moveEditor.csvToMoves("MoveData.csv", "com/turtleisaac/pokeditor/editors/moves");
    }

    private static String path= System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath= path;
    private static String resourcePath= path + "Program Files" + File.separator;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel","Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"};
    private static final String[] categories= {"Status","Physical","Special"};
    private static final String[] statusArr= {"None","Sleep","Poison","Burn","Freeze","Paralysis","Confusion","Infatuation"};
    private static final String[] statArr= {"None","Atk","Def","Spd","SpAtk","SpDef","Accuracy","Evasiveness","Ancient Power boost"};
    private static final String[] effectCategories= {"Status Inflicting","Stat Changing","Healing","Status Inflicting2",""};
    private static final String[] moveProperties= {"Contact","Requires Charge","Recharge Turn","Blocked by Protect","Reflected by Magic Coat","Affected by Snatch","Affected by Mirror Move","Punching Move","Sound Move","Affected by Gravity","Melts frozen targets","Hits non-adjacent opponents","Healing move","Hits through Substitute"};
    private static String[] nameData;
    private static String[] moveData;
    private static String[] effects;
    private static String[] flags;
    private static String[] targets= new String[] {"Any adjacent","Random (User/ Adjacent ally)","Random adjacent ally","Any adjacent opponent","All excluding user","All adjacent opponents","User's party","User","Entire Field","Random adjacent opponent","Field Itself","Opponent's side of field","User's side of field","User (Selects target automatically)"};


    public MoveEditorGen5() throws IOException
    {
        Scanner scanner= new Scanner(System.in);
        String entryPath= resourcePath;
        String movePath= resourcePath + "MoveList.txt";

        System.out.println("Black and White 1, or Black and White 2? (1 or 2)");
        String version= scanner.nextLine().toLowerCase();
        switch (version) {
            case "1":
                entryPath += "EntryDataGen5-1.txt";
                break;
            case "2":
                entryPath += "EntryDataGen5-2.txt";
                break;
            default:
                throw new RuntimeException("Invalid arguments");
        }

        BufferedReader reader= new BufferedReader(new FileReader(entryPath));
        ArrayList<String> nameList= new ArrayList<>();
        String line;
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            nameList.add(line);
        }
        nameData= nameList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(movePath));
        ArrayList<String> moveList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            moveList.add(line);
        }
        moveData= moveList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "Effects.txt"));
        ArrayList<String> effectList= new ArrayList<>();
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            effectList.add(line);
        }
        effects= effectList.toArray(new String[0]);
        reader.close();


        flags= new String[500];
        for(int i= 0; i < flags.length; i++)
        {
            flags[i]= "" + i;
        }
    }


    public void movesToCsv(String moveDir) throws IOException
    {
        dataPath += moveDir;

        Buffer buffer;
        ArrayList<MoveDataGen5> dataList = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for (int i= 0; i < files.length; i++)
        {
            file= files[i];
            buffer = new Buffer(file.toString());
            System.out.println(moveData[i] + ", " + i);

            short type= (short) buffer.readByte();
            short effectCategory= (short) buffer.readByte();
            short category= (short) buffer.readByte();
            short power= (short) buffer.readByte();
//            System.out.println("Type: " + typeArr[type] + " ");
//            System.out.println("Effect Category: " + effectCategory + " ");
//            String effectCategories= fixBinaryString(Integer.toBinaryString(effectCategory));
//            int length= effectCategories.length();
//            System.out.println("    1: " + effectCategories.substring(length-1));
//            System.out.println("    2: " + effectCategories.substring(length-2,length-1));
//            System.out.println("    3: " + effectCategories.substring(length-3,length-2));
//            System.out.println("    4: " + effectCategories.substring(length-4,length-3));
//            System.out.println("    5: " + effectCategories.substring(length-5,length-4));
//            System.out.println("    6: " + effectCategories.substring(length-6,length-5));
//            System.out.println("    7: " + effectCategories.substring(length-7,length-6));
//            System.out.println("    8: " + effectCategories.substring(length-8,length-7));
//            System.out.println("Category: " + categories[category] + " ");
//            System.out.println("Power: " + power + " ");

            short accuracy= (short) buffer.readByte();
            short pp= (short) buffer.readByte();
            byte priority= (byte) buffer.readByte();
            short numHits= (short) buffer.readByte();
            short minHits= Short.parseShort(fixBinaryString(Integer.toBinaryString(numHits),8).substring(4),2);
            short maxHits= Short.parseShort(fixBinaryString(Integer.toBinaryString(numHits),8).substring(0,4),2);
//            System.out.println("Accuracy: " + accuracy + " ");
//            System.out.println("PP: " + pp + " ");
//            System.out.println("Priority: " + priority + " ");
//            System.out.println("Min # Hits: " + Integer.parseInt(fixBinaryString(Integer.toBinaryString(numHits),8).substring(4),2) + " ");
//            System.out.println("Max # Hits: " + Integer.parseInt(fixBinaryString(Integer.toBinaryString(numHits),8).substring(0,4),2) + " ");

            int resultEffect= buffer.readUIntS();
            short effectChance= (short) buffer.readByte();
            short status= (short) buffer.readByte();
            short minTurns= (short) buffer.readByte();
            short maxTurns= (short) buffer.readByte();
//            System.out.println("Result Effect: " + resultEffect + " ");
//            String result= fixBinaryString(Integer.toBinaryString(resultEffect));
//            int length= result.length();
//            System.out.println("    1: " + result.substring(length-1));
//            System.out.println("    2: " + result.substring(length-2,length-1));
//            System.out.println("    3: " + result.substring(length-3,length-2));
//            System.out.println("    Binding Move: " + result.substring(length-4,length-3));
//            System.out.println("    5: " + result.substring(length-5,length-4));
//            System.out.println("    6: " + result.substring(length-6,length-5));
//            System.out.println("    7: " + result.substring(length-7,length-6));
//            System.out.println("    8: " + result.substring(length-8,length-7));
//            System.out.println("    9: " + result.substring(length-9,length-8));
//            System.out.println("    10: " + result.substring(length-10,length-9));
//            System.out.println("    11: " + result.substring(length-11,length-10));
//            System.out.println("    12: " + result.substring(length-12,length-11));
//            System.out.println("    13: " + result.substring(length-13,length-12));
//            System.out.println("    14: " + result.substring(length-14,length-13));
//            System.out.println("    15: " + result.substring(length-15,length-14));
//            System.out.println("    16: " + result.substring(length-16,length-15));
//            System.out.println("Effect Chance: " + effectChance + " ");
//            System.out.println("Status: " + status + " ");
//            if(minTurns != 0)
//            {
//                System.out.println("Effect Min # Turns: " + minTurns + " ");
//                System.out.println("Effect Max # Turns: " + maxTurns + " ");
//            }


            short crit= (short) buffer.readByte();
            short flinch= (short) buffer.readByte();
            int effect= buffer.readUIntS();
            short recoil= (short) buffer.readByte();
            short healing= (short) buffer.readByte();
            short target= (short) buffer.readByte();
//            System.out.println("Crit: " + crit + " ");
//            System.out.println("Flinch: " + flinch + " ");
//            try
//            {
//                System.out.println("Effect: " + effects[effect] + " ");
//            }
//            catch (IndexOutOfBoundsException e)
//            {
//                System.out.println("Effect: " + effect + " ");
//            }
//            String effectStr= fixBinaryString(Integer.toBinaryString(effect));
//            int length= effectStr.length();
//            System.out.println("    1: " + effectStr.substring(length-1));
//            System.out.println("    2: " + effectStr.substring(length-2,length-1));
//            System.out.println("    3: " + effectStr.substring(length-3,length-2));
//            System.out.println("    4: " + effectStr.substring(length-4,length-3));
//            System.out.println("    5: " + effectStr.substring(length-5,length-4));
//            System.out.println("    6: " + effectStr.substring(length-6,length-5));
//            System.out.println("    7: " + effectStr.substring(length-7,length-6));
//            System.out.println("    8: " + effectStr.substring(length-8,length-7));
//            System.out.println("    9: " + effectStr.substring(length-9,length-8));
//            System.out.println("    10: " + effectStr.substring(length-10,length-9));
//            System.out.println("    11: " + effectStr.substring(length-11,length-10));
//            System.out.println("    12: " + effectStr.substring(length-12,length-11));
//            System.out.println("    13: " + effectStr.substring(length-13,length-12));
//            System.out.println("    14: " + effectStr.substring(length-14,length-13));
//            System.out.println("    15: " + effectStr.substring(length-15,length-14));
//            System.out.println("    16: " + effectStr.substring(length-16,length-15));
//            System.out.println("Recoil: " + recoil + " ");
//            System.out.println("Healing: " + healing + " ");
//            System.out.println("Target: " + targets[target] + " ");

            short stat1= (short) buffer.readByte();
            short stat2= (short) buffer.readByte();
            short stat3= (short) buffer.readByte();
//            System.out.println("Stat 1: " + statArr[stat1] + " ");
//            System.out.println("Stat 2: " + statArr[stat2] + " ");
//            System.out.println("Stat 3: " + statArr[stat3] + " ");

            byte magnitude1= (byte) buffer.readByte();
            byte magnitude2= (byte) buffer.readByte();
            byte magnitude3= (byte) buffer.readByte();
//            System.out.println("Magnitude 1: " + magnitude1 + " ");
//            System.out.println("Magnitude 2: " + magnitude2 + " ");
//            System.out.println("Magnitude 3: " + magnitude3 + " ");

            short statChance1= (short) buffer.readByte();
            short statChance2= (short) buffer.readByte();
            short statChance3= (short) buffer.readByte();
            short flag= buffer.readShort();
//            System.out.println("Stat Chance 1: " + statChance1 + " ");
//            System.out.println("Stat Chance 2: " + statChance2 + " ");
//            System.out.println("Stat Chance 3: " + statChance3 + " ");
//            System.out.println("Flag: " + flag + " ");

            int last4= buffer.readInt();
            String bitfield= fixBinaryString(Integer.toBinaryString(last4),16);
            int len= bitfield.length();
            boolean[] properties= new boolean[14];
            for(int x= 0; x < 14; x++)
            {
                String thisVal = bitfield.substring(len-1-x, len-x);
                System.out.println("    " + moveProperties[x] + ": " + Integer.parseInt(thisVal,2));
                properties[x]= parseBoolean(thisVal);
            }

            dataList.add(new MoveDataGen5() {
                @Override
                public short getType() {
                    return type;
                }

                @Override
                public short getEffectCategory() {
                    return effectCategory;
                }

                @Override
                public short getCategory() {
                    return category;
                }

                @Override
                public short getPower() {
                    return power;
                }

                @Override
                public short getAccuracy() {
                    return accuracy;
                }

                @Override
                public short getPp() {
                    return pp;
                }

                @Override
                public short getPriority() {
                    return priority;
                }

                @Override
                public short getMinHits() {
                    return minHits;
                }

                @Override
                public short getMaxHits() {
                    return maxHits;
                }

                @Override
                public int getResultEffect() {
                    return resultEffect;
                }

                @Override
                public short getEffectChance() {
                    return effectChance;
                }

                @Override
                public short getStatus() {
                    return status;
                }

                @Override
                public short getMinTurns() {
                    return minTurns;
                }

                @Override
                public short getMaxTurns() {
                    return maxTurns;
                }

                @Override
                public short getCrit() {
                    return crit;
                }

                @Override
                public short getFlinch() {
                    return flinch;
                }

                @Override
                public int getEffect() {
                    return effect;
                }

                @Override
                public short getRecoil() {
                    return recoil;
                }

                @Override
                public short getHealing() {
                    return healing;
                }

                @Override
                public short getTarget() {
                    return target;
                }

                @Override
                public short getStat1() {
                    return stat1;
                }

                @Override
                public short getStat2() {
                    return stat2;
                }

                @Override
                public short getStat3() {
                    return stat3;
                }

                @Override
                public short getMagnitude1() {
                    return magnitude1;
                }

                @Override
                public short getMagnitude2() {
                    return magnitude2;
                }

                @Override
                public short getMagnitude3() {
                    return magnitude3;
                }

                @Override
                public short getStatChance1() {
                    return statChance1;
                }

                @Override
                public short getStatChance2() {
                    return statChance2;
                }

                @Override
                public short getStatChance3() {
                    return statChance3;
                }

                @Override
                public int getFlag() {
                    return flag;
                }

                @Override
                public boolean getContact() {
                    return properties[0];
                }

                @Override
                public boolean getChargingTurn() {
                    return properties[1];
                }

                @Override
                public boolean getRechargeTurn() {
                    return properties[2];
                }

                @Override
                public boolean getBlockedProtect() {
                    return properties[3];
                }

                @Override
                public boolean getMagicCoat() {
                    return properties[4];
                }

                @Override
                public boolean getSnatch() {
                    return properties[5];
                }

                @Override
                public boolean getMirrorMove() {
                    return properties[6];
                }

                @Override
                public boolean getPunchingMove() {
                    return properties[7];
                }

                @Override
                public boolean getSoundMove() {
                    return properties[8];
                }

                @Override
                public boolean getGravity() {
                    return properties[9];
                }

                @Override
                public boolean getMeltTarget() {
                    return properties[10];
                }

                @Override
                public boolean getNonAdjacent() {
                    return properties[11];
                }

                @Override
                public boolean getHealingMove() {
                    return properties[12];
                }

                @Override
                public boolean getSubstituteHit() {
                    return properties[13];
                }
            });
            System.out.println();
        }


        String[][] moveTable= new String[dataList.size()][44];
        for(int i= 0; i < dataList.size(); i++)
        {
            String[] thisMove= new String[44];
            MoveDataGen5 move= dataList.get(i);
            int idx= 0;

            thisMove[idx++]= typeArr[move.getType()];
            thisMove[idx++]= "" + move.getEffectCategory();
            thisMove[idx++]= categories[move.getCategory()];
            thisMove[idx++]= "" + move.getPower();
            thisMove[idx++]= "" + move.getAccuracy();
            thisMove[idx++]= "" + move.getPp();
            thisMove[idx++]= "" + move.getPriority();
            thisMove[idx++]= "" + move.getMinHits();
            thisMove[idx++]= "" + move.getMaxHits();
            thisMove[idx++]= "" + move.getResultEffect();
            thisMove[idx++]= "" + move.getEffectChance();
            thisMove[idx++]= "" + move.getStatus();
            thisMove[idx++]= "" + move.getMinTurns();
            thisMove[idx++]= "" + move.getMaxTurns();
            thisMove[idx++]= "" + move.getCrit();
            thisMove[idx++]= "" + move.getFlinch();
            thisMove[idx++]= "" + move.getEffect();
            thisMove[idx++]= "" + (byte)move.getRecoil();
            thisMove[idx++]= "" + (byte)move.getHealing();
            thisMove[idx++]= targets[move.getTarget()];
            thisMove[idx++]= statArr[move.getStat1()];
            thisMove[idx++]= statArr[move.getStat2()];
            thisMove[idx++]= statArr[move.getStat3()];
            thisMove[idx++]= "" + move.getMagnitude1();
            thisMove[idx++]= "" + move.getMagnitude2();
            thisMove[idx++]= "" + move.getMagnitude3();
            thisMove[idx++]= "" + move.getStatChance1();
            thisMove[idx++]= "" + move.getStatChance2();
            thisMove[idx++]= "" + move.getStatChance3();
            thisMove[idx++]= "" + move.getFlag();
            thisMove[idx++]= "" + move.getContact();
            thisMove[idx++]= "" + move.getChargingTurn();
            thisMove[idx++]= "" + move.getRechargeTurn();
            thisMove[idx++]= "" + move.getBlockedProtect();
            thisMove[idx++]= "" + move.getMagicCoat();
            thisMove[idx++]= "" + move.getSnatch();
            thisMove[idx++]= "" + move.getMirrorMove();
            thisMove[idx++]= "" + move.getPunchingMove();
            thisMove[idx++]= "" + move.getSoundMove();
            thisMove[idx++]= "" + move.getGravity();
            thisMove[idx++]= "" + move.getMeltTarget();
            thisMove[idx++]= "" + move.getNonAdjacent();
            thisMove[idx++]= "" + move.getHealingMove();
            thisMove[idx]= "" + move.getSubstituteHit();

            moveTable[i]= thisMove;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "MoveData.csv"));
        writer.write("ID Number,Name,Type,Effect Category,Category,Power,Accuracy,PP,Priority,Min # of Hits,Max # of Hits,Result Effect,Effect Chance,Status,Effect Min # of Turns,Effect Max # of Turns,Crit,Flinch,Effect,Max HP Recovery from Damage Dealt (%),Max HP Recovery from User's Max HP (%),Target(s),Stat 1,Stat 2,Stat 3,Stat 1 Amount,Stat 2 Amount,Stat 3 Amount,Stat 1 Chance,Stat 2 Chance,Stat 3 Chance,DO NOT TOUCH,Contact Move,Charging Turn,Recharge Turn,Blocked by Protect,Reflected by Magic Coat,Affected by Snatch,Affected by Mirror Move,Punching Move,Sound Move,Affected by Gravity,Melts Frozen Targets,Hits Non-Adjacent Foes,Healing Move,Hits through Substitute\n"); //header in spreadsheet output
        String line;
        for (int row = 0; row < dataList.size(); row++)
        {
            line = row + "," + moveData[row] + ",";
            for (int col = 0; col < moveTable[0].length; col++)
            {
                line += moveTable[row][col] + ",";
            }
            line += "\n";
            writer.write(line);
        }
        writer.close();
    }


    public void csvToMoves(String moveCsv, String outputDir) throws IOException
    {
        String movePath = path + moveCsv;
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

        CsvReader csvReader = new CsvReader(movePath);
        BinaryWriter writer;
        BitStream bitStream;
        for (int i = 0; i < csvReader.length(); i++)
        {
            initializeIndex(csvReader.next());
            writer = new BinaryWriter(outputPath + i + ".bin");
            bitStream= new BitStream();

            writer.writeByte(getType(next())); //type
            writer.writeByte((byte)Short.parseShort(next())); //effect category
            writer.writeByte(getCategory(next())); //category (status, physical, special)
            writer.writeByte((byte)Short.parseShort(next())); //power

            writer.writeByte((byte)Short.parseShort(next())); //accuracy
            writer.writeByte((byte)Short.parseShort(next())); //pp
            writer.writeByte(Byte.parseByte(next())); //priority
            String numHits= "" + fixBinaryString(Integer.toBinaryString(Integer.parseInt(next())),4);
            numHits= fixBinaryString(Integer.toBinaryString(Integer.parseInt(next())),4) + numHits;
            writer.writeByte((byte)Short.parseShort(numHits,2));

            writer.writeShort((short)Integer.parseInt(next())); //result effect
            writer.writeByte((byte)Short.parseShort(next())); //effect chance
            writer.writeByte((byte)Short.parseShort(next())); //status
            writer.writeByte((byte)Short.parseShort(next())); //effect min turns
            writer.writeByte((byte)Short.parseShort(next())); //effect max turns

            writer.writeByte((byte)Short.parseShort(next())); //crit chance (0 = never, 6 = always)
            writer.writeByte((byte)Short.parseShort(next())); //flinch chance (0 = never, 6 = always)
            writer.writeShort((short)Integer.parseInt(next())); //effect (same as gen 4 effect scripts)
            writer.writeByte((byte)Short.parseShort(next())); //percentage of move's damage dealt to heal/ damage user (n/100)%
            writer.writeByte((byte)Short.parseShort(next())); //percentage of user's max HP to heal/ damage user (n/100)%
            writer.writeByte(getTargets(next())); //target

            writer.writeByte(getStat(next())); //stat1
            writer.writeByte(getStat(next())); //stat2
            writer.writeByte(getStat(next())); //stat3

            writer.writeByte((byte)Short.parseShort(next())); //stat 1 change amount
            writer.writeByte((byte)Short.parseShort(next())); //stat 2 change amount
            writer.writeByte((byte)Short.parseShort(next())); //stat 3 change amount

            writer.writeByte((byte)Short.parseShort(next())); //stat 1 change chance
            writer.writeByte((byte)Short.parseShort(next())); //stat 2 change chance
            writer.writeByte((byte)Short.parseShort(next())); //stat 3 change chance
            writer.writeBytes(0x53,0x53); //always this in every file
            skip(1); //skips this column because it doesn't matter anymore

            for(int x= 0; x < 14; x++) //gets data for the 14 move properties
            {
                bitStream.append(Boolean.parseBoolean(next()));
            }
            writer.write(bitStream.toBytes()); //writes data for the 14 move properties
            writer.writeBytes(0,0); //writes two blank bytes
        }
    }



    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(MoveEditorGen5::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private int arrIdx;
    private String[] input;

    private void initializeIndex(String[] arr)
    {
        arrIdx= 0;
        input= arr;
    }

    private String next()
    {
        try
        {
            return input[arrIdx++];
        }
        catch (IndexOutOfBoundsException e)
        {
            return "";
        }
    }

    private void skip(int num)
    {
        arrIdx+= num;
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
            if(effect.equals(effects[i]))
            {
                return (short) i;
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

    private static byte getTargets(String target)
    {
        for(int i= 0; i < targets.length; i++)
        {
            if(target.equals(targets[i]))
            {
                return (byte) i;
            }
        }
        throw new RuntimeException("Invalid target(s) entered: " + target);
    }

    private static byte getStat(String stat)
    {
        for(int i= 0; i < statArr.length; i++)
        {
            if(stat.equals(statArr[i]))
            {
                return (byte) i;
            }
        }
        throw new RuntimeException("Invalid stat entered: " + stat);
    }

    private static String fixBinaryString(String str)
    {
        StringBuilder strBuilder = new StringBuilder(str);
        while(strBuilder.length() != 16)
        {
            strBuilder.insert(0, "0");
        }
        str = strBuilder.toString();
        return str;
    }

    private static String fixBinaryString(String str, int len)
    {
        StringBuilder strBuilder = new StringBuilder(str);
        while(strBuilder.length() != len)
        {
            strBuilder.insert(0, "0");
        }
        str = strBuilder.toString();
        return str;
    }

    private static boolean parseBoolean(String str)
    {
        return (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("1"));
    }
}
