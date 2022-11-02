package com.turtleisaac.pokeditor.framework.narctowl;

import com.turtleisaac.pokeditor.framework.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
/**
 * Author: turtleisaac
 * Start Date: 5/18/2020
 *
 * Thanks/ Credits to:
 * PlatinumMaster for writing the python NARCTool version I used as a reference
 * Vendor, FrankieD, and Zekromaegis for updating and releasing new versions of PlatinumMaster's NARCTool
 * FrankieD for assisting me as we figured out how to read a narc and correctly extract other files from it
 */
public class Narctowl
{
//    public static void main(String[] args) throws Exception {
//        Narctowl narc = new Narctowl(); //creates new NarcEditor object
//        if (args.length != 0) //checks if arguments were provided
//        {
//            if (args[0].toLowerCase().equals("unpack")) //if first argument is unpack
//            {
//                narc.unpack(args[1]); //run NARC.unpack with second argument as parameter
//                System.exit(0);
//            } else if (args[0].toLowerCase().equals("pack")) {
//                narc.pack(narc.extractPath + args[1], args[2]);
//                System.exit(0);
//            }
//        }
//        throw new RuntimeException("\nInvalid arguments. Usage is as follows: java -jar NARCtowl.jar <arguments> \n Unpacking: java -jar NARCtowl.jar unpack <file name (include .narc)> \n Packing: java -jar NARCtowl.jar pack <name of directory in extracted folder> <name of file to create (do not include .narc)> \n   Note: Narc must be in the same folder as NARCtowl.jar \n   Note: Directory to construct narc from must be in \"extracted\" directory");
//    }

    private String separator = File.separator;
    private String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private String extractPath = path + "temp" + File.separator; //creates a new String field containing path, the directory "extracted", and File.separator (/ on Unix systems, \ on Windows)
    private static String[] extensionStrings; //creates a new String[] to be filled later (see static)
    private String outputNarcPath= path + "temp" + File.separator;
    private static final int MAX_SIZE = 1024 * 1024;
    private boolean manualAccess;

    static {
        extensionStrings = new String[]{"bmd0", "btx0", "ncsr", "nclr", "ncgr", "nanr", "nmar", "nmcr", "ncer", "sdat", "narc", "nscr", "ntfp", "ntft", "ntfs", "pmcp", "sseq", "ssar", "swar", "sbnk"}; //creates a String[] that contains each file extension corresponding to the hex string at the same index in identifiers.hex
    }

