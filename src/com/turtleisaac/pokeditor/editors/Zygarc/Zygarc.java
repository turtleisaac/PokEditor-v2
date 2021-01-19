package com.turtleisaac.pokeditor.editors.Zygarc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;

public class Zygarc
{
    public static void main(String[] args) throws IOException
    {
        Zygarc zygarc= new Zygarc(true);
        zygarc.unpack("5");
    }

    private String separator = File.separator;
    private String path = System.getProperty("user.dir") + File.separator;
    private String extractPath = path + "temp" + File.separator;
    private static String[] extensionStrings;
    private String outputGarcPath= path + "temp" + File.separator;

    static {
        extensionStrings = new String[]{"bmd0", "btx0", "ncsr", "nclr", "ncgr", "nanr", "nmar", "nmcr", "ncer", "sdat", "narc", "nscr", "ntfp", "ntft", "ntfs", "pmcp", "sseq", "ssar", "swar", "sbnk"}; //creates a String[] that contains each file extension corresponding to the hex string at the same index in identifiers.hex
    }

    public Zygarc(boolean manualAccess) throws IOException
    {
        if(manualAccess)
        {
            extractPath = path + "extracted" + File.separator;
            outputGarcPath= path;
        }

        String extensionPath = path + "Program Files" + File.separator + "identifiers.hex"; //creates a String containing the path to identifiers.hex (constant)
        for (int i = 0; i < new File(extensionPath).length() / 4; i++) //goes through the file four bytes at a time
        {
            Buffer extensions = new Buffer(extensionPath); //creates a new Framework.Buffer object for reading from identifiers.hex
            //creates a new ArrayList of byte[] to contain the file extension hex strings
            ArrayList<byte[]> fileExtensions = new ArrayList<>();
            fileExtensions.add(extensions.readBytes(4)); //adds the current set of four bytes to the fileExtensions ArrayList as a byte[]
            extensions.close(); //closes Framework.Buffer object extensions' internal BufferedInputStream and flushes data
        }

        if (!separator.equals("/")) {
            separator = "\\";
        }
    }

