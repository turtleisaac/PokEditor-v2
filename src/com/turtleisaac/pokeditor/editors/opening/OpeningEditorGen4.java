package com.turtleisaac.pokeditor.editors.opening;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class OpeningEditorGen4
{
    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String resourcePath = path + "Program Files" + File.separator;
    private static String[] nameData;
    private String gameCode;
    private Buffer starterBuffer;
    private BinaryWriter writer;

    public OpeningEditorGen4(Project project, String gameCode) throws IOException
    {
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
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }
    }

    public void changeOpening(String openingFile) throws IOException
    {
        starterBuffer= new Buffer(openingFile);
        writer= new BinaryWriter(openingFile + "Recompile");
        int offset;

        String noRegion= gameCode.substring(0,3).toLowerCase();
        switch (noRegion)
        {
            case "cpu": //Platinum
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offset = 0x6bb4;
                } else
                {
                    offset = 0x6bb4;
                }
                editOpeningData(offset);
                break;

            case "apa": //Pearl
            case "ada"://Diamond
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offset = 0x49ec;
                } else
                {
                    offset = 0x49ec;
                }
                editOpeningData(offset);
                break;

            default:
                throw new RuntimeException("This editor can't be used with Gen 5 or HGSS currently.");
        }
    }

    private void editOpeningData(int offset) throws IOException
    {
        Scanner scanner= new Scanner(System.in);
        String ans;
        int pokemonId;

        writer.write(starterBuffer.readBytes(offset));
        for(int i= 0; i < 3; i++)
        {
            pokemonId= starterBuffer.readInt();
            System.out.println("Opening Cutscene Pokemon " + (i + 1) + " is currently: " + nameData[pokemonId] + ". Do you want to change it? (y/N)\n");
            ans= scanner.nextLine();
            System.out.println("\n");

            if(ans.equalsIgnoreCase("y"))
            {
                System.out.println("Please enter the name of the Pokemon you wish to replace it with\n");
                pokemonId= getPokemon(scanner.nextLine());
                System.out.println("\nPokemon has been replaced with " + nameData[pokemonId] + "\n");
            }
            writer.writeInt(pokemonId);
        }
        writer.write(starterBuffer.readRemainder());
        writer.close();
    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(OpeningEditorGen4::fileToInt));
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
