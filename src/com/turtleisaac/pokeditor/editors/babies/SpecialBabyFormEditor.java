package com.turtleisaac.pokeditor.editors.babies;

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

public class SpecialBabyFormEditor
{
    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String resourcePath = path + "Program Files" + File.separator;
    private static String[] nameData;
    private String gameCode;
    private ArrayList<SpecialBabyFormData> dataList;
    private int offset;
    private Buffer starterBuffer;
    private BinaryWriter writer;

    private static final int S_BABIES_PT_E= 0x17a7ec;
    private static final int S_BABIES_PT_J= 0x17a7ec;
    private static final int S_BABIES_PT_F= 0x17a7ec;
    private static final int S_BABIES_PT_G= 0x17a7ec;
    private static final int S_BABIES_PT_I= 0x17a7ec;
    private static final int S_BABIES_PT_S= 0x17a7ec;
    private static final int S_BABIES_PT_K= 0x17a7ec;

    private static final int S_BABIES_DP_E= 0x165a32;
    private static final int S_BABIES_DP_J= 0x165a32;
    private static final int S_BABIES_DP_F= 0x165a32;
    private static final int S_BABIES_DP_G= 0x165a32;
    private static final int S_BABIES_DP_I= 0x165a32;
    private static final int S_BABIES_DP_S= 0x165a32;
    private static final int S_BABIES_DP_K= 0x165a32;

    public SpecialBabyFormEditor(String gameCode) throws IOException
    {
        //TODO make work with v2
        this.gameCode = gameCode;
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

        String noRegion= gameCode.substring(0,3).toLowerCase();

        switch (noRegion)
        {
            case "cpu" :
                switch (gameCode.toLowerCase().substring(3))
                {
                    case "e" :
                        offset= S_BABIES_PT_E;
                        break;
                    case "j" :
                        offset= S_BABIES_PT_J;
                        break;
                    case "f" :
                        offset= S_BABIES_PT_F;
                        break;
                    case "g" :
                        offset= S_BABIES_PT_G;
                        break;
                    case "i" :
                        offset= S_BABIES_PT_I;
                        break;
                    case "s" :
                        offset= S_BABIES_PT_S;
                        break;
                    case "k" :
                        offset= S_BABIES_PT_K;
                        break;
                    default:
                        throw new RuntimeException("Unsupported Region");
                }
                break;

            case "apa" :
            case "ada" :
                switch (gameCode.toLowerCase().substring(3))
                {
                    case "e" :
                        offset= S_BABIES_DP_E;
                        break;
                    case "j" :
                        offset= S_BABIES_DP_J;
                        break;
                    case "f" :
                        offset= S_BABIES_DP_F;
                        break;
                    case "g" :
                        offset= S_BABIES_DP_G;
                        break;
                    case "i" :
                        offset= S_BABIES_DP_I;
                        break;
                    case "s" :
                        offset= S_BABIES_DP_S;
                        break;
                    case "k" :
                        offset= S_BABIES_DP_K;
                        break;
                    default:
                        throw new RuntimeException("Unsupported Region");
                }
                break;
            default:
                throw new RuntimeException("This editor can't be used with Gen 5 or HGSS currently.");
        }
    }

    public void changeSpecialBabies(String babyFile) throws IOException
    {
        dataList= new ArrayList<>();
        Buffer buffer= new Buffer(path + "temp" + File.separator + babyFile);
        buffer.skipTo(offset);

        for(int i= 0; i < buffer.getLength() / 3; i++)
        {

        }

    }

    private void editSpecialBabies() throws IOException
    {
        Scanner scanner= new Scanner(System.in);
        String ans;
        int pokemonId;
        int itemId;
        int defaultId;


    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(SpecialBabyFormEditor::fileToInt));
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
