package com.turtleisaac.pokeditor.framework.narctowl;

import com.turtleisaac.pokeditor.framework.StringWork;

import java.io.File;
import java.util.Objects;

public class NarcWork
{
    public static String getSubfilePath(int idx, String folder)
    {
        String dataPath = folder;
        if (!dataPath.endsWith(File.separator))
            dataPath += File.separator;

        int numFiles = -1;
        if (new File(folder).exists())
        {
            numFiles = Objects.requireNonNull(new File(folder).listFiles()).length;
        }

        String indexString;
        if (numFiles > 0)
        {
            int numFilesLen = ("" + numFiles).length();
            indexString = StringWork.appendLeadingZeros(idx, numFilesLen);
        }
//        else if (numFiles == 0)
        else
        {
            indexString = StringWork.appendLeadingZeros(idx, 4);
        }

        String returnPath = dataPath + indexString + ".bin";

        String indexStringCopy = indexString.substring(1);
        while (indexStringCopy.length() > 1)
        {
            if (new File(dataPath + indexString + ".bin").exists())
            {
                returnPath = dataPath + indexString + ".bin";
                break;
            }
            indexStringCopy = indexStringCopy.substring(1);
        }

        if (numFiles == -1)
        {
            System.err.println("Folder didn't exist when creating name of subfile - NarcWork.java:36");
        }

        return returnPath;
    }

    public static String getSubfilePath(int idx, File folder)
    {
        return getSubfilePath(idx, folder.getAbsolutePath());
    }
}
