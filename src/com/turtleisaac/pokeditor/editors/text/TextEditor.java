package com.turtleisaac.pokeditor.editors.text;

import com.jackhack96.dspre.handlers.gen4.text.MessageFile;
import com.jackhack96.jNdstool.io.jBinaryStream;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.StringWork;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Project;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TextEditor
{
    private static String unpackedFolderPath;

    public static String[] getBank(Project project, TextBank bank) throws IOException
    {
        return getBank(project, bank.value);
    }

    public static String[] getBank(Project project, int bank) throws IOException
    {
        String textNarcPath;
        String textDirPath;

        unpackedFolderPath = project.getProjectPath().toString() + File.separator + "temp" + File.separator;

        switch (project.getBaseRom())
        {
            case Platinum:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "pl_msg.narc";
                textDirPath= unpackedFolderPath + "msg";
                break;

            case HeartGold:
            case SoulSilver:
                textNarcPath= project.getDataPath() + File.separator + "a" + File.separator + "0" + File.separator + "2" + File.separator + "7";
                textDirPath= unpackedFolderPath + "msg";
                break;

            case Diamond:
            case Pearl:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "msg.narc";
                textDirPath= unpackedFolderPath + "msg";
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        if(!new File(textDirPath).exists())
        {
            Narctowl narctowl= new Narctowl(true);
            narctowl.unpack(textNarcPath,textDirPath);
        }
        String bankFile = StringWork.appendLeadingZeros(bank, ("" + new File(textDirPath).listFiles().length).length());

        Buffer buffer= new Buffer(textDirPath + File.separator + bankFile + ".bin");
        jBinaryStream binaryStream= new jBinaryStream(buffer.readRemainder());

        MessageFile.decodeText(binaryStream);
        buffer.close();

        return MessageFile.getTexts().toArray(new String[0]);
    }

    public static void writeBank(Project project, Object[] arr, int bank, boolean canTrim) throws IOException
    {
        String[] stringArr= new String[arr.length];
        for(int i= 0; i < arr.length; i++)
        {
            stringArr[i]= (String) arr[i];
        }

        writeBank(project,new ArrayList<>(Arrays.asList(stringArr)),bank,canTrim);
    }

    public static void writeBank(Project project, String[] arr, int bank, boolean canTrim) throws IOException
    {
        writeBank(project,new ArrayList<>(Arrays.asList(arr)),bank,canTrim);
    }

    public static void writeBank(Project project, String[] arr, TextBank bank, boolean canTrim) throws IOException
    {
        writeBank(project,new ArrayList<>(Arrays.asList(arr)),bank.value,canTrim);
    }

    public static void writeBank(Project project, ArrayList<String> stringList, TextBank bank, boolean canTrim) throws IOException
    {
        writeBank(project,stringList,bank.value,canTrim);
    }

    public static void writeBank(Project project, ArrayList<String> stringList, int bank, boolean canTrim) throws IOException
    {
        stringList = stringList.stream().map(s ->
        {
            s = s.replaceAll("[`'‘]{1}", "’");
            return s;
        }).collect(Collectors.toCollection(ArrayList::new));

        String textNarcPath;
        String textDirPath;

        switch (project.getBaseRom())
        {
            case Platinum:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "pl_msg.narc";
                textDirPath= unpackedFolderPath + "msg";
                break;

            case HeartGold:
            case SoulSilver:
                textNarcPath= project.getDataPath() + File.separator + "a" + File.separator + "0" + File.separator + "2" + File.separator + "7";
                textDirPath= unpackedFolderPath + "msg";
                break;

            case Diamond:
            case Pearl:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "msg.narc";
                textDirPath= unpackedFolderPath + "msg";
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        Narctowl narctowl= new Narctowl(true);

        if(!new File(textDirPath).exists())
        {
            narctowl.unpack(textNarcPath,textDirPath);
        }

        String bankFile = StringWork.appendLeadingZeros(bank, ("" + new File(textDirPath).listFiles().length).length());

        Buffer buffer= new Buffer(textDirPath + File.separator + bankFile + ".bin");
        jBinaryStream binaryStream;

        if(canTrim)
        {
            binaryStream= new jBinaryStream(buffer.readRemainder());

            MessageFile.decodeText(binaryStream);
            int originalLength= MessageFile.getTexts().size();
            stringList.subList(originalLength,stringList.size()).clear();

            if(stringList.size() == originalLength)
            {
                System.out.println("Text Bank Length Corrected");
            }
            buffer.close();

            buffer= new Buffer(textDirPath + File.separator + bank + ".bin");
        }

        binaryStream= new jBinaryStream(MessageFile.getSize(stringList));

        buffer.skipBytes(2);
        MessageFile.setSeed(buffer.readShort());
        MessageFile.setTexts(stringList);
        MessageFile.encodeText(binaryStream);

        BinaryWriter writer= new BinaryWriter(textDirPath + File.separator + bank + ".bin");
        writer.write(binaryStream.readAll());
        writer.close();
        buffer.close();

        narctowl.pack(textDirPath,"",textNarcPath);
    }

    public static void search(Project project, String str) throws IOException
    {
        String textNarcPath;
        switch (project.getBaseRom())
        {
            case Platinum:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "pl_msg.narc";
                break;

            case HeartGold:
            case SoulSilver:
                textNarcPath= project.getDataPath() + File.separator + "a" + File.separator + "0" + File.separator + "2" + File.separator + "7";
                break;

            case Diamond:
            case Pearl:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "msg.narc";
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        int numBanks= Narctowl.getNumFiles(textNarcPath);
        String[] text;

        for(int i= 0; i < numBanks; i++)
        {
            text= getBank(project,i);

            if(contains(text,str))
            {
                System.out.println("Bank " + i);

                for(String s : text)
                    System.out.println("    " + s);

                System.out.println();
            }
        }
    }

    public static void cleanup(Project project)
    {
        File folder= new File(unpackedFolderPath + "msg");

        if(folder.exists())
            clearDirs(folder);
    }

    private static boolean contains(String[] arr, String str)
    {
        str= str.toLowerCase().trim();
        for(String s : arr)
        {
            if(s.toLowerCase().trim().contains(str))
                return true;
        }
        return false;
    }

    private static void clearDirs(File folder)
    {
        try
        {
            FileUtils.deleteDirectory(folder);
        }
        catch(IOException e)
        {
            System.err.println("\tFailed to delete folder: " + folder.getAbsolutePath());
            e.printStackTrace();
        }

        if(folder.exists())
        {
            System.err.println("\tFolder wasn't deleted?");
        }
    }
}
