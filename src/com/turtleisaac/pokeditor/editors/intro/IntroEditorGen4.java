package com.turtleisaac.pokeditor.editors.intro;

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

public class IntroEditorGen4
{
    private static final String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static final String resourcePath = path + "Program Files" + File.separator;
    private static String[] nameData;
    private final String gameCode;
    Buffer introBuffer;
    BinaryWriter writer;

    public IntroEditorGen4(Project project, String gameCode) throws IOException
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
        }
    }

    public void changeIntroPokemon(String introFile) throws IOException
    {
        introBuffer= new Buffer(introFile);
        writer= new BinaryWriter(introFile + "Recompile");
        int[] offsets;

        String noRegion= gameCode.substring(0,3).toLowerCase();
        switch (noRegion)
        {
            case "cpu": //Platinum
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offsets= new int[] {0x1bac};
                } else
                {
                    offsets= new int[] {0xf54,0x1588};
                }
                editIntroData(offsets);
                break;

            case "apa": //Pearl
            case "ada"://Diamond
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offsets =new int[] {0x30};
                } else
                {
                    offsets= new int[] {0xf0c,0x1430};
                }
                editIntroData(offsets);
                break;

            default:
                throw new RuntimeException("This editor can't be used with Gen 5 or HGSS currently.");
        }
    }

    private void editIntroData(int[] offsets) throws IOException
    {
        Scanner scanner= new Scanner(System.in);
        String ans;
        int pokemonId;

        for(int x= 0; x < offsets.length; x++)
        {
            int offset= offsets[x] - introBuffer.getPosition();
            writer.write(introBuffer.readBytes(offset));
            pokemonId= introBuffer.readInt();
            if(x == 0)
            {
                System.out.println("The intro cutscene Pokemon sprite is currently: " + nameData[pokemonId] + ". Do you want to change it? (y/N)\n");
            }
            else
            {
                System.out.println("The intro cutscene Pokemon cry is currently: " + nameData[pokemonId] + ". Do you want to change it? (y/N)\n");
            }

            ans= scanner.nextLine();
            System.out.println("\n");

            if(ans.equalsIgnoreCase("y"))
            {
                System.out.println("Please enter the name of the Pokemon you wish to replace it with\n");
                pokemonId= getPokemon(scanner.nextLine());
                System.out.println("\nIntro Pokemon has been replaced with " + nameData[pokemonId] + "\n");
            }
            else if (x == 0)
            {
                break;
            }
            writer.writeInt(pokemonId);
        }
        writer.write(introBuffer.readRemainder());
    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(IntroEditorGen4::fileToInt));
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
