package com.turtleisaac.pokeditor.editors.personal.gen6;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;

import java.io.*;
import java.util.*;

public class PersonalEditorGen6
{
    public static void main(String[] args) throws IOException
    {
        PersonalEditorGen6 editor= new PersonalEditorGen6();
        editor.personalToCsv("5_");
    }

    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath = path;
    private static String resourcePath = path + "Program Files" + File.separator;
    private static final String[] typeArr = {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fire", "Water", "Grass", "Electric", "Psychic", "Ice", "Dragon", "Dark", "Fairy"};
    private static final String[] eggGroupArr= {"~","Monster","Water 1","Bug","Flying","Field","Fairy","Grass","Human-Like","Water 3","Mineral","Amorphous","Water 2","Ditto","Dragon","Undiscovered"};
    private static final String[] growthTableIdArr= {"Medium Fast","Erratic","Fluctuating","Medium Slow","Fast","Slow","Medium Fast","Medium Fast"};
    private static String[] nameData;
    private static String[] moveData;
    private static String[] tmData;
    private static String[] itemData;
    private static String[] abilityData;
    private static String[] tmNameData;

    public PersonalEditorGen6() throws IOException
    {
        String entryPath = resourcePath + "EntryDataGen6.txt";
        String movePath = resourcePath + "MoveList.txt";


        BufferedReader reader = new BufferedReader(new FileReader(entryPath));
        ArrayList<String> nameList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null)
        {
            nameList.add(line);
        }
        nameData = nameList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(movePath));
        ArrayList<String> moveList = new ArrayList<>();

        while ((line = reader.readLine()) != null)
        {
            moveList.add(line);
        }
        moveData = moveList.toArray(new String[0]);
        reader.close();

//        reader = new BufferedReader(new FileReader(resourcePath + "TmListGen6.txt"));
//        ArrayList<String> tmList = new ArrayList<>();
//
//        while ((line = reader.readLine()) != null)
//        {
//            tmList.add(line);
//        }
//        tmData = tmList.toArray(new String[0]);
//        reader.close();

        reader = new BufferedReader(new FileReader(resourcePath + "ItemList.txt"));
        ArrayList<String> itemList = new ArrayList<>();

        while ((line = reader.readLine()) != null)
        {
            itemList.add(line);
        }
        itemData = itemList.toArray(new String[0]);
        reader.close();

        reader = new BufferedReader(new FileReader(resourcePath + "AbilityList.txt"));
        ArrayList<String> abilityList = new ArrayList<>();

