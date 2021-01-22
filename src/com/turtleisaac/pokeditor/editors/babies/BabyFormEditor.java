package com.turtleisaac.pokeditor.editors.babies;

import com.turtleisaac.pokeditor.framework.*;

import java.io.*;
import java.util.*;

public class BabyFormEditor
{
//    public static void main(String[] args) throws IOException
//    {
//        BabyFormEditor editor= new BabyFormEditor();
//        editor.babyFormsToSheet("pms.narc");
//    }

    private static String projectPath;
    private String dataPath;
    private static String resourcePath;
    private static String[] nameData;

    public BabyFormEditor(String projectPath) throws IOException
    {
        this.projectPath= projectPath;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;

        String entryPath = resourcePath + "EntryData.txt";

        BufferedReader reader = new BufferedReader(new FileReader(entryPath));
        ArrayList<String> nameList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null)
        {
            nameList.add(line);
        }
        nameData = nameList.toArray(new String[0]);
        reader.close();
    }

    public Object[][] babyFormsToSheet(String babyFormFile) throws IOException
    {
        dataPath += babyFormFile;

        File file= new File(dataPath);
        Buffer buffer= new Buffer(dataPath);
        int numPokemon;

        if(file.length() % 2 != 0)
        {
            throw new RuntimeException("Invalid baby form table");
        }
        else
        {
            numPokemon= (int) (file.length() / 2);
        }

        int[] babyForms= new int[numPokemon];

        for(int i= 0; i < numPokemon; i++)
        {
            babyForms[i]= buffer.readUIntS();
        }

        ArrayProcessor processor= new ArrayProcessor();
        processor.append("ID Number,Name,Baby Form"); //header in spreadsheet output
        processor.newLine();
        String line;
        for (int col = 0; col < numPokemon; col++)
        {
            line = col + "," + nameData[col] + "," + nameData[babyForms[col]];
            processor.append(line);
            processor.newLine();
        }

        return processor.getTable();
    }


    public void sheetToBabyForms(Object[][] babyFormsCsv, String outputFile) throws IOException
    {
        String outputPath= projectPath + outputFile;

        if (!new File(outputPath).exists())
        {
            throw new RuntimeException("Could not create output directory. Check write permissions");
        }

        babyFormsCsv= ArrayModifier.trim(babyFormsCsv,1,2);
        BinaryWriter writer= new BinaryWriter(outputPath);
        for (int i = 0; i < babyFormsCsv.length; i++)
        {
            writer.writeShort(getPokemon((String) babyFormsCsv[i][0]));
        }
    }


    private static short getPokemon(String pokemon)
    {
        for(int i= 0; i < nameData.length; i++)
        {
            if(pokemon.equalsIgnoreCase(nameData[i]))
            {
                return (short) i;
            }
        }
        throw new RuntimeException("Invalid pokemon entered: " + pokemon);
    }

}
