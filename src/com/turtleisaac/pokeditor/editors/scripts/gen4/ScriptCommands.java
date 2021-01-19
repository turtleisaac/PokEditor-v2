package com.turtleisaac.pokeditor.editors.scripts.gen4;

import java.io.*;
import java.util.HashMap;

public class ScriptCommands
{
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader= new BufferedReader(new FileReader(path + "DSPRE PT.xml"));
        String line;
        while((line= reader.readLine()) != null)
        {
            line= reader.readLine();
            line= reader.readLine().trim();
            line= line.replaceAll("value","").replaceAll("<","").replaceAll(">","").replaceAll("/","");

            String[] arr= line.split(" ");

            for(int i= 1; i < arr.length; i++)
            {
                System.out.print(arr[i] + "\t");
            }
            System.out.println();
        }
    }

    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
    private static String resourcePath = path + "Program Files" + File.separator;
    private HashMap<Integer, ScriptData> indexMap;
    private HashMap<String, Integer> nameMap;

    public ScriptCommands(String gameCode) throws IOException
    {
        indexMap= new HashMap<>();
        nameMap= new HashMap<>();
        String commandList= resourcePath;

        switch(gameCode.toLowerCase().substring(0,3))
        {
            case "apa" :
            case "ada" :
            case "cpu" :
                commandList+= "ScriptCommandsPlatinum.txt";
                break;

            case "ipk" :
            case "ipg" :
                commandList+= "ScriptCommandsJohto.txt";
                break;

            default :
                throw new RuntimeException("Game not supported: " + gameCode.toUpperCase());
        }


        BufferedReader reader= new BufferedReader(new FileReader(commandList));
        String paramString;
        String[] arr;
        String line;

        int idx= 0;
        while((line= reader.readLine()) != null)
        {
            line= line.trim();

            if(line.contains("\t"))
            {
                arr= line.split("\t");
                String name= arr[0];
//                System.out.println(idx + ": " + Arrays.toString(arr));

                int[] paramArr= new int[arr.length-1];
                for(int i= 0; i < paramArr.length; i++)
                {
                    paramArr[i]= Integer.parseInt(arr[i+1]);
                }

                indexMap.put(idx, new ScriptData()
                {
                    @Override
                    public String getName()
                    {
                        return name;
                    }

                    @Override
                    public int[] getParamBytes()
                    {
                        return paramArr;
                    }
                });

                nameMap.put(name,idx++);

            }
            else
            {
                String name= line;
                indexMap.put(idx, new ScriptData()
                {
                    @Override
                    public String getName()
                    {
                        return name;
                    }

                    @Override
                    public int[] getParamBytes()
                    {
                        return new int[0];
                    }
                });

                nameMap.put(name,idx++);
            }

//            System.out.print(idx-1 + ": " + indexMap.get(idx-1).getName());
//            System.out.println("    " + Arrays.toString(indexMap.get(idx-1).getParamBytes()));
        }
    }

    public ScriptData getScript(int idx)
    {
        return indexMap.get(idx);
    }

    public int getIndex(String name)
    {
        return nameMap.get(name);
    }
}
