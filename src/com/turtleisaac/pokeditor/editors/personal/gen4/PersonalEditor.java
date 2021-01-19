package com.turtleisaac.pokeditor.editors.personal.gen4;

import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.project.Game;

import java.io.*;
import java.util.*;

public class PersonalEditor
{
    public static void main(String[] args) throws IOException
    {
        PersonalEditor editor= new PersonalEditor(System.getProperty("user.dir") + "/data",Game.Platinum);
        editor.personalToSheet("/poketool/personal/pl_personal");
    }


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
    private Game baseRom;

    public PersonalEditor(String projectPath, Game baseRom) throws IOException
    {
        this.baseRom= baseRom;
        this.projectPath= projectPath;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf("/"));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf("/")) + File.separator + "Program Files" + File.separator;
        defaultsPath= resourcePath + "Defaults" + File.separator;

        String itemPath= resourcePath;

        switch(baseRom)
        {
            case Pearl:
            case Diamond:
                itemPath+= "ItemListDP.txt";
                break;

            case Platinum:
                itemPath+= "ItemListPt.txt";
                break;

            case HeartGold:
            case SoulSilver:
                itemPath+= "ItemListJohto.txt";
                break;

            default :
                throw new RuntimeException("Invalid rom header: Game Code/ Title");
        }

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "EntryData.txt"));
        ArrayList<String> nameList= new ArrayList<>();
        String line;
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            nameList.add(line);
        }
        nameData= nameList.toArray(new String[0]);
        reader.close();


        reader= new BufferedReader(new FileReader(resourcePath + "TmList.txt"));
        ArrayList<Object> tmList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            tmList.add(line);
        }
        tmData= tmList.toArray(new Object[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(itemPath));
        ArrayList<String> itemList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            itemList.add(line);
        }
        itemData= itemList.toArray(new String[0]);
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

        dataPath+= personalDir;

        Buffer personalBuffer;
        ArrayList<PersonalData> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        CsvReader csvReader= new CsvReader(defaultsPath + "personal4.csv");

        int count= 0;

        for(int i= 0; i < files.length; i++)
        {
            int localCount= count;
            System.out.println(nameData[i]);
            file= files[i];
            personalBuffer= new Buffer(file.toString());
            initializeIndex(csvReader.next());

            int hp= personalBuffer.readByte();
            int atk= personalBuffer.readByte();
            int def= personalBuffer.readByte();
            int spe= personalBuffer.readByte();
            int spAtk= personalBuffer.readByte();
            int spDef= personalBuffer.readByte();
            int type1;
            int type2;
            if(autoFix)
            {
                type1= personalBuffer.readSelectiveByte(typeArr.length-1,getType(next()));
                type2= personalBuffer.readSelectiveByte(typeArr.length-1,getType(next()));
            }
            else
            {
                type1= personalBuffer.readByte();
                type2= personalBuffer.readByte();
            }
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

            int uncommonItem;
            int rareItem;
            if(autoFix)
            {
                uncommonItem= personalBuffer.readSelectiveShort(itemData.length-1, (short) getItem(next()));
                rareItem= personalBuffer.readSelectiveShort(itemData.length-1, (short) getItem(next()));
            }
            else
            {
                uncommonItem= personalBuffer.readShort();
                rareItem= personalBuffer.readShort();
            }
            int genderRatio= personalBuffer.readByte();
            int hatchMultiplier= personalBuffer.readByte();
            int baseHappiness= personalBuffer.readByte();
            int expRate;
            int eggGroup1;
            int eggGroup2;
            int ability1;
            int ability2;
            if (autoFix)
            {
                expRate = personalBuffer.readSelectiveByte(growthTableIdArr.length-1,getGrowthRate(next()));
                eggGroup1= personalBuffer.readSelectiveByte(eggGroupArr.length-1,getEggGroup(next()));
                eggGroup2= personalBuffer.readSelectiveByte(eggGroupArr.length-1,getEggGroup(next()));
                ability1= personalBuffer.readSelectiveByte(abilityData.length-1,getAbility(next()));
                ability2= personalBuffer.readSelectiveByte(abilityData.length-1,getAbility(next()));
            }
            else
            {
                expRate= personalBuffer.readByte();
                eggGroup1= personalBuffer.readByte();
                eggGroup2= personalBuffer.readByte();
                ability1= personalBuffer.readByte();
                ability2= personalBuffer.readByte();
            }
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
            pokeTable[row][18]= itemData[dataList.get(row).getUncommonItem()];
            pokeTable[row][19]= itemData[dataList.get(row).getRareItem()];
            pokeTable[row][20]= "" + dataList.get(row).getGenderRatio();
            pokeTable[row][21]= "" + dataList.get(row).getHatchMultiplier();
            pokeTable[row][22]= "" + dataList.get(row).getBaseHappiness();
            pokeTable[row][23]= growthTableIdArr[dataList.get(row).getExpRate()];
            pokeTable[row][24]= eggGroupArr[dataList.get(row).getEggGroup1()];
            pokeTable[row][25]= eggGroupArr[dataList.get(row).getEggGroup2()];
            pokeTable[row][26]= abilityData[dataList.get(row).getAbility1()];
            pokeTable[row][27]= abilityData[dataList.get(row).getAbility2()];
            pokeTable[row][28]= "" + dataList.get(row).getRunChance();
            pokeTable[row][29]= "" + dataList.get(row).getDexColor();
        }

        ArrayProcessor processor= new ArrayProcessor();
        processor.append("ID Number,Name,HP,Attack,Defense,Speed,Sp. Atk,Sp. Def,Type 1,Type 2,Catch Rate,Exp Drop,HP EV Yield,Spe EV Yield,Attack EV Yield,Defense EV Yield,Sp. Atk EV Yield,Sp. Def EV Yield,Uncommon Held Item,Rare Held Item,Gender Ratio,Hatch Multiplier,Base Happiness,Growth Rate,Egg Group 1,Egg Group 2,Ability 1,Ability 2,Run Chance (Safari Zone only),DO NOT TOUCH");
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
            line= dataList.get(row).getNum() + "," + nameData[row] + ",";
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



    public void csvToPersonal(Object[][] personalCsv, Object[][] tmLearnsetCsv, String outputDir) throws IOException
    {
        String outputPath= dataPath + File.separator + outputDir;

        personalCsv= ArrayModifier.trim(personalCsv,1,2);
        tmLearnsetCsv= ArrayModifier.trim(tmLearnsetCsv,2,2);

        CsvReader defaultReader= new CsvReader(resourcePath + "Defaults" + File.separator + "personal4.csv");
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
            String[] defaults= defaultReader.next();
            System.out.println(nameData[row] + " (" + row + "): " + Arrays.toString(personalCsv[row]));
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
                    try
                    {
                        return dexColor;
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {

                        if(autoFix)
                        {
                            return Integer.parseInt(defaults[9]);
                        }
                        else
                        {
                            throw new RuntimeException("There is a problem with the provided personalDataRecompile.csv. Please try exporting/ downloading it again. If this does not work, please open a new Issue on the GitHub or contact me on Discord (https://discord.gg/cTKQq5Y).\nAdditionally, if you are seeing this message on a non-Windows computer, please let me know as quickly as possible");
                        }
                    }
                }

                @Override
                public boolean getTm(int idx) {
                    return false;
                }
            });
        }

        System.out.println("");



        for(int i= 0; i < personalCsv.length; i++)
        {
            tmLearnsetData[i] = new BitStream();

//            String[] strs = new String[tmLearnsetCsv[i].length];
//            for(int x= 0; x < strs.length; x++)
//            {
//                strs[i]= (String) tmLearnsetCsv[i][x];
//            }


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
    }

