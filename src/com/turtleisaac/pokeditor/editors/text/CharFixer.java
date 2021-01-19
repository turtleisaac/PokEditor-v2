package com.turtleisaac.pokeditor.editors.text;

import java.io.*;

public class CharFixer
{
    public static void main(String[] args) throws IOException
    {
        CharFixer charFixer= new CharFixer();
    }

    public CharFixer() throws IOException
    {
        BufferedReader reader= new BufferedReader(new FileReader(System.getProperty("user.dir") + File.separator + "CharTable.txt"));
        BufferedWriter writer= new BufferedWriter(new FileWriter(System.getProperty("user.dir") + File.separator + "CharFix.txt"));
        String line;
        boolean flag= false;

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            if(line.contains("case"))
            {
                flag= true;
                String[] lineArr= line.split(" ");
                String hexId= lineArr[lineArr.length-1];
                for(int i= 0; !hexId.substring(i,i+1).equals(":"); i++)
                {
                    switch (hexId.substring(i,i+1))
                    {
                        case "(":
                        case ")":
                        case"\"":
                            break;
                        default:
                            writer.write(hexId.substring(i,i+1));
                    }
                }
                writer.write(" ");
            }
            else if(flag)
            {
                String[] lineArr= line.split(" ");
                String hexId= lineArr[lineArr.length-1];
                for(int i= 0; !hexId.substring(i,i+1).equals(";"); i++)
                {
                    switch (hexId.substring(i,i+1))
                    {
                        case "(":
                        case ")":
                        case"\"":
                            break;
                        default:
                            writer.write(hexId.substring(i,i+1));
                    }
                }
                writer.write("\n");
                flag= false;
                writer.flush();
            }
        }
    }
}
