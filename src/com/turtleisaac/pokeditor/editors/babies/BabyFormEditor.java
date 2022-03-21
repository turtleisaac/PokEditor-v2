package com.turtleisaac.pokeditor.editors.babies;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;

import java.io.*;
import java.util.*;

public class BabyFormEditor
{
    Project project;
    Game baseRom;

    private static String projectPath;
    private String dataPath;
    private static String resourcePath;
    private static String[] nameData;

    public BabyFormEditor(String dataPath, Project project) throws IOException
    {
        this.project= project;
        this.baseRom= project.getBaseRom();
        this.projectPath= dataPath;
        this.dataPath = dataPath;
        resourcePath= dataPath.substring(0,dataPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                break;
        }
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
            babyForms[i]= buffer.readUInt16();
        }

        ArrayProcessor processor= new ArrayProcessor();
        processor.append("ID Number,Name,Baby Form"); //header in spreadsheet output
        processor.newLine();
        String line;
        for (int col = 0; col < numPokemon; col++)
        {
            line = col + ",=Personal!$B$" + (col+2) + ",=Personal!$B$" + (babyForms[col]+2);
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
        for(Object[] row : babyFormsCsv)
        {
            writer.writeShort(getPokemon((String) row[0]));
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
