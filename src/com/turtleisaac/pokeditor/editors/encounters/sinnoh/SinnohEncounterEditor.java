package com.turtleisaac.pokeditor.editors.encounters.sinnoh;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.*;
import com.turtleisaac.pokeditor.project.Project;

import java.io.*;
import java.util.*;

public class SinnohEncounterEditor
{
    private static String projectPath= System.getProperty("user.dir") + File.separator;
    private String dataPath;
    private String separator= File.separator;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fairy", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] colorArr= {"Red","Blue","Yellow","Green","Black","Brown","Purple","Gray","White","Pink"};
    private static final int[] fieldRateArr= {20,20,10,10,10,10,5,5,4,4,1,1};
    private static final int[] smashRateArr= {90,10};
    private static final int[] waterRateArr= {60,30,5,4,1};
    private static String resourcePath;
    private static String[] nameData;
    private static String[] itemData;
    private static String[] moveData;
    private static String[] areaData;
//    private static String[] outdoorData;
//    private static String[] indoorData;


    public SinnohEncounterEditor(Project project, String projectPath) throws IOException
    {
        this.projectPath= projectPath;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator)) + File.separator + "Program Files" + File.separator;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                moveData= TextEditor.getBank(project,588);
                itemData= TextEditor.getBank(project,344);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                moveData= TextEditor.getBank(project,647);
                itemData= TextEditor.getBank(project,392);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                moveData= TextEditor.getBank(project,750);
                itemData= TextEditor.getBank(project,222);
                break;
        }

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "LocationsPt.txt"));
        String line;

        ArrayList<String> areaList= new ArrayList<>();

        while ((line= reader.readLine()) != null)
        {
            line= line.trim();
            areaList.add(line);
        }
        areaData= areaList.toArray(new String[0]);
        reader.close();