//    public void csvToPersonal2(String personalCsv, String tmLearnsetCsv, String outputDir) throws IOException
//    {
//        String personalPath= path + personalCsv;
//        String tmPath= path + tmLearnsetCsv;
//
//        String outputPath;
//        if(outputDir.contains("Recompile"))
//        {
//            outputPath= path + "temp" + File.separator+ outputDir;
//        }
//        else
//        {
//            outputPath= path + File.separator + outputDir;
//        }
//
//        int xValue;
//        int yValue;
//
//        if(!personalCsv.substring(personalCsv.length()-4).equals(".csv"))
//        {
//            throw new RuntimeException("The provided personal data file is not a .csv");
//        }
//        if(!tmLearnsetCsv.substring(tmLearnsetCsv.length()-4).equals(".csv"))
//        {
//            throw new RuntimeException("The provided TM learnset data file is not a .csv");
//        }
//
//        if(!new File(outputPath).exists() && !new File(outputPath).mkdirs())
//        {
//            throw new RuntimeException("Could not create output directory");
//        }
//
//        ArrayList<PersonalData> personalList= new ArrayList<>();
//        CsvReader personalReader= new CsvReader(personalPath);
//
//        long[][] tmLongs= new long[personalReader.length()][2];
//        CsvReader tmReader= new CsvReader(tmPath);
//        for(int i= 0; i < personalReader.length(); i++)
//        {
//            String[] mon= personalReader.next();
//            String[] tmLearnset= tmReader.next();
//            int finalI = i;
//            personalList.add(new PersonalData() {
//                @Override
//                public int getNum() {
//                    return finalI;
//                }
//
//                @Override
//                public int getHP() {
//                    return Integer.parseInt(mon[0]);
//                }
//
//                @Override
//                public int getAtk() {
//                    return Integer.parseInt(mon[1]);
//                }
//
//                @Override
//                public int getDef() {
//                    return Integer.parseInt(mon[2]);
//                }
//
//                @Override
//                public int getSpe() {
//                    return Integer.parseInt(mon[3]);
//                }
//
//                @Override
//                public int getSpAtk() {
//                    return Integer.parseInt(mon[4]);
//                }
//
//                @Override
//                public int getSpDef() {
//                    return Integer.parseInt(mon[5]);
//                }
//
//                @Override
//                public int getType1() {
//                    return getType(mon[6]);
//                }
//
//                @Override
//                public int getType2() {
//                    return getType(mon[7]);
//                }
//
//                @Override
//                public int getCatchRate() {
//                    return Integer.parseInt(mon[8]);
//                }
//
//                @Override
//                public int getBaseExp() {
//                    return Integer.parseInt(mon[9]);
//                }
//
//                @Override
//                public int getHpEv() {
//                    return Integer.parseInt(mon[10]);
//                }
//
//                @Override
//                public int getSpeEv() {
//                    return Integer.parseInt(mon[11]);
//                }
//
//                @Override
//                public int getAtkEv() {
//                    return Integer.parseInt(mon[12]);
//                }
//
//                @Override
//                public int getDefEv() {
//                    return Integer.parseInt(mon[13]);
//                }
//
//                @Override
//                public int getSpAtkEv() {
//                    return Integer.parseInt(mon[14]);
//                }
//
//                @Override
//                public int getSpDefEv() {
//                    return Integer.parseInt(mon[15]);
//                }
//
//                @Override
//                public int getPadding() {
//                    return 0;
//                }
//
//                @Override
//                public int getUncommonItem() {
//                    return getItem(mon[16]);
//                }
//
//                @Override
//                public int getRareItem() {
//                    return getItem(mon[17]);
//                }
//
//                @Override
//                public int getGenderRatio() {
//                    return Integer.parseInt(mon[18]);
//                }
//
//                @Override
//                public int getHatchMultiplier() {
//                    return Integer.parseInt(mon[19]);
//                }
//
//                @Override
//                public int getBaseHappiness() {
//                    return Integer.parseInt(mon[20]);
//                }
//
//                @Override
//                public int getExpRate() {
//                    return getGrowthRate(mon[21]);
//                }
//
//                @Override
//                public int getEggGroup1() {
//                    return getEggGroup(mon[22]);
//                }
//
//                @Override
//                public int getEggGroup2() {
//                    return getEggGroup(mon[23]);
//                }
//
//                @Override
//                public int getAbility1() {
//                    return getAbility(mon[24]);
//                }
//
//                @Override
//                public int getAbility2() {
//                    return getAbility(mon[25]);
//                }
//
//                @Override
//                public int getRunChance() {
//                    return Integer.parseInt(mon[26]);
//                }
//
//                @Override
//                public int getDexColor() {
//                    return Integer.parseInt(mon[27]);
//                }
//
//                @Override
//                public boolean getTm(int idx) {
//                    assert idx >= 0 && idx < 128;
//                    return tmLearnset[idx].toLowerCase().equals("true");
//                }
//            });
//
//            StringBuilder tmString= new StringBuilder("0000000000000000000000000000");
//            for(int tm= 100; tm != -1; tm--)
//            {
//                if(personalList.get(i).getTm(tm))
//                {
//                    tmString.append("1");
//                }
//                else
//                {
//                    tmString.append("0");
//                }
//            }
//            tmLongs[i][0]= Long.parseLong(tmString.substring(0,64),2);
//            tmLongs[i][1]= Long.parseLong(tmString.substring(64),2);
//        }
//
//        BinaryWriter writer;
//        for(int i= 0; i < personalList.size(); i++)
//        {
//            writer= new BinaryWriter(outputPath + File.separator + i + ".bin");
//            PersonalData personalData= personalList.get(i);
//
//            writer.writeBytes(personalData.getHP(),personalData.getAtk(),personalData.getDef(),personalData.getSpe(),personalData.getSpAtk(),personalData.getSpDef(),personalData.getType1(),personalData.getType2(),personalData.getCatchRate(),personalData.getBaseExp());
//            writer.writeShort(parseShort(Integer.toBinaryString(personalData.getHpEv()),Integer.toBinaryString(personalData.getSpeEv()),Integer.toBinaryString(personalData.getAtkEv()),Integer.toBinaryString(personalData.getDefEv()),Integer.toBinaryString(personalData.getSpAtkEv()),Integer.toBinaryString(personalData.getSpDefEv()),"0000"));
//            writer.writeShorts(personalData.getUncommonItem(),personalData.getRareItem());
//            writer.writeBytes();
//        }
//    }