    public Narctowl(boolean manualAccess) throws IOException
    {
        if(manualAccess)
        {
            extractPath = path + "extracted" + File.separator;
            outputNarcPath= path;
        }

        this.manualAccess= manualAccess;


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

    public Narctowl(String projectPath) throws IOException
    {
        extractPath= projectPath;
        outputNarcPath= path;

        manualAccess= true;


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

    public static int getNumFiles(String narc)
    {
        Buffer buffer= new Buffer(narc);
        buffer.skipBytes(24);
        return (int) buffer.readUInt32();
    }

    public void unpack(String narc, String targetDirectory, boolean makeFolder) throws IOException
    {
        File file = new File(narc);
        String fileName = file.getName();
        String noExtension = fileName.substring(0, fileName.lastIndexOf("."));

        if (makeFolder)
        {
            if (new File(targetDirectory + File.separator + noExtension).exists())
            {
                int result = JOptionPane.showConfirmDialog(null, "There already is a folder with a name matching what unpacking this narc would create.\nContinuing will clear the contents of the folder you have selected.\nWould you like to continue?", "Narc Unpacker", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION)
                {
                    unpack(narc, targetDirectory + File.separator + noExtension);
                }
            }
            else
            {
                unpack(narc, targetDirectory + File.separator + noExtension);
            }
        }
        else
        {
            if (new File(targetDirectory).exists() && new File(targetDirectory).listFiles().length != 0)
            {
                int result = JOptionPane.showConfirmDialog(null, "There already is a folder with a name matching what unpacking this narc would create.\nContinuing will clear the contents of the folder you have selected.\nWould you like to continue?", "Narc Unpacker", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (result == JOptionPane.YES_OPTION)
                {
                    unpack(narc, targetDirectory);
                }
            }
            else
            {
                unpack(narc, targetDirectory);
            }
        }

    }

    public void unpack(String narc, String targetDirectory) throws IOException
    {
//        System.out.println("Unpacking: " + narc);

        if(manualAccess)
        {
            extractPath= targetDirectory;
        }

        boolean decompress= false;
//        Scanner scanner = new Scanner(System.in); //creates Scanner object

        File file = new File(narc);
        String fileName = file.getName();
        String noExtension= targetDirectory.substring(targetDirectory.lastIndexOf(File.separator) + 1);

        if (!new File(extractPath).exists()) //checks if "extracted" directory in user.dir does not exist
        {
            if (!new File(extractPath).mkdirs()) //if it does not exist, creates "extracted" directory and directory matching name of parameter narc if able to, otherwise see RuntimeException
            {
                throw new RuntimeException("Could not create narc directory. Check write permissions");
            }
        }
        else //if a folder matching the name of the input narc already exists within "extracted"
        {
            while (true) //this makes sure that the program will keep asking if the user inputs an invalid response
            {
//                System.out.println("A directory named " + noExtension + " already exists. Do you want to overwrite? (y/N)"); //asks user to confirm overwriting existing folder
//                String input = scanner.nextLine().toLowerCase(); //prompt for user input
//                if (input.equals("y")) //checks to see if the user typed "y", meaning "yes"
//                {
                    clearDirectory(new File(extractPath));
                    if (!new File(extractPath).mkdir()) //creates new folder matching the name of the input narc if able to, otherwise see RuntimeException
                    {
                        throw new RuntimeException("Could not create " + noExtension + " directory. Check write permissions.");
                    }
                    break; //escapes the infinite loop and continues to narc unpacking
//                } else if (input.equals("n")) //checks to see if the user typed "N", meaning "no"
//                {
//                    System.out.println("Process aborted"); //alerts user to the process being aborted
//                    System.exit(0); //escapes the infinite loop by ending program
//                } else //if the user input does not match either "y" or "N"
//                {
//                    System.out.println("User input does not match either specified input. Please try again."); //tells user to try again, returns to top of while-loop following this
//                }
            }
        }

        if(!manualAccess)
            extractPath += noExtension;

        extractPath+= File.separator;

        //Begins the unpacking of narc
        Buffer buffer = new Buffer(narc); //creates new "Framework.Buffer" object using the path to the narc as a parameter

        String magic = buffer.readString(4); //sets String "magic" to the ascii value of the first 4 bytes
        if (!magic.equals("NARC")) { //if the file doesn't start with the NARC hex identifier
            throw new RuntimeException("Not a NARC file"); //end program, throws runtime exception
        }
        int constant = buffer.readInt(); //data between NARC hex identifier and the file size, is constant in all narcs
        int fileSize = buffer.readInt(); //how many bytes are in the file
        short headerSize = buffer.readShort();
        short numSections = buffer.readShort();

        //File Allocation Table Block (FATB)
        String fatbMagic = buffer.readString(4); //sets String "fatbMagic" to the BTAF identifier
        int fatbSize = buffer.readInt(); //obtains size of the incremental length table (number of bytes)
        int numFiles = buffer.readInt(); //obtains number of files to be made

        ArrayList<NarcSubFile> subFiles = new ArrayList<>(); //creates an ArrayList of narcs.NarcSubFile objects that contain the starting offset, ending offset, and name of each subfile
        long lastEnd = 0;
        int count = 0;
        for (int i = 0; i < numFiles; i++) //runs numFiles times, assigns starting offset, ending offset, and name data representing one file to each index
        {
            long startingOffset = buffer.readInt() & 0xffffffffL; //the next four bytes, which contain the starting offset
            long endingOffset = buffer.readInt() & 0xffffffffL; //the next four bytes, which contain the ending offset
            if (startingOffset != lastEnd) {
                count++;
            }

//            if ((startingOffset & 0xffffffffL) == 0xffff950cL)
//            {
//                System.err.println("error: " + i + ": " + startingOffset + ", " + endingOffset);
//            }

            long startEndOffset = startingOffset - lastEnd;
            if ((startingOffset & 0xffffffffL) != 0xffff950cL)
            {
                lastEnd = endingOffset;
            }
            int id = i; //creates a final int variable that can be used in calling the interface narcs.NarcSubFile, as the int "i" is not immutable and constantly changing
            String name = StringWork.appendLeadingZeros(id, ("" + numFiles).length());
            subFiles.add(new NarcSubFile() { //adds a new narcs.NarcSubFile object to the ArrayList
                @Override
                public long getStartingOffset() {
                    return startingOffset; //stores the starting offset in the object
                }

                @Override
                public long getEndingOffset() {
                    return endingOffset; //stores the ending offset in the object
                }

                @Override
                public long getTrashBytes() {
                    return startEndOffset;
                } //stores the number of trash bytes to remove later on

                @Override
                public String getName() {
                    return name; //stores the "name" (number in this scenario) in the object
                }
            });
        }

        //File Name Table Block (FNTB)
        String fntbMagic = buffer.readString(4); //sets String "fntbMagic" to the BTNF identifier
        int fntbSize = buffer.readInt(); //obtains the size of the fntb (number of bytes)
        int fntbStartOffset = buffer.readInt(); //the next four bytes, which contain the starting offset of the fntb
        int fntbFirstFilePos = buffer.readShort(); //the next two bytes, which contain the first file position
        int fntbNDir = buffer.readShort(); //the next two bytes, which contain the number of directories

        if(fntbSize > 16) //if the fntb is more than just the header
        {
//            System.out.println("Skipping to end of FNTB");
            buffer.skipBytes(fntbSize-16); //jump to end of fntb
        }


        //File Images (FIMG)
        int fimgOffset = buffer.getPosition(); //gets current position
        String fimgMagic = buffer.readString(4); //sets String "fimgMagic" to the GMIF identifier
        int fimgSize = buffer.readInt(); //obtains the size of the fimg (number of bytes)
        BinaryWriter writer; //creates new BinaryWriter object to be defined later in for-loop

        for(int i= 0; i < numFiles; i++) //goes through each file in ArrayList of NarcSubFile objects
        {
            if ((subFiles.get(i).getStartingOffset() & 0xffffffffL) != 0xffff950cL)
            {
                long trashBytes= subFiles.get(i).getTrashBytes(); //sets int "trashBytes" to the number of trash bytes between previous and current file
//                System.out.println(i + ": Number of trash bytes: " + trashBytes);
                while(trashBytes != 0) //while there are still trash bytes
                {
                    int throwAway= buffer.readByte(); //reads one byte and "throws it away"
                    trashBytes--; //lowers the amount of trash bytes left by one
                }
            }

            byte[] bytes; //creates a new byte[] to be defined later in for-loop
            String extension = ""; //creates a new String to contain the file extension of the current file being made
            String trueExtension = "";
            bytes = buffer.readBytes((int) (subFiles.get(i).getEndingOffset() - subFiles.get(i).getStartingOffset())); //reads the amount of bytes equal to the length of the current file

            byte[] identifier = new byte[4]; //creates a new byte[] of size 4 to contain the first four bytes of a file so file extension can be appended properly
            if(bytes.length >= 4)
                System.arraycopy(bytes, 0, identifier, 0, 4); //copies the first four bytes of the current file's contents to the identifier byte[]
            int index = -1; //creates a new int variable and sets value to -1
            for (int j = 0; j < extensionStrings.length; j++) //goes through each byte[] in the fileExtensions ArrayList
            {
                if (new String(identifier).equalsIgnoreCase(inverseString(extensionStrings[j]))) //if the two byte[] are the same
                {
                    index = j; //changes index to the current index in fileExtensions
                }
            }
            if (index != -1) //if index was changed and a matching file extension did exist
            {
                extension = extensionStrings[index]; //sets extension to the value of element "index" in the String[] extensionStrings
            }
            else //if there were no matching in fileExtensions, check for File compression
            {
                extension = "bin"; //sets extension to the default ".bin"

//                System.out.println("Current file: " + i);

                if(decompress)
                {
                    byte[] compressedIdentifier = new byte[4];
                    System.arraycopy(bytes, 5, compressedIdentifier, 0, 4); //copies bytes 5-8 of the current file's contents to the compressedIdentifier byte[]
                    int index2 = -1;
                    for (int j = 0; j < extensionStrings.length; j++) //goes through each extension string
                    {
                        if (new String(compressedIdentifier).equalsIgnoreCase(inverseString(extensionStrings[j]))) //if the two byte[] are the same
                        {
                            index2 = j; //changes index to the current index in fileExtensions
                        }
                    }

                    if (index2 != -1) //if index was changed and a matching file extension did exist (and therefore the file is compressed)
                    {
                        extension = "lzss";

//                long bitfield= byteArrToLong(identifier);
//                int compSize= (int) (bitfield & 0xf); //reserved for all but Huffman- is compression size for Huffman
//                int compType= (int) ((bitfield >> 4) & 0xf); //1= LZ77, 2= Huffman, 3= RLUncomp
//                int decompFileSize= (int) (bitfield >> 8); //size of decompressed file
//
//                System.out.println("Reserved/ Compression Size: " + compSize);
//                System.out.println("Compression Type: " + compType);
//                System.out.println("Decompressed File Size: " + decompFileSize);
//
//                bytes= lzDecompressor(bytes,decompFileSize);
//                System.out.println("");
                        trueExtension = extensionStrings[index2];
                    }

                }
            }

            //writes data to output files
            String outName= extractPath + subFiles.get(i).getName() + ".bin";
            writer = new BinaryWriter(outName); //creates new BinaryWriter object using specified name/ numbering from subFiles ArrayList of NarcSubFiles
            writer.write(bytes); //writes the bytes read from the narc located in byte[] bytes to the output file
//            if(bytes[0] == 0)
//            {
//                System.out.println(i + ": " + Arrays.toString(bytes));
//            }

            writer.close(); //closes BinaryWriter and flushes data

            if (extension.equalsIgnoreCase("lzss"))
            {
//                HexInputStream hexInputStream= new HexInputStream(extractPath + subFiles.get(i).getName() + "." + extension);
//
//                writer = new BinaryWriter(extractPath + subFiles.get(i).getName() + "." + trueExtension);
//                int[] decompArr= Objects.requireNonNull(JavaDSDecmp.decompress(hexInputStream));
//                byte[] contents= new byte[decompArr.length];
//                for(int x= 0; x < contents.length; x++)
//                {
//                    contents[x]= (byte) decompArr[x];
//                }
//                writer.write(contents);
//                writer.close();
//                if(!new File(extractPath + subFiles.get(i).getName() + "." + extension).delete())
//                {
//                    throw new RuntimeException("Check write perms...");
//                }

                BLZCoder blzCoder= new BLZCoder(new String[] {"-d",outName});

            }
        }
        buffer.close(); //closes Buffer object buffer's internal BufferedInputStream and flushes data
//        System.out.println("Narc unpacking completed. Output directory can be found at: " + extractPath);
    }



    public void pack(String directory, String name, String targetFile) throws IOException
    {
        outputNarcPath= targetFile;

//        if (new File(outputNarcPath + name).exists()) //if a narc matching parameter name exists
//        {
//            Scanner scanner = new Scanner(System.in); //creates a new Scanner object
//            while (true) //will keep running until valid input is received
//            {
//                System.out.println("A file named " + name + " already exists. Overwrite? (y/N)");
//                String input = scanner.nextLine().toLowerCase(); //prompts user for response
//                if (input.equals("y")) //if user input equals "y", meaning "yes"
//                {
//                    break; //escapes the infinite loop and continues to narc packing
//                }
//                if (input.equals("n")) //if user input equals "N", meaning "no"
//                {
//                    System.out.println("Process aborted");
//                    System.exit(0); //closes program
//                } else //if the user input does not match either "y" or "N"
//                {
//                    System.out.println("User input does not match either specified input. Please try again."); //tells user to try again, returns to top of while-loop following this
//                }
//            }
//        }
        Buffer fimgBuffer; //creates new Framework.Buffer object

        ArrayList<TableSubFile> fimgTable = new ArrayList<>(); //creates new ArrayList of tableSubFile instances

        File file; //creates a File object to be defined later in for-loop
        List<File> fileList;
        try
        {
            fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(directory).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        }
        catch (NullPointerException e)
        {
            throw new RuntimeException("Invalid path: " + directory);
        }

        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)



        int fimgPosition = 0; //creates a counter to track the length of the fimg section
        for (int i = 0; i < files.length; i++) //goes through each file in order
        {
            file = files[i];
            fimgBuffer = new Buffer(file.toString()); //sets the Framework.Buffer object to the current file
            byte[] contents = fimgBuffer.readRemainder(); //contains the contents of the file
            byte[] fixedContents;
            int fimgPosition2 = fimgPosition; //creates a local, final variable to hold the fimgPosition data

            if(contents.length % 4 != 0)
            {
//                System.out.println("File " + i + " padding added: " + contents.length % 4);
                fixedContents= new byte[contents.length + (contents.length % 4)];
                Arrays.fill(fixedContents,(byte) 0xff);
                System.arraycopy(contents,0,fixedContents,0,contents.length);
            }
            else
            {
                fixedContents= contents;
            }

            fimgTable.add(new TableSubFile() {
                @Override
                public int getStartingOffset() {
                    return fimgPosition2;
                }

                @Override
                public int getEndingOffset() {
                    return fimgPosition2 + contents.length;
                }

                @Override
                public byte[] getFileContents() {
                    return fixedContents;
                }

                @Override
                public int length() {
                    return fixedContents.length;
                }
            });
            fimgPosition += fixedContents.length; //increments fimgPosition by the length of the file contents for purpose of keeping track of starting offsets/ ending offsets and the length of the section
            fimgBuffer.close();
        }

        MemBuf fimgBuf = MemBuf.create(); //creates new Framework.MemBuf object of maximum size of 1 MB for fimg section
        int fimgSize = fimgPosition + 8; //creates new int variable to hold length of fimg table. Additional 8 bytes are for the header length
        MemBuf.MemBufWriter writer = fimgBuf.writer().writeBytes(0x47, 0x4D, 0x49, 0x46).writeInt(fimgSize); //writes fimg magic number

        for (int i = 0; i < fimgTable.size(); i++) //goes through each narcs.TableSubFile object in the ArrayList
        {
            TableSubFile tableSubFile = fimgTable.get(i);
            writer.write(tableSubFile.getFileContents()); //writes contents of each TableSubFile object
        }


        MemBuf fntbBuf = MemBuf.create(); //creates new Framework.MemBuf object of maximum size of 1 MB for fntb section
        int fntbSize = 16; //creates and sets variable to 16 (length of fntb in all HGSS files) (constant)
        fntbBuf.writer().writeBytes(0x42, 0x54, 0x4E, 0x46, 0x10, 0x00, 0x00, 0x00, 0x04, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00); //writes fntb data (constant)


        MemBuf fatbBuf = MemBuf.create(); //creates new Framework.MemBuf object of maximum size of 1 MB for fatb section
        writer = fatbBuf.writer(); //initializes writer
        writer.writeBytes(0x42, 0x54, 0x41, 0x46); //writes fatb header
        int fatbSize = (fimgTable.size() * 8) + 12; //sets size of fatb section to the amount of files multiplied by 8, then adds 12 bytes for fatb header
        writer.writeInt(fatbSize); //writes file size as four bytes
        writer.writeInt(fimgTable.size()); //writes number of files in the narc as four bytes
        for (TableSubFile tableSubFile : fimgTable) //goes through each narcs.TableSubFile object in the ArrayList
        {
            writer.writeInt(tableSubFile.getStartingOffset()); //writes the starting offset as four bytes
            writer.writeInt(tableSubFile.getEndingOffset()); //writes the ending offset as four bytes
        }

        BinaryWriter narcWriter = new BinaryWriter(outputNarcPath + name); //creates new file to output to in user directory with parameter "name" + .narc
        narcWriter.writeBytes(0x4E, 0x41, 0x52, 0x43, 0xFE, 0xFF, 0x00, 0x01); //writes the narc magic number and constant data (constant)
        int fileLength = fimgSize + fatbSize + fntbSize + 16; //creates new int variable and sets it to combined lengths of fimg section, fntb section, fatb section, and file header
        narcWriter.writeInt(fileLength); //writes the file length as four bytes
        narcWriter.writeShort((short) 0x10); //writes the header size as two bytes (16 bytes in every narc) (constant)
        narcWriter.writeShort((short) 0x03); //writes the number of sections as two bytes (3 sections in every narc) (constant)
        narcWriter.write(fatbBuf.reader().getBuffer()); //writes the entire fatb buffer
        narcWriter.write(fntbBuf.reader().getBuffer()); //writes the entire fntb buffer
        narcWriter.write(fimgBuf.reader().getBuffer()); //writes the entire fimg buffer
        narcWriter.close(); //closes writer and flushes data

//        System.out.println("Narc packing completed. Output file can be found at: " + outputNarcPath + name);

    }

    public void packRewrite(String directory, String output) {
        String dataPath = path + directory;
        ArrayList<TableSubFile> dataList = new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(new File(dataPath).listFiles())); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)

        int fimgPosition = 0;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            Buffer buffer = new Buffer(file.toString());
            int start = fimgPosition;
            byte[] contents = buffer.readBytes(12);

            dataList.add(new TableSubFile() {
                @Override
                public int getStartingOffset() {
                    return fimgPosition;
                }

                @Override
                public int getEndingOffset() {
                    return 0;
                }

                @Override
                public byte[] getFileContents() {
                    return contents;
                }

                @Override
                public int length() {
                    return 0;
                }
            });

        }
    }

    private boolean compareBytes(byte[] one, byte[] two) //compares two byte arrays of equal length
    {
        return Arrays.equals(one, two);
    }

    private void sort(File arr[]) {
        Arrays.sort(arr, Comparator.comparingInt(Narctowl::fileToInt));
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

    private static int byteArrToInt(byte[] arr) {
        String str = "";
        for (byte b : arr) {
            str = fixBinaryString(Integer.toBinaryString(b), 8) + str;
        }
        return Integer.parseInt(str, 2);
    }

    private static long byteArrToLong(byte... arr) {
        String str = "";
        for (byte b : arr) {
            str = fixBinaryString(Integer.toBinaryString(b), 8) + str;
        }
        return Long.parseLong(str, 2);
    }

    private byte[] lzDecompressor(byte[] contents, int decompSize) {
        byte[] noHeader = new byte[contents.length - 5];
        System.arraycopy(contents, 5, noHeader, 0, contents.length - 5);
        contents = noHeader;

        byte[] decompressed = new byte[decompSize];
        int len = contents.length;
        int destOffset = 0;
        String bitStr;
        short mostSigBits;
        short copyCount;
        short leastSigBits;
        int copyOffset;

        for (int i = 0; i < len; ) {
            bitStr = fixBinaryString(Integer.toBinaryString(contents[i]), 8);
            initializeBits(bitStr);
            boolean isCompressed = nextBit().equalsIgnoreCase("1");

            if (!isCompressed) {
                decompressed[destOffset++] = contents[i++];
            } else {
//                bitfield= byteArrToShort(contents[i++],contents[i++]);
//                System.out.println("Bitfield: " + bitfield);
//                mostSigBits= (short) (bitfield & 0xf);
//                copyCount= (short) ((bitfield >> 4) & 0xf);
//                leastSigBits= (short) (bitfield >> 8);
//
//                copyOffset= (leastSigBits | mostSigBits << 8) + 1;
//                System.out.println("Copy offset: " + copyOffset);


            }
        }

        return decompressed;
    }

    private String bits;
    private int bitIdx;

    private void initializeBits(String bits) {
        this.bits = bits;
        bitIdx = bits.length() - 1;
    }

    private String nextBit() {
        return bits.substring(bitIdx, --bitIdx + 2);
    }

    private String inverseString(String str) {
        StringBuilder inv = new StringBuilder();
        for (int i = str.length() - 1; i >= 0; i--) {
            inv.append(str.substring(i, i + 1));
        }
        return inv.toString();
    }

    public static void makeVolatile(File folder)
    {
        folder.deleteOnExit();
        for(File f : Objects.requireNonNull(folder.listFiles()))
        {
            if(f.isDirectory())
                makeVolatile(f);
            else
                f.deleteOnExit();
        }
    }
}