//        reader= new BufferedReader(new FileReader(resourcePath + "OutdoorLocations.txt"));
//        ArrayList<String> outdoorList= new ArrayList<>();
//
//        while((line= reader.readLine()) != null)
//        {
//            outdoorList.add(line.substring(3));
//        }
//        outdoorData= outdoorList.toArray(new String[0]);
//        reader.close();
//
//        reader= new BufferedReader(new FileReader(resourcePath + "IndoorLocations.txt"));
//        ArrayList<String> indoorList= new ArrayList<>();
//
//        while((line= reader.readLine()) != null)
//        {
//            indoorList.add(line.substring(3));
//        }
//        indoorData= indoorList.toArray(new String[0]);
//        reader.close();
    }

    public SinnohEncounterReturn encountersToSheet(String encounterDir) throws IOException
    {
        dataPath+= encounterDir;
        ArrayList<SinnohEncounterData> dataList= new ArrayList<>();
        String tempPath= dataPath.substring(0,dataPath.lastIndexOf(File.separator)+1) + "temp";

        System.out.println(tempPath);
        if(!new File(tempPath).exists() && !new File(tempPath).mkdir())
        {
            throw new RuntimeException("Could not create temp directory. Check write perms.");
        }
        tempPath+= File.separator;


        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for(int i= 0; i < files.length; i++)
        {
            file= files[i];
            Buffer buffer= new Buffer(file.toString());

            int fieldRate= buffer.readInt();

            int fieldLevel1= buffer.readInt();
            int fieldMon1= buffer.readInt();
            int fieldLevel2= buffer.readInt();
            int fieldMon2= buffer.readInt();
            int fieldLevel3= buffer.readInt();
            int fieldMon3= buffer.readInt();
            int fieldLevel4= buffer.readInt();
            int fieldMon4= buffer.readInt();
            int fieldLevel5= buffer.readInt();
            int fieldMon5= buffer.readInt();
            int fieldLevel6= buffer.readInt();
            int fieldMon6= buffer.readInt();
            int fieldLevel7= buffer.readInt();
            int fieldMon7= buffer.readInt();
            int fieldLevel8= buffer.readInt();
            int fieldMon8= buffer.readInt();
            int fieldLevel9= buffer.readInt();
            int fieldMon9= buffer.readInt();
            int fieldLevel10= buffer.readInt();
            int fieldMon10= buffer.readInt();
            int fieldLevel11= buffer.readInt();
            int fieldMon11= buffer.readInt();
            int fieldLevel12= buffer.readInt();
            int fieldMon12= buffer.readInt();
            int[] fieldLevels= new int[] {fieldLevel1,fieldLevel2,fieldLevel3,fieldLevel4,fieldLevel5,fieldLevel6,fieldLevel7,fieldLevel8,fieldLevel9,fieldLevel10,fieldLevel11,fieldLevel12};
            int[] fieldMons= new int[] {fieldMon1,fieldMon2,fieldMon3,fieldMon4,fieldMon5,fieldMon6,fieldMon7,fieldMon8,fieldMon9,fieldMon10,fieldMon11,fieldMon12};

            int swarm1= buffer.readInt();
            int swarm2= buffer.readInt();
            int[] swarmMons= new int[] {swarm1,swarm2};

            int day1= buffer.readInt();
            int day2= buffer.readInt();
            int[] dayMons= new int[] {day1,day2};

            int night1= buffer.readInt();
            int night2= buffer.readInt();
            int[] nightMons= new int[] {night1,night2};

            int radar1= buffer.readInt();
            int radar2= buffer.readInt();
            int radar3= buffer.readInt();
            int radar4= buffer.readInt();
            int[] radarMons= new int[] {radar1,radar2,radar3,radar4};

//            buffer.skipBytes(0x18);
            byte[] padding= buffer.readBytes(0x18);
            BinaryWriter writer= new BinaryWriter(tempPath + i);
            writer.write(padding);
            writer.close();

            int ruby1= buffer.readInt();
            int ruby2= buffer.readInt();
            int[] rubyMons= new int[] {ruby1,ruby2};
            int sapphire1= buffer.readInt();
            int sapphire2= buffer.readInt();
            int[] sapphireMons= new int[] {sapphire1,sapphire2};
            int emerald1= buffer.readInt();
            int emerald2= buffer.readInt();
            int[] emeraldMons= new int[] {emerald1,emerald2};
            int fireRed1= buffer.readInt();
            int fireRed2= buffer.readInt();
            int[] fireRedMons= new int[] {fireRed1,fireRed2};
            int leafGreen1= buffer.readInt();
            int leafGreen2= buffer.readInt();
            int[] leafGreenMons= new int[] {leafGreen1,leafGreen2};

            int surfRate= buffer.readInt();
            int[] surfMaxs= new int[5];
            int[] surfMins= new int[5];
            int[] surfMons= new int[5];
            for(int x= 0; x < surfMaxs.length; x++)
            {
                surfMaxs[x]= buffer.readByte();
                surfMins[x]= buffer.readByte();
                buffer.skipBytes(2);
                surfMons[x]= buffer.readInt();
            }
            buffer.skipBytes(0x2C);

            int oldRate= buffer.readInt();
            int[] oldMaxs= new int[5];
            int[] oldMins= new int[5];
            int[] oldMons= new int[5];
            for(int x= 0; x < oldMaxs.length; x++)
            {
                oldMaxs[x]= buffer.readByte();
                oldMins[x]= buffer.readByte();
                buffer.skipBytes(2);
                oldMons[x]= buffer.readInt();
            }

            int goodRate= buffer.readInt();
            int[] goodMaxs= new int[5];
            int[] goodMins= new int[5];
            int[] goodMons= new int[5];
            for(int x= 0; x < goodMaxs.length; x++)
            {
                goodMaxs[x]= buffer.readByte();
                goodMins[x]= buffer.readByte();
                buffer.skipBytes(2);
                goodMons[x]= buffer.readInt();
            }

            int superRate= buffer.readInt();
            int[] superMaxs= new int[5];
            int[] superMins= new int[5];
            int[] superMons= new int[5];
            for(int x= 0; x < superMaxs.length; x++)
            {
                superMaxs[x]= buffer.readByte();
                superMins[x]= buffer.readByte();
                buffer.skipBytes(2);
                superMons[x]= buffer.readInt();
            }

            dataList.add(new SinnohEncounterData() {
                @Override
                public int getFieldRate() {
                    return fieldRate;
                }

                @Override
                public int[] getFieldLevels() {
                    return fieldLevels;
                }

                @Override
                public int[] getFieldEncounters() {
                    return fieldMons;
                }

                @Override
                public int[] getSwarmEncounters() {
                    return swarmMons;
                }

                @Override
                public int[] getDayEncounters() {
                    return dayMons;
                }

                @Override
                public int[] getNightEncounters() {
                    return nightMons;
                }

                @Override
                public int[] getRadarEncounters() {
                    return radarMons;
                }

                @Override
                public int[] getRuby() {
                    return rubyMons;
                }

                @Override
                public int[] getSapphire() {
                    return sapphireMons;
                }

                @Override
                public int[] getEmerald() {
                    return emeraldMons;
                }

                @Override
                public int[] getFireRed() {
                    return fireRedMons;
                }

                @Override
                public int[] getLeafGreen() {
                    return leafGreenMons;
                }

                @Override
                public int getSurfRate() {
                    return surfRate;
                }

                @Override
                public int[] getSurfMaxs() {
                    return surfMaxs;
                }

                @Override
                public int[] getSurfMins() {
                    return surfMins;
                }

                @Override
                public int[] getSurfEncounters() {
                    return surfMons;
                }

                @Override
                public int getOldRate() {
                    return oldRate;
                }

                @Override
                public int[] getOldMaxs() {
                    return oldMaxs;
                }

                @Override
                public int[] getOldMins() {
                    return oldMins;
                }

                @Override
                public int[] getOldEncounters() {
                    return oldMons;
                }

                @Override
                public int getGoodRate() {
                    return goodRate;
                }

                @Override
                public int[] getGoodMaxs() {
                    return goodMaxs;
                }

                @Override
                public int[] getGoodMins() {
                    return goodMins;
                }

                @Override
                public int[] getGoodEncounters() {
                    return goodMons;
                }

                @Override
                public int getSuperRate() {
                    return superRate;
                }

                @Override
                public int[] getSuperMaxs() {
                    return superMaxs;
                }

                @Override
                public int[] getSuperMins() {
                    return superMins;
                }

                @Override
                public int[] getSuperEncounters() {
                    return superMons;
                }
            });
        }


        ArrayList<String[][]> fieldEncounterTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            SinnohEncounterData encounterData= dataList.get(i);
            String[][] area= new String[12][3];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= fieldRateArr[row] + "%";
                area[row][1]= nameData[encounterData.getFieldEncounters()[row]];
                area[row][2]= "" + encounterData.getFieldLevels()[row];
            }
            fieldEncounterTable.add(area);
        }

        ArrayList<String[][]> swarmDayNightTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            SinnohEncounterData encounterData= dataList.get(i);
            String[][] area= new String[2][3];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= nameData[encounterData.getSwarmEncounters()[row]];
                area[row][1]= nameData[encounterData.getDayEncounters()[row]];
                area[row][2]= nameData[encounterData.getNightEncounters()[row]];
            }
            swarmDayNightTable.add(area);
        }

        ArrayList<String[][]> dualSlotTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            SinnohEncounterData encounterData= dataList.get(i);
            String[][] area= new String[2][6];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= "4%";
                area[row][1]= nameData[encounterData.getRuby()[row]];
                area[row][2]= nameData[encounterData.getSapphire()[row]];
                area[row][3]= nameData[encounterData.getEmerald()[row]];
                area[row][4]= nameData[encounterData.getFireRed()[row]];
                area[row][5]= nameData[encounterData.getLeafGreen()[row]];
            }
            dualSlotTable.add(area);
        }

        ArrayList<String[]> radarTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            SinnohEncounterData encounterData= dataList.get(i);
            String[] area= new String[5];
            Arrays.fill(area,"");

            area[0]= "10%";
            area[1]= nameData[encounterData.getRadarEncounters()[0]];
            area[2]= nameData[encounterData.getRadarEncounters()[1]];
            area[3]= nameData[encounterData.getRadarEncounters()[2]];
            area[4]= nameData[encounterData.getRadarEncounters()[3]];

            radarTable.add(area);
        }

        ArrayList<String[][]> waterTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            SinnohEncounterData encounterData= dataList.get(i);
            String[][] area= new String[5][13];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= waterRateArr[row] + "%";
                area[row][1]= nameData[encounterData.getSurfEncounters()[row]];
                area[row][2]= "" + encounterData.getSurfMins()[row];
                area[row][3]= "" + encounterData.getSurfMaxs()[row];
                area[row][4]= nameData[encounterData.getOldEncounters()[row]];
                area[row][5]= "" + encounterData.getOldMins()[row];
                area[row][6]= "" + encounterData.getOldMaxs()[row];
                area[row][7]= nameData[encounterData.getGoodEncounters()[row]];
                area[row][8]= "" + encounterData.getGoodMins()[row];
                area[row][9]= "" + encounterData.getGoodMaxs()[row];
                area[row][10]= nameData[encounterData.getSuperEncounters()[row]];
                area[row][11]= "" + encounterData.getSuperMins()[row];
                area[row][12]= "" + encounterData.getSuperMaxs()[row];
            }
            waterTable.add(area);
        }

        /**
         * Output
         */

        ArrayProcessor processor= new ArrayProcessor();
        processor.append("ID Number,Area,Rate,Pokemon,Level");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            String[][] area= fieldEncounterTable.get(i);
            processor.append(i + "," + areaData[i] + ",,Encounter Rate:," + dataList.get(i).getFieldRate());
            processor.newLine();
            processor.append(",,");
            for(int row= 0; row < area.length; row++)
            {
                for(int col= 0; col < area[row].length; col++)
                {
                    processor.append(area[row][col] + ',');
                }
                if(row != area.length-1)
                {
                    processor.newLine();
                    processor.append(",,");
                }
            }
            processor.newLine();
        }
        Object[][] field= processor.getTable();

        processor= new ArrayProcessor();
        processor.append("ID Number,Area,Rate,Surf Encounter,Min Level,Max Level,Old Rod Encounter,Min Level,Max Level,Good Rod Encounter,Min Level,Max Level,Super Rod Encounter,Min Level,Max Level");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            String[][] area= waterTable.get(i);
            processor.append(i + "," + areaData[i] + ",,Surf Encounter Rate:," + dataList.get(i).getSurfRate() + ",Old Rod Encounter Rate:," + dataList.get(i).getOldRate() + ",Good Rod Encounter Rate:," + dataList.get(i).getGoodRate() + ",Super Rod Encounter Rate:," + dataList.get(i).getSuperRate());
            processor.newLine();
            processor.append(",,");
            for(int row= 0; row < area.length; row++)
            {
                for(int col= 0; col < area[row].length; col++)
                {
                    processor.append(area[row][col] + ',');
                }
                if (row != area.length-1)
                {
                    processor.newLine();
                    processor.append(",,");
                }
            }
            processor.newLine();
        }
        Object[][] water= processor.getTable();

        processor= new ArrayProcessor();
        processor.append("ID Number,Area,Rate,Slot 1,Slot 2,Slot 3,Slot 4");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            processor.append(i + "," + areaData[i]);
            processor.newLine();
            processor.append(",,");
            String[] area= radarTable.get(i);
            for(int x= 0; x < area.length; x++)
            {
                processor.append(area[x] + ",");
            }
            processor.newLine();
        }
        Object[][] radar= processor.getTable();

        processor= new ArrayProcessor();
        processor.append("ID Number,Area,Rate,Ruby,Sapphire,Emerald,FireRed,LeafGreen");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            String[][] area= dualSlotTable.get(i);
            processor.append(i + "," + areaData[i]);
            processor.newLine();
            processor.append(",,");
            for(int row= 0; row < area.length; row++)
            {
                for(int col= 0; col < area[row].length; col++)
                {
                    processor.append(area[row][col] + ',');
                }
                if (row != area.length-1)
                {
                    processor.newLine();
                    processor.append(",,");
                }
            }
            processor.newLine();
        }
        Object[][] dualSlot= processor.getTable();


        processor= new ArrayProcessor();
        processor.append("ID Number,Area,Swarm (20%),10am - 8pm (10%),8pm - 4am (10%)");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            String[][] area= swarmDayNightTable.get(i);
            processor.append(i + "," + areaData[i]);
            processor.newLine();
            processor.append(",,");
            for(int row= 0; row < area.length; row++)
            {
                for(int col= 0; col < area[row].length; col++)
                {
                    processor.append(area[row][col] + ',');
                }
                if (row != area.length-1)
                {
                    processor.newLine();
                    processor.append(",,");
                }
            }
            processor.newLine();
        }
        Object[][] swarm= processor.getTable();


        return new SinnohEncounterReturn()
        {
            @Override
            public Object[][] getField()
            {
                return field;
            }

            @Override
            public Object[][] getWater()
            {
                return water;
            }

            @Override
            public Object[][] getSwarm()
            {
                return swarm;
            }

            @Override
            public Object[][] getRadar()
            {
                return radar;
            }

            @Override
            public Object[][] getDualSlot()
            {
                return dualSlot;
            }
        };
    }