//    public void csvReformat(String tmLearnsetCsv) throws IOException
//    {
//        ArrayList<String[][]> reformat= new ArrayList<>();
//        CsvReader csvReader= new CsvReader(path + tmLearnsetCsv);
//
//        for(int i= 0; i < csvReader.length(); i++)
//        {
//            String[][] edited= new String[4][25];
//            String[] mon= csvReader.next();
//            int idx= 0;
//            for(int row= 0; row < 4; row++)
//            {
//                if(row == 1)
//                {
//                    idx= 1;
//                }
//                if(row == 2)
//                {
//                    idx= 2;
//                }
//                if(row == 3)
//                {
//                    idx= 3;
//                }
//                for(int col= 0; col < 25; col++)
//                {
//                    edited[row][col]= mon[idx+=4];
//                }
//            }
//            reformat.add(edited);
//        }
//
//        BufferedWriter writer= new BufferedWriter(new FileWriter(path + "Reformatted TM Learnset.csv"));
//        writer.write("Dex Number,Pokémon\n");
//        for(int i= 0; i < reformat.size(); i++)
//        {
//            String[][] mon= reformat.get(i);
//            writer.write(i + "," + nameData[i] + ",");
//            int idx= 1;
//            int hm= 1;
//            for(int row= 0; row < mon.length; row++)
//            {
//                for(int col= 0; col < mon[0].length; col++)
//                {
//                    if(idx < 10)
//                    {
//                        writer.write(mon[row][col] + "," + "TM0" + idx + ",");
//                        idx++;
//                    }
//                    else if(idx <= 92)
//                    {
//                        writer.write(mon[row][col] + "," + "TM" + idx + ",");
//                        idx++;
//                    }
//                    else
//                    {
//                        writer.write(mon[row][col] + "," + "HM0" + hm + ",");
//                        hm++;
//                    }
//                }
//                if(hm != 8)
//                {
//                    writer.write("\n, ,");
//                }
//                else
//                {
//                    writer.write("\n");
//                }
//            }
//        }
//        writer.close();
//    }


    private void sort (File arr[])
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
            if(ability.equals(abilityData[i]))
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
                if(i <= 112)
                    return i;
                else
                    return i+22;
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
