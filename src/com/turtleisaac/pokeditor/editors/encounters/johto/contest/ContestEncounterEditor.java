//package encounters.johto.contest;
//
//import framework.BinaryWriter;
//import framework.Buffer;
//import framework.CsvReader;
//
//import java.io.*;
//import java.util.ArrayList;
//
//public class ContestEncounterEditor
//{
//    public static void main(String[] args) throws IOException
//    {
//        ContestEncounterEditor editor= new ContestEncounterEditor();
//        editor.babyFormsToCsv("mushi_encount.bin");
//    }
//
//    private static String path = System.getProperty("user.dir") + File.separator; //creates a new String field containing user.dir and File.separator (/ on Unix systems, \ on Windows)
//    private String dataPath = path;
//    private static String resourcePath = path + "Program Files" + File.separator;
//    private static String[] nameData;
//
//    public ContestEncounterEditor() throws IOException
//    {
//        String entryPath = resourcePath + "EntryData.txt";
//
//        BufferedReader reader = new BufferedReader(new FileReader(entryPath));
//        ArrayList<String> nameList = new ArrayList<>();
//        String line;
//        while ((line = reader.readLine()) != null)
//        {
//            nameList.add(line);
//        }
//        nameData = nameList.toArray(new String[0]);
//        reader.close();
//    }
//
//    public void babyFormsToCsv(String encounterFile) throws IOException
//    {
//        dataPath += encounterFile;
//
//        File file= new File(dataPath);
//        Buffer buffer= new Buffer(dataPath);
//
//        for(int i= 0; i < numPokemon; i++)
//        {
//            babyForms[i]= buffer.readUIntS();
//        }
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(path + "babyFormsData.csv"));
//        writer.write("ID Number,Name,Baby Form\n"); //header in spreadsheet output
//        String line;
//        for (int col = 0; col < numPokemon; col++)
//        {
//            line = col + "," + nameData[col] + "," + nameData[babyForms[col]];
//            line += "\n";
//            writer.write(line);
//        }
//        writer.close();
//    }
//
//
//    public void csvToBabyForms(String babyFormsCsv, String outputFile) throws IOException
//    {
//        String babyFormsPath = path + babyFormsCsv;
//        String outputPath;
//
//        if (outputFile.contains("Recompile"))
//        {
//            outputPath = path + "temp" + File.separator + outputFile;
//        } else
//        {
//            outputPath = path + outputFile;
//        }
//
//        if (!new File(outputPath).exists())
//        {
//            throw new RuntimeException("Could not create output directory. Check write permissions");
//        }
//
//        CsvReader csvReader = new CsvReader(babyFormsPath);
//        BinaryWriter writer= new BinaryWriter(outputPath);
//        for (int i = 0; i < csvReader.length(); i++)
//        {
//            writer.writeShort(getPokemon(csvReader.next()[0]));
//        }
//    }
//
//
//    private static short getPokemon(String pokemon)
//    {
//        for(int i= 0; i < nameData.length; i++)
//        {
//            if(pokemon.equalsIgnoreCase(nameData[i]))
//            {
//                return (short) i;
//            }
//        }
//        throw new RuntimeException("Invalid pokemon entered: " + pokemon);
//    }
//
//}
