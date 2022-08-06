package com.turtleisaac.pokeditor.editors.items;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;

public class ItemEditorGen4
{
    private Project project;
    private static String projectPath;
    private String dataPath= "";
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "???", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] growthTableIdArr= {"Medium Fast","Erratic","Fluctuating","Medium Slow","Fast","Slow","Medium Fast","Medium Fast"};
    private static String resourcePath;
    private static String[] nameData;
    private static String[] itemData;
    private static String[] abilityData;
    private static String[] fieldFunctions;
    private static String[] pluckFlingEffects;
    private static String[] fieldBagPockets;
    private static String[] battleBagPockets= {"None","Pokeballs","Battle Items","","Recovery","","","","Status Healing","","","","Status/ HP Recovery","","","","PP Recovery"};
    private static String[] battleFunctions;
    private static String[] workTypes= {"Dummy","Usable"};

    private Game baseRom;

    private static ArrayList<ItemTableEntry> itemTableData;

    public ItemEditorGen4(String dataPath, Project project, ArrayList<ItemTableEntry> itemTableData) throws IOException
    {
        this.project= project;
        this.baseRom= project.getBaseRom();
        this.projectPath= dataPath;
        this.dataPath = dataPath;
        resourcePath= dataPath.substring(0,dataPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;
        ItemEditorGen4.itemTableData = itemTableData;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                itemData= TextEditor.getBank(project,344);
                abilityData= TextEditor.getBank(project,552);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                itemData= TextEditor.getBank(project,392);
                abilityData= TextEditor.getBank(project,610);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                itemData= TextEditor.getBank(project,222);
                abilityData= TextEditor.getBank(project,720);
                break;
        }

//        ArrayList<String> itemList= new ArrayList<>(Arrays.asList(itemData));
//        itemList.removeIf(s -> s.equals("???"));
//        itemData= itemList.toArray(new String[0]);

        String fieldFunctionsPath= resourcePath;

        BufferedReader reader;
        String line;

        switch(baseRom)
        {
            case Pearl:
            case Diamond:
            case Platinum:
                fieldFunctionsPath+= "ItemFieldFunctionsSinnoh.txt";
                break;

            case HeartGold:
            case SoulSilver:
                fieldFunctionsPath+= "ItemFieldFunctionsJohto.txt";
                break;

            default :
                throw new RuntimeException("Invalid rom header: Game Code/ Title");
        }


        reader= new BufferedReader(new FileReader(fieldFunctionsPath));
        ArrayList<String> fieldEffectList= new ArrayList<>();
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            fieldEffectList.add(line);
        }
        fieldFunctions = fieldEffectList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "ItemFieldBagPocketsGen4.txt"));
        ArrayList<String> fieldPocketList= new ArrayList<>();
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            fieldPocketList.add(line);
        }
        fieldBagPockets = fieldPocketList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "ItemBattleFunctionsGen4.txt"));
        ArrayList<String> battleEffectList= new ArrayList<>();
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            battleEffectList.add(line);
        }
        battleFunctions = battleEffectList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "PluckFlingEffectsGen4.txt"));
        ArrayList<String> pluckEffectList= new ArrayList<>();
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            pluckEffectList.add(line);
        }
        pluckFlingEffects = pluckEffectList.toArray(new String[0]);
        reader.close();
    }


    public Object[][] itemsToSheet(String itemDir) throws IOException
    {
        dataPath= itemDir;

        Buffer buffer;
        ArrayList<ItemData> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        if(files.length > itemData.length)
            itemData= ArrayModifier.accommodateLength(itemData,files.length);

        for(int i= 0; i < files.length; i++)
        {
            file= files[i];
            buffer= new Buffer(file.toString());
            //System.out.println(itemData[i] + ": " + i);

            int price= buffer.readUInt16();
            int equipmentEffect= buffer.readByte();
            int power= buffer.readByte();
            int pluckEffect= buffer.readByte();
            int flingEffect= buffer.readByte();
            int flingPower= buffer.readByte();
            int naturalGiftPower= buffer.readByte();
            //System.out.println("Price: " + price);
            //System.out.println("Equipment Effect: " + equipmentEffect);
            //System.out.println("Power: " + power);
            //System.out.println("Pluck Effect: " + pluckFlingEffects[pluckEffect]);
            //System.out.println("Fling Effect: " + pluckFlingEffects[flingEffect]);
            //System.out.println("Fling Power: " + flingPower);
            //System.out.println("Natural Gift Power: " + naturalGiftPower);

            int bitField1= buffer.readShort();
            byte naturalGiftType= (byte)(bitField1 & 0x1f);
            try
            {
                //System.out.println("Natural Gift Type: " + typeArr[naturalGiftType]);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                //System.out.println("Natural Gift Type: None");
            }
            boolean discardUnable= (((byte)(bitField1 >> 5) & 0x01) == 1);
            boolean canRegister= (((byte)(bitField1 >> 6) & 0x01) == 1);
            byte fieldBag= (byte) ((bitField1 >> 7) & 0xf);
            byte battleBag= (byte) ((bitField1 >> 11) & 0x1f);
            //System.out.println("Unable to Discard: " + discardUnable);
            //System.out.println("Can Register: " + canRegister);
            //System.out.println("Field Bag: " + fieldBagPockets[fieldBag]);
            //System.out.println("Battle Bag: " + battleBagPockets[battleBag]);

            int fieldFunction= buffer.readByte();
            int battleFunction= buffer.readByte();
            int workType= buffer.readByte();
            buffer.skipBytes(1);
            //System.out.println("Field Function: " + fieldFunctions[fieldFunction]);
            //System.out.println("Battle Function: " + battleFunctions[battleFunction]);
            //System.out.println("Item Usage Type: " + workTypes[workType]);

            int recovery= buffer.readByte();
            boolean sleepRecovery= (recovery & 0x01) == 1;
            boolean poisonRecovery= ((recovery >> 1) & 0x01) == 1;
            boolean burnRecovery= ((recovery >> 2) & 0x01) == 1;
            boolean freezeRecovery= ((recovery >> 3) & 0x01) == 1;
            boolean paralyzeRecovery= ((recovery >> 4) & 0x01) == 1;
            boolean confuseRecovery= ((recovery >> 5) & 0x01) == 1;
            boolean attractRecovery= ((recovery >> 6) & 0x01) == 1;
            boolean guardSpec= ((recovery >> 7) & 0x01) == 1;
            //System.out.println("Sleep Recovery: " + sleepRecovery);
            //System.out.println("Poison Recovery:" + poisonRecovery);
            //System.out.println("Burn Recovery: " + burnRecovery);
            //System.out.println("Freeze Recovery: " + freezeRecovery);
            //System.out.println("Paralyze Recovery: " + paralyzeRecovery);
            //System.out.println("Confuse Recovery: " + confuseRecovery);
            //System.out.println("Attract Recovery: " + attractRecovery);
            //System.out.println("Guard Spec Effect: " + guardSpec);

            int utilityInfo= buffer.readByte();
            boolean revive= (utilityInfo & 0x01) == 1;
            boolean reviveAll= ((utilityInfo >> 1) & 0x01) == 1;
            boolean rareCandy= ((utilityInfo >> 2) & 0x01) == 1;
            boolean evolution= ((utilityInfo >> 3) & 0x01) == 1;
            byte atkUp= (byte)((utilityInfo >> 4) & 0xf);
            //System.out.println("revive: " + revive);
            //System.out.println("revive all: " + reviveAll);
            //System.out.println("rare candy: " + rareCandy);
            //System.out.println("Evolution Stone: " + evolution);
            //System.out.println("Attack Stat Boost: " + atkUp);

            int battleBoost1= buffer.readByte();
            byte defUp= (byte)(battleBoost1 & 0xf);
            byte spAtkUp= (byte)((battleBoost1 >> 4) & 0xf);
            //System.out.println("Defense Stat Boost: " + defUp);
            //System.out.println("Special Attack Stat Boost: " + spAtkUp);

            int battleBoost2= buffer.readByte();
            byte spDefUp= (byte)(battleBoost2 & 0xf);
            byte speedUp= (byte)((battleBoost2 >> 4) & 0xf);
            //System.out.println("Special Defense Stat Boost: " + spDefUp);
            //System.out.println("Speed Stat Boost: " + speedUp);


            int battleBoost3= buffer.readByte();
            byte accuracyUp= (byte)(battleBoost3 & 0xf);
            byte critUp= (byte)((battleBoost3 >> 4) & 0x3);
            boolean ppUp= ((battleBoost3 >> 6) & 0x1) == 1;
            boolean ppUp3= ((battleBoost3 >> 7) & 0x1) == 1;
            //System.out.println("Accuracy Stat Boost: " + accuracyUp);
            //System.out.println("Critical Hit Chance Boost: " + critUp);
            //System.out.println("pp up effect: " + ppUp);
            //System.out.println("pp max effect: " + ppUp3);

            int recovery2= buffer.readByte();
            boolean ppRecovery= (recovery2 & 0x1) == 1;
            boolean ppRecoveryAll= ((recovery2 >> 1) & 0x01) == 1;
            boolean hpRecovery= ((recovery2 >> 2) & 0x01) == 1;
            boolean hpEv= ((recovery2 >> 3) & 0x01) == 1;
            boolean atkEv= ((recovery2 >> 4) & 0x01) == 1;
            boolean defEv= ((recovery2 >> 5) & 0x01) == 1;
            boolean speedEv= ((recovery2 >> 6) & 0x01) == 1;
            boolean spAtkEv= ((recovery2 >> 7) & 0x01) == 1;
            //System.out.println("PP Recovery: " + ppRecovery);
            //System.out.println("All PP Recovery: " + ppRecoveryAll);
            //System.out.println("HP Recovery: " + hpRecovery);
            //System.out.println("HP EV: " + hpEv);
            //System.out.println("Attack EV: " + atkEv);
            //System.out.println("Defense EV: " + defEv);
            //System.out.println("Speed EV: " + speedEv);
            //System.out.println("Special Attack EV: " + spAtkEv);

            int bitField2= buffer.readByte();
            boolean spDefEv= (bitField2 & 0x1) == 1;
            boolean friendExp1= ((bitField2 >> 1) & 0x01) == 1;
            boolean friendExp2= ((bitField2 >> 2) & 0x01) == 1;
            boolean friendExp3= ((bitField2 >> 3) & 0x01) == 1;
            //System.out.println("Special Defense EV: " + spDefEv);
            //System.out.println("Friendship points 1: " + friendExp1);
            //System.out.println("Friendship points 2: " + friendExp2);
            //System.out.println("Friendship points 3: " + friendExp3);

            byte hpEvChange= (byte)buffer.readByte();
            byte atkEvChange= (byte)buffer.readByte();
            byte defEvChange= (byte)buffer.readByte();
            byte speedEvChange= (byte)buffer.readByte();
            byte spAtkEvChange= (byte)buffer.readByte();
            byte spDefEvChange= (byte)buffer.readByte();
            int hpRecoveryAmount= buffer.readByte();
            int ppRecoveryAmount= buffer.readByte();
            byte friendExpChange1= (byte)buffer.readByte();
            byte friendExpChange2= (byte)buffer.readByte();
            byte friendExpChange3= (byte)buffer.readByte();
            //System.out.println("HP EV Change: " + hpEvChange);
            //System.out.println("Attack EV Change: " + atkEvChange);
            //System.out.println("Defense EV Change: " + defEvChange);
            //System.out.println("Speed EV Change: " + speedEvChange);
            //System.out.println("Special Attack EV Change: " + spAtkEvChange);
            //System.out.println("Special Defense EV Change: " + spDefEvChange);
            //System.out.println("HP Recovery Amount: " + hpRecoveryAmount);
            //System.out.println("PP Recovery Amount: " + ppRecoveryAmount);
            //System.out.println("Friendship Point Change 1: " + friendExpChange1);
            //System.out.println("Friendship Point Change 2: " + friendExpChange2);
            //System.out.println("Friendship Point Change 3: " + friendExpChange3);

            //System.out.println();

            dataList.add(new ItemData() {
                @Override
                public int getPrice() {
                    return price;
                }

                @Override
                public short getEffect() {
                    return (short) equipmentEffect;
                }

                @Override
                public short getPower() {
                    return (short) power;
                }

                @Override
                public short getPluck() {
                    return (short) pluckEffect;
                }

                @Override
                public short getFlingEffect() {
                    return (short) flingEffect;
                }

                @Override
                public short getFlingPower() {
                    return (short) flingPower;
                }

                @Override
                public short getNaturalPower() {
                    return (short) naturalGiftPower;
                }

                @Override
                public byte getNaturalType() {
                    return naturalGiftType;
                }

                @Override
                public boolean getDiscardUnable() {
                    return discardUnable;
                }

                @Override
                public boolean getAbleRegister() {
                    return canRegister;
                }

                @Override
                public byte getFieldPocketNumber() {
                    return fieldBag;
                }

                @Override
                public byte getBattlePocketNumber() {
                    return battleBag;
                }

                @Override
                public short getFieldFunction() {
                    return (short) fieldFunction;
                }

                @Override
                public short getBattleFunction() {
                    return (short) battleFunction;
                }

                @Override
                public short getWorkType() {
                    return (short) workType;
                }

                @Override
                public boolean getSlpRecovery() {
                    return sleepRecovery;
                }

                @Override
                public boolean getPsnRecovery() {
                    return poisonRecovery;
                }

                @Override
                public boolean getBrnRecovery() {
                    return burnRecovery;
                }

                @Override
                public boolean getFrzRecovery() {
                    return freezeRecovery;
                }

                @Override
                public boolean getPrlzRecovery() {
                    return paralyzeRecovery;
                }

                @Override
                public boolean getCnfsRecovery() {
                    return confuseRecovery;
                }

                @Override
                public boolean getAttractRecovery() {
                    return attractRecovery;
                }

                @Override
                public boolean getAbilityGuard() {
                    return guardSpec;
                }

                @Override
                public boolean getRevive() {
                    return revive;
                }

                @Override
                public boolean getReviveAll() {
                    return reviveAll;
                }

                @Override
                public boolean getLevelUp() {
                    return rareCandy;
                }

                @Override
                public boolean getEvolution() {
                    return evolution;
                }

                @Override
                public byte getAtkUp() {
                    return atkUp;
                }

                @Override
                public byte getDefUp() {
                    return defUp;
                }

                @Override
                public byte getSpAtkUp() {
                    return spAtkUp;
                }

                @Override
                public byte getSpDefUp() {
                    return spDefUp;
                }

                @Override
                public byte getSpeedUp() {
                    return speedUp;
                }

                @Override
                public byte getAccuracyUp() {
                    return accuracyUp;
                }

                @Override
                public byte getCritUp() {
                    return critUp;
                }

                @Override
                public boolean getPpUp() {
                    return ppUp;
                }

                @Override
                public boolean getPpUp3() {
                    return ppUp3;
                }

                @Override
                public boolean getPpRecovery() {
                    return ppRecovery;
                }

                @Override
                public boolean getPpFullRecovery() {
                    return ppRecoveryAll;
                }

                @Override
                public boolean getHpRecovery() {
                    return hpRecovery;
                }

                @Override
                public boolean getHpEv() {
                    return hpEv;
                }

                @Override
                public boolean getAtkEv() {
                    return atkEv;
                }

                @Override
                public boolean getDefEv() {
                    return defEv;
                }

                @Override
                public boolean getSpeedEv() {
                    return speedEv;
                }

                @Override
                public boolean getSpAtkEv() {
                    return spAtkEv;
                }

                @Override
                public boolean getSpDefEv() {
                    return spDefEv;
                }

                @Override
                public boolean getFriendExp1() {
                    return friendExp1;
                }

                @Override
                public boolean getFriendExp2() {
                    return friendExp2;
                }

                @Override
                public boolean getFriendExp3() {
                    return friendExp3;
                }

                @Override
                public byte getHpEvChange() {
                    return hpEvChange;
                }

                @Override
                public byte getAtkEvChange() {
                    return atkEvChange;
                }

                @Override
                public byte getDefEvChange() {
                    return defEvChange;
                }

                @Override
                public byte getSpeedEvChange() {
                    return speedEvChange;
                }

                @Override
                public byte getSpAtkEvChange() {
                    return spAtkEvChange;
                }

                @Override
                public byte getSpDefEvChange() {
                    return spDefEvChange;
                }

                @Override
                public short getHpRecoveryAmount() {
                    return (short) hpRecoveryAmount;
                }

                @Override
                public short getPpRecoveryAmount() {
                    return (short) ppRecoveryAmount;
                }

                @Override
                public byte getFriendExpChange1() {
                    return friendExpChange1;
                }

                @Override
                public byte getFriendExpChange2() {
                    return friendExpChange2;
                }

                @Override
                public byte getFriendExpChange3() {
                    return friendExpChange3;
                }
            });
        }

        String[][] itemTable= new String[dataList.size()][];
        for(int i= 0; i < itemTable.length; i++)
        {
            int idx= 0;
            ItemData itemData= dataList.get(i);
            String[] item= new String[65];
            Arrays.fill(item,"");

            item[idx++]= "" + itemData.getPrice();
            item[idx++]= "" + itemData.getEffect();
            item[idx++]= "" + itemData.getPower();
            item[idx++]= "" + pluckFlingEffects[itemData.getPluck()];
            item[idx++]= "" + pluckFlingEffects[itemData.getFlingEffect()];
            item[idx++]= "" + itemData.getFlingPower();
            item[idx++]= "" + itemData.getNaturalPower();
            try
            {
                item[idx]= typeArr[itemData.getNaturalType()];
            }
            catch (IndexOutOfBoundsException e)
            {
                item[idx]= "None";
            }
            idx++;
            item[idx++]= "" + itemData.getDiscardUnable();
            item[idx++]= "" + itemData.getAbleRegister();
            item[idx++]= "" + fieldBagPockets[itemData.getFieldPocketNumber()];
            item[idx++]= "" + battleBagPockets[itemData.getBattlePocketNumber()];
            item[idx++]= "" + fieldFunctions[itemData.getFieldFunction()];
            item[idx++]= "" + battleFunctions[itemData.getBattleFunction()];
            item[idx++]= "" + workTypes[itemData.getWorkType()];
            item[idx++]= "" + itemData.getSlpRecovery();
            item[idx++]= "" + itemData.getPsnRecovery();
            item[idx++]= "" + itemData.getBrnRecovery();
            item[idx++]= "" + itemData.getFrzRecovery();
            item[idx++]= "" + itemData.getPrlzRecovery();
            item[idx++]= "" + itemData.getCnfsRecovery();
            item[idx++]= "" + itemData.getAttractRecovery();
            item[idx++]= "" + itemData.getAbilityGuard();
            item[idx++]= "" + itemData.getRevive();
            item[idx++]= "" + itemData.getReviveAll();
            item[idx++]= "" + itemData.getLevelUp();
            item[idx++]= "" + itemData.getEvolution();
            item[idx++]= "" + itemData.getAtkUp();
            item[idx++]= "" + itemData.getDefUp();
            item[idx++]= "" + itemData.getSpAtkUp();
            item[idx++]= "" + itemData.getSpDefUp();
            item[idx++]= "" + itemData.getSpeedUp();
            item[idx++]= "" + itemData.getAccuracyUp();
            item[idx++]= "" + itemData.getCritUp();
            item[idx++]= "" + itemData.getPpUp();
            item[idx++]= "" + itemData.getPpUp3();
            item[idx++]= "" + itemData.getPpRecovery();
            item[idx++]= "" + itemData.getPpFullRecovery();
            item[idx++]= "" + itemData.getHpRecovery();
            item[idx++]= "" + itemData.getHpEv();
            item[idx++]= "" + itemData.getAtkEv();
            item[idx++]= "" + itemData.getDefEv();
            item[idx++]= "" + itemData.getSpeedEv();
            item[idx++]= "" + itemData.getSpAtkEv();
            item[idx++]= "" + itemData.getSpDefEv();
            item[idx++]= "" + itemData.getFriendExp1();
            item[idx++]= "" + itemData.getFriendExp2();
            item[idx++]= "" + itemData.getFriendExp3();
            item[idx++]= "" + itemData.getHpEvChange();
            item[idx++]= "" + itemData.getAtkEvChange();
            item[idx++]= "" + itemData.getDefEvChange();
            item[idx++]= "" + itemData.getSpeedEvChange();
            item[idx++]= "" + itemData.getSpAtkEvChange();
            item[idx++]= "" + itemData.getSpDefEvChange();
            item[idx++]= "" + itemData.getHpRecoveryAmount();
            item[idx++]= "" + itemData.getPpRecoveryAmount();
            item[idx++]= "" + itemData.getFriendExpChange1();
            item[idx++]= "" + itemData.getFriendExpChange2();
            item[idx]= "" + itemData.getFriendExpChange3();

            itemTable[i]= item;
        }


        ArrayProcessor processor= new ArrayProcessor();
        processor.append("ID Number,Name,Price,Battle Script,Power,Pluck Effect,Fling Effect,Fling Power,Natural Gift Power,Natural Gift Type,Unable to Discard,Can Register,Field Bag,Battle Bag,Field Function,Battle Function,Usability,Slp Recovery,Psn Recovery,Brn Recovery,Frz Recovery,Prlz Recovery,Confuse Recovery,Attract Recovery,Guard Spec,Revive,Revive All Party Members,Level-Up,Evolution Stone,Attack Boost,Defense Boost,Special Attack Boost,Special Defense Boost,Speed Boost,Accuracy Boost,Crit Chance Boost,PP-Up Effect,PP-Max Effect,PP Recovery,Recover All PP,HP Recovery,Changes HP Ev,Changes Atk Ev,Changes Def Ev,Changes Spd Ev,Changes SpAtk Ev,Changes SpDef Ev,Changes Friendship 1,Changes Friendship 2,Changes Friendship 3,HP EV Amount,Atk EV Amount,Def EV Amount,Spd EV Amount,SpAtk EV Amount,SpDef EV Amount,HP Recovery Amount,PP Recovery Amount,Friendship 1 Amount,Friendship 2 Amount,Friendship 3 Amount");
        processor.newLine();

        String line;
        for(int row= 0; row < dataList.size(); row++)
        {
            line= row + "," + getItemName(row) + ",";
            for(int col= 0; col < itemTable[0].length; col++)
            {
                line+= itemTable[row][col] + ",";
            }
            processor.append(line);
            processor.newLine();

        }

        return processor.getTable();
    }

    private String getItemName(int fileIdx)
    {
        ItemTableEntry itemTableEntry;
        for (int i = 0; i < itemTableData.size(); i++)
        {
            itemTableEntry = itemTableData.get(i);
            if (itemTableEntry.getDataArchive() == fileIdx)
            {
                return itemData[i];
            }
        }
        return "INVALID_NAME";
    }

    private void setItemName(int fileIdx, String name)
    {
        ItemTableEntry itemTableEntry;
        for (int i = 0; i < itemTableData.size(); i++)
        {
            itemTableEntry = itemTableData.get(i);
            if (itemTableEntry.getDataArchive() == fileIdx)
            {
                itemData[i] = name;
            }
        }
    }


    public void sheetToItems(Object[][] itemSheet, String outputDir, String predictedOutputNarc) throws IOException
    {
        String outputPath= outputDir;

        if(!new File(outputPath).exists() && !new File(outputPath).mkdir())
        {
            throw new RuntimeException("Could not create output directory. Check write permissions");
        }
        outputPath+= File.separator;

        Object[] nameColumn= Arrays.copyOfRange(ArrayModifier.getColumn(itemSheet,1),1,itemSheet.length);
        itemSheet= ArrayModifier.trim(itemSheet,1,2);

        BinaryWriter writer;
        for(int i= 0; i < itemSheet.length; i++)
        {
            System.out.println(getItemName(i));
            Object[] thisLine= itemSheet[i];
            initializeIndex(thisLine);

            writer= new BinaryWriter(outputPath + i + ".bin");

            writer.writeShort(Short.parseShort(next())); //price
            writer.writeByte((byte) Short.parseShort(next())); //equipment effect
            writer.writeByte((byte) Short.parseShort(next())); //power
            writer.writeByte((byte) getPluckEffect(next())); //pluck effect
            writer.writeByte((byte) getPluckEffect(next())); //fling effect
            writer.writeByte((byte) Short.parseShort(next())); //fling power
            writer.writeByte((byte) Short.parseShort(next())); //natural gift power

            String bitField1= "";
            bitField1+= fixString(getType(next()),5); //natural gift type
            bitField1= (Boolean.parseBoolean(next()) ? 1 : 0) + bitField1; //unable to discard
            bitField1= (Boolean.parseBoolean(next()) ? 1 : 0) + bitField1; //can register
            bitField1= fixString(getFieldPocket(next()),4) + bitField1; //field bag pocket
            bitField1= fixString(getBattlePocket(next()),5) + bitField1; //battle bag pocket
            writer.writeShort((short) Integer.parseInt(bitField1,2));

            writer.writeByte((byte)getFieldFunction(next())); //field function
            writer.writeByte((byte)getBattleFunction(next())); //battle function
            writer.writeByte((byte)(next().equalsIgnoreCase(workTypes[1].toLowerCase()) ? 1 : 0));
            writer.writeByte((byte) 0);

            BitStream bitStream= new BitStream(); //recovery information 1 (sleep, poison, burn, freeze, paralyze, confusion, attract, guard spec)
            for(int x= 0; x < 8; x++)
            {
                bitStream.append(Boolean.parseBoolean(next()));
            }
            writer.writeByte(bitStream.toBytes()[0]);

            String utility1= "";
            utility1+= (Boolean.parseBoolean(next()) ? 1 : 0); //revive
            utility1= (Boolean.parseBoolean(next()) ? 1 : 0) + utility1; //revive all party members
            utility1= (Boolean.parseBoolean(next()) ? 1 : 0) + utility1; //rare candy (level up) effect
            utility1= (Boolean.parseBoolean(next()) ? 1 : 0) + utility1; //function as evolution stone
            utility1= fixString(next(),4) + utility1; //battle atk stat boost
            writer.writeByte((byte) Short.parseShort(utility1,2));

            String battle1= "";
            battle1+= fixString(next(),4); //battle def stat boost
            battle1= fixString(next(),4) + battle1; //battle spAtk stat boost
            writer.writeByte((byte) Short.parseShort(battle1,2));

            String battle2= "";
            battle2+= fixString(next(),4); //battle spDef stat boost
            battle2= fixString(next(),4) + battle2; //battle speed stat boost
            writer.writeByte((byte) Short.parseShort(battle2,2));

            String battle3= "";
            battle3+= fixString(next(),4); //battle accuracy stat boost
            battle3= fixString(next(),2) + battle3; //battle speed stat boost
            battle3= (Boolean.parseBoolean(next()) ? 1 : 0) + battle3; //PP up effect
            battle3= (Boolean.parseBoolean(next()) ? 1 : 0) + battle3; //PP Max effect
            writer.writeByte((byte) Short.parseShort(battle3,2));

            bitStream= new BitStream(); //recovery information 2/ ev information 1 (pp recovery, recover all pp, hp recovery, hp ev change, atk ev change, def ev change, speed ev change, spAtk ev change)
            for(int x= 0; x < 8; x++)
            {
                bitStream.append(Boolean.parseBoolean(next()));
            }
            writer.writeByte(bitStream.toBytes()[0]);

            bitStream= new BitStream(); //final bit field (spDef ev change, friendship change 1, friendship change 2, friendship change 3)
            for(int x= 0; x < 4; x++)
            {
                bitStream.append(Boolean.parseBoolean(next()));
            }
            writer.writeByte(bitStream.toBytes()[0]);

            for(int x= 0; x < 6; x++) //increase amounts (hp ev amount, atk ev amount, def ev amount, speed ev amount, spAtk ev amount, spDef ev amount)
            {
                writer.writeByte(Byte.parseByte(next()));
            }
            writer.writeByte((byte) Short.parseShort(next())); //HP recovery amount
            writer.writeByte((byte) Short.parseShort(next())); //PP recovery amount
            for(int x= 0; x < 3; x++) //increase amounts (friendship amount 1, friendship amount 2, friendship amount 3)
            {
                writer.writeByte(Byte.parseByte(next()));
            }
            writer.writeByteNumTimes((byte) 0,2);
            writer.close();
        }

        int itemNameBank;
        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                itemNameBank= 344;
                break;

            case Platinum:
                itemNameBank= 392;
                break;

            case HeartGold:
            case SoulSilver:
                itemNameBank= 222;
                break;

            default:
                throw new RuntimeException("Invalid base rom: " + baseRom);
        }


        ArrayList<String> newItemNames = new ArrayList<>();
        for(int i = 0; i < nameColumn.length; i++)
        {
            if(i < itemData.length)
            {
                if(!nameColumn[i].equals(getItemName(i)))
                {
                    setItemName(i, (String) nameColumn[i]);
                }
            }
            else
            {
                newItemNames.add((String) nameColumn[i]);
            }
        }

        if(newItemNames.size() != 0)
        {
            ArrayList<String> itemNames= new ArrayList<>(Arrays.asList(itemData));
            itemNames.addAll(newItemNames);
            itemData = itemNames.toArray(itemNames.toArray(new String[0]));
        }



        boolean canTrim= true;
        if(new File(predictedOutputNarc).exists())
        {
            int numOriginalFiles= Narctowl.getNumFiles(predictedOutputNarc);

            if(itemSheet.length > numOriginalFiles)
            {
                canTrim= false;
            }
        }

        TextEditor.writeBank(project,nameColumn,itemNameBank,false); //TODO return here and change this
    }








    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(ItemEditorGen4::fileToInt));
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

    private static String fixString(String num, int len)
    {
        String in= Integer.toBinaryString(Integer.parseInt(num));
        while(in.length() != len)
        {
            in= "0" + in;
        }
        return in;
    }

    private static String fixString(int num, int len)
    {
        String in= Integer.toBinaryString(num);
        if(in.length() > len)
        {
            throw new RuntimeException("Invalid target length: " + len + ", for binary string: " + in);
        }
        while(in.length() != len)
        {
            in= "0" + in;
        }
        return in;
    }

    private static int getType(String type)
    {
        if(type.equalsIgnoreCase("none"))
        {
            return 31;
        }

        for(int i= 0; i < typeArr.length; i++)
        {
            if(type.equals(typeArr[i]))
            {
                return i;
            }
        }

        throw new RuntimeException("Invalid type entered: " + type);
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

    private static int getItem(String item)
    {
        item= item.replaceAll("\\?","e").replaceAll("é","e");
        for(int i = 0; i < itemData.length; i++)
        {
            if(item.equals(itemData[i].replaceAll("\\?","e").replaceAll("é","e")))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid item entered: " + item);
    }

    private static int getFieldPocket(String pocket)
    {
        for(int i = 0; i < fieldBagPockets.length; i++)
        {
            if(pocket.equals(fieldBagPockets[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid field pocket entered: " + pocket);
    }

    private static int getBattlePocket(String pocket)
    {
        for(int i = 0; i < battleBagPockets.length; i++)
        {
            if(pocket.equals(battleBagPockets[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid battle pocket entered: " + pocket);
    }

    private static int getFieldFunction(String function)
    {
        function= function.replaceAll("\\?","e").replaceAll("é","e");
        for(int i = 0; i < fieldFunctions.length; i++)
        {
            if(function.equals(fieldFunctions[i].replaceAll("\\?","e").replaceAll("é","e")))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid field function entered: " + function);
    }

    private static int getBattleFunction(String function)
    {
        for(int i = 0; i < battleFunctions.length; i++)
        {
            if(function.equals(battleFunctions[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid battle function entered: " + function);
    }

    private static int getPluckEffect(String effect)
    {
        for(int i= 0; i < pluckFlingEffects.length; i++)
        {
            if(effect.equals(pluckFlingEffects[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid pluck effect entered: " + effect);
    }
}