//    public void sheetsToEncounters(Object[][] fieldData, Object[][] waterData, Object[][] swarmData, Object[][] radarData, Object[][] dualSlotData, String outputDir) throws IOException
//    {
//        String outputPath= dataPath + File.separator + outputDir;
//
//        String tempPath= path + "temp" + File.separator + "temp";
//        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(tempPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
//        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden
//
//        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
//        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
//        File file;
//
//        tempPath+= File.separator;
//
//        if(!new File(outputPath).exists() && !new File(outputPath).mkdir())
//        {
//            throw new RuntimeException("Could not create output directory. Check write permissions");
//        }
//        ArrayList<SinnohEncounterData> dataList= new ArrayList<>();
//
//        fieldData= ArrayModifier.trim(fieldData,1,2);
//        int idx= 0;
//
//        for(int i= 0; i < fieldData.length/13; i++) //field encounters
//        {
//            Object[] line;
//            int fieldRate= 0;
//            int[] mons= new int[12];
//            int[] levels= new int[12];
//
//            for(int row= 0; row < 13; row++)
//            {
//                line= fieldData[idx++];
//                if(row == 0)
//                {
//                    fieldRate= Integer.parseInt((String) line[2]);
//                }
//                else
//                {
//                    mons[row-1]= getPokemon((String) line[1]);
//                    levels[row-1]= Integer.parseInt((String) line[2]);
//                }
//            }
//            int finalFieldRate = fieldRate;
//            dataList.add(new SinnohEncounterData() {
//                @Override
//                public int getFieldRate() {
//                    return finalFieldRate;
//                }
//
//                @Override
//                public int[] getFieldLevels() {
//                    return levels;
//                }
//
//                @Override
//                public int[] getFieldEncounters() {
//                    return mons;
//                }
//
//                @Override
//                public int[] getSwarmEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getDayEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getNightEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getRadarEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getRuby() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getSapphire() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getEmerald() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getFireRed() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getLeafGreen() {
//                    return new int[0];
//                }
//
//                @Override
//                public int getSurfRate() {
//                    return 0;
//                }
//
//                @Override
//                public int[] getSurfMaxs() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getSurfMins() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getSurfEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int getOldRate() {
//                    return 0;
//                }
//
//                @Override
//                public int[] getOldMaxs() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getOldMins() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getOldEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int getGoodRate() {
//                    return 0;
//                }
//
//                @Override
//                public int[] getGoodMaxs() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getGoodMins() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getGoodEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int getSuperRate() {
//                    return 0;
//                }
//
//                @Override
//                public int[] getSuperMaxs() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getSuperMins() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getSuperEncounters() {
//                    return new int[0];
//                }
//            });
//        }
//
//        waterData= ArrayModifier.trim(waterData,1,2);
//        idx= 0;
//
//        for(int i= 0; i < waterData.length/6; i++) //water encounters
//        {
//            Object[] line;
//            int surfRate= 0;
//            int oldRate= 0;
//            int goodRate= 0;
//            int superRate= 0;
//            int[] surfMons= new int[5];
//            int[] surfMins= new int[5];
//            int[] surfMaxs= new int[5];
//            int[] oldMons= new int[5];
//            int[] oldMins= new int[5];
//            int[] oldMaxs= new int[5];
//            int[] goodMons= new int[5];
//            int[] goodMins= new int[5];
//            int[] goodMaxs= new int[5];
//            int[] superMons= new int[5];
//            int[] superMins= new int[5];
//            int[] superMaxs= new int[5];
//
//            for(int row= 0; row < 6; row++)
//            {
//                line= waterData[idx++];
//                if(row == 0)
//                {
//                    surfRate= Integer.parseInt((String) line[2]);
//                    oldRate= Integer.parseInt((String) line[4]);
//                    goodRate= Integer.parseInt((String) line[6]);
//                    superRate= Integer.parseInt((String) line[8]);
//                }
//                else
//                {
//                    surfMons[row-1]= getPokemon((String) line[1]);
//                    surfMins[row-1]= Integer.parseInt((String) line[2]);
//                    surfMaxs[row-1]= Integer.parseInt((String) line[3]);
//
//                    oldMons[row-1]= getPokemon((String) line[4]);
//                    oldMins[row-1]= Integer.parseInt((String) line[5]);
//                    oldMaxs[row-1]= Integer.parseInt((String) line[6]);
//
//                    goodMons[row-1]= getPokemon((String) line[7]);
//                    goodMins[row-1]= Integer.parseInt((String) line[8]);
//                    goodMaxs[row-1]= Integer.parseInt((String) line[9]);
//
//                    superMons[row-1]= getPokemon((String) line[10]);
//                    superMins[row-1]= Integer.parseInt((String) line[11]);
//                    superMaxs[row-1]= Integer.parseInt((String) line[12]);
//                }
//            }
//
//            SinnohEncounterData encounterData= dataList.get(i);
//            int finalSurfRate = surfRate;
//            int finalOldRate = oldRate;
//            int finalGoodRate = goodRate;
//            int finalSuperRate = superRate;
//            dataList.set(i, new SinnohEncounterData() {
//                @Override
//                public int getFieldRate() {
//                    return encounterData.getFieldRate();
//                }
//
//                @Override
//                public int[] getFieldLevels() {
//                    return encounterData.getFieldLevels();
//                }
//
//                @Override
//                public int[] getFieldEncounters() {
//                    return encounterData.getFieldEncounters();
//                }
//
//                @Override
//                public int[] getSwarmEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getDayEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getNightEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getRadarEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getRuby() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getSapphire() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getEmerald() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getFireRed() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getLeafGreen() {
//                    return new int[0];
//                }
//
//                @Override
//                public int getSurfRate() {
//                    return finalSurfRate;
//                }
//
//                @Override
//                public int[] getSurfMaxs() {
//                    return surfMaxs;
//                }
//
//                @Override
//                public int[] getSurfMins() {
//                    return surfMins;
//                }
//
//                @Override
//                public int[] getSurfEncounters() {
//                    return surfMons;
//                }
//
//                @Override
//                public int getOldRate() {
//                    return finalOldRate;
//                }
//
//                @Override
//                public int[] getOldMaxs() {
//                    return oldMaxs;
//                }
//
//                @Override
//                public int[] getOldMins() {
//                    return oldMins;
//                }
//
//                @Override
//                public int[] getOldEncounters() {
//                    return oldMons;
//                }
//
//                @Override
//                public int getGoodRate() {
//                    return finalGoodRate;
//                }
//
//                @Override
//                public int[] getGoodMaxs() {
//                    return goodMaxs;
//                }
//
//                @Override
//                public int[] getGoodMins() {
//                    return goodMins;
//                }
//
//                @Override
//                public int[] getGoodEncounters() {
//                    return goodMons;
//                }
//
//                @Override
//                public int getSuperRate() {
//                    return finalSuperRate;
//                }
//
//                @Override
//                public int[] getSuperMaxs() {
//                    return superMaxs;
//                }
//
//                @Override
//                public int[] getSuperMins() {
//                    return superMins;
//                }
//
//                @Override
//                public int[] getSuperEncounters() {
//                    return superMons;
//                }
//            });
//        }
//
//        dualSlotData= ArrayModifier.trim(dualSlotData,1,2);
//        idx= 0;
//
//        for(int i= 0; i < dualSlotData.length/3; i++) //dual-slot encounters
//        {
//            Object[] line;
//            idx++;
//            int[] ruby= new int[2];
//            int[] sapphire= new int[2];
//            int[] emerald= new int[2];
//            int[] fireRed= new int[2];
//            int[] leafGreen= new int[2];
//
//            for(int row= 0; row < 2; row++)
//            {
//                line= dualSlotData[idx++];
//
//                ruby[row]= getPokemon((String) line[1]);
//                sapphire[row]= getPokemon((String) line[2]);
//                emerald[row]= getPokemon((String) line[3]);
//                fireRed[row]= getPokemon((String) line[4]);
//                leafGreen[row]= getPokemon((String) line[5]);
//            }
//            SinnohEncounterData encounterData= dataList.get(i);
//            dataList.set(i, new SinnohEncounterData() {
//                @Override
//                public int getFieldRate() {
//                    return encounterData.getFieldRate();
//                }
//
//                @Override
//                public int[] getFieldLevels() {
//                    return encounterData.getFieldLevels();
//                }
//
//                @Override
//                public int[] getFieldEncounters() {
//                    return encounterData.getFieldEncounters();
//                }
//
//                @Override
//                public int[] getSwarmEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getDayEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getNightEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getRadarEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getRuby() {
//                    return ruby;
//                }
//
//                @Override
//                public int[] getSapphire() {
//                    return sapphire;
//                }
//
//                @Override
//                public int[] getEmerald() {
//                    return emerald;
//                }
//
//                @Override
//                public int[] getFireRed() {
//                    return fireRed;
//                }
//
//                @Override
//                public int[] getLeafGreen() {
//                    return leafGreen;
//                }
//
//                @Override
//                public int getSurfRate() {
//                    return encounterData.getSurfRate();
//                }
//
//                @Override
//                public int[] getSurfMaxs() {
//                    return encounterData.getSurfMaxs();
//                }
//
//                @Override
//                public int[] getSurfMins() {
//                    return encounterData.getSurfMins();
//                }
//
//                @Override
//                public int[] getSurfEncounters() {
//                    return encounterData.getSurfEncounters();
//                }
//
//                @Override
//                public int getOldRate() {
//                    return encounterData.getOldRate();
//                }
//
//                @Override
//                public int[] getOldMaxs() {
//                    return encounterData.getOldMaxs();
//                }
//
//                @Override
//                public int[] getOldMins() {
//                    return encounterData.getOldMins();
//                }
//
//                @Override
//                public int[] getOldEncounters() {
//                    return encounterData.getOldEncounters();
//                }
//
//                @Override
//                public int getGoodRate() {
//                    return encounterData.getGoodRate();
//                }
//
//                @Override
//                public int[] getGoodMaxs() {
//                    return encounterData.getGoodMaxs();
//                }
//
//                @Override
//                public int[] getGoodMins() {
//                    return encounterData.getGoodMins();
//                }
//
//                @Override
//                public int[] getGoodEncounters() {
//                    return encounterData.getGoodEncounters();
//                }
//
//                @Override
//                public int getSuperRate() {
//                    return encounterData.getSuperRate();
//                }
//
//                @Override
//                public int[] getSuperMaxs() {
//                    return encounterData.getSuperMaxs();
//                }
//
//                @Override
//                public int[] getSuperMins() {
//                    return encounterData.getSuperMins();
//                }
//
//                @Override
//                public int[] getSuperEncounters() {
//                    return encounterData.getSuperEncounters();
//                }
//            });
//        }
//
//        radarData= ArrayModifier.trim(radarData,1,2);
//        idx= 0;
//
//        for(int i= 0; i < radarData.length/2; i++) //poke radar encounters
//        {
//            Object[] line;
//            idx++;
//            int[] encounters= new int[4];
//
//            line= radarData[idx++];
//            for(int col= 0; col < 4; col++)
//            {
//                encounters[col]= getPokemon((String) line[col+1]);
//            }
//
//            SinnohEncounterData encounterData= dataList.get(i);
//            dataList.set(i, new SinnohEncounterData() {
//                @Override
//                public int getFieldRate() {
//                    return encounterData.getFieldRate();
//                }
//
//                @Override
//                public int[] getFieldLevels() {
//                    return encounterData.getFieldLevels();
//                }
//
//                @Override
//                public int[] getFieldEncounters() {
//                    return encounterData.getFieldEncounters();
//                }
//
//                @Override
//                public int[] getSwarmEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getDayEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getNightEncounters() {
//                    return new int[0];
//                }
//
//                @Override
//                public int[] getRadarEncounters() {
//                    return encounters;
//                }
//
//                @Override
//                public int[] getRuby() {
//                    return encounterData.getRuby();
//                }
//
//                @Override
//                public int[] getSapphire() {
//                    return encounterData.getSapphire();
//                }
//
//                @Override
//                public int[] getEmerald() {
//                    return encounterData.getEmerald();
//                }
//
//                @Override
//                public int[] getFireRed() {
//                    return encounterData.getFireRed();
//                }
//
//                @Override
//                public int[] getLeafGreen() {
//                    return encounterData.getLeafGreen();
//                }
//
//                @Override
//                public int getSurfRate() {
//                    return encounterData.getSurfRate();
//                }
//
//                @Override
//                public int[] getSurfMaxs() {
//                    return encounterData.getSurfMaxs();
//                }
//
//                @Override
//                public int[] getSurfMins() {
//                    return encounterData.getSurfMins();
//                }
//
//                @Override
//                public int[] getSurfEncounters() {
//                    return encounterData.getSurfEncounters();
//                }
//
//                @Override
//                public int getOldRate() {
//                    return encounterData.getOldRate();
//                }
//
//                @Override
//                public int[] getOldMaxs() {
//                    return encounterData.getOldMaxs();
//                }
//
//                @Override
//                public int[] getOldMins() {
//                    return encounterData.getOldMins();
//                }
//
//                @Override
//                public int[] getOldEncounters() {
//                    return encounterData.getOldEncounters();
//                }
//
//                @Override
//                public int getGoodRate() {
//                    return encounterData.getGoodRate();
//                }
//
//                @Override
//                public int[] getGoodMaxs() {
//                    return encounterData.getGoodMaxs();
//                }
//
//                @Override
//                public int[] getGoodMins() {
//                    return encounterData.getGoodMins();
//                }
//
//                @Override
//                public int[] getGoodEncounters() {
//                    return encounterData.getGoodEncounters();
//                }
//
//                @Override
//                public int getSuperRate() {
//                    return encounterData.getSuperRate();
//                }
//
//                @Override
//                public int[] getSuperMaxs() {
//                    return encounterData.getSuperMaxs();
//                }
//
//                @Override
//                public int[] getSuperMins() {
//                    return encounterData.getSuperMins();
//                }
//
//                @Override
//                public int[] getSuperEncounters() {
//                    return encounterData.getSuperEncounters();
//                }
//            });
//        }
//
//        swarmData= ArrayModifier.trim(swarmData,1,2);
//        idx= 0;
//
//        for(int i= 0; i < swarmData.length/3; i++) //swarm (mass outbreak), day, and night encounters
//        {
//            Object[] line;
//            idx++;
//            int[] swarm= new int[2];
//            int[] day= new int[2];
//            int[] night= new int[2];
//
//            for(int row= 0; row < 2; row++)
//            {
//                line= swarmData[idx++];
//                swarm[row]= getPokemon((String) line[0]);
//                day[row]= getPokemon((String) line[1]);
//                night[row]= getPokemon((String) line[2]);
//            }
//
//            SinnohEncounterData encounterData= dataList.get(i);
//            dataList.set(i, new SinnohEncounterData() {
//                @Override
//                public int getFieldRate() {
//                    return encounterData.getFieldRate();
//                }
//
//                @Override
//                public int[] getFieldLevels() {
//                    return encounterData.getFieldLevels();
//                }
//
//                @Override
//                public int[] getFieldEncounters() {
//                    return encounterData.getFieldEncounters();
//                }
//
//                @Override
//                public int[] getSwarmEncounters() {
//                    return swarm;
//                }
//
//                @Override
//                public int[] getDayEncounters() {
//                    return day;
//                }
//
//                @Override
//                public int[] getNightEncounters() {
//                    return night;
//                }
//
//                @Override
//                public int[] getRadarEncounters() {
//                    return encounterData.getRadarEncounters();
//                }
//
//                @Override
//                public int[] getRuby() {
//                    return encounterData.getRuby();
//                }
//
//                @Override
//                public int[] getSapphire() {
//                    return encounterData.getSapphire();
//                }
//
//                @Override
//                public int[] getEmerald() {
//                    return encounterData.getEmerald();
//                }
//
//                @Override
//                public int[] getFireRed() {
//                    return encounterData.getFireRed();
//                }
//
//                @Override
//                public int[] getLeafGreen() {
//                    return encounterData.getLeafGreen();
//                }
//
//                @Override
//                public int getSurfRate() {
//                    return encounterData.getSurfRate();
//                }
//
//                @Override
//                public int[] getSurfMaxs() {
//                    return encounterData.getSurfMaxs();
//                }
//
//                @Override
//                public int[] getSurfMins() {
//                    return encounterData.getSurfMins();
//                }
//
//                @Override
//                public int[] getSurfEncounters() {
//                    return encounterData.getSurfEncounters();
//                }
//
//                @Override
//                public int getOldRate() {
//                    return encounterData.getOldRate();
//                }
//
//                @Override
//                public int[] getOldMaxs() {
//                    return encounterData.getOldMaxs();
//                }
//
//                @Override
//                public int[] getOldMins() {
//                    return encounterData.getOldMins();
//                }
//
//                @Override
//                public int[] getOldEncounters() {
//                    return encounterData.getOldEncounters();
//                }
//
//                @Override
//                public int getGoodRate() {
//                    return encounterData.getGoodRate();
//                }
//
//                @Override
//                public int[] getGoodMaxs() {
//                    return encounterData.getGoodMaxs();
//                }
//
//                @Override
//                public int[] getGoodMins() {
//                    return encounterData.getGoodMins();
//                }
//
//                @Override
//                public int[] getGoodEncounters() {
//                    return encounterData.getGoodEncounters();
//                }
//
//                @Override
//                public int getSuperRate() {
//                    return encounterData.getSuperRate();
//                }
//
//                @Override
//                public int[] getSuperMaxs() {
//                    return encounterData.getSuperMaxs();
//                }
//
//                @Override
//                public int[] getSuperMins() {
//                    return encounterData.getSuperMins();
//                }
//
//                @Override
//                public int[] getSuperEncounters() {
//                    return encounterData.getSuperEncounters();
//                }
//            });
//        }
//
//
//
//        BinaryWriter writer;
//        for(int i= 0; i < dataList.size(); i++) //recreating game files
//        {
//            writer= new BinaryWriter(outputPath + separator + i + ".bin");
//            SinnohEncounterData encounterData= dataList.get(i);
//
//            writer.writeInt(encounterData.getFieldRate());
//            for(int x= 0; x < 12; x++)
//            {
//                writer.writeInt(encounterData.getFieldLevels()[x]);
//                writer.writeInt(encounterData.getFieldEncounters()[x]);
//            }
//
//            writer.writeInts(encounterData.getSwarmEncounters());
//
//            writer.writeInts(encounterData.getDayEncounters());
//
//            writer.writeInts(encounterData.getNightEncounters());
//
//            writer.writeInts(encounterData.getRadarEncounters());
//
//            byte[] padding;
//            if(i >= files.length)
//            {
//                padding= new byte[0x18];
//                Arrays.fill(padding, (byte) 0);
//            }
//            else
//            {
//                file= files[i];
//                Buffer buffer= new Buffer(file.toString());
//                padding= buffer.readBytes((int)file.length());
//                buffer.close();
//            }
//            writer.write(padding);
//
//            writer.writeInts(encounterData.getRuby());
//            writer.writeInts(encounterData.getSapphire());
//            writer.writeInts(encounterData.getEmerald());
//            writer.writeInts(encounterData.getFireRed());
//            writer.writeInts(encounterData.getLeafGreen());
//
//            writer.writeInt(encounterData.getSurfRate());
//            for(int x= 0; x < 5; x++)
//            {
//                writer.writeByte((byte)encounterData.getSurfMaxs()[x]);
//                writer.writeByte((byte)encounterData.getSurfMins()[x]);
//                writer.writePadding(2);
//                writer.writeInt(encounterData.getSurfEncounters()[x]);
//            }
//            writer.writePadding(0x2C);
//
//            writer.writeInt(encounterData.getOldRate());
//            for(int x= 0; x < 5; x++)
//            {
//                writer.writeByte((byte)encounterData.getOldMaxs()[x]);
//                writer.writeByte((byte)encounterData.getOldMins()[x]);
//                writer.writePadding(2);
//                writer.writeInt(encounterData.getOldEncounters()[x]);
//            }
//
//            writer.writeInt(encounterData.getGoodRate());
//            for(int x= 0; x < 5; x++)
//            {
//                writer.writeByte((byte)encounterData.getGoodMaxs()[x]);
//                writer.writeByte((byte)encounterData.getGoodMins()[x]);
//                writer.writePadding(2);
//                writer.writeInt(encounterData.getGoodEncounters()[x]);
//            }
//
//            writer.writeInt(encounterData.getSuperRate());
//            for(int x= 0; x < 5; x++)
//            {
//                writer.writeByte((byte)encounterData.getSuperMaxs()[x]);
//                writer.writeByte((byte)encounterData.getSuperMins()[x]);
//                writer.writePadding(2);
//                writer.writeInt(encounterData.getSuperEncounters()[x]);
//            }
//            writer.close();
//        }
//    }



    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(SinnohEncounterEditor::fileToInt));
    }

    private static int fileToInt (File f)
    {
        return Integer.parseInt(f.getName().split("\\.")[0]);
    }

    private static int getType(String type)
    {
        for(int i= 0; i < typeArr.length; i++)
        {
            if(type.equals(typeArr[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid type entered");
    }

    private static int getMove(String move)
    {
        for(int i= 0; i < moveData.length; i++)
        {
            if(move.equals(moveData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid move entered");
    }

    private static int getPokemon(String pokemon)
    {
        for(int i= 0; i < nameData.length; i++)
        {
            if(pokemon.equals(nameData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid pokemon entered");
    }

    private static int getArea(String area)
    {
        for(int i= 0; i < areaData.length; i++)
        {
            if(area.equals(areaData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid area entered");
    }


    private static int getItem(String item)
    {
        for(int i= 0; i < itemData.length; i++)
        {
            if(item.equals(itemData[i]))
            {
                return i;
            }
        }
        throw new RuntimeException("Invalid item entered");
    }

    private static short parseShort(String... evs)
    {
        String fullEv= "";
        for(String ev : evs)
        {
            fullEv+= ev;
        }

        short num= 0;
        for(int i= 0; i < fullEv.length(); i++)
        {
            String thisEv= fullEv.substring(fullEv.length()-i-1,fullEv.length()-i);
            if(thisEv.equals("1"))
            {
                num+= Math.pow(2,i);
            }
        }
        return num;
    }

    private static short[] reverseBytes(short[] shorts)
    {
        short[] reverse= new short[shorts.length];
        for(int i= 0; i < shorts.length; i++) {
            short thisShort= shorts[i];
            reverse[i]= Short.reverseBytes(thisShort);
            System.out.println("Original: " + shorts[i] + ", Reversed: " + reverse[i]);
        }
        return reverse;
    }

    private static short[] intToShort(int[] bytes)
    {
        short[] shorts= new short[bytes.length];
        for(int i= 0; i < bytes.length; i++)
        {
            shorts[i]= (short)bytes[i];
        }
        return shorts;
    }
}
