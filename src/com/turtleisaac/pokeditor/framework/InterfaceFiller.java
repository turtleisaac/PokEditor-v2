package com.turtleisaac.pokeditor.framework;

import java.io.*;
import java.util.ArrayList;

public class InterfaceFiller
{
    public static void main(String[] args) throws IOException {
        InterfaceFiller interfaceFiller= new InterfaceFiller(new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "HeadbuttData.java"));
    }

    private String path= System.getProperty("user.dir") + File.separator;

    public InterfaceFiller(File file) throws IOException
    {
        ArrayList<String> variables= new ArrayList<>();
        ArrayList<String> types= new ArrayList<>();
        ArrayList<String> methodCalls= new ArrayList<>();
        ArrayList<String> lines= new ArrayList<>();
        BufferedReader reader= new BufferedReader(new FileReader(file));
        String line;

//        for (Method method : Encounters.Johto.EncounterData.class.getMethods()) {
//            System.out.println(method.getName()+" "+method.getReturnType());
//        }

        while((line= reader.readLine()) != null)
        {
            if(!line.equals("//") && !line.equals("{") && !line.equals("}") && !line.contains("public") && !line.contains("import"))
            {
                line= line.substring(4);
                lines.add(line.substring(0,line.length()-1));
                //System.out.println(line);
                String[] thisLine= line.split(" ");
                String varType= thisLine[0];
                String modified= thisLine[1].substring(3,4).toLowerCase() + thisLine[1].substring(4,thisLine[1].length()-3);
//                String modified= thisLine[1].substring(0,thisLine[1].length()-3);
                types.add(varType);
                variables.add(modified);

                if(varType.contains("ArrayList"))
                {
                    methodCalls.add("FIX THIS MANUALLY");
                }
                if(varType.equals("int"))
                {
                    methodCalls.add("buffer.readByte()");
                }
                if(varType.equals("byte[]"))
                {
                    methodCalls.add("buffer.readBytes()");
                }
                if(varType.equals("short"))
                {
                    methodCalls.add("buffer.readShort()");
                }
                if(varType.equals("short[]"))
                {
                    methodCalls.add("buffer.readShorts()");
                }
                if(varType.equals("String"))
                {
                    methodCalls.add("buffer.readString()");
                }
            }
        }
        reader.close();

        BufferedWriter writer= new BufferedWriter(new FileWriter(new File(path + "interfaceFiller.txt")));
        for(int i= 0; i < variables.size(); i++)
        {
            writer.write(types.get(i) + " " + variables.get(i) + "= " + methodCalls.get(i) + ";\n");
        }

        writer.write("\ndataList.add(new " + file.getName().substring(0,file.getName().length()-5) + "() {\n");
        for(int i= 0; i < variables.size(); i++)
        {
            writer.write("@Override\n");
            writer.write("public " + lines.get(i) + " { return " + variables.get(i) + "; }\n\n");
        }
        writer.write("});");
        writer.close();
    }
}
