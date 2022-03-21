package com.turtleisaac.pokeditor.editors.encounters.johto.headbutt;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;

import java.io.*;
import java.util.*;

public class HeadbuttEncounterEditor
{
    private static String path= System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath= path;
    private String separator= File.separator;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fairy", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] colorArr= {"Red","Blue","Yellow","Green","Black","Brown","Purple","Gray","White","Pink"};
    private static final int[] fieldRateArr= {20,20,10,10,10,10,5,5,4,4,1,1};
    private static final int[] smashRateArr= {90,10};
    private static final int[] waterRateArr= {60,30,5,4,1};
    private static String resourcePath= path + "Program Files" + File.separator;
    private static String[] nameData;
    private static String[] itemData;
    private static String[] moveData;
    private static String[] areaData;
    private static String[] outdoorData;
    private static String[] indoorData;

    public HeadbuttEncounterEditor() throws IOException {
        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "EntryData.txt"));
        ArrayList<String> nameList= new ArrayList<>();
        String line;
        while((line= reader.readLine()) != null)
        {
            nameList.add(line);
        }
        nameData= nameList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "ItemList.txt"));
        ArrayList<String> itemList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            itemList.add(line);
        }
        itemData= itemList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "MoveList.txt"));
        ArrayList<String> moveList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            moveList.add(line);
        }
        moveData= moveList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "HeadbuttLocations.txt"));
        ArrayList<String> areaList= new ArrayList<>();

        while ((line= reader.readLine()) != null)
        {
            areaList.add(line);
        }
        areaData= areaList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "OutdoorLocations.txt"));
        ArrayList<String> outdoorList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            outdoorList.add(line.substring(3));
        }
        outdoorData= outdoorList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "IndoorLocations.txt"));
        ArrayList<String> indoorList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            indoorList.add(line.substring(3));
        }
        indoorData= indoorList.toArray(new String[0]);
        reader.close();
    }

    public void headbuttToCsv(String headbuttDir) throws IOException
    {
        dataPath+= headbuttDir;
        if(!new File(headbuttDir).exists())
        {
            throw new RuntimeException("Invalid directory");
        }
        if(!new File(path + "Headbutt Temp Framework.Data").mkdir() && !new File(path + "Headbutt Temp Framework.Data").exists())
        {
            throw new RuntimeException("Couldn't create temp directory. Check write perms.");
        }

        ArrayList<HeadbuttData> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for(int i= 0; i < files.length; i++)
        {
            file= files[i];
            Buffer buffer= new Buffer(file.toString());

            int numTrees= buffer.readByte();
            int padding= buffer.readByte();
            int numSpecialTrees= buffer.readByte();
            padding= buffer.readByte();

            short[] normalEncounters= new short[12];
            int[] normalMins= new int[12];
            int[] normalMaxs= new int[12];

            short[] specialEncounters= new short[6];
            int[] specialMins= new int[6];
            int[] specialMaxs= new int[6];

            ArrayList<TreeCoordinates> normalCoordinates= new ArrayList<>();
            ArrayList<TreeCoordinates> specialCoordinates= new ArrayList<>();
            if(numTrees != 0)
            {
                for(int x= 0; x < 12; x++)
                {
                    normalEncounters[x]= buffer.readShort();
                    normalMins[x]= buffer.readByte();
                    normalMaxs[x]= buffer.readByte();
                }
            }
            if(numSpecialTrees != 0)
            {
                for(int x= 0; x < 6; x++)
                {
                    specialEncounters[x]= buffer.readShort();
                    specialMins[x]= buffer.readByte();
                    specialMaxs[x]= buffer.readByte();
                }
            }
            if(numTrees != 0)
            {
                byte[] normal= buffer.readBytes(6*numTrees);
                normalCoordinates.add(new TreeCoordinates() {
                    @Override
                    public byte[] getXValues() {
                        return normal;
                    }
                });
            }
            if(numSpecialTrees != 0)
            {
                for(int x= 0; x < numSpecialTrees; x++)
                {
                    byte[] special= buffer.readBytes(6*numSpecialTrees);
                    specialCoordinates.add(new TreeCoordinates() {
                        @Override
                        public byte[] getXValues() {
                            return special;
                        }
                    });
                }
            }


            dataList.add(new HeadbuttData() {
                @Override
                public int getNumTrees() {
                    return numTrees;
                }

                @Override
                public int getNumSpecialTrees() {
                    return numSpecialTrees;
                }

                @Override
                public short[] getNormalEncounters() {
                    return normalEncounters;
                }

                @Override
                public int[] getNormalMins() {
                    return normalMins;
                }

                @Override
                public int[] getNormalMaxs() {
                    return normalMaxs;
                }

                @Override
                public short[] getSpecialEncounters() {
                    return specialEncounters;
                }

                @Override
                public int[] getSpecialMins() {
                    return specialMins;
                }

                @Override
                public int[] getSpecialMaxs() {
                    return specialMaxs;
                }

                @Override
                public ArrayList<TreeCoordinates> getNormalCoordinates() {
                    return normalCoordinates;
                }

                @Override
                public ArrayList<TreeCoordinates> getSpecialCoordinates() {
                    return specialCoordinates;
                }
            });


            BinaryWriter binaryWriter= new BinaryWriter(new File(path + "Headbutt Temp Framework.Data" + separator + i + ".bin"));
            for(int x= 0; x < normalCoordinates.size(); x++)
            {
                TreeCoordinates treeCoordinates= normalCoordinates.get(x);
                binaryWriter.write(treeCoordinates.getXValues());
                if(x < numSpecialTrees)
                {
                    treeCoordinates= specialCoordinates.get(x);
                    binaryWriter.write(treeCoordinates.getXValues());
                }
            }
        }

        ArrayList<String[][]> normalTable= new ArrayList<>();
        ArrayList<String[][]> specialTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            HeadbuttData headbuttData= dataList.get(i);
            String[][] area= new String[12][3];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            int idx= 0;
            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= nameData[headbuttData.getNormalEncounters()[idx]];
                area[row][1]= "" + headbuttData.getNormalMins()[idx];
                area[row][2]= "" + headbuttData.getNormalMaxs()[idx++];
            }
            normalTable.add(area);
        }
        for(int i= 0; i < dataList.size(); i++)
        {
            HeadbuttData headbuttData= dataList.get(i);
            String[][] area= new String[6][3];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            int idx= 0;
            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= nameData[headbuttData.getNormalEncounters()[idx]];
                area[row][1]= "" + headbuttData.getNormalMins()[idx];
                area[row][2]= "" + headbuttData.getNormalMaxs()[idx++];
            }
            specialTable.add(area);
        }

        BufferedWriter writer= new BufferedWriter(new FileWriter(path + "normalHeadbuttEncounters.csv"));
        writer.write("ID Number,Map Header Name,Pokemon,Min Level,Max Level\n");
        for(int i= 0; i < dataList.size(); i++)
        {
            HeadbuttData headbuttData= dataList.get(i);
            String[][] area= normalTable.get(i);
            writer.write(i + "," + areaData[i] + ",DO NOT EDIT:," + headbuttData.getNumTrees() + "\n,,");
            for(int row= 0; row < area.length; row++)
            {
                for(int col= 0; col < area[row].length; col++)
                {
                    writer.write(area[row][col] + ',');
                }
                if(row != area.length-1)
                {
                    writer.write("\n,,");
                }
            }
            writer.write("\n");
        }
        writer.close();

        writer= new BufferedWriter(new FileWriter(path + "specialHeadbuttEncounters.csv"));
        writer.write("ID Number,Map Header Name,Pokemon,Min Level,Max Level\n");
        for(int i= 0; i < dataList.size(); i++)
        {
            HeadbuttData headbuttData= dataList.get(i);
            String[][] area= specialTable.get(i);
            writer.write(i + "," + areaData[i] + ",DO NOT EDIT:," + headbuttData.getNumSpecialTrees() + "\n,,");
            System.out.println(headbuttData.getNumSpecialTrees());
            for(int row= 0; row < area.length; row++)
            {
                for(int col= 0; col < area[row].length; col++)
                {
                    writer.write(area[row][col] + ',');
                }
                if(row != area.length-1)
                {
                    writer.write("\n,,");
                }
            }
            writer.write("\n");
        }
        writer.close();
    }

    public void csvToHeadbutt(String headbuttCsv, String outputDir)
    {
        String outputPath;
        if(outputDir.contains("Recompile"))
        {
            outputPath= path + "temp" + File.separator+ outputDir;
        }
        else
        {
            outputPath= path + File.separator + outputDir;
        }


    }



    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(HeadbuttEncounterEditor::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
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
        throw new RuntimeException("Invalid type entered");
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
        throw new RuntimeException("Invalid move entered");
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
        throw new RuntimeException("Invalid pokemon entered");
    }

    private static int getArea(String area)
    {
        for(int i= 0; i < areaData.length; i++)
        {
            if(area.equals(areaData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid area entered");
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
        throw new RuntimeException("Invalid item entered");
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

    private static short[] reverseBytes(short[] shorts)
    {
        short[] reverse= new short[shorts.length];
        for(int i= 0; i < shorts.length; i++) {
            short thisShort= shorts[i];
            reverse[i]= Short.reverseBytes(thisShort);
            System.out.println("Original: " + shorts[i] + ", Reversed: " + reverse[i]);
        }
        return reverse;
    }

    private static short[] intToShort(int[] bytes)
    {
        short[] shorts= new short[bytes.length];
        for(int i= 0; i < bytes.length; i++)
        {
            shorts[i]= (short)bytes[i];
        }
        return shorts;
    }
}
