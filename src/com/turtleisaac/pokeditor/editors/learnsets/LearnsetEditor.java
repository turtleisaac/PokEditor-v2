package com.turtleisaac.pokeditor.editors.learnsets;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;

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
    private Project project;

    private int maxMoves;


    public LearnsetEditor(String projectPath, Project project) throws IOException
    {
        this.project= project;
        this.projectPath= projectPath;
        this.baseRom= project.getBaseRom();
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;

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

            default:
                throw new RuntimeException("Invalid rom: " + baseRom);
        }

        int oldLength= nameData.length;;
        nameData= Arrays.copyOf(nameData,oldLength + 12);
        nameData[oldLength++]= "Deoxys (A)";
        nameData[oldLength++]= "Deoxys (D)";
        nameData[oldLength++]= "Deoxys (S)";
        nameData[oldLength++]= "Wormadam (S)";
        nameData[oldLength++]= "Wormadam (T)";
        nameData[oldLength++]= "Giratina (O)";
        nameData[oldLength++]= "Shaymin (S)";
        nameData[oldLength++]= "Rotom (Heat)";
        nameData[oldLength++]= "Rotom (Wash)";
        nameData[oldLength++]= "Rotom (Frost)";
        nameData[oldLength++]= "Rotom (Fan)";
        nameData[oldLength]= "Rotom (Mow)";

        switch (baseRom)
        {
            case Pearl:
            case Diamond:
            case Platinum:
            case HeartGold:
            case SoulSilver:
                maxMoves= 20;
                break;

            case White:
            case Black:
            case White2:
            case Black2:
                maxMoves= 25;
                gen5= true;
                break;

            default:
                throw new RuntimeException("Invalid arguments");
        }
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

        if(files.length > nameData.length)
            nameData= ArrayModifier.accommodateLength(nameData,files.length);

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
                learnsetTable[row][idx++]= "='Formatting (DO NOT TOUCH)'!I" + (thisLearnset.get(col).getID()+1);
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
            line= row + ",=Personal!B" + (row+2) + ",";
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
            int numMoves= indexOfEnd(thisLine)/2;
            int numBytes= 0;

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

            ArrayList<MoveLearnsetData> levelLearnset= new ArrayList<>();

            for(int m= 0; m < thisLine.length; m+= 2)
            {
                if(thisLine[m].equals(""))
                    break;

                System.out.println("    " + thisLine[m]);
                int moveID= getMove((String) thisLine[m]);
                int level= Integer.parseInt((String) thisLine[m+1]);
                levelLearnset.add(new MoveLearnsetData() {
                    @Override
                    public int getID() {
                        return moveID;
                    }

                    @Override
                    public int getLevel() {
                        return level;
                    }
                });
            }
            levelLearnset= sortLearnset(levelLearnset);

            BinaryWriter writer= new BinaryWriter(outputPath + File.separator + i + ".bin");
            for (MoveLearnsetData thisMove : levelLearnset)
            {
                if (!gen5) //gen 4
                {
                    writer.writeShort((short) produceLearnData(thisMove));
                } else //gen 5
                {
                    writer.writeShort((short) thisMove.getID());
                    writer.writeShort((short) thisMove.getLevel());
                }

                numBytes += 2;
            }


            if(!gen5) //gen 4
            {
                if(numBytes % 4 == 0)
                {
                    writer.write(new byte[] {(byte) 0xFF, (byte) 0xFF,0x00,0x00});
                }
                else
                {
                    writer.write(new byte[] {(byte) 0xFF, (byte) 0xFF});
                }
            }
            else //gen 5
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

    private static ArrayList<MoveLearnsetData> sortLearnset(ArrayList<MoveLearnsetData> learnsetData)
    {
        ArrayList<MoveLearnsetData> sortedLearnset= new ArrayList<>();
        for(int i= 0; i < learnsetData.size(); i++)
        {
            for (MoveLearnsetData thisMove : learnsetData)
            {
                if(thisMove.getLevel() == i)
                {
                    sortedLearnset.add(thisMove);
                }
            }
        }

        return sortedLearnset;
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

    private static int produceLearnData(MoveLearnsetData moveEntry)
    {
        int id= moveEntry.getID();
        int level= moveEntry.getLevel();

        return ((level & 0x7f) << 9) | (id & 0x1ff);
    }

    private static int indexOfEnd(Object[] arr)
    {
        for(int i= 0; i < arr.length; i++)
        {
            if("".equals(arr[i]))
            {
                return i;
            }
        }

        return -1;
    }
}
