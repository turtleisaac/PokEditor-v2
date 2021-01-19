package com.turtleisaac.pokeditor.editors.personal.gen5;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.BitStream;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;

import java.io.*;
import java.util.*;

public class Gen5PersonalEditor2
{

//    public static void main(String[] args) throws IOException {
//        Gen5PersonalEditor2 personalEditor= new Gen5PersonalEditor2();
//    }

    private static String path= System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath= path;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark","Fairy"};
    private static final String[] eggGroupArr= {"~","Monster","Water 1","Bug","Flying","Field","Fairy","Grass","Human-Like","Water 3","Mineral","Amorphous","Water 2","Ditto","Dragon","Undiscovered"};
    private static final String[] growthTableIdArr= {"Medium Fast","Erratic","Fluctuating","Medium Slow","Fast","Slow","Medium Fast","Medium Fast"};
    private static String resourcePath= path + "Program Files" + File.separator;
    private static String[] nameData;
    private static String[] tmData;
    private static String[] itemData;
    private static String[] abilityData;
    private static String[] tmNameData;
    private boolean b2w2;

    public Gen5PersonalEditor2(String gameCode) throws IOException
    {
        String entryPath= resourcePath;
        switch (gameCode.substring(0,3).toLowerCase())
        {
            case "irb" :
            case "irw" :
                b2w2= false;
                entryPath+= "EntryDataGen5-1.txt";
                break;

            case "ird" :
            case "ire" :
                b2w2= true;
                entryPath+= "EntryDataGen5-2.txt";
                break;
            default:
                throw new RuntimeException("Invalid game");
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

        reader= new BufferedReader(new FileReader(resourcePath + "TmListGen5.txt"));
        ArrayList<String> tmList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            tmList.add(line);
        }
        tmData= tmList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "ItemListGen5.txt"));
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

        reader= new BufferedReader(new FileReader(resourcePath + "TmNameListGen5.txt"));
        ArrayList<String> tmNameList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            tmNameList.add(line);
        }
        tmNameData= tmNameList.toArray(new String[0]);
        reader.close();
    }

    public void personalToCSV(String personalDir) throws IOException
    {
        String tempPath= path + "temp" + File.separator + "temp";

        System.out.println(tempPath);
        if(!new File(tempPath).exists() && !new File(tempPath).mkdir())
        {
            throw new RuntimeException("Could not create temp directory. Check write perms.");
        }
        tempPath+= File.separator;

        dataPath+= personalDir;

        Buffer personalBuffer;
        ArrayList<Gen5PersonalData> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;
        int count= 0;

        for(int i= 0; i < files.length-1; i++)
        {
            int localCount= count;
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
            int stage= personalBuffer.readByte();

            short evYields= personalBuffer.readShort();
            int hpYield= getHp(evYields);
            int speYield= getSpe(evYields);
            int atkYield= getAtk(evYields);
            int defYield= getDef(evYields);
            int spAtkYield= getSpAtk(evYields);
            int spDefYield= getSpDef(evYields);
            int evPadded= getPadded(evYields);


            int item1= personalBuffer.readShort();
            int item2= personalBuffer.readShort();
            int item3= personalBuffer.readShort();
            int genderRatio= personalBuffer.readByte();
            int hatchMultiplier= personalBuffer.readByte();
            int baseHappiness= personalBuffer.readByte();
            int expRate= personalBuffer.readByte();
            int eggGroup1= personalBuffer.readByte();
            int eggGroup2= personalBuffer.readByte();
            int ability1= personalBuffer.readByte();
            int ability2= personalBuffer.readByte();
            int ability3= personalBuffer.readByte();
            int runChance= personalBuffer.readByte();
            int formID= personalBuffer.readShort();
            int form= personalBuffer.readShort();
            int numForms= personalBuffer.readByte();
            int dexColor= personalBuffer.readByte();
            int baseExp= personalBuffer.readShort();
            int height= personalBuffer.readShort();
            int weight= personalBuffer.readShort();
            byte[] tmLearnset= personalBuffer.readBytes(16);

            byte[] unknown= personalBuffer.readRemainder();
            BinaryWriter writer= new BinaryWriter(tempPath + i);
            writer.write(unknown);
            writer.close();


            dataList.add(new Gen5PersonalData() {
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
                public int getStage() {
                    return stage;
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
                public int getAbility3() {
                    return ability3;
                }

                @Override
                public int getRunChance() {
                    return runChance;
                }

                @Override
                public int getFormID() {
                    return formID;
                }

                @Override
                public int getForm() {
                    return form;
                }

                @Override
                public int getNumForms() {
                    return numForms;
                }

                @Override
                public int getDexColor() {
                    return dexColor;
                }

                @Override
                public int getBaseExp() {
                    return baseExp;
                }

                @Override
                public int getHeight() {
                    return height;
                }

                @Override
                public int getWeight() {
                    return weight;
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

//        if(b2w2)
//        {
            file= files[files.length-1];
            personalBuffer= new Buffer(file.toString());
            byte[] unknown= personalBuffer.readRemainder();
            if(!new File(path + "temp" + File.separator + "personalRecompile").exists() && !new File(path + "temp" + File.separator + "personalRecompile").mkdir())
            {
                throw new RuntimeException("Check write perms");
            }
            BinaryWriter writer2= new BinaryWriter(path + "temp" + File.separator + "personalRecompile" + File.separator +  "709.bin");
            writer2.write(unknown);
//            writer2.writeBytes(0xFF,0xFF);
            writer2.close();
            personalBuffer.close();

//        }


        Scanner scanner= new Scanner(System.in);


        String[][] pokeTable= new String[dataList.size()+1][39];
        for(int i= 0; i < pokeTable.length; i++)
        {
            Arrays.fill(pokeTable[i],"");
        }

//        System.out.println("Display BST? (y/N)");
        boolean bst= false;
        for(int row= 0; row < dataList.size(); row++)
        {
            System.out.println(nameData[row]);
            int idx= 0;
            pokeTable[row][idx++]= "" + dataList.get(row).getNum();
            pokeTable[row][idx++]= nameData[row];
            pokeTable[row][idx++]= "" + dataList.get(row).getHP();
            pokeTable[row][idx++]= "" + dataList.get(row).getAtk();
            pokeTable[row][idx++]= "" + dataList.get(row).getDef();
            pokeTable[row][idx++]= "" + dataList.get(row).getSpe();
            pokeTable[row][idx++]= "" + dataList.get(row).getSpAtk();
            pokeTable[row][idx++]= "" + dataList.get(row).getSpDef();
            if(bst)
            {
                pokeTable[row][idx++]= dataList.get(row).getHP() + dataList.get(row).getAtk() + dataList.get(row).getDef() + dataList.get(row).getSpe() + dataList.get(row).getSpAtk() + dataList.get(row).getSpDef() + "";
            }
            pokeTable[row][idx++]= typeArr[dataList.get(row).getType1()];
            pokeTable[row][idx++]= typeArr[dataList.get(row).getType2()];

            pokeTable[row][idx++]= "" + dataList.get(row).getCatchRate();
            pokeTable[row][idx++]= "" + dataList.get(row).getStage();

            pokeTable[row][idx++]= "" + dataList.get(row).getHpEv();
            pokeTable[row][idx++]= "" + dataList.get(row).getSpeEv();
            pokeTable[row][idx++]= "" + dataList.get(row).getAtkEv();
            pokeTable[row][idx++]= "" + dataList.get(row).getDefEv();
            pokeTable[row][idx++]= "" + dataList.get(row).getSpAtkEv();
            pokeTable[row][idx++]= "" + dataList.get(row).getSpDefEv();

            pokeTable[row][idx++]= itemData[dataList.get(row).getItem1()];
            pokeTable[row][idx++]= itemData[dataList.get(row).getItem2()];
            pokeTable[row][idx++]= itemData[dataList.get(row).getItem3()];

            pokeTable[row][idx++]= "" + dataList.get(row).getGenderRatio();
            pokeTable[row][idx++]= "" + dataList.get(row).getHatchMultiplier();
            pokeTable[row][idx++]= "" + dataList.get(row).getBaseHappiness();
            pokeTable[row][idx++]= growthTableIdArr[dataList.get(row).getExpRate()];

            pokeTable[row][idx++]= eggGroupArr[dataList.get(row).getEggGroup1()];
            pokeTable[row][idx++]= eggGroupArr[dataList.get(row).getEggGroup2()];
            pokeTable[row][idx++]= abilityData[dataList.get(row).getAbility1()];
            pokeTable[row][idx++]= abilityData[dataList.get(row).getAbility2()];
            pokeTable[row][idx++]= abilityData[dataList.get(row).getAbility3()];
            pokeTable[row][idx++]= "" + dataList.get(row).getRunChance();

            pokeTable[row][idx++]= nameData[dataList.get(row).getFormID()];
            pokeTable[row][idx++]= "" + dataList.get(row).getForm();
            pokeTable[row][idx++]= "" + dataList.get(row).getNumForms();

            pokeTable[row][idx++]= "" + dataList.get(row).getDexColor();
            pokeTable[row][idx++]= "" + dataList.get(row).getBaseExp();
            pokeTable[row][idx++]= "" + dataList.get(row).getHeight();
            pokeTable[row][idx]= "" + dataList.get(row).getWeight();
        }

        BufferedWriter writer= new BufferedWriter(new FileWriter(path + "personalData.csv"));
        String line;
        writer.write("ID Number,Name,HP,Attack,Defense,Speed,Sp. Atk,Sp. Def,Type 1,Type 2,Catch Rate,Stage (For eviolite),HP EV Yield,Spe EV Yield,Attack EV Yield,Defense EV Yield,Sp. Atk EV Yield,Sp. Def EV Yield,Held Item 1,Held Item 2,Held Item 3,Gender Ratio,Hatch Multiplier,Base Happiness,Growth Rate,Egg Group 1,Egg Group 2,Ability 1,Ability 2,Hidden Ability,Run Chance (Safari Zone only),Alt Form Personal ID,Alt Form Sprite,# of Forms,DO NOT TOUCH,Exp Drop,Height,Weight\n");
        for(int row= 0; row < dataList.size(); row++)
        {
            line= "";
            for(int col= 0; col < pokeTable[0].length; col++)
            {
                line+= pokeTable[row][col] + ",";
            }
            line+= "\n";
            writer.write(line);
        }
        writer.close();

        String[][] tmTable= new String[nameData.length][101];
        for(int mon= 0; mon < dataList.size(); mon++)
        {
            for(int tm= 0; tm < 101; tm++)
            {
                tmTable[mon][tm]= Boolean.toString(dataList.get(mon).getTm(tm));
            }
        }

        writer= new BufferedWriter(new FileWriter(path + "tmLearnsetData.csv"));
        writer.write("ID Number,Name,");
        for(int i= 0; i < 101; i++)
        {
            writer.write(tmData[i] + ",");
        }
        writer.write("\n,,");
        for(int i= 0; i < 101; i++)
        {
            writer.write(tmNameData[i] + ",");
        }
        writer.write("\n");
        for(int row= 0; row < dataList.size(); row++)
        {
            line= dataList.get(row).getNum() + "," + nameData[row] + ",";
            for(int col= 0; col < tmTable[0].length; col++)
            {
                line+= tmTable[row][col] + ",";
            }
            line+= "\n";
            writer.write(line);
        }
        writer.close();
    }



    public void csvToPersonal(String personalCsv, String tmLearnsetCsv, String outputDir) throws IOException
    {
        String personalPath= path + personalCsv;
        String tmPath= path + tmLearnsetCsv;
        String tempPath= path + "temp" + File.separator + "temp";

        String outputPath;
        if(outputDir.contains("Recompile"))
        {
            outputPath= path + "temp" + File.separator+ outputDir;
        }
        else
        {
            outputPath= path + File.separator + outputDir;
        }

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(tempPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        int xValue;
        int yValue;

        if(!new File(outputPath).exists())
        {
            if(!new File(outputPath).mkdir())
            {
                throw new RuntimeException("Could not create output directory");
            }
        }

        CsvReader personalReader= new CsvReader(personalPath);
        String[] csvRow;
        ArrayList<Gen5PersonalData> personalData= new ArrayList<>();
        for(int row= 0; row < personalReader.length(); row++)
        {
            csvRow= personalReader.next();
            String final35= csvRow[35];
            String[] finalCsvRow = csvRow;
            int finalRow = row;
            personalData.add(new Gen5PersonalData() {
                @Override
                public int getNum() {
                    return finalRow;
                }

                @Override
                public int getHP() {
                    return Integer.parseInt(finalCsvRow[0]);
                }

                @Override
                public int getAtk() {
                    return Integer.parseInt(finalCsvRow[1]);
                }

                @Override
                public int getDef() {
                    return Integer.parseInt(finalCsvRow[2]);
                }

                @Override
                public int getSpe() {
                    return Integer.parseInt(finalCsvRow[3]);
                }

                @Override
                public int getSpAtk() {
                    return Integer.parseInt(finalCsvRow[4]);
                }

                @Override
                public int getSpDef() {
                    return Integer.parseInt(finalCsvRow[5]);
                }

                @Override
                public int getType1() {
                    return getType(finalCsvRow[6]);
                }

                @Override
                public int getType2() {
                    return getType(finalCsvRow[7]);
                }

                @Override
                public int getCatchRate() {
                    return Integer.parseInt(finalCsvRow[8]);
                }

                @Override
                public int getStage() {
                    return Integer.parseInt(finalCsvRow[9]);
                }

                @Override
                public int getHpEv() {
                    return Integer.parseInt(finalCsvRow[10]);
                }

                @Override
                public int getSpeEv() {
                    return Integer.parseInt(finalCsvRow[11]);
                }

                @Override
                public int getAtkEv() {
                    return Integer.parseInt(finalCsvRow[12]);
                }

                @Override
                public int getDefEv() {
                    return Integer.parseInt(finalCsvRow[13]);
                }

                @Override
                public int getSpAtkEv() {
                    return Integer.parseInt(finalCsvRow[14]);
                }

                @Override
                public int getSpDefEv() {
                    return Integer.parseInt(finalCsvRow[15]);
                }

                @Override
                public int getPadding() {
                    return 0;
                }

                @Override
                public int getItem1() {
                    return getItem(finalCsvRow[16]);
                }

                @Override
                public int getItem2() {
                    return getItem(finalCsvRow[17]);
                }

                @Override
                public int getItem3() {
                    return getItem(finalCsvRow[18]);
                }

                @Override
                public int getGenderRatio() {
                    return Integer.parseInt(finalCsvRow[19]);
                }

                @Override
                public int getHatchMultiplier() {
                    return Integer.parseInt(finalCsvRow[20]);
                }

                @Override
                public int getBaseHappiness() {
                    return Integer.parseInt(finalCsvRow[21]);
                }

                @Override
                public int getExpRate() {
                    return getGrowthRate(finalCsvRow[22]);
                }

                @Override
                public int getEggGroup1() {
                    return getEggGroup(finalCsvRow[23]);
                }

                @Override
                public int getEggGroup2() {
                    return getEggGroup(finalCsvRow[24]);
                }

                @Override
                public int getAbility1() {
                    return getAbility(finalCsvRow[25]);
                }

                @Override
                public int getAbility2() {
                    return getAbility(finalCsvRow[26]);
                }

                @Override
                public int getAbility3() {
                    return getAbility(finalCsvRow[27]);
                }

                @Override
                public int getRunChance() {
                    return Integer.parseInt(finalCsvRow[28]);
                }

                @Override
                public int getFormID() {
                    return getPokemon(finalCsvRow[29]);
                }

                @Override
                public int getForm() {
                    return Integer.parseInt(finalCsvRow[30]);
                }

                @Override
                public int getNumForms() {
                    return Integer.parseInt(finalCsvRow[31]);
                }

                @Override
                public int getDexColor() {
                    return Integer.parseInt(finalCsvRow[32]);
                }

                @Override
                public int getBaseExp() {
                    return Integer.parseInt(finalCsvRow[33]);
                }

                @Override
                public int getHeight() {
                    return Integer.parseInt(finalCsvRow[34]);
                }

                @Override
                public int getWeight() {
                    return Integer.parseInt(final35);
                }

                @Override
                public boolean getTm(int idx) {
                    return false;
                }
            });
        }



        CsvReader csvReader= new CsvReader(tmPath);
        csvReader.skipLine();
        BitStream[] tmLearnsetData = new BitStream[csvReader.length()-1];
        for(int i= 0; i < tmLearnsetData.length; i++)
        {
            tmLearnsetData[i] = new BitStream();

            String[] strs = csvReader.next();
            for (String str : strs) {
                tmLearnsetData[i].append(Boolean.parseBoolean(str));
            }

            tmLearnsetData[i].append(false, 27);
            System.out.println(tmLearnsetData[i]);
        }

        outputPath+= File.separator;
        BinaryWriter writer;
        for(int i= 0; i < personalData.size(); i++)
        {
            System.out.println(nameData[i]);
            Gen5PersonalData data= personalData.get(i);
            writer= new BinaryWriter(new File(outputPath + i + ".bin"));
            writer.writeBytes(data.getHP(), data.getAtk(), data.getDef(), data.getSpe(), data.getSpAtk(), data.getSpDef(), data.getType1(), data.getType2(), data.getCatchRate(), data.getStage());
            writer.writeShort(parseShort("0000", evYieldBinary(data.getSpDefEv()), evYieldBinary(data.getSpAtkEv()), evYieldBinary(data.getDefEv()), evYieldBinary(data.getAtkEv()), evYieldBinary(data.getSpeEv()), evYieldBinary(data.getHpEv())));
            writer.writeShort((short)data.getItem1());
            writer.writeShort((short)data.getItem2());
            writer.writeShort((short)data.getItem3());
            writer.writeBytes(data.getGenderRatio(),data.getHatchMultiplier(),data.getBaseHappiness(),data.getExpRate(),data.getEggGroup1(),data.getEggGroup2(),data.getAbility1(),data.getAbility2(),data.getAbility3(),data.getRunChance());
            writer.writeShort((short)data.getFormID());
            writer.writeShort((short)data.getForm());
            writer.writeBytes((byte)data.getNumForms(),(byte)data.getDexColor());
            writer.writeShort((short)data.getBaseExp());
            writer.writeShort((short)data.getHeight());
            writer.writeShort((short)data.getWeight());
            System.out.println(i + ":   " + tmLearnsetData[i]);
            writer.write(tmLearnsetData[i].toBytes());

            byte[] padding;
            if(i >= files.length)
            {
                padding= new byte[0x18];
                Arrays.fill(padding, (byte) 0);
            }
            else
            {
                file= files[i];
                Buffer buffer= new Buffer(file.toString());
                padding= buffer.readBytes((int)file.length());
                buffer.close();
            }
            writer.write(padding);
        }
    }


    public void csvReformat(String tmLearnsetCsv) throws IOException
    {
        ArrayList<String[][]> reformat= new ArrayList<>();
        CsvReader csvReader= new CsvReader(path + tmLearnsetCsv);

        for(int i= 0; i < csvReader.length(); i++)
        {
            String[][] edited= new String[4][25];
            String[] mon= csvReader.next();
            int idx= 0;
            for(int row= 0; row < 4; row++)
            {
                if(row == 1)
                {
                    idx= 1;
                }
                if(row == 2)
                {
                    idx= 2;
                }
                if(row == 3)
                {
                    idx= 3;
                }
                for(int col= 0; col < 25; col++)
                {
                    edited[row][col]= mon[idx+=4];
                }
            }
            reformat.add(edited);
        }

        BufferedWriter writer= new BufferedWriter(new FileWriter(path + "Reformatted TM Learnset.csv"));
        writer.write("Dex Number,Pokémon\n");
        for(int i= 0; i < reformat.size(); i++)
        {
            String[][] mon= reformat.get(i);
            writer.write(i + "," + nameData[i] + ",");
            int idx= 1;
            int hm= 1;
            for(int row= 0; row < mon.length; row++)
            {
                for(int col= 0; col < mon[0].length; col++)
                {
                    if(idx < 10)
                    {
                        writer.write(mon[row][col] + "," + "TM0" + idx + ",");
                        idx++;
                    }
                    else if(idx <= 92)
                    {
                        writer.write(mon[row][col] + "," + "TM" + idx + ",");
                        idx++;
                    }
                    else
                    {
                        writer.write(mon[row][col] + "," + "HM0" + hm + ",");
                        hm++;
                    }
                }
                if(hm != 8)
                {
                    writer.write("\n, ,");
                }
                else
                {
                    writer.write("\n");
                }
            }
        }
        writer.close();
    }






    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(Gen5PersonalEditor2::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
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

    private static int getPokemon(String pokemon)
    {
        for(int i= 0; i < nameData.length; i++)
        {
            if(pokemon.equals(nameData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid pokemon entered: " + pokemon);
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
