package com.turtleisaac.pokeditor.editors.text;

import com.jackhack96.dspre.handlers.gen4.text.MessageFile;
import com.jackhack96.jNdstool.io.jBinaryStream;
import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Project;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class TextEditor
{
    public static String[] getBank(Project project, int bank) throws IOException
    {
        String textNarcPath;
        String textDirPath;

        switch (project.getBaseRom())
        {
            case Platinum:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "pl_msg.narc";
                textDirPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "pl_msg";
                break;

            case HeartGold:
            case SoulSilver:
                textNarcPath= project.getDataPath() + File.separator + "a" + File.separator + "0" + File.separator + "2" + File.separator + "7";
                textDirPath= project.getDataPath() + File.separator + "a" + File.separator + "0" + File.separator + "2" + File.separator + "7_";
                break;

            case Diamond:
            case Pearl:
                textNarcPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "msg.narc";
                textDirPath= project.getDataPath() + File.separator + "msgdata" + File.separator + "msg";
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

        if(!new File(textDirPath).exists())
        {
            Narctowl narctowl= new Narctowl(true);
            narctowl.unpack(textNarcPath,textDirPath);
        }
        Buffer buffer= new Buffer(textDirPath + File.separator + bank + ".bin");
        jBinaryStream binaryStream= new jBinaryStream(buffer.readRemainder());

        MessageFile.decodeText(binaryStream);

        return MessageFile.getTexts().toArray(new String[0]);
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
        File folder;

        switch (project.getBaseRom())
        {
            case Platinum:
                folder= new File(project.getDataPath() + File.separator + "msgdata" + File.separator + "pl_msg");
                break;

            case HeartGold:
            case SoulSilver:
                folder= new File(project.getDataPath() + File.separator + "a" + File.separator + "0" + File.separator + "2" + File.separator + "7_");
                break;

            case Diamond:
            case Pearl:
                folder= new File(project.getDataPath() + File.separator + "msgdata" + File.separator + "msg");
                break;

            default:
                throw new RuntimeException("Invalid game: " + project.getBaseRom());
        }

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
        for(File f : Objects.requireNonNull(folder.listFiles()))
        {
            if(f.isDirectory())
                clearDirs(f);
            else
                f.delete();
        }
        folder.delete();
    }
}
