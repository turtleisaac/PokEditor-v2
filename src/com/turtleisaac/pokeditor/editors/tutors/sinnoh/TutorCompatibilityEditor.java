package com.turtleisaac.pokeditor.editors.tutors.sinnoh;

import com.turtleisaac.pokeditor.editors.babies.SpecialBabyFormData;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.BitStream;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;
import com.turtleisaac.pokeditor.project.Project;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class TutorCompatibilityEditor
{

    private Project project;
    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String resourcePath = path + "Program Files" + File.separator;
    private static String[] nameData;
    private static String[] moveData;
    private String compatibilityFile;
    private String gameCode;
    private ArrayList<SpecialBabyFormData> dataList;
    private int offset;

    private static final int S_TUTORS_PT_E= 0x30127+5;
    private static final int S_TUTORS_PT_J= 0x30127+5;
    private static final int S_TUTORS_PT_F= 0x30127+5;
    private static final int S_TUTORS_PT_G= 0x30127+5;
    private static final int S_TUTORS_PT_I= 0x30127+5;
    private static final int S_TUTORS_PT_S= 0x30127+5;
    private static final int S_TUTORS_PT_K= 0x30127+5;

    private static final int S_TUTORS_DP_E= 0x165a32;
    private static final int S_TUTORS_DP_J= 0x165a32;
    private static final int S_TUTORS_DP_F= 0x165a32;
    private static final int S_TUTORS_DP_G= 0x165a32;
    private static final int S_TUTORS_DP_I= 0x165a32;
    private static final int S_TUTORS_DP_S= 0x165a32;
    private static final int S_TUTORS_DP_K= 0x165a32;

    public TutorCompatibilityEditor(Project project, String gameCode) throws IOException
    {
        this.project= project;
        this.gameCode = gameCode;
        String entryPath = resourcePath + "EntryData.txt";
        String movePath= resourcePath + "MoveList.txt";


        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                moveData= TextEditor.getBank(project,588);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                moveData= TextEditor.getBank(project,647);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                moveData= TextEditor.getBank(project,750);
                break;
        }

        String noRegion= gameCode.substring(0,3).toLowerCase();

        switch (noRegion)
        {
            case "cpu" :
                switch (gameCode.toLowerCase().substring(3))
                {
                    case "e" :
                        offset= S_TUTORS_PT_E;
                        break;
                    case "j" :
                        offset= S_TUTORS_PT_J;
                        break;
                    case "f" :
                        offset= S_TUTORS_PT_F;
                        break;
                    case "g" :
                        offset= S_TUTORS_PT_G;
                        break;
                    case "i" :
                        offset= S_TUTORS_PT_I;
                        break;
                    case "s" :
                        offset= S_TUTORS_PT_S;
                        break;
                    case "k" :
                        offset= S_TUTORS_PT_K;
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
                        offset= S_TUTORS_DP_E;
                        break;
                    case "j" :
                        offset= S_TUTORS_DP_J;
                        break;
                    case "f" :
                        offset= S_TUTORS_DP_F;
                        break;
                    case "g" :
                        offset= S_TUTORS_DP_G;
                        break;
                    case "i" :
                        offset= S_TUTORS_DP_I;
                        break;
                    case "s" :
                        offset= S_TUTORS_DP_S;
                        break;
                    case "k" :
                        offset= S_TUTORS_DP_K;
                        break;
                    default:
                        throw new RuntimeException("Unsupported Region");
                }
                break;
            default:
                throw new RuntimeException("This editor can't be used with Gen 5 or HGSS currently.");
        }

        System.out.println("Target offset: " + offset + " (0x" + Integer.toHexString(offset) + ")");
    }

    public void compatibilityToCsv(Buffer buffer) throws IOException
    {
        buffer.skipTo(offset);
        byte[] thisMon;
        String[][] compatibilityTable= new String[nameData.length-1][38];
        for(int i= 0; i < compatibilityTable.length; i++)
        {
            Arrays.fill(compatibilityTable[i],"");
        }
        int idx;

        for(int i= 0; i < compatibilityTable.length; i++)
        {
            idx= 0;
            thisMon= buffer.readBytes(5);
            for(int x= 0; x < thisMon.length; x++)
            {
                compatibilityTable[i][idx++]= "" + ((thisMon[x]  & 0x01) == 1);
                compatibilityTable[i][idx++]= "" + (((thisMon[x] >> 1) & 0x01) == 1);
                compatibilityTable[i][idx++]= "" + (((thisMon[x] >> 2) & 0x01) == 1);
                compatibilityTable[i][idx++]= "" + (((thisMon[x] >> 3) & 0x01) == 1);
                compatibilityTable[i][idx++]= "" + (((thisMon[x] >> 4) & 0x01) == 1);
                compatibilityTable[i][idx++]= "" + (((thisMon[x] >> 5) & 0x01) == 1);
                if(x != 4)
                {
                    compatibilityTable[i][idx++]= "" + (((thisMon[x] >> 6) & 0x01) == 1);
                    compatibilityTable[i][idx++]= "" + (((thisMon[x] >> 7) & 0x01) == 1);
                }
            }
        }

        BufferedWriter csvWriter= new BufferedWriter(new FileWriter(path + "tutorCompatibilityData.csv"));
        csvWriter.write("ID Number,Name,");
        for(int i= 0; i < 38; i++)
        {
            csvWriter.write("='Tutor Move Data'!B" + (i+2) + ",");
        }
        csvWriter.write("\n");

        String line;
        for(int row= 1; row < compatibilityTable.length; row++)
        {
            line= row + "," + nameData[row] + ",";
            for(int col= 0; col < compatibilityTable[0].length; col++)
            {
                line+= compatibilityTable[row][col] + ",";
            }
            line+= "\n";
            csvWriter.write(line);
//            System.out.println(line);
        }
        csvWriter.close();
    }

    public void csvToCompatibility(String compatibilityCsv, BinaryWriter writer, Buffer buffer) throws IOException
    {
        String compatibilityPath = path + compatibilityCsv;

        CsvReader csvReader = new CsvReader(compatibilityPath,2,1);
        BitStream bitStream;
        writer.write(buffer.readBytes(offset-buffer.getPosition()));
        for (int i = 0; i < csvReader.length(); i++)
        {
            bitStream= new BitStream();
            initializeIndex(csvReader.next());
            for(int x= 0; x < 38; x++)
            {
                bitStream.append(next().equalsIgnoreCase("true"));
            }
            buffer.skipBytes(5);
            writer.write(bitStream.toBytes());
//            System.out.println(Arrays.toString(bitStream.toBytes()));
        }
        writer.write(buffer.readRemainder());
        writer.close();
    }


    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(TutorCompatibilityEditor::fileToInt));
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

    private static short getMove(String move)
    {
        for(short i= 0; i < moveData.length; i++)
        {
            if(move.equals(moveData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid move entered: " + move);
    }
}
