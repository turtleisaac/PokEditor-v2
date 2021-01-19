package com.turtleisaac.pokeditor.editors.text;

import java.io.*;
import java.util.HashMap;


public class CharTable
{
    private static HashMap<Integer,String> mapGet = new HashMap<>();
    private static HashMap<String,Integer> mapWrite = new HashMap<>();

    public CharTable() throws IOException
    {
        BufferedReader reader= new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + "CharFix.txt"));
        String line;

        while((line= reader.readLine()) != null)
        {
            String[] lineArr= line.trim().split(" ");
            int hexId= Integer.parseInt(lineArr[0].substring(2),16);
            String character= lineArr[1];

            if(hexId != 0x1DE)
            {
                mapGet.put(hexId,character);
                mapWrite.put(character,hexId);
            }
            else //this is for the space character " "
            {
                mapGet.put(hexId," ");
                mapWrite.put(" ",hexId);
            }
        }
    }

    public String getCharacter(int p)
    {
        return mapGet.getOrDefault(p, "0");
    }

    public int writeCharacter(String p)
    {
        return mapWrite.getOrDefault(p, 0);
    }

}
