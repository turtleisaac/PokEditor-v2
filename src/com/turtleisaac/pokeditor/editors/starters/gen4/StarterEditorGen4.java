package com.turtleisaac.pokeditor.editors.starters.gen4;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Project;
import sun.plugin.dom.exception.InvalidStateException;

import java.io.*;
import java.util.*;

public class StarterEditorGen4
{
    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String resourcePath = path + "Program Files" + File.separator;
    private static String[] nameData;
    private final String gameCode;
    Buffer starterBuffer;
    BinaryWriter writer;
    private Project project;

    public StarterEditorGen4(Project project, String gameCode) throws IOException
    {
        this.project= project;
        this.gameCode = gameCode;

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

            default:
                throw new InvalidStateException("Invalid game: " + project.getBaseRom());
        }
    }

    public void changeStarters(String startersFile) throws IOException
    {
        starterBuffer= new Buffer(startersFile);
        writer= new BinaryWriter(startersFile + "Recompile");
        int offset;

        switch (project.getBaseRom())
        {
            case Platinum:
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offset = 0x1bac;
                } else
                {
                    offset = 0x1bc0;
                }
                editStarterData(offset);
                break;

            case Pearl:
            case Diamond:
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offset = 0x30;
                } else
                {
                    offset = 0x1b88;
                }
                editStarterData(offset);
                break;

            default:
                throw new InvalidStateException("This editor can't be used with Gen 5 or HGSS currently.");
        }
    }

    private void editStarterData(int offset) throws IOException
    {
        Scanner scanner= new Scanner(System.in);
        String ans;
        int pokemonId;
        BinaryWriter starterWriter= new BinaryWriter(path + "temp" + File.separator + "starters.bin");

        writer.write(starterBuffer.readBytes(offset));
        for(int i= 0; i < 3; i++)
        {
            pokemonId= starterBuffer.readInt();
            System.out.println("Starter " + (i + 1) + " is currently: " + nameData[pokemonId] + ". Do you want to change it? (y/N)\n");
            ans= scanner.nextLine();
            System.out.println("\n");

            if(ans.equalsIgnoreCase("y"))
            {
                System.out.println("Please enter the name of the Pokemon you wish to replace it with\n");
                pokemonId= getPokemon(scanner.nextLine());
                System.out.println("\nStarter has been replaced with " + nameData[pokemonId] + "\n");
            }
            writer.writeInt(pokemonId);
            starterWriter.writeShort((short)pokemonId);
        }
        writer.write(starterBuffer.readRemainder());
        starterWriter.close();
        writer.close();
    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(StarterEditorGen4::fileToInt));
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

    private static int getPokemon(String pokemon)
    {
        for(int i= 0; i < nameData.length; i++)
        {
            if(pokemon.equalsIgnoreCase(nameData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid pokemon entered: " + pokemon);
    }
}
