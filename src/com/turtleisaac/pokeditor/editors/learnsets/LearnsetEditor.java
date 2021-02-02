package com.turtleisaac.pokeditor.editors.learnsets;

import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.project.Game;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class LearnsetEditor
{
    private static String projectPath;
    private String dataPath= "";
    private static String resourcePath;
    private static String[] nameData;
    private static String[] moveData;
    private static boolean gen5;
    private Game baseRom;

    private int maxMoves;


    public LearnsetEditor(String projectPath, Game baseRom) throws IOException
    {
        this.projectPath= projectPath;
        this.baseRom= baseRom;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;

        String entryPath= resourcePath;
        String movePath= resourcePath +  "MoveList.txt";

        switch (baseRom)
        {
            case Pearl:
            case Diamond:
            case Platinum:
            case HeartGold:
            case SoulSilver:
                maxMoves= 20;
                entryPath+= "EntryData.txt";
                break;

            case White:
            case Black:
                maxMoves= 25;
                gen5= true;
                entryPath += "EntryDataGen5-1.txt";
                break;

            case White2:
            case Black2:
                maxMoves= 25;
                gen5= true;
                entryPath += "EntryDataGen5-2.txt";
                break;

            default:
                throw new RuntimeException("Invalid arguments");
        }

        BufferedReader reader= new BufferedReader(new FileReader(entryPath));
        ArrayList<String> nameList= new ArrayList<>();
        String line;
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            nameList.add(line);
        }
        nameData= nameList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(movePath));
        ArrayList<String> moveNameList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            moveNameList.add(line);
        }
        moveData= moveNameList.toArray(new String[0]);
        reader.close();
    }

    public Object[][] learnsetToSheet(String learnsetDir)
    {
        dataPath+= learnsetDir;

        Buffer learnsetBuffer;
        ArrayList<ArrayList<MoveLearnsetData>> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for(int i= 0; i < files.length; i++)
        {
            file= files[i];
            learnsetBuffer= new Buffer(file.toString());
            int numMoves;
            learnsetBuffer.skipTo((int) (file.length()-4));
            byte[] last4= learnsetBuffer.readRemainder();
            byte[] properDelimeter= new byte[] {(byte) 0xFF, (byte) 0xFF,0x00,0x00};

            if(!gen5) //gen 4
            {
                if(Arrays.equals(last4, properDelimeter))
                {
                    numMoves= (int) ((file.length()-4)/2);
                }
                else
                {
                    numMoves= (int) ((file.length()-2)/2);
                }
            }
            else //gen 5
            {
                numMoves= (int) ((file.length()-4)/4);
            }

            learnsetBuffer= new Buffer((file.toString()));
            System.out.println(nameData[i] + " numMoves: " + numMoves);
            ArrayList<MoveLearnsetData> moveList= new ArrayList<>();
            for(int m= 0; m < numMoves; m++)
            {
                if(!gen5)
                {
                    short move= learnsetBuffer.readShort();
                    System.out.print("  " + moveData[getMoveId(move)]);
                    System.out.println(": " + getLevelLearned(move));
                    moveList.add(new MoveLearnsetData() {
                        @Override
                        public int getID() {
                            return getMoveId(move);
                        }

                        @Override
                        public int getLevel() {
                            return getLevelLearned(move);
                        }
                    });
                }
                else
                {
                    int move= learnsetBuffer.readUInt16();
                    int level= learnsetBuffer.readUInt16();
                    System.out.print("  " + moveData[move]);
                    System.out.println(": " + level);
                    moveList.add(new MoveLearnsetData() {
                        @Override
                        public int getID() {
                            return move;
                        }

                        @Override
                        public int getLevel() {
                            return level;
                        }
                    });
                }

            }
            dataList.add(moveList);
        }

        String[][] learnsetTable;
        if(gen5)
        {
            learnsetTable= new String[dataList.size()][80];
        }
        else
        {
            learnsetTable= new String[dataList.size()][40];
        }

        for (String[] row : learnsetTable) {
            Arrays.fill(row, "");
        }

        for(int row= 0; row < dataList.size(); row++)
        {
            int idx= 0;
            ArrayList<MoveLearnsetData> thisLearnset= dataList.get(row);
            for(int col= 0; col < thisLearnset.size(); col++)
            {
                learnsetTable[row][idx++]= moveData[thisLearnset.get(col).getID()];
                learnsetTable[row][idx++]= "" + thisLearnset.get(col).getLevel();
            }
        }

        ArrayProcessor processor= new ArrayProcessor((maxMoves*2)+2);
        processor.append("ID Number,Name,");
        for(int i= 0; i < 20; i++)
        {
            processor.append("Move,Level,");
        }
        processor.newLine();

        String line;
        for(int row= 0; row < dataList.size(); row++)
        {
            line= row + "," + nameData[row] + ",";
            for(int col= 0; col < learnsetTable[0].length; col++)
            {
                line+= learnsetTable[row][col] + ",";
            }
            processor.append(line);

            processor.newLine();

        }

        return processor.getTable();
    }

    public void sheetToLearnsets(Object[][] learnsetCsv, String outputDir) throws IOException
    {
        String outputPath= projectPath + outputDir;



        if(!new File(outputPath).exists() && !new File(outputPath).mkdir())
        {
            throw new RuntimeException("Could not create output directory. Check write permissions");
        }

        learnsetCsv= ArrayModifier.trim(learnsetCsv,1,2);
        for(int i= 0; i < learnsetCsv.length; i++)
        {
            System.out.println(nameData[i]);
            Object[] thisLine= learnsetCsv[i];
            int numMoves= thisLine.length/ 2;

            if(!gen5 && numMoves > 20)
            {
                JOptionPane.showMessageDialog(null,"You can't have more than 20 moves","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }
            else if(gen5 && numMoves > 25)
            {
                JOptionPane.showMessageDialog(null,"You can't have more than 25 moves","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            BinaryWriter writer= new BinaryWriter(outputPath + File.separator + i + ".bin");
            for(int m= 0; m < thisLine.length; m+= 2)
            {
                System.out.println("    " + thisLine[m]);
                int moveID= getMove((String) thisLine[m]);
                int level= Integer.parseInt((String) thisLine[m+1]);
                MoveLearnsetData thisMove= new MoveLearnsetData() {
                    @Override
                    public int getID() {
                        return moveID;
                    }

                    @Override
                    public int getLevel() {
                        return level;
                    }
                };
                if(!gen5)
                {
                    writer.writeShort(produceLearnData(thisMove));
                }
                else
                {
                    writer.writeShort((short) moveID);
                    writer.writeShort((short) level);
                }
            }

            if(!gen5)
            {
                if(numMoves % 2 == 0)
                {
                    writer.write(new byte[] {(byte) 0xFF, (byte) 0xFF,0x00,0x00});
                }
                else
                {
                    writer.write(new byte[] {(byte) 0xFF, (byte) 0xFF});
                }
            }
            else
            {
                writer.write(new byte[] {(byte) 0xFF, (byte) 0xFF,0x00,0x00});
            }

            writer.close();
        }
    }




    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(LearnsetEditor::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private static int getMoveId (short x)
    {
        return x & 0x1FF;
    }

    private static int getLevelLearned (short x)
    {
        return (x >> 9) & 0x7F;
    }

    private static int getMove(String move)
    {
        for(int i= 0; i < moveData.length; i++)
        {
            if(move.equals(moveData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid move entered: " + move);
    }

    private static short parseShort(String... evs)
    {
        String fullEv= "";
        for(String ev : evs)
        {
            fullEv+= ev;
        }

        short num= 0;
        for(int i= 0; i < fullEv.length(); i++)
        {
            String thisEv= fullEv.substring(fullEv.length()-i-1,fullEv.length()-i);
            if(thisEv.equals("1"))
            {
                num+= Math.pow(2,i);
            }
        }
        return num;
    }

    private static short produceLearnData(MoveLearnsetData moveList)
    {
        int id= moveList.getID();
        String idBinary= Integer.toBinaryString(id);
        int level= moveList.getLevel();
        String levelBinary= Integer.toBinaryString(level);

        while(idBinary.length() != 9)
        {
            idBinary= "0" + idBinary;
        }
        while(levelBinary.length() != 7)
        {
            levelBinary= "0" + levelBinary;
        }

        System.out.print("        " + levelBinary);
        System.out.println(idBinary);
        return parseShort(levelBinary,idBinary);
    }
}