    public void unpack(String garc) throws IOException
    {
        boolean decompress= false;
        Scanner scanner = new Scanner(System.in); //creates Scanner object

        File file = new File(garc);
        String fileName = file.getName();
        String noExtension;
        if(fileName.toLowerCase().contains(".garc"))
        {
            noExtension = fileName.substring(0, fileName.length() - 5);
        }
        else
        {
            noExtension= fileName;
        }

        System.out.println("No Extension: " + noExtension);
        if (!new File(extractPath + noExtension).exists()) //checks if "extracted" directory in user.dir does not exist
        {
            if (!new File(extractPath + noExtension).mkdirs()) //if it does not exist, creates "extracted" directory and directory matching name of parameter garc if able to, otherwise see RuntimeException
            {
                throw new RuntimeException("Could not create garc directory. Check write permissions");
            }
        }
        else //if a folder matching the name of the input garc already exists within "extracted"
        {
            while (true) //this makes sure that the program will keep asking if the user inputs an invalid response
            {
                System.out.println("A directory named " + noExtension + " already exists. Do you want to overwrite? (y/N)"); //asks user to confirm overwriting existing folder
                String input = scanner.nextLine().toLowerCase(); //prompt for user input
                if (input.equals("y")) //checks to see if the user typed "y", meaning "yes"
                {
                    clearDirectory(new File(extractPath + noExtension));
                    if (!new File(extractPath + noExtension).mkdir()) //creates new folder matching the name of the input garc if able to, otherwise see RuntimeException
                    {
                        throw new RuntimeException("Could not create " + noExtension + " directory. Check write permissions.");
                    }
                    break; //escapes the infinite loop and continues to garc unpacking
                } else if (input.equals("n")) //checks to see if the user typed "N", meaning "no"
                {
                    System.out.println("Process aborted"); //alerts user to the process being aborted
                    System.exit(0); //escapes the infinite loop by ending program
                } else //if the user input does not match either "y" or "N"
                {
                    System.out.println("User input does not match either specified input. Please try again."); //tells user to try again, returns to top of while-loop following this
                }
            }
        }
        extractPath += noExtension + File.separator;

        //Garc Header (CRAG/ GARC)
        Buffer buffer = new Buffer(garc); //creates new "Framework.Buffer" object using the path to the garc as a parameter

        String magic = buffer.readString(4);
        if (!magic.equals("CRAG"))
            throw new RuntimeException("Not a GARC file");

        int headerSize = buffer.readInt();
        int endianId= buffer.readUIntS();
        if (endianId == 0xfffe)
            throw new RuntimeException("Big endian GARC files are not supported yet");
        else if (endianId != 0xfeff)
            throw new RuntimeException("Not a GARC file");

        short headerConstant= buffer.readShort();
        long fileSize= buffer.readUIntI();
        long dataOffset= buffer.readUIntI();
        long fileLength= buffer.readUIntI();
        long lastSize= buffer.readUIntI();

        //File Allocation Table Offsets (FATO/ OTAF)
        ArrayList<GarcSubFile> subFiles= new ArrayList<>();
        String otafMagic = buffer.readString(4);
        long otafSize= buffer.readUIntI();

        buffer.skipBytes((int) (otafSize-8));
//        long numFiles = buffer.readUIntI();
//        System.out.println("Number of Files: " + numFiles);
//        buffer.skipBytes(2); //padding
//
//        for(int i= 0; i < numFiles; i++)
//        {
//            long val= buffer.readUIntI();
//            subFiles.add(new GarcSubFile()
//            {
//                @Override
//                public long getOtafEntry()
//                {
//                    return val;
//                }
//
//                @Override
//                public long getBits()
//                {
//                    return 0;
//                }
//
//                @Override
//                public long getStartingOffset()
//                {
//                    return 0;
//                }
//
//                @Override
//                public long getEndingOffset()
//                {
//                    return 0;
//                }
//
//                @Override
//                public long getLength()
//                {
//                    return 0;
//                }
//
//                @Override
//                public long getTrashBytes()
//                {
//                    return 0;
//                }
//
//                @Override
//                public String getName()
//                {
//                    return null;
//                }
//            });
//        }

        //File Allocation Table Block (BTAF/ FATB)
        String fatbMagic = buffer.readString(4); //sets String "fatbMagic" to the BTAF identifier
        int fatbSize = buffer.readInt();
        int numFiles2 = buffer.readInt();

        long lastEnd= 0;
        int count= 0;
        for(int i= 0; i < numFiles2; i++)
        {
            long bits= buffer.readUIntI();
            long startingOffset= buffer.readUIntI();
            long endingOffset= buffer.readUIntI();
            long length= buffer.readUIntI();

            if(length != endingOffset-startingOffset)
                throw new RuntimeException("Invalid FATB entry at file number: " + i + ", Lengths: " + length + ", " + (endingOffset-startingOffset) + ", at position: " + buffer.getPosition());

            if (startingOffset != lastEnd) {
                count++;
            }
            long startEndOffset = startingOffset - lastEnd;
            lastEnd = endingOffset;

            int finalI = i;
            subFiles.set(i, new GarcSubFile()
            {
                @Override
                public long getOtafEntry()
                {
//                    return subFiles.get(finalI).getOtafEntry();
                    return 0;
                }

                @Override
                public long getBits()
                {
                    return bits;
                }

                @Override
                public long getStartingOffset()
                {
                    return startingOffset;
                }

                @Override
                public long getEndingOffset()
                {
                    return endingOffset;
                }

                @Override
                public long getLength()
                {
                    return length;
                }

                @Override
                public long getTrashBytes()
                {
                    return startEndOffset;
                }

                @Override
                public String getName()
                {
                    return "" + finalI;
                }
            });
        }

        //File Image Block (BMIF/ FIMB)
        long fimbOffset= buffer.getPosition() + 8;
        String fimbMagic= buffer.readString(4);
        long fimbSize= buffer.readUIntI();
        long fimbDataSize= buffer.readUIntI();

        BinaryWriter writer;
        for(int i= 0; i < subFiles.size(); i++)
        {
            writer= new BinaryWriter(extractPath + i + ".bin");
            GarcSubFile subFile= subFiles.get(i);
            buffer.skipTo((int) subFile.getStartingOffset());
            writer.write(buffer.readBytes((int) subFile.getLength()));
            writer.close();
        }

        buffer.close();
        System.out.println("Process completed. Output directory can be found at: " + extractPath);
    }

    public void pack()
    {

    }

    private void sort(File arr[]) {
        Arrays.sort(arr, Comparator.comparingInt(Zygarc::fileToInt));
    }

    private static int fileToInt(File f) {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private void clearDirectory(File directory) {
        for (File subfile : directory.listFiles()) {
            if (subfile.isDirectory()) {
                clearDirectory(subfile);
            } else {
                subfile.delete();
            }
        }
        directory.delete();
    }

    private static String fixBinaryString(String str, int len) {
        StringBuilder strBuilder = new StringBuilder(str);
        while (strBuilder.length() != len) {
            strBuilder.insert(0, "0");
        }
        str = strBuilder.toString();
        return str;
    }
}
