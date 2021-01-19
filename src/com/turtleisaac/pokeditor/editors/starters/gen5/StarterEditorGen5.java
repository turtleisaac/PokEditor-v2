package com.turtleisaac.pokeditor.editors.starters.gen5;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class StarterEditorGen5
{
    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath = path;
    private static String resourcePath = path + "Program Files" + File.separator;
    private static final String[] typeArr = {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fire", "Water", "Grass", "Electric", "Psychic", "Ice", "Dragon", "Dark", "Fairy"};
    private static final String[] categories = {"Physical", "Special", "Status"};
    private static final String[] statusArr = {"None", "Sleep", "Poison", "Burn", "Freeze", "Paralysis", "Confusion", "Infatuation"};
    private static String[] nameData;
    private String gameCode;
    Buffer starterBuffer;
    BinaryWriter writer;

    public StarterEditorGen5(String gameCode) throws IOException
    {
        this.gameCode = gameCode;

        String entryPath = resourcePath + "EntryData.txt";
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
    }

    public void changeStarters(String startersFile) throws IOException
    {
        starterBuffer= new Buffer(startersFile);
        writer= new BinaryWriter(startersFile + "Recompile");
        int offset;

        switch (gameCode.substring(0,3).toLowerCase())
        {
            case "IPK": //HeartGold
            case "IPG": //SoulSilver
                switch (gameCode.substring(3).toLowerCase())
                {

                }

                break;

            case "CPU": //Platinum
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offset = 0x1bac;
                } else
                {
                    offset = 0x1bc0;
                }
                editStarterData(0x1bc0);
                break;

            case "APA": //Pearl
            case "ADA"://Diamond
                if(gameCode.substring(3).equalsIgnoreCase("j"))
                {
                    offset = 0x30;
                } else
                {
                    offset = 0x1b88;
                }
                editStarterData(0x1b88);
                break;

            case "IRB" : //Black
            case "POKEMON W" : //White
                switch (gameCode.substring(3).toLowerCase())
                {

                }
                break;

            case "IRD" : //White2
            case "POKEMON B2" : //Black2
                switch (gameCode.substring(3).toLowerCase())
                {

                }
                break;
        }
    }

    private void editStarterData(int offset) throws IOException
    {
        Scanner scanner= new Scanner(System.in);
        String ans;
        int pokemonId;

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
            starterBuffer.skipBytes(4);
        }
    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(StarterEditorGen5::fileToInt));
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
