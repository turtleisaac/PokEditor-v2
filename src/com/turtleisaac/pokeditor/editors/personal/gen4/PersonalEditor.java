package com.turtleisaac.pokeditor.editors.personal.gen4;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class PersonalEditor
{

//    private static String path= System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String projectPath;
    private String dataPath= "";
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "???", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] eggGroupArr= {"~","Monster","Water 1","Bug","Flying","Field","Fairy","Grass","Human-Like","Water 3","Mineral","Amorphous","Water 2","Ditto","Dragon","Undiscovered"};
    private static final String[] growthTableIdArr= {"Medium Fast","Erratic","Fluctuating","Medium Slow","Fast","Slow","Medium Fast","Medium Fast"};
    private static String resourcePath;
    private static String defaultsPath;
    private static String[] nameData;
    private static Object[] tmData;
    private static String[] itemData;
    private static String[] abilityData;
    private static String[] tmNameData;
    private static boolean autoFix;
    private Project project;
    private Game baseRom;

    public PersonalEditor(String projectPath, Project project) throws IOException
    {
        this.project= project;
        this.baseRom= project.getBaseRom();
        this.projectPath= projectPath;
        dataPath= projectPath;
        resourcePath= new File(projectPath).getParentFile().getParent() + File.separator + "Program Files" + File.separator;
        defaultsPath= resourcePath + "Defaults" + File.separator;

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

        int oldLength= nameData.length;;
        nameData= Arrays.copyOf(nameData,oldLength + 12);
        nameData[oldLength++]= "Deoxys (A)";
        nameData[oldLength++]= "Deoxys (D)";
        nameData[oldLength++]= "Deoxys (S)";
        nameData[oldLength++]= "Wormadam (S)";
        nameData[oldLength++]= "Wormadam (T)";
        nameData[oldLength++]= "Giratina (O)";
        nameData[oldLength++]= "Shaymin (S)";
        nameData[oldLength++]= "Rotom (Heat)";
        nameData[oldLength++]= "Rotom (Wash)";
        nameData[oldLength++]= "Rotom (Frost)";
        nameData[oldLength++]= "Rotom (Fan)";
        nameData[oldLength]= "Rotom (Mow)";

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "TmList.txt"));
        String line;
        ArrayList<Object> tmList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            tmList.add(line);
        }
        tmData= tmList.toArray(new Object[0]);
        reader.close();
    }

    public PersonalReturnGen4 personalToSheet(String personalDir) throws IOException
    {
        String tmNameFile= "TmNameList";

        switch (baseRom)
        {
            case HeartGold:
            case SoulSilver:
                tmNameFile+= "Johto.txt";

                break;
            case Platinum:
            case Pearl:
            case Diamond:
                tmNameFile+= "Sinnoh.txt";
                break;
            default:
                throw new RuntimeException("Invalid rom header: Game Code/ Title");
        }

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + tmNameFile));
        ArrayList<String> tmNameList= new ArrayList<>();
        String line;

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            tmNameList.add(line);
        }
        tmNameData= tmNameList.toArray(new String[0]);
        reader.close();

