package com.turtleisaac.pokeditor.editors.evolutions.gen4;

import com.turtleisaac.pokeditor.editors.evolutions.EvolutionData;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;

import java.io.*;
import java.util.*;

public class EvolutionEditor
{
    private Project project;
    private static String projectPath;
    private String dataPath;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fairy", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static String[] evolutionMethodArr;
    private static String resourcePath;
    private static String defaultsPath;
    private static String[] nameData;
    private static String[] itemData;
    private static String[] moveData;
    private static boolean autoFix;

    private Game baseRom;


    public EvolutionEditor(Project project, String projectPath, Game baseRom) throws IOException
    {
        this.project= project;
        this.projectPath= projectPath;
        this.baseRom= baseRom;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;
        defaultsPath= resourcePath + "Defaults" + File.separator;


        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                moveData= TextEditor.getBank(project,588);
                itemData= TextEditor.getBank(project,344);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                moveData= TextEditor.getBank(project,647);
                itemData= TextEditor.getBank(project,392);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                moveData= TextEditor.getBank(project,750);
                itemData= TextEditor.getBank(project,222);
                break;
        }

        System.out.println();
        for(int i= 0; i < itemData.length; i++)
        {
            System.out.println(i + ": " + itemData[i]);
        }
        System.out.println();

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "EvolutionMethodsGen4.txt"));
        String line;
        ArrayList<String> evolutionList= new ArrayList<>();
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            evolutionList.add(line);
        }
        evolutionMethodArr= evolutionList.toArray(new String[0]);
        reader.close();
    }


    public Object[][] evolutionsToSheet(String evolutionDir, boolean easyDisplay, boolean random) throws IOException
    {
        autoFix= false;

        dataPath+= evolutionDir;
        Buffer evolutionBuffer;
        ArrayList<EvolutionData> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        CsvReader csvReader= new CsvReader(defaultsPath + "evolution4.csv");
        int count= 0;

        for(int i= 0; i < files.length; i++)
        {
            file= files[i];
            if(file.length() > 44)
            {
                throw new RuntimeException("This is not an evolution file");
            }
            evolutionBuffer= new Buffer(file.toString());
            initializeIndex(csvReader.next());
            int finalCount= count;
            count++;

            int[] evolutionMethods= new int[7];
            int[] padding= new int[7];
            int[] requirementNumbers= new int[7];
            int[] evolvedIDs= new int[7];
            for(int e= 0; e < 7; e++)
            {
                if(autoFix)
                {
                    evolutionMethods[e]= evolutionBuffer.readSelectiveByte(evolutionMethodArr.length-1,getEvolutionMethod(next()));
                }
                else
                {
                    evolutionMethods[e]= evolutionBuffer.readByte();
                }
                padding[e]= evolutionBuffer.readByte();
                requirementNumbers[e]= evolutionBuffer.readShort();
                if(autoFix)
                {
                    evolvedIDs[e]= evolutionBuffer.readSelectiveShort(nameData.length-1, (short) getPokemon(next()));
                }
                else
                {
                    evolvedIDs[e]= evolutionBuffer.readShort();
                }

            }

            dataList.add(new EvolutionData() {
                @Override
                public int getNum() {
                    return finalCount;
                }

                @Override
                public int[] getEvolutionMethod() {
                    return evolutionMethods;
                }

                @Override
                public int[] getPadding() {
                    return padding;
                }

                @Override
                public int[] getRequirementNumber() {
                    return requirementNumbers;
                }

                @Override
                public int[] getEvolvedID() {
                    return evolvedIDs;
                }
            });
            evolutionBuffer.close();
        }

        if(random)
        {
            for(int i= 0; i < dataList.size(); i++)
            {
                dataList.set(i,dataList.get(0));
            }
        }

        String[][] evolutionTable;
        if(easyDisplay)
        {
            evolutionTable= new String[dataList.size()][7];
        }
        else
        {
            evolutionTable= new String[dataList.size()][7*3];
        }

        for (String[] row : evolutionTable)
        {
            Arrays.fill(row, "");
        }

        for(int row= 0; row < dataList.size(); row++)
        {
            String name;
            try
            {
                name= nameData[row];
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                name= "p" + row;
            }

            if(easyDisplay)
            {
                for (int col = 0; col < evolutionTable[0].length; col++)
                {
                    int evolutionMethodID = dataList.get(row).getEvolutionMethod()[col];

                    int requirementNumber = dataList.get(row).getRequirementNumber()[col];
                    int evolvedID = dataList.get(row).getEvolvedID()[col];
                    if (evolutionMethodID == 0) {
                        evolutionTable[row][col] = "None";
                    }
                    if (evolutionMethodID == 1 || evolutionMethodID == 2 || evolutionMethodID == 3) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " with max Happiness";
                    }
                    if (evolutionMethodID == 2) {
                        evolutionTable[row][col] += " (Day)";
                    }
                    if (evolutionMethodID == 3) {
                        evolutionTable[row][col] += " (Night)";
                    }
                    if (evolutionMethodID == 4) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber;
                    }
                    if (evolutionMethodID == 5 || evolutionMethodID == 6) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " upon trading";
                    }
                    if (evolutionMethodID == 6) {
                        evolutionTable[row][col] += " while holding " + itemData[requirementNumber];
                    }
                    if (evolutionMethodID == 7 || evolutionMethodID == 16 || evolutionMethodID == 17 || evolutionMethodID == 18 || evolutionMethodID == 19) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " when " + itemData[requirementNumber] + " is used";
                    }
                    if (evolutionMethodID == 8 || evolutionMethodID == 9 || evolutionMethodID == 10 || evolutionMethodID == 11 || evolutionMethodID == 12) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber;
                    }
                    if (evolutionMethodID == 8) {
                        evolutionTable[row][col] += " when Attack stat is greater than Defense stat";
                    }
                    if (evolutionMethodID == 9) {
                        evolutionTable[row][col] += " when Attack stat is equal to Defense stat";
                    }
                    if (evolutionMethodID == 10) {
                        evolutionTable[row][col] += " when Defense stat is greater than Attack stat";
                    }
                    if (evolutionMethodID == 11) {
                        evolutionTable[row][col] += " when PID is greater than 5";
                    }
                    if (evolutionMethodID == 12) {
                        evolutionTable[row][col] += " when PID is less than 5";
                    }
                    if (evolutionMethodID == 13) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber;
                    }
                    if (evolutionMethodID == 14) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber;
                    }
                    if (evolutionMethodID == 15) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at maximum beauty " + requirementNumber;
                    }
                    if (evolutionMethodID == 16) {
                        evolutionTable[row][col] += " (Male)";
                    }
                    if (evolutionMethodID == 17) {
                        evolutionTable[row][col] += " (Female)";
                    }
                    if (evolutionMethodID == 18) {
                        evolutionTable[row][col] += " (Day)";
                    }
                    if (evolutionMethodID == 19) {
                        evolutionTable[row][col] += " (Night)";
                    }
                    if (evolutionMethodID == 20) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " when the move " + moveData[requirementNumber] + " is known";
                    }
                    if (evolutionMethodID == 21) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " when the Pokemon " + nameData[requirementNumber] + " is in the party";
                    }
                    if (evolutionMethodID == 22) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber + " (Male)";
                    }
                    if (evolutionMethodID == 23) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber + " (Female)";
                        System.out.println(evolutionTable[row][col]);
                    }
                    if (evolutionMethodID == 24) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber + " in Mt. Coronet";
                    }
                    if (evolutionMethodID == 25) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber + " in Eterna Forest";
                    }
                    if (evolutionMethodID == 26) {
                        evolutionTable[row][col] = "Evolves into " + nameData[evolvedID] + " at level " + requirementNumber + " in Route 217";
                    }
                    if (evolutionMethodID > 26) {
                        evolutionTable[row][col] = "ERROR";
                    }
                }
            }
            else
            {
                int current= 0;
                System.out.println(name);
                for (int col = 0; col < 7; col++)
                {
                    int evolutionMethodID = dataList.get(row).getEvolutionMethod()[col];
                    int requirementNumber = dataList.get(row).getRequirementNumber()[col];
                    int evolvedID = dataList.get(row).getEvolvedID()[col];


                    evolutionTable[row][current]= evolutionMethodArr[evolutionMethodID];
                    System.out.println("    " + evolutionTable[row][current]);
                    current++;

                    if (evolutionMethodID > 0 && evolutionMethodID <= 3) //all happiness evolutions
                    {
                        evolutionTable[row][current]= "" + requirementNumber;
                    }
                    else if (evolutionMethodID == 4 || (evolutionMethodID >= 8 && evolutionMethodID <= 14) || evolutionMethodID >= 22) //all level up evolutions
                    {
                        evolutionTable[row][current]= "" + requirementNumber;
                    }
                    else if (evolutionMethodID == 5) //trade without item
                    {
                        evolutionTable[row][current]= "" + requirementNumber;
                    }
                    else if (evolutionMethodID == 6 || evolutionMethodID == 7 || (evolutionMethodID >= 16 && evolutionMethodID <= 19)) //all item-based evolutions (including trades)
                    {
                        evolutionTable[row][current]= itemData[requirementNumber];
                    }
                    else if (evolutionMethodID == 15) //max beauty
                    {
                        evolutionTable[row][current]= "" + requirementNumber;
                    }
                    else if (evolutionMethodID == 20) //attack known
                    {
                        evolutionTable[row][current]= moveData[requirementNumber];
                    }
                    else if (evolutionMethodID == 21)
                    {
                        String requireName;
                        try
                        {
                            requireName= nameData[requirementNumber];
                        }
                        catch (ArrayIndexOutOfBoundsException e)
                        {
                            requireName= "p" + requirementNumber;
                        }

                        evolutionTable[row][current]= requireName;
                    }
                    System.out.println("    " + evolutionTable[row][current]);
                    current++;

                    String evolvedName;
                    try
                    {
                        evolvedName= nameData[evolvedID];
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        evolvedName= "p" + evolvedID;
                    }


                    evolutionTable[row][current]= evolvedName;
                    System.out.println("    " + evolutionTable[row][current]);
                    current++;
                }
                System.out.println("    Current: " + current);
            }
        }

        ArrayProcessor processor= new ArrayProcessor();
        if(easyDisplay)
        {
            processor.append("ID Number,Name,Evolution 1,Evolution 2,Evolution 3,Evolution 4,Evolution 5,Evolution 6,Evolution 7\n");
        }
        else
        {
            processor.append("ID Number,Name,Method,Required,Result,Method,Required,Result,Method,Required,Result,Method,Required,Result,Method,Required,Result,Method,Required,Result,Method,Required,Result\n");
        }

        String line;
        for(int row= 0; row < dataList.size(); row++)
        {
            String name;
            try
            {
                name= nameData[row];
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                name= "p" + row;
            }

            line= dataList.get(row).getNum() + "," + name + ",";
            for(int col= 0; col < evolutionTable[0].length; col++)
            {
                line+=evolutionTable[row][col] + ",";
            }
            processor.newLine();
            processor.append(line);
        }

        return processor.getTable();
    }

    public void sheetToEvolutions(Object[][] evolutionCsv, String outputDir) throws IOException
    {
        String outputPath= dataPath + outputDir;

        if(!new File(outputPath).exists())
        {
            if(!new File(outputPath).mkdir())
            {
                System.err.println(outputPath);
                throw new RuntimeException("Could not create output directory. Check write permissions");
            }
        }

        evolutionCsv= ArrayModifier.trim(evolutionCsv,1,2);

        for(int i= 0; i < evolutionCsv.length; i++)
        {
            Object[] thisLine= evolutionCsv[i];
            System.out.println(i + ": " + Arrays.toString(thisLine));
            BinaryWriter writer= new BinaryWriter(outputPath + File.separator + i + ".bin");
            for(int e= 0; e < thisLine.length; e+= 3)
            {
                int method= getEvolutionMethod((String) thisLine[e]);
                writer.writeByte((byte)method);
                writer.writeByte((byte) 0x00);
                String require= (String) thisLine[e+1];
                if(require.equals(""))
                {
                    require= "0";
                }

                if(method <= 3 || method == 5 || method == 15)
                {
                    writer.writeShort(Short.parseShort(require));
                }
                if(method == 4 || (method >= 8 && method <= 14) || method >= 22)
                {
                    writer.writeShort(Short.parseShort(require));
                }
                if(method == 6 || method == 7 || (method >= 16 && method <= 19))
                {
                    writer.writeShort((short)getItem(require));
                }
                if(method == 20)
                {
                    writer.writeShort((short)getMove(require));
                }
                if(method == 21)
                {
                    writer.writeShort((short)getPokemon(require));
                }
                writer.writeShort((short)getPokemon((String) thisLine[e+2]));
            }
            writer.writeShort((short) 0x00);
            writer.close();
        }

    }




    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(EvolutionEditor::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private int arrIdx;
    private String[] input;

    private void initializeIndex(String[] arr)
    {
        arrIdx= 0;
        input= arr;
    }

    private String next()
    {
        try
        {
            return input[arrIdx++];
        }
        catch (IndexOutOfBoundsException e)
        {
            return "";
        }
    }

    private static int getEvolutionMethod(String evo)
    {
        for(int i= 0; i < evolutionMethodArr.length; i++)
        {
            if(evo.equals(evolutionMethodArr[i]))
            {
                return i;
            }
        }

        if(evo.equals(""))
            return 0;
        throw new RuntimeException("Invalid evolution method entered: " + evo);
    }

    private static int getType(String type)
    {
        for(int i= 0; i < typeArr.length; i++)
        {
            if(type.equals(typeArr[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid type entered");
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

    private static int getPokemon(String pokemon)
    {
        for(int i= 0; i < nameData.length; i++)
        {
            if(pokemon.equals(nameData[i]))
            {
                return i;
            }
        }

        if(pokemon.equals(""))
            return 0;

        int species;
        try
        {
            species= Integer.parseInt(pokemon);
        }
        catch (NumberFormatException ignored)
        {
            throw new RuntimeException("Invalid pokemon entered: " + pokemon);
        }

        return species;
    }

    private static int getItem(String item)
    {
        for(int i= 0; i < itemData.length; i++)
        {
            if(item.equals(itemData[i]))
            {
                    return i;
            }
        }
        throw new RuntimeException("Invalid item entered: " + item);
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

    private static byte[] parseShorts(short[] shorts)
    {
        byte[] buf= new byte[shorts.length*2];
        for(int i= 0; i < shorts.length; i+=2)
        {
            short s= shorts[i];
            buf[i] = (byte) (s & 0xff);
            buf[i+1] = (byte) ((s >> 8) & 0xff);
        }
        return buf;
    }
}
