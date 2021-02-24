package com.turtleisaac.pokeditor.editors.tutors.sinnoh;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.CsvReader;
import com.turtleisaac.pokeditor.project.Project;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class TutorMoveListEditor
{
    private Project project;
    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String resourcePath = path + "Program Files" + File.separator;
    private static String[] nameData;
    private static String[] moveData;
    private static String[] tutorLocations;
//    private static String[] tutorLocations= new String[] {"Route 212", "Survival Area", "Snowpoint City"};
    private String tutorFile;
    private String gameCode;
    private ArrayList<TutorMoveListData> dataList;
    private int offset;
    private Buffer buffer;
    private BinaryWriter writer;

    private static final int S_TUTORS_PT_E= 0x2ff64;
    private static final int S_TUTORS_PT_J= 0x2ff64;
    private static final int S_TUTORS_PT_F= 0x2ff64;
    private static final int S_TUTORS_PT_G= 0x2ff64;
    private static final int S_TUTORS_PT_I= 0x2ff64;
    private static final int S_TUTORS_PT_S= 0x2ff64;
    private static final int S_TUTORS_PT_K= 0x2ff64;

    private static final int S_TUTORS_DP_E= 0x165a32;
    private static final int S_TUTORS_DP_J= 0x165a32;
    private static final int S_TUTORS_DP_F= 0x165a32;
    private static final int S_TUTORS_DP_G= 0x165a32;
    private static final int S_TUTORS_DP_I= 0x165a32;
    private static final int S_TUTORS_DP_S= 0x165a32;
    private static final int S_TUTORS_DP_K= 0x165a32;

    public TutorMoveListEditor(Project project, String gameCode, String tutorFile) throws IOException
    {
        this.project= project;
        this.gameCode = gameCode;
        this.tutorFile= tutorFile;
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

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "TutorLocationsSinnoh.txt"));
        String line;
        ArrayList<String> locationList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            locationList.add(line);
        }
        tutorLocations= locationList.toArray(new String[0]);
        reader.close();


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

        System.out.println("Offset: " + offset + " (0x" + Integer.toHexString(offset) + ")");
    }

    public void moveListToCsv() throws IOException
    {
        dataList= new ArrayList<>();
        buffer= new Buffer(tutorFile);
        buffer.skipTo(offset);


        for(int i= 0; i < 38; i++)
        {
            int moveId= buffer.readUInt16();

            int redShards= buffer.readByte();
            int blueShards= buffer.readByte();
            int yellowShards= buffer.readByte();
            int greenShards= buffer.readByte();

            buffer.skipBytes(2);
            int tutor= buffer.readInt();

            System.out.println("ID: " + i);
            System.out.println("Move: " + moveData[moveId]);
            System.out.println("# Red Shards: " + redShards);
            System.out.println("# Blue Shards: " + blueShards);
            System.out.println("# Yellow Shards: " + yellowShards);
            System.out.println("# Green Shards: " + greenShards);
            System.out.println("Tutor #: " + tutor + " (" + tutorLocations[tutor] + ")\n");

            dataList.add(new TutorMoveListData()
            {
                @Override
                public int getMoveId()
                {
                    return moveId;
                }

                @Override
                public int getRedShards()
                {
                    return redShards;
                }

                @Override
                public int getBlueShards()
                {
                    return blueShards;
                }

                @Override
                public int getYellowShards()
                {
                    return yellowShards;
                }

                @Override
                public int getGreenShards()
                {
                    return greenShards;
                }

                @Override
                public int getTutor()
                {
                    return tutor;
                }
            });
        }

        String[][] tutorTable= new String[dataList.size()][];
        int idx;
        for(int i= 0; i < tutorTable.length; i++)
        {
            TutorMoveListData listData= dataList.get(i);
            String[] line= new String[6];
            Arrays.fill(line,"");
            idx= 0;

            line[idx++]= moveData[listData.getMoveId()];
            line[idx++]= "" + listData.getRedShards();
            line[idx++]= "" + listData.getBlueShards();
            line[idx++]= "" + listData.getYellowShards();
            line[idx++]= "" + listData.getGreenShards();
            line[idx]= "" + listData.getTutor();

            tutorTable[i]= line;
        }


        BufferedWriter csvWriter= new BufferedWriter(new FileWriter(path + "tutorMoveData.csv"));
        csvWriter.write("ID Number,Move,Red Shard Cost,Blue Shard Cost,Yellow Shard Cost,Green Shard Cost,Tutor\n");
        String line;
        for(int row= 0; row < tutorTable.length; row++)
        {
            line= row + ",";
            for(int col= 0; col < tutorTable[0].length; col++)
            {
                line+= tutorTable[row][col] + ",";
            }
            line+= "\n";
            csvWriter.write(line);
        }
        csvWriter.close();

        System.out.println("Current position: " + buffer.getPosition());
        TutorCompatibilityEditor tutorCompatibilityEditor= new TutorCompatibilityEditor(project, gameCode);
        tutorCompatibilityEditor.compatibilityToCsv(buffer);
    }



    public void csvToMoveList(String moveListCsv, String compatibilityCsv, String outputFile) throws IOException
    {
        String moveListPath = path + moveListCsv;
        String outputPath;

        if (outputFile.contains("Recompile"))
        {
            outputPath = path + "temp" + File.separator + outputFile;
        } else
        {
            outputPath = path + outputFile;
        }


        CsvReader csvReader = new CsvReader(moveListPath,1,1);
        BinaryWriter writer= new BinaryWriter(outputPath);
        Buffer buffer= new Buffer(tutorFile);
        writer.write(buffer.readBytes(offset));
        for (int i = 0; i < csvReader.length(); i++)
        {
            initializeIndex(csvReader.next());
            writer.writeShort(getMove(next()));
            writer.writeByte((byte) Short.parseShort(next()));
            writer.writeByte((byte) Short.parseShort(next()));
            writer.writeByte((byte) Short.parseShort(next()));
            writer.writeByte((byte) Short.parseShort(next()));
            writer.writeBytes(0,0);
            writer.writeInt((int) Long.parseLong(next()));
            buffer.skipBytes(12);
        }

        TutorCompatibilityEditor tutorCompatibilityEditor= new TutorCompatibilityEditor(project, gameCode);
        tutorCompatibilityEditor.csvToCompatibility(compatibilityCsv,writer,buffer);
    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(TutorMoveListEditor::fileToInt));
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

    private static int getTutorLocation(String tutor)
    {
        for(int i= 0; i < tutorLocations.length; i++)
        {
            if(tutor.equals(tutorLocations[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid tutor location entered: " + tutor);
    }
}