        while ((line = reader.readLine()) != null)
        {
            abilityList.add(line);
        }
        abilityData = abilityList.toArray(new String[0]);
        reader.close();

//        reader = new BufferedReader(new FileReader(resourcePath + "TmNameListGen6.txt"));
//        ArrayList<String> tmNameList = new ArrayList<>();
//
//        while ((line = reader.readLine()) != null)
//        {
//            tmNameList.add(line);
//        }
//        tmNameData = tmNameList.toArray(new String[0]);
        reader.close();
    }

    public void personalToCsv(String personalDir) throws IOException
    {
        dataPath+= personalDir;

        Buffer personalBuffer;
        ArrayList<PersonalDataGen6> dataList = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for (int i= 0; i < files.length; i++)
        {
            file= files[i];
            personalBuffer = new Buffer(file.toString());

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
            int numForms= personalBuffer.readByte();
            int formSprite= personalBuffer.readByte();
//            int formID= personalBuffer.readShort();
            int dexColor= personalBuffer.readByte();
            int baseExp= personalBuffer.readShort();

            int height= personalBuffer.readShort();
            int weight= personalBuffer.readShort();
            byte[] tmLearnset= personalBuffer.readBytes(16);

            System.out.println(nameData[i] + ": " + i);
            System.out.println("HP: " + hp);
            System.out.println("Atk: " + atk);
            System.out.println("Def: " + def);
            System.out.println("Spe: " + spe);
            System.out.println("SpAtk: " + spAtk);
            System.out.println("SpDef: " + spDef);
            System.out.println("Type 1: " + typeArr[type1]);
            System.out.println("Type 2: " + typeArr[type2]);
            System.out.println("Catch Rate: " + catchRate);
            System.out.println("Stage: " + stage);
            System.out.println("HP EV Yield: " + hpYield);
            System.out.println("Spe EV Yield: " + speYield);
            System.out.println("Atk EV Yield: " + atkYield);
            System.out.println("Def EV Yield: " + defYield);
            System.out.println("SpAtk EV Yield: " + spAtkYield);
            System.out.println("SpDef EV Yield: " + spDefYield);
            System.out.println("Item 1: " + itemData[item1]);
            System.out.println("Item 2: " + itemData[item2]);
            System.out.println("Item 3: " + itemData[item3]);
            System.out.println("Gender Ratio: " + genderRatio);
            System.out.println("Hatch Cycles: " + hatchMultiplier);
            System.out.println("Base Happiness: " + baseHappiness);
            System.out.println("Exp Rate: " + growthTableIdArr[expRate]);
            System.out.println("Egg Group 1: " + eggGroupArr[eggGroup1]);
            System.out.println("Egg Group 2: " + eggGroupArr[eggGroup2]);
            System.out.println("Ability 1: " + abilityData[ability1]);
            System.out.println("Ability 2: " + abilityData[ability2]);
            System.out.println("Hidden Ability: " + abilityData[ability3]);
//            System.out.println("Form ID: " + nameData[formID]);
            System.out.println("Number of Forms: " + numForms);
            System.out.println("Form Sprite: " + formSprite);
            System.out.println("Colors: " + dexColor);
            System.out.println("Exp Yield: " + baseExp);
            System.out.println("Height: " + (height/100));
            System.out.println("Weight: " + (weight/10) + "\n");
//            System.out.println(": " + );
//            System.out.println(": " + );


            dataList.add(new PersonalDataGen6()
            {
                @Override
                public int getNum()
                {
                    return 0;
                }

                @Override
                public int getHP()
                {
                    return 0;
                }

                @Override
                public int getAtk()
                {
                    return 0;
                }

                @Override
                public int getDef()
                {
                    return 0;
                }

                @Override
                public int getSpe()
                {
                    return 0;
                }

                @Override
                public int getSpAtk()
                {
                    return 0;
                }

                @Override
                public int getSpDef()
                {
                    return 0;
                }

                @Override
                public int getType1()
                {
                    return 0;
                }

                @Override
                public int getType2()
                {
                    return 0;
                }

                @Override
                public int getCatchRate()
                {
                    return 0;
                }

                @Override
                public int getStage()
                {
                    return 0;
                }

                @Override
                public int getHpEv()
                {
                    return 0;
                }

                @Override
                public int getSpeEv()
                {
                    return 0;
                }

                @Override
                public int getAtkEv()
                {
                    return 0;
                }

                @Override
                public int getDefEv()
                {
                    return 0;
                }

                @Override
                public int getSpAtkEv()
                {
                    return 0;
                }

                @Override
                public int getSpDefEv()
                {
                    return 0;
                }

                @Override
                public int getPadding()
                {
                    return 0;
                }

                @Override
                public int getItem1()
                {
                    return 0;
                }

                @Override
                public int getItem2()
                {
                    return 0;
                }

                @Override
                public int getItem3()
                {
                    return 0;
                }

                @Override
                public int getGenderRatio()
                {
                    return 0;
                }

                @Override
                public int getHatchMultiplier()
                {
                    return 0;
                }

                @Override
                public int getBaseHappiness()
                {
                    return 0;
                }

                @Override
                public int getExpRate()
                {
                    return 0;
                }

                @Override
                public int getEggGroup1()
                {
                    return 0;
                }

                @Override
                public int getEggGroup2()
                {
                    return 0;
                }

                @Override
                public int getAbility1()
                {
                    return 0;
                }

                @Override
                public int getAbility2()
                {
                    return 0;
                }

                @Override
                public int getAbility3()
                {
                    return 0;
                }

                @Override
                public int getFormID()
                {
                    return 0;
                }

                @Override
                public int getFormSprite()
                {
                    return 0;
                }

                @Override
                public int getNumForms()
                {
                    return 0;
                }

                @Override
                public int getDexColor()
                {
                    return 0;
                }

                @Override
                public int getBaseExp()
                {
                    return 0;
                }

                @Override
                public int getHeight()
                {
                    return 0;
                }

                @Override
                public int getWeight()
                {
                    return 0;
                }

                @Override
                public boolean getTm(int idx)
                {
                    return false;
                }
            });
        }


//        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "personalData.csv"));
//        writer.write("ID Number,Name,\n"); //header in spreadsheet output
//        String line;
//        for (int row = 0; row < dataList.size(); row++)
//        {
//            line = row + "," + personalData[row] + ",";
//            for (int col = 0; col < personalTable[0].length; col++)
//            {
//                line += personalTable[row][col] + ",";
//            }
//            line += "\n";
//            writer.write(line);
//        }
//        writer.close();
    }


    public void csvTo(String personalCsv, String outputDir) throws IOException
    {
        String personalPath = path + personalCsv;
        String outputPath;

        if (outputDir.contains("Recompile"))
        {
            outputPath = path + "temp" + File.separator + outputDir;
        } else
        {
            outputPath = path + File.separator + outputDir;
        }

        if (!new File(outputPath).exists() && !new File(outputPath).mkdir())
        {
            throw new RuntimeException("Could not create output directory. Check write permissions");
        }
        outputPath += File.separator;

        CsvReader csvReader = new CsvReader(personalPath);
        BinaryWriter writer;
        for (int i = 0; i < csvReader.length(); i++)
        {
            initializeIndex(csvReader.next());
            writer = new BinaryWriter(outputPath + i + ".bin");

            writer.close();
        }

    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(PersonalEditorGen6::fileToInt));
    }

    private static int fileToInt(File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private int arrIdx;
    private String[] input;

    private void initializeIndex(String[] arr)
    {
        arrIdx = 0;
        input = arr;
    }

    private String next()
    {
        try
        {
            return input[arrIdx++];
        } catch (IndexOutOfBoundsException e)
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

    private static byte getType(String type)
    {
        for (int i = 0; i < typeArr.length; i++)
        {
            if (type.equals(typeArr[i]))
            {
                return (byte) i;
            }
        }

        throw new RuntimeException("Invalid type entered: " + type);
    }

    private static int getMove(String move)
    {
        for (int i = 0; i < moveData.length; i++)
        {
            if (move.equals(moveData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid move entered: " + move);
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


}
