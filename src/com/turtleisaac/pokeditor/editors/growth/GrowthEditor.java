package com.turtleisaac.pokeditor.editors.growth;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;

import java.io.*;
import java.util.*;

public class GrowthEditor
{
    private static String path= System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath= path;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fairy", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] evolutionMethodArr= {"None","Happiness","Happiness (Day)","Happiness (Night)","Level Up","Trade","Trade (Item)","Use Item","Level (Attack > Defense)","Level (Attack = Defense)","Level (Attack < Defense)","Level (PID > 5)","Level (PID < 5)","Level (1 of 2)","Level (2 of 2)","Max Beauty","Use Item (Male)","Use Item (Female)","Use Item (Day)","Use Item (Night)","Attack Known","Pokemon in Party","Level (Male)","Level (Female)","Level (Mt. Coronet)","Level (Eterna Forest)","Level (Route 217)"};
    private static final String[] growthTableIdArr= {"Medium Fast","Erratic","Fluctuating","Medium Slow","Fast","Slow","Medium Fast","Medium Fast"};
    private static String resourcePath= path + "Program Files" + File.separator;
    private static String[] nameData;

    public GrowthEditor() throws IOException
    {
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
    }

    public void growthToCsv(String growthDir) throws IOException
    {
        dataPath+= growthDir;

        Buffer buffer;
        ArrayList<long[]> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        for (File file : files)
        {
            long[] entry = new long[101];
            buffer = new Buffer(file.toString());
            for (int x = 0; x < entry.length; x++)
            {
                entry[x]= buffer.readUInt32();
            }
            buffer.close();
            dataList.add(entry);
        }

        BufferedWriter writer= new BufferedWriter(new FileWriter(path + "growthTable.csv"));
        writer.write("ID Number,Growth Type,");
        for(int i= 0; i < 101; i++)
        {
            writer.write("Level " + i + ",");
        }
        writer.write("\n");

        for(int row= 0; row < dataList.size(); row++)
        {
            writer.write(row + "," + growthTableIdArr[row] + ",");
            long[] thisRow= dataList.get(row);
            for(int col= 0; col < thisRow.length; col++)
            {
                writer.write(thisRow[col] + ",");
            }
            writer.write("\n");
        }
        writer.close();
    }

    public void csvToGrowth(String growthCsv, String outputDir) throws IOException
    {
        String outputPath;
        if(outputDir.contains("Recompile"))
        {
            outputPath= path + "temp" + File.separator+ outputDir + File.separator;
        }
        else
        {
            outputPath= path + File.separator + outputDir + File.separator;
        }

        String growthPath= path + growthCsv;
        CsvReader csvReader= new CsvReader(growthPath);
        BinaryWriter writer;

        for(int i= 0; i < csvReader.length(); i++)
        {
            initializeIndex(csvReader.next());
            writer= new BinaryWriter(outputPath + i + ".bin");
            for(int x= 0; x < input.length; x++)
            {
                writer.writeInt((int)Long.parseLong(next()));
            }

        }

    }


    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(GrowthEditor::fileToInt));
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
}