//        Scanner scanner= new Scanner(System.in);
//        System.out.println("Do you wish to toggle automatic correction of incorrect/ broken data? (Y/n) (If the rom you are editing has an expanded move, ability, type, or item table, and you have not yet adjusted the data in the \"Program Files\" directory, it is safest to say no)");
//        autoFix= !scanner.nextLine().equalsIgnoreCase("n");
        autoFix= false;

        dataPath= personalDir;

        Buffer personalBuffer;
        ArrayList<PersonalData> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        if(files.length > nameData.length)
            nameData= ArrayModifier.accommodateLength(nameData,files.length);

        int count= 0;

        for(int i= 0; i < files.length; i++)
        {
            int localCount= count;
            System.out.println(nameData[i]);
            file= files[i];
            personalBuffer= new Buffer(file.toString());

            int hp= personalBuffer.readByte();
            int atk= personalBuffer.readByte();
            int def= personalBuffer.readByte();
            int spe= personalBuffer.readByte();
            int spAtk= personalBuffer.readByte();
            int spDef= personalBuffer.readByte();
            int type1= personalBuffer.readByte();
            int type2= personalBuffer.readByte();
            int catchRate= personalBuffer.readByte();
            int baseExp= personalBuffer.readByte();

            short evYields= personalBuffer.readShort();
            int hpYield= getHp(evYields);
            int speYield= getSpe(evYields);
            int atkYield= getAtk(evYields);
            int defYield= getDef(evYields);
            int spAtkYield= getSpAtk(evYields);
            int spDefYield= getSpDef(evYields);
            int evPadded= getPadded(evYields);

            int uncommonItem= personalBuffer.readShort();
            int rareItem= personalBuffer.readShort();

            int genderRatio= personalBuffer.readByte();
            int hatchMultiplier= personalBuffer.readByte();
            int baseHappiness= personalBuffer.readByte();
            int expRate= personalBuffer.readByte();
            int eggGroup1= personalBuffer.readByte();
            int eggGroup2= personalBuffer.readByte();
            int ability1= personalBuffer.readByte();
            int ability2= personalBuffer.readByte();

            int runChance= personalBuffer.readByte();
            int dexColor= personalBuffer.readByte();
            personalBuffer.readShort(); // 2 bytes padding
            byte[] tmLearnset= personalBuffer.readBytes(16);


            dataList.add(new PersonalData() {
                @Override
                public int getNum() {
                    return localCount;
                }

                @Override
                public int getHP() {
                    return hp;
                }

                @Override
                public int getAtk() {
                    return atk;
                }

                @Override
                public int getDef() {
                    return def;
                }

                @Override
                public int getSpe() {
                    return spe;
                }

                @Override
                public int getSpAtk() {
                    return spAtk;
                }

                @Override
                public int getSpDef() {
                    return spDef;
                }

                @Override
                public int getType1() {
                    return type1;
                }

                @Override
                public int getType2() {
                    return type2;
                }

                @Override
                public int getCatchRate() {
                    return catchRate;
                }

                @Override
                public int getBaseExp() {
                    return baseExp;
                }

                @Override
                public int getHpEv() {
                    return hpYield;
                }

                @Override
                public int getSpeEv() {
                    return speYield;
                }

                @Override
                public int getAtkEv() {
                    return atkYield;
                }

                @Override
                public int getDefEv() {
                    return defYield;
                }

                @Override
                public int getSpAtkEv() {
                    return spAtkYield;
                }

                @Override
                public int getSpDefEv() {
                    return spDefYield;
                }

                @Override
                public int getPadding() {
                    return evPadded;
                }

                @Override
                public int getUncommonItem() {
                    return uncommonItem;
                }

                @Override
                public int getRareItem() {
                    return rareItem;
                }

                @Override
                public int getGenderRatio() {
                    return genderRatio;
                }

                @Override
                public int getHatchMultiplier() {
                    return hatchMultiplier;
                }

                @Override
                public int getBaseHappiness() {
                    return baseHappiness;
                }

                @Override
                public int getExpRate() {
                    return expRate;
                }

                @Override
                public int getEggGroup1() {
                    return eggGroup1;
                }

                @Override
                public int getEggGroup2() {
                    return eggGroup2;
                }

                @Override
                public int getAbility1() {
                    return ability1;
                }

                @Override
                public int getAbility2() {
                    return ability2;
                }

                @Override
                public int getRunChance() {
                    return runChance;
                }

                @Override
                public int getDexColor() {
                    return dexColor;
                }

                @Override
                public boolean getTm(int idx) {
                    assert idx >= 0 && idx < 128;
                    return (tmLearnset[idx/8] & 1<<(idx%8)) != 0;
                }
            });

            personalBuffer.close();
            count++;
        }


        String[][] pokeTable= new String[dataList.size()+1][30];
        for(int row= 0; row < dataList.size(); row++)
        {
            pokeTable[row][0]= "" + dataList.get(row).getNum();
            pokeTable[row][1]= nameData[row];
            pokeTable[row][2]= "" + dataList.get(row).getHP();
            pokeTable[row][3]= "" + dataList.get(row).getAtk();
            pokeTable[row][4]= "" + dataList.get(row).getDef();
            pokeTable[row][5]= "" + dataList.get(row).getSpe();
            pokeTable[row][6]= "" + dataList.get(row).getSpAtk();
            pokeTable[row][7]= "" + dataList.get(row).getSpDef();
            pokeTable[row][8]= typeArr[dataList.get(row).getType1()];
            pokeTable[row][9]= typeArr[dataList.get(row).getType2()];
            pokeTable[row][10]= "" + dataList.get(row).getCatchRate();
            pokeTable[row][11]= "" + dataList.get(row).getBaseExp();
            pokeTable[row][12]= "" + dataList.get(row).getHpEv();
            pokeTable[row][13]= "" + dataList.get(row).getSpeEv();
            pokeTable[row][14]= "" + dataList.get(row).getAtkEv();
            pokeTable[row][15]= "" + dataList.get(row).getDefEv();
            pokeTable[row][16]= "" + dataList.get(row).getSpAtkEv();
            pokeTable[row][17]= "" + dataList.get(row).getSpDefEv();
            pokeTable[row][18]= "='Formatting (DO NOT TOUCH)'!$A$" + (dataList.get(row).getUncommonItem()+1);
            pokeTable[row][19]= "='Formatting (DO NOT TOUCH)'!$A$" + (dataList.get(row).getRareItem()+1);
            pokeTable[row][20]= "" + dataList.get(row).getGenderRatio();
            pokeTable[row][21]= "" + dataList.get(row).getHatchMultiplier();
            pokeTable[row][22]= "" + dataList.get(row).getBaseHappiness();
            pokeTable[row][23]= growthTableIdArr[dataList.get(row).getExpRate()];
            pokeTable[row][24]= eggGroupArr[dataList.get(row).getEggGroup1()];
            pokeTable[row][25]= eggGroupArr[dataList.get(row).getEggGroup2()];
            pokeTable[row][26]= "='Formatting (DO NOT TOUCH)'!$C$" + (dataList.get(row).getAbility1()+1);
            pokeTable[row][27]= "='Formatting (DO NOT TOUCH)'!$C$" + (dataList.get(row).getAbility2()+1);
            pokeTable[row][28]= "" + dataList.get(row).getRunChance();
            pokeTable[row][29]= "" + dataList.get(row).getDexColor();
        }

        ArrayProcessor processor= new ArrayProcessor();
        processor.append("ID Number,Name,HP,Attack,Defense,Speed,Sp. Atk,Sp. Def,Type 1,Type 2,Catch Rate,Exp Drop,HP EV Drop,Attack EV Drop,Defense EV Drop,Speed EV Drop,Sp. Atk EV Drop,Sp. Def EV Drop,Uncommon Held Item,Rare Held Item,Gender Ratio,Hatch Multiplier,Base Happiness,Growth Rate,Egg Group 1,Egg Group 2,Ability 1,Ability 2,Run Chance (Safari Zone only),DO NOT TOUCH");
        processor.newLine();
        for(int row= 0; row < dataList.size(); row++)
        {
            line= "";
            for(int col= 0; col < pokeTable[0].length; col++)
            {
                line+= pokeTable[row][col] + ",";
            }
            processor.append(line);
            processor.newLine();
        }
        Object[][] personalSheet= processor.getTable();



        String[][] tmTable= new String[nameData.length][100];
        for(int mon= 0; mon < dataList.size(); mon++)
        {
            for(int tm= 0; tm < 100; tm++)
            {
                tmTable[mon][tm]= Boolean.toString(dataList.get(mon).getTm(tm));
            }
        }

        processor= new ArrayProcessor();
        processor.append("ID Number,Name,");
        for(int i= 0; i < 100; i++)
        {
            processor.append(tmData[i] + ",");
        }
        processor.newLine();
        processor.append(",,");
        for(int i= 0; i < 100; i++)
        {
            processor.append(tmNameData[i] + ",");
        }
        processor.newLine();
        for(int row= 0; row < dataList.size(); row++)
        {
            line= dataList.get(row).getNum() + ",=Personal!$B$" + (row+2) + ",";
            for(int col= 0; col < tmTable[0].length; col++)
            {
                line+= tmTable[row][col] + ",";
            }
            processor.append(line);
            processor.newLine();
        }
        Object[][] tmSheet= processor.getTable();




        return new PersonalReturnGen4()
        {
            @Override
            public Object[][] getPersonalData()
            {
                return personalSheet;
            }

            @Override
            public Object[][] getTMData()
            {
                return tmSheet;
            }
        };
    }



    public void csvToPersonal(Object[][] personalCsv, Object[][] tmLearnsetCsv, String outputDir, String predictedNarcOutput) throws IOException
    {
        String outputPath= outputDir;

        Object[] nameColumn= Arrays.copyOfRange(ArrayModifier.getColumn(personalCsv,1),1,personalCsv.length);
        Object[] tmList= Arrays.copyOfRange(tmLearnsetCsv[1],2,tmLearnsetCsv[1].length);

        personalCsv= ArrayModifier.trim(personalCsv,1,2);
        tmLearnsetCsv= ArrayModifier.trim(tmLearnsetCsv,2,2);

        BitStream[] tmLearnsetData = new BitStream[tmLearnsetCsv.length];

        if(!new File(outputPath).exists())
        {
            if(!new File(outputPath).mkdir())
            {
                throw new RuntimeException("Could not create output directory");
            }
        }

        ArrayList<PersonalData> personalData= new ArrayList<>();
        for(int row= 0; row < personalCsv.length; row++)
        {
//            System.out.println(nameData[row] + " (" + row + "): " + Arrays.toString(personalCsv[row]));
            initializeIndex(personalCsv[row]);
            int finalRow = row;

            int hp= Integer.parseInt(next());
            int atk= Integer.parseInt(next());
            int def= Integer.parseInt(next());
            int spe= Integer.parseInt(next());
            int spAtk= Integer.parseInt(next());
            int spDef= Integer.parseInt(next());

            int type1= getType(next());
            int type2= getType(next());
            int catchRate= Integer.parseInt(next());
            int baseExp= Integer.parseInt(next());

            int hpEv= Integer.parseInt(next());
            int speEv= Integer.parseInt(next());
            int atkEv= Integer.parseInt(next());
            int defEv= Integer.parseInt(next());
            int spAtkEv= Integer.parseInt(next());
            int spDefEv= Integer.parseInt(next());

            int item1= getItem(next());
            int item2= getItem(next());
            int genderRatio= Integer.parseInt(next());
            int hatchMultiplier= Integer.parseInt(next());
            int baseHappiness= Integer.parseInt(next());
            int expRate= getGrowthRate(next());

            int eggGroup1= getEggGroup(next());
            int eggGroup2= getEggGroup(next());
            int ability1= getAbility(next());
            int ability2= getAbility(next());
            int runChance= Integer.parseInt(next());
            int dexColor= Integer.parseInt(next());

            personalData.add(new PersonalData() {
                @Override
                public int getNum() {
                    return finalRow;
                }

                @Override
                public int getHP() {
                    return hp;
                }

                @Override
                public int getAtk() {
                    return atk;
                }

                @Override
                public int getDef() {
                    return def;
                }

                @Override
                public int getSpe() {
                    return spe;
                }

                @Override
                public int getSpAtk() {
                    return spAtk;
                }

                @Override
                public int getSpDef() {
                    return spDef;
                }

                @Override
                public int getType1() {
                    return type1;
                }

                @Override
                public int getType2() {
                    return type2;
                }

                @Override
                public int getCatchRate() {
                    return catchRate;
                }

                @Override
                public int getBaseExp() {
                    return baseExp;
                }

                @Override
                public int getHpEv() {
                    return hpEv;
                }

                @Override
                public int getSpeEv() {
                    return speEv;
                }

                @Override
                public int getAtkEv() {
                    return atkEv;
                }

                @Override
                public int getDefEv() {
                    return defEv;
                }

                @Override
                public int getSpAtkEv() {
                    return spAtkEv;
                }

                @Override
                public int getSpDefEv() {
                    return spDefEv;
                }

                @Override
                public int getPadding() {
                    return 0;
                }

                @Override
                public int getUncommonItem() {
                    return item1;
                }

                @Override
                public int getRareItem() {
                    return item2;
                }

                @Override
                public int getGenderRatio() {
                    return genderRatio;
                }

                @Override
                public int getHatchMultiplier() {
                    return hatchMultiplier;
                }

                @Override
                public int getBaseHappiness() {
                    return baseHappiness;
                }

                @Override
                public int getExpRate() {
                    return expRate;
                }

                @Override
                public int getEggGroup1() {
                    return eggGroup1;
                }

                @Override
                public int getEggGroup2() {
                    return eggGroup2;
                }

                @Override
                public int getAbility1() {
                    return ability1;
                }

                @Override
                public int getAbility2() {
                    return ability2;
                }

                @Override
                public int getRunChance() {
                    return runChance;
                }

                @Override
                public int getDexColor()
                {
                    return dexColor;
                }

                @Override
                public boolean getTm(int idx) {
                    return false;
                }
            });
        }


        for(int i= 0; i < personalCsv.length; i++)
        {
            tmLearnsetData[i] = new BitStream();


            for (Object obj : tmLearnsetCsv[i]) {
                tmLearnsetData[i].append(Boolean.parseBoolean((String) obj));
            }

            tmLearnsetData[i].append(false, 28);
        }

        outputPath+= File.separator;
        BinaryWriter writer;
        for(int i= 0; i < personalData.size(); i++)
        {

            PersonalData data= personalData.get(i);
            writer= new BinaryWriter(new File(outputPath + i + ".bin"));
            writer.writeBytes(data.getHP(), data.getAtk(), data.getDef(), data.getSpe(), data.getSpAtk(), data.getSpDef(), data.getType1(), data.getType2(), data.getCatchRate(), data.getBaseExp());
            writer.writeShort(parseShort("0000", evYieldBinary(data.getSpDefEv()), evYieldBinary(data.getSpAtkEv()), evYieldBinary(data.getDefEv()), evYieldBinary(data.getAtkEv()), evYieldBinary(data.getSpeEv()), evYieldBinary(data.getHpEv())));
            writer.writeShort((short)data.getUncommonItem());
            writer.writeShort((short)data.getRareItem());
            writer.writeBytes(data.getGenderRatio(),data.getHatchMultiplier(),data.getBaseHappiness(),data.getExpRate(),data.getEggGroup1(),data.getEggGroup2(),data.getAbility1(),data.getAbility2(),data.getRunChance(),data.getDexColor());
            writer.writeBytes(0x00,0x00);
            System.out.println(i + ":   " + tmLearnsetData[i]);
            writer.write(tmLearnsetData[i].toBytes());
        }


        int nameBank;
        switch(project.getBaseRom())
        {
            case Platinum:
                nameBank= 412;
                break;

            case HeartGold:
            case SoulSilver:
                nameBank= 237;
                break;

            default:
                throw new RuntimeException("Invalid base rom: " + baseRom);
        }

        boolean canTrim= true;
        if(new File(predictedNarcOutput).exists())
        {
            int numOriginalFiles= Narctowl.getNumFiles(predictedNarcOutput);

            if(personalData.size() > numOriginalFiles)
            {
                canTrim= false;
            }

        }

        TextEditor.writeBank(project,nameColumn,nameBank,canTrim);
    }




    private void sort (File[] arr)
    {
        Arrays.sort(arr, Comparator.comparingInt(PersonalEditor::fileToInt));
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

    private static int getHp (short x)
    {
        return x & 0x03;
    }

    private static int getSpe (short x)
    {
        return (x >> 2) & 0x03;
    }

    private static int getAtk (short x)
    {
        return (x >> 4) & 0x03;
    }

    private static int getDef (short x)
    {
        return (x >> 6) & 0x03;
    }

    private static int getSpAtk (short x)
    {
        return (x >> 8) & 0x03;
    }

    private static int getSpDef (short x)
    {
        return (x >> 10) & 0x03;
    }

    private static int getPadded (short x)
    {
        return (x >> 12) & 0x0F;
    }

    private static int getType(String type)
    {
        for(int i= 0; i < typeArr.length; i++)
        {
            if(type.equals(typeArr[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid type entered: " + type);
    }

    private static int getEggGroup(String group)
    {
        for(int i= 0; i < eggGroupArr.length; i++)
        {
            if(group.equals(eggGroupArr[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid egg group entered: " + group);
    }

    private static int getAbility(String ability)
    {
        for(int i= 0; i < abilityData.length; i++)
        {
            if(ability.trim().equals(abilityData[i].trim()))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid ability entered: " + ability);
    }

    private static int getItem(String item)
    {
        for(int i= 0; i < itemData.length; i++)
        {
            if(item.equals(itemData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid item entered: " + item);
    }

    private static int getGrowthRate(String growthRate)
    {
        for(int i= 0; i < growthTableIdArr.length; i++)
        {
            if(growthRate.equals(growthTableIdArr[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid growth rate id entered: " + growthRate);
    }

    private static String evYieldBinary(int num)
    {
        if(num > 3)
        {
            throw new RuntimeException("Invalid ev yield entered");
        }

        if(num == 0)
        {
            return "00";
        }
        else if(num == 1)
        {
            return "01";
        }
        else if(num == 2)
        {
            return "10";
        }
        else
        {
            return "11";
        }
    }

    private static short parseShort(String... evs)
    {
        String fullEv= "";
        for(String ev : evs)
        {
            fullEv+= ev;
        }

        short num= 0;
        for(int i= 0; i < fullEv.length(); i++)
        {
            String thisEv= fullEv.substring(fullEv.length()-i-1,fullEv.length()-i);
            if(thisEv.equals("1"))
            {
                num+= Math.pow(2,i);
            }
        }
        return num;
    }

    private static byte[] parseShorts(short[] shorts)
    {
        byte[] buf= new byte[shorts.length*2];
        for(int i= 0; i < shorts.length; i+=2)
        {
            short s= shorts[i];
            buf[i] = (byte) (s & 0xff);
            buf[i+1] = (byte) ((s >> 8) & 0xff);
        }
        return buf;
    }

    private static long parseLong(String fullLong)
    {
        long num= 0;
        for(int i= 0; i < fullLong.length(); i++)
        {
            String thisEv= fullLong.substring(fullLong.length()-i-1,fullLong.length()-i);
            if(thisEv.equals("1"))
            {
                num+= Math.pow(2,i);
            }
        }
        return num;
    }

    private static String[] reverse(String[] arr)
    {
        for(int i= 0; i < arr.length/2; i++)
        {
            String foo= arr[i];
            arr[i]= arr[arr.length-i-1];
            arr[arr.length-i-1]= foo;
        }
        return arr;
    }

    private static byte toByte(String[] arr) {
        int ret = 0;
        for (int i=0; i<arr.length; i++) {
            ret |= (Boolean.parseBoolean(arr[i])?1:0) << i;
        }
        return (byte) ret;
    }
}
