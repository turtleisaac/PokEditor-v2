package com.turtleisaac.pokeditor.editors.randomizer;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;

import java.io.*;
import java.util.*;

public class FileRandomizer implements Serializable
{
    public static void main(String[] args) throws IOException
    {
        FileRandomizer randomizer= new FileRandomizer("CPUE",false);
        randomizer.randomizeCompletely("temp" + File.separator + "com/turtleisaac/pokeditor/editors/personal");
//        randomizer.randomize(Objects.requireNonNull(new File("temp" + File.separator + "personal").listFiles()).length);
    }

    private static String path= System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String dataPath = path;
    private String gameCode;
    private boolean gen4;
    private static String[] nameData;
    private boolean names;
    private int[] arr;
    private List<File> fileList;
    private File[] files;




    public FileRandomizer(String gameCode, boolean names) throws IOException
    {
        this.gameCode= gameCode;
        this.names= names;

        String entryFile;

        String noRegion= gameCode.substring(0,3).toLowerCase();
        switch (noRegion)
        {
            case "cpu": //Platinum
            case "apa": //Pearl
            case "ada": //Diamond
            case "ipk": //HeartGold
            case "ipg": //SoulSilver
                gen4= true;
                entryFile= "EntryData.txt";
                break;

            case "irb" : //Black
            case "irw" : //White
                gen4= false;
                entryFile= "EntryDataGen5-1.txt";
                break;

            case "ird" : //White2
            case "ire" : //Black2
                gen4= false;
                entryFile= "EntryDataGen5-2.txt";
                break;

            default:
                throw new RuntimeException("Invalid game code");
        }


        BufferedReader reader= new BufferedReader(new FileReader(path + "Program Files" + File.separator + entryFile));
        ArrayList<String> nameList= new ArrayList<>();
        String line;
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            nameList.add(line);
        }
        nameData= nameList.toArray(new String[0]);
        reader.close();
    }

    public void randomizeCompletely(String dir) throws IOException
    {
        dataPath+= dir;
        fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        arr= generateOrder(files.length);
        printOrder(arr);

        BinaryWriter writer;
        Buffer buffer;
        if(!new File(dataPath + "Recompile").mkdir() && !new File(dataPath + "Recompile").exists())
        {
            throw new RuntimeException("Check write perms");
        }

        for(int i= 0; i < arr.length; i++)
        {
            buffer= new Buffer(files[arr[i]].toString());
            writer= new BinaryWriter(dataPath + "Recompile" + File.separator + files[arr[i]].getName());
            writer.write(buffer.readRemainder());
            buffer.close();
            writer.close();
        }
    }

    public void randomizeWithSet(String dir) throws IOException, ClassNotFoundException
    {
        dataPath+= dir;
        fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        if(new File(path + "temp" + File.separator + "random.ser").exists())
        {
            FileInputStream fileIn= new FileInputStream(path + "temp" + File.separator + "random.ser");
            ObjectInputStream objIn= new ObjectInputStream(fileIn);

            arr= (int[]) objIn.readObject();
            if(arr.length != files.length)
            {
                throw new RuntimeException("The narc that you are trying to randomize contains an amount of files that does not match the amount in the existing randomized set");
            }
            printOrder(arr);

            BinaryWriter writer;
            Buffer buffer;
            if(!new File(dataPath + "Recompile").mkdir() && !new File(dataPath + "Recompile").exists())
            {
                throw new RuntimeException("Check write perms");
            }

            for(int i= 0; i < arr.length; i++)
            {
                buffer= new Buffer(files[arr[i]].toString());
                writer= new BinaryWriter(dataPath + "Recompile" + File.separator + i + ".bin");
                writer.write(buffer.readRemainder());
                buffer.close();
                writer.close();
            }
        }
        else
        {
            throw new RuntimeException("A random order has not been generated yet");
        }
    }

    public void createOrder(int numFiles) throws IOException
    {
        if(!new File(path + "temp").mkdir() && !new File(path + "temp").exists())
        {
            throw new RuntimeException("Check write perms");
        }

        arr= generateOrder(numFiles);
        FileOutputStream fileOut= new FileOutputStream(path + "temp" + File.separator + "random.ser");
        ObjectOutputStream objOut= new ObjectOutputStream(fileOut);
        objOut.writeObject(arr);
    }

    public void randomizeNums(int numEntries)
    {
        int[] arr= generateOrder(numEntries);
        printOrder(arr);
    }



    private int[] generateOrder(int numFiles)
    {
        int[] randomArr= new int[numFiles];
        randomArr[0]= 0;

        for(int i= 1; i < numFiles; i++)
        {
            int num;
            while(true)
            {
                if(gen4 && (i == 494 || i == 495))
                {
                    num= i;
                }
                else if(i >= 650 && i <= 684)
                {
                    num= i;
                }
                else
                {
                    do
                    {
                        num = (int) (Math.random() * numFiles);
                    } while (num == 494 || num == 495 || (num >= 650 && num <= 684));
                }

                if(!contains(randomArr,num))
                {
                    randomArr[i]= num;
                    break;
                }
            }
        }

        return randomArr;
    }

    private void sort(File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(FileRandomizer::fileToInt));
    }

    private static int fileToInt(File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private boolean contains(int[] arr, int val)
    {
        for(int num : arr)
        {
            if(num == val)
            {
                return true;
            }
        }
        return false;
    }

    private void arrChecker(int[] arr)
    {
        for(int i= 0; i < arr.length; i++)
        {
            if(!contains(arr, i))
            {
                throw new RuntimeException("Invalid set: missing value (" + i + ")");
            }
        }

        for (int i = 0; i < arr.length - 1; i++)
        {
            for(int x= 1; x < arr.length; x++)
            {
                if(arr[i] == arr[x] && i != x)
                {
                    System.err.println("Invalid set: value at index " + i + " (" + arr[i] + ")" + " is equal to value at index " + x + " (" + arr[x] + ")");
                }
            }
        }
    }

    private void printOrder(int[] arr)
    {
        arrChecker(arr);
        System.out.println("Generating random list of " + arr.length + " entries\n");
        for(int i= 0; i < arr.length; i++)
        {
            if(names)
                System.out.println(nameData[arr[i]]);
            else
                System.out.println(arr[i]);
        }
    }


}
