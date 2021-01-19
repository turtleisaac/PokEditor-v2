package com.turtleisaac.pokeditor.editors.encounters.johto;

import com.turtleisaac.pokeditor.framework.ArrayModifier;
import com.turtleisaac.pokeditor.framework.ArrayProcessor;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;

import java.io.*;
import java.util.*;

public class JohtoEncounterEditor
{
    public static void main(String[] args) throws IOException {
        JohtoEncounterEditor encounterEditor= new JohtoEncounterEditor("ipge");
        encounterEditor.encountersToSheet("a136SS");
    }

    private static String projectPath;
    private String dataPath= "";
    private String separator= File.separator;
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "Fairy", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] colorArr= {"Red","Blue","Yellow","Green","Black","Brown","Purple","Gray","White","Pink"};
    private static final int[] fieldRateArr= {20,20,10,10,10,10,5,5,4,4,1,1};
    private static final int[] smashRateArr= {90,10};
    private static final int[] waterRateArr= {60,30,5,4,1};
    private static String resourcePath;
    private static String[] nameData;
    private static String[] areaData;
    private static String[] outdoorData;
    private static String[] indoorData;

    public JohtoEncounterEditor(String projectPath) throws IOException
    {
        this.projectPath= projectPath;
        dataPath= projectPath;
        resourcePath= projectPath.substring(0,projectPath.lastIndexOf("/"));
        resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf("/")) + File.separator + "Program Files" + File.separator;

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "EntryData.txt"));
        ArrayList<String> nameList= new ArrayList<>();
        String line;
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            nameList.add(line);
        }
        nameData= nameList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "Locations.txt"));
        ArrayList<String> areaList= new ArrayList<>();

        while ((line= reader.readLine()) != null)
        {
            line= line.trim();
            areaList.add(line);
        }
        areaData= areaList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "OutdoorLocations.txt"));
        ArrayList<String> outdoorList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            outdoorList.add(line.substring(3));
        }
        outdoorData= outdoorList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "IndoorLocations.txt"));
        ArrayList<String> indoorList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            indoorList.add(line.substring(3));
        }
        indoorData= indoorList.toArray(new String[0]);
        reader.close();
    }

    public JohtoEncounterReturn encountersToSheet(String encounterDir) throws IOException
    {
        dataPath+= encounterDir;
        ArrayList<EncounterData> dataList= new ArrayList<>();

        List<File> fileList = new ArrayList<>(Arrays.asList(Objects.requireNonNull(new File(dataPath).listFiles()))); //creates a List of File objects representing every file in specified parameter directory
        fileList.removeIf(File::isHidden); //removes all File objects from List that are hidden

        File[] files = fileList.toArray(new File[0]); //creates an array of File objects using the contents of the modified List
        sort(files); //sorts files numerically (0.bin, 1.bin, 2.bin, etc...)
        File file;

        for(int i= 0; i < files.length; i++)
        {
            file= files[i];
            Buffer buffer= new Buffer(file.toString());

            int fieldRate= buffer.readByte();
            int surfRate= buffer.readByte();
            int smashRate= buffer.readByte();
            int oldRate= buffer.readByte();
            int goodRate= buffer.readByte();
            int superRate= buffer.readByte();
            short padding= buffer.readShort();
            byte[] fieldLevels= buffer.readBytes(12);
            short[] fieldMorning= buffer.readShorts(12);
            short[] fieldDay= buffer.readShorts(12);
            short[] fieldNight= buffer.readShorts(12);
            short[] hoenn= buffer.readShorts(2);
            short[] sinnoh= buffer.readShorts(2);
            byte[] surf1MinMax= buffer.readBytes(2);
            short surf1= buffer.readShort();
            byte[] surf2MinMax= buffer.readBytes(2);
            short surf2= buffer.readShort();
            byte[] surf3MinMax= buffer.readBytes(2);
            short surf3= buffer.readShort();
            byte[] surf4MinMax= buffer.readBytes(2);
            short surf4= buffer.readShort();
            byte[] surf5MinMax= buffer.readBytes(2);
            short surf5= buffer.readShort();
            byte[] smash1MinMax= buffer.readBytes(2);
            short smash1= buffer.readShort();
            byte[] smash2MinMax= buffer.readBytes(2);
            short smash2= buffer.readShort();
            byte[] old1MinMax= buffer.readBytes(2);
            short old1= buffer.readShort();
            byte[] old2MinMax= buffer.readBytes(2);
            short old2= buffer.readShort();
            byte[] old3MinMax= buffer.readBytes(2);
            short old3= buffer.readShort();
            byte[] old4MinMax= buffer.readBytes(2);
            short old4= buffer.readShort();
            byte[] old5MinMax= buffer.readBytes(2);
            short old5= buffer.readShort();
            byte[] good1MinMax= buffer.readBytes(2);
            short good1= buffer.readShort();
            byte[] good2MinMax= buffer.readBytes(2);
            short good2= buffer.readShort();
            byte[] good3MinMax= buffer.readBytes(2);
            short good3= buffer.readShort();
            byte[] good4MinMax= buffer.readBytes(2);
            short good4= buffer.readShort();
            byte[] good5MinMax= buffer.readBytes(2);
            short good5= buffer.readShort();
            byte[] super1MinMax= buffer.readBytes(2);
            short super1= buffer.readShort();
            byte[] super2MinMax= buffer.readBytes(2);
            short super2= buffer.readShort();
            byte[] super3MinMax= buffer.readBytes(2);
            short super3= buffer.readShort();
            byte[] super4MinMax= buffer.readBytes(2);
            short super4= buffer.readShort();
            byte[] super5MinMax= buffer.readBytes(2);
            short super5= buffer.readShort();
            short fieldSwarm= buffer.readShort();
            short surfSwarm= buffer.readShort();
            short goodSwarm= buffer.readShort();
            short superSwarm= buffer.readShort();

            dataList.add(new EncounterData() {
                @Override
                public int getFieldRate() { return fieldRate; }

                @Override
                public int getSurfRate() { return surfRate; }

                @Override
                public int getSmashRate() { return smashRate; }

                @Override
                public int getOldRate() { return oldRate; }

                @Override
                public int getGoodRate() { return goodRate; }

                @Override
                public int getSuperRate() { return superRate; }

                @Override
                public short getPadding() { return padding; }

                @Override
                public byte[] getFieldLevels() { return fieldLevels; }

                @Override
                public short[] getFieldMorning() { return fieldMorning; }

                @Override
                public short[] getFieldDay() { return fieldDay; }

                @Override
                public short[] getFieldNight() { return fieldNight; }

                @Override
                public short[] getHoenn() { return hoenn; }

                @Override
                public short[] getSinnoh() { return sinnoh; }

                @Override
                public byte[] getSurf1MinMax() { return surf1MinMax; }

                @Override
                public short getSurf1() { return surf1; }

                @Override
                public byte[] getSurf2MinMax() { return surf2MinMax; }

                @Override
                public short getSurf2() { return surf2; }

                @Override
                public byte[] getSurf3MinMax() { return surf3MinMax; }

                @Override
                public short getSurf3() { return surf3; }

                @Override
                public byte[] getSurf4MinMax() { return surf4MinMax; }

                @Override
                public short getSurf4() { return surf4; }

                @Override
                public byte[] getSurf5MinMax() { return surf5MinMax; }

                @Override
                public short getSurf5() { return surf5; }

                @Override
                public short[] getSurfs() {
                    return new short[] {surf1,surf2,surf3,surf4,surf5};
                }

                @Override
                public byte[] getSurfMins() {
                    return new byte[] {surf1MinMax[0],surf2MinMax[0],surf3MinMax[0],surf4MinMax[0],surf5MinMax[0]};
                }

                @Override
                public byte[] getSurfMaxs() {
                    return new byte[] {surf1MinMax[1],surf2MinMax[1],surf3MinMax[1],surf4MinMax[1],surf5MinMax[1]};
                }

                @Override
                public short getSmash1() { return smash1; }

                @Override
                public byte[] getSmash1MinMax() { return smash1MinMax; }

                @Override
                public short getSmash2() { return smash2; }

                @Override
                public byte[] getSmash2MinMax() { return smash2MinMax; }

                @Override
                public short[] getSmashes() {
                    return new short[] {smash1,smash2};
                }

                @Override
                public byte[] getSmashMins() {
                    return new byte[] {smash1MinMax[0],smash2MinMax[0]};
                }

                @Override
                public byte[] getSmashMaxs() {
                    return new byte[] {smash1MinMax[0],smash2MinMax[1]};
                }

                @Override
                public byte[] getOld1MinMax() { return old1MinMax; }

                @Override
                public short getOld1() { return old1; }

                @Override
                public byte[] getOld2MinMax() { return old2MinMax; }

                @Override
                public short getOld2() { return old2; }

                @Override
                public byte[] getOld3MinMax() { return old3MinMax; }

                @Override
                public short getOld3() { return old3; }

                @Override
                public byte[] getOld4MinMax() { return old4MinMax; }

                @Override
                public short getOld4() { return old4; }

                @Override
                public byte[] getOld5MinMax() { return old5MinMax; }

                @Override
                public short getOld5() { return old5; }

                @Override
                public short[] getOlds() {
                    return new short[] {old1,old2,old3,old4,old5};
                }

                @Override
                public byte[] getOldMins() {
                    return new byte[] {old1MinMax[0],old2MinMax[0],old3MinMax[0],old4MinMax[0],old5MinMax[0]};
                }

                @Override
                public byte[] getOldMaxs() {
                    return new byte[] {old1MinMax[1],old2MinMax[1],old3MinMax[1],old4MinMax[1],old5MinMax[1]};
                }

                @Override
                public byte[] getGood1MinMax() { return good1MinMax; }

                @Override
                public short getGood1() { return good1; }

                @Override
                public byte[] getGood2MinMax() { return good2MinMax; }

                @Override
                public short getGood2() { return good2; }

                @Override
                public byte[] getGood3MinMax() { return good3MinMax; }

                @Override
                public short getGood3() { return good3; }

                @Override
                public byte[] getGood4MinMax() { return good4MinMax; }

                @Override
                public short getGood4() { return good4; }

                @Override
                public byte[] getGood5MinMax() { return good5MinMax; }

                @Override
                public short getGood5() { return good5; }

                @Override
                public short[] getGoods() {
                    return new short[] {good1,good2,good3,good4,good5};
                }

                @Override
                public byte[] getGoodMins() {
                    return new byte[] {good1MinMax[0],good2MinMax[0],good3MinMax[0],good4MinMax[0],good5MinMax[0]};
                }

                @Override
                public byte[] getGoodMaxs() {
                    return new byte[] {good1MinMax[1],good2MinMax[1],good3MinMax[1],good4MinMax[1],good5MinMax[1]};
                }

                @Override
                public byte[] getSuper1MinMax() { return super1MinMax; }

                @Override
                public short getSuper1() { return super1; }

                @Override
                public byte[] getSuper2MinMax() { return super2MinMax; }

                @Override
                public short getSuper2() { return super2; }

                @Override
                public byte[] getSuper3MinMax() { return super3MinMax; }

                @Override
                public short getSuper3() { return super3; }

                @Override
                public byte[] getSuper4MinMax() { return super4MinMax; }

                @Override
                public short getSuper4() { return super4; }

                @Override
                public byte[] getSuper5MinMax() { return super5MinMax; }

                @Override
                public short getSuper5() { return super5; }

                @Override
                public short[] getSupers() {
                    return new short[] {super1,super2,super3,super4,super5};
                }

                @Override
                public byte[] getSuperMins() {
                    return new byte[] {super1MinMax[0],super2MinMax[0],super3MinMax[0],super4MinMax[0],super5MinMax[0]};
                }

                @Override
                public byte[] getSuperMaxs() {
                    return new byte[] {super1MinMax[1],super2MinMax[1],super3MinMax[1],super4MinMax[1],super5MinMax[1]};
                }

                @Override
                public short getFieldSwarm() { return fieldSwarm; }

                @Override
                public short getSurfSwarm() { return surfSwarm; }

                @Override
                public short getGoodSwarm() { return goodSwarm; }

                @Override
                public short getSuperSwarm() { return superSwarm; }

            });
        }

        ArrayList<String[][]> fieldEncounterTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            EncounterData encounterData= dataList.get(i);
            String[][] area= new String[12][5];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            int idx= 0;
            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= fieldRateArr[idx] + "";
                System.out.print(area[row][0] + ", ");
                area[row][1]= nameData[encounterData.getFieldMorning()[idx]];
                System.out.print(area[row][1] + ", ");
                area[row][2]= nameData[encounterData.getFieldDay()[idx]];
                System.out.print(area[row][2] + ", ");
                area[row][3]= nameData[encounterData.getFieldNight()[idx]];
                System.out.print(area[row][3] + ", ");
                area[row][4]= "" + encounterData.getFieldLevels()[idx++];
                System.out.println(area[row][4] + "\n");
            }
            fieldEncounterTable.add(area);
            System.out.println("\n");
        }

        ArrayList<String[][]> waterEncounterTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            EncounterData encounterData= dataList.get(i);
            String[][] area= new String[5][13];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            int idx= 0;
            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= waterRateArr[idx] + "";
                System.out.print(area[row][0] + ", ");
                area[row][1]= nameData[encounterData.getSurfs()[idx]];
                System.out.print(area[row][1] + ", ");
                area[row][2]= "" + encounterData.getSurfMins()[idx];
                System.out.print(area[row][2] + ", ");
                area[row][3]= "" + encounterData.getSurfMaxs()[idx];
                System.out.print(area[row][3] + ", ");
                area[row][4]= nameData[encounterData.getOlds()[idx]];
                System.out.print(area[row][4] + ", ");
                area[row][5]= "" + encounterData.getOldMins()[idx];
                System.out.print(area[row][5] + ", ");
                area[row][6]= "" + encounterData.getOldMaxs()[idx];
                System.out.print(area[row][6] + ", ");
                area[row][7]= nameData[encounterData.getGoods()[idx]];
                System.out.print(area[row][7] + ", ");
                area[row][8]= "" + encounterData.getGoodMins()[idx];
                System.out.print(area[row][8] + ", ");
                area[row][9]= "" + encounterData.getGoodMaxs()[idx];
                System.out.print(area[row][9] + ", ");
                area[row][10]= nameData[encounterData.getSupers()[idx]];
                System.out.print(area[row][10] + ", ");
                area[row][11]= "" + encounterData.getSuperMins()[idx];
                System.out.print(area[row][11] + ", ");
                area[row][12]= "" + encounterData.getSuperMaxs()[idx++];
                System.out.println(area[row][12] + "\n");
            }
            waterEncounterTable.add(area);
            System.out.println("\n");
        }

        ArrayList<String[][]> smashEncounterTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            EncounterData encounterData= dataList.get(i);
            String[][] area= new String[2][4];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            int idx= 0;
            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= smashRateArr[idx] + "";
                System.out.print(area[row][0] + ", ");
                area[row][1]= nameData[encounterData.getSmashes()[idx]];
                System.out.print(area[row][1] + ", ");
                area[row][2]= "" + encounterData.getSmashMins()[idx];
                System.out.print(area[row][2] + ", ");
                area[row][3]= "" + encounterData.getSmashMaxs()[idx++];
                System.out.println(area[row][3] + "\n");
            }
            smashEncounterTable.add(area);
        }

        ArrayList<String[]> massOutbreakEncounterTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            EncounterData encounterData= dataList.get(i);
            String[] area= new String[4];
            Arrays.fill(area,"");

            area[0]= nameData[encounterData.getFieldSwarm()];
            area[1]= nameData[encounterData.getSurfSwarm()];
            area[2]= nameData[encounterData.getGoodSwarm()];
            area[3]= nameData[encounterData.getSuperSwarm()];
            massOutbreakEncounterTable.add(area);
        }

        ArrayList<String[][]> soundTable= new ArrayList<>();
        for(int i= 0; i < dataList.size(); i++)
        {
            EncounterData encounterData= dataList.get(i);
            String[][] area= new String[2][2];
            for(int x= 0; x < area.length; x++)
            {
                Arrays.fill(area[x],"");
            }

            int idx= 0;
            for(int row= 0; row < area.length; row++)
            {
                area[row][0]= nameData[encounterData.getHoenn()[idx]];
                area[row][1]= nameData[encounterData.getSinnoh()[idx]];
            }
            soundTable.add(area);
        }


        /**
         * Output
         */


        ArrayProcessor processor= new ArrayProcessor();
        processor.append("ID Number,Area,Rate,Morning,Day,Night,Level");
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
            String[][] area= waterEncounterTable.get(i);
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
        processor.append("ID Number,Area,Rate,Encounter,Min Level,Max Level");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            String[][] area= smashEncounterTable.get(i);
            processor.append(i + "," + areaData[i] + ",,Encounter Rate:," + dataList.get(i).getSmashRate());
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
        Object[][] smash= processor.getTable();


        processor= new ArrayProcessor();
        processor.append("ID Number,Area,Field,Surf,Good Rod,Super Rod");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            String[] area= massOutbreakEncounterTable.get(i);
            processor.append(i + "," + areaData[i]);
            processor.newLine();
            processor.append(",,");
            for(int x= 0; x < area.length; x++)
            {
                processor.append(area[x] + ",");
            }
            processor.newLine();
        }
        Object[][] outbreak= processor.getTable();


        processor= new ArrayProcessor();
        processor.append("ID Number,Area,Hoenn,Sinnoh");
        processor.newLine();
        for(int i= 0; i < dataList.size(); i++)
        {
            String[][] area= soundTable.get(i);
            processor.append(i + "," + areaData[i]);
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
        Object[][] sound= processor.getTable();


        return new JohtoEncounterReturn()
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
            public Object[][] getSmash()
            {
                return smash;
            }

            @Override
            public Object[][] getOutbreak()
            {
                return outbreak;
            }

            @Override
            public Object[][] getSound()
            {
                return sound;
            }
        };
    }




    public void sheetsToEncounters(Object[][] fieldData, Object[][] waterData, Object[][] smashData, Object[][] outbreakData, Object[][] soundData, String outputDir) throws IOException
    {
        String outputPath= dataPath + File.separator + outputDir;

        if(!new File(outputPath).exists() && !new File(outputPath).mkdir())
        {
            throw new RuntimeException("Could not create output directory. Check write permissions");
        }

        ArrayList<int[]> rateList= new ArrayList<>();
        ArrayList<int[]> fieldLevelList= new ArrayList<>();
        ArrayList<int[]> fieldMorningList= new ArrayList<>();
        ArrayList<int[]> fieldDayList= new ArrayList<>();
        ArrayList<int[]> fieldNightList= new ArrayList<>();

        fieldData= ArrayModifier.trim(fieldData,1,2);
        int idx= 0;

        for(int i= 0; i < fieldData.length/13; i++)
        {
            int[] morningArr= new int[12];
            int[] dayArr= new int[12];
            int[] nightArr= new int[12];
            int[] levelArr= new int[12];
            for(int row= 0; row < 13; row++)
            {
                Object[] line= fieldData[idx++];
                if(row == 0)
                {
                    int[] rateArr= new int[6];
                    rateArr[0]= Integer.parseInt((String) line[2]);
                    rateList.add(rateArr);
                }
                else
                {
                    morningArr[row-1]= getPokemon((String) line[1]);
                    dayArr[row-1]= getPokemon((String) line[2]);
                    nightArr[row-1]= getPokemon((String) line[3]);
                    levelArr[row-1]= Integer.parseInt((String) line[4]);
                }
            }
            fieldMorningList.add(morningArr);
            System.out.println(Arrays.toString(morningArr));
            fieldDayList.add(dayArr);
            System.out.println(Arrays.toString(dayArr));
            fieldNightList.add(nightArr);
            System.out.println(Arrays.toString(nightArr));
            fieldLevelList.add(levelArr);
            System.out.println(Arrays.toString(levelArr) + "\n");
        }

        ArrayList<int[]> surfLevelMins= new ArrayList<>();
        ArrayList<int[]> surfLevelMaxs= new ArrayList<>();
        ArrayList<int[]> surfEncounterList= new ArrayList<>();

        ArrayList<int[]> oldLevelMins= new ArrayList<>();
        ArrayList<int[]> oldLevelMaxs= new ArrayList<>();
        ArrayList<int[]> oldEncounterList= new ArrayList<>();

        ArrayList<int[]> goodLevelMins= new ArrayList<>();
        ArrayList<int[]> goodLevelMaxs= new ArrayList<>();
        ArrayList<int[]> goodEncounterList= new ArrayList<>();

        ArrayList<int[]> superLevelMins= new ArrayList<>();
        ArrayList<int[]> superLevelMaxs= new ArrayList<>();
        ArrayList<int[]> superEncounterList= new ArrayList<>();

        waterData= ArrayModifier.trim(waterData,1,2);
        idx= 0;

        for(int i= 0; i < waterData.length/6; i++)
        {
            int[] rates= rateList.get(i);

            int[] surfArr= new int[5];
            int[] surfMinArr= new int[5];
            int[] surfMaxArr= new int[5];

            int[] oldArr= new int[5];
            int[] oldMinArr= new int[5];
            int[] oldMaxArr= new int[5];

            int[] goodArr= new int[5];
            int[] goodMinArr= new int[5];
            int[] goodMaxArr= new int[5];

            int[] superArr= new int[5];
            int[] superMinArr= new int[5];
            int[] superMaxArr= new int[5];

            for(int row= 0; row < 6; row++)
            {
                Object[] line= waterData[idx++];
                if(row == 0)
                {
                    rates[1]= Integer.parseInt((String) line[2]);
                    rates[3]= Integer.parseInt((String) line[4]);
                    rates[4]= Integer.parseInt((String) line[6]);
                    rates[5]= Integer.parseInt((String) line[8]);
                    rateList.set(i,rates);
                }
                else
                {
                    surfArr[row-1]= getPokemon((String) line[1]);
                    surfMinArr[row-1]= Integer.parseInt((String) line[2]);
                    surfMaxArr[row-1]= Integer.parseInt((String) line[3]);

                    oldArr[row-1]= getPokemon((String) line[4]);
                    oldMinArr[row-1]= Integer.parseInt((String) line[5]);
                    oldMaxArr[row-1]= Integer.parseInt((String) line[6]);

                    goodArr[row-1]= getPokemon((String) line[7]);
                    goodMinArr[row-1]= Integer.parseInt((String) line[8]);
                    goodMaxArr[row-1]= Integer.parseInt((String) line[9]);

                    superArr[row-1]= getPokemon((String) line[10]);
                    superMinArr[row-1]= Integer.parseInt((String) line[11]);
                    superMaxArr[row-1]= Integer.parseInt((String) line[12]);
                }
            }
            surfLevelMins.add(surfMinArr);
            surfLevelMaxs.add(surfMaxArr);
            surfEncounterList.add(surfArr);

            oldLevelMins.add(oldMinArr);
            oldLevelMaxs.add(oldMaxArr);
            oldEncounterList.add(oldArr);

            goodLevelMins.add(goodMinArr);
            goodLevelMaxs.add(goodMaxArr);
            goodEncounterList.add(goodArr);

            superLevelMins.add(superMinArr);
            superLevelMaxs.add(superMaxArr);
            superEncounterList.add(superArr);
        }

        ArrayList<int[]> smashLevelMins= new ArrayList<>();
        ArrayList<int[]> smashLevelMaxs= new ArrayList<>();
        ArrayList<int[]> smashEncounterList= new ArrayList<>();

        smashData= ArrayModifier.trim(smashData,1,2);
        idx= 0;

        for(int i= 0; i < smashData.length/3; i++)
        {
            int[] rates = rateList.get(i);

            int[] smashArr= new int[2];
            int[] smashMinArr= new int[2];
            int[] smashMaxArr= new int[2];
            for(int row= 0; row < 3; row++)
            {
                Object[] line= smashData[idx++];
                if(row == 0)
                {
                    rates[2]= Integer.parseInt((String) line[2]);
                    rateList.set(i,rates);
                }
                else
                {
                    smashArr[row-1]= getPokemon((String) line[1]);
                    smashMinArr[row-1]= Integer.parseInt((String) line[2]);
                    smashMaxArr[row-1]= Integer.parseInt((String) line[3]);
                }
            }
            smashLevelMins.add(smashMinArr);
            smashLevelMaxs.add(smashMaxArr);
            smashEncounterList.add(smashArr);
        }

        ArrayList<int[]> massOutbreakEncounterList= new ArrayList<>();

        outbreakData= ArrayModifier.trim(outbreakData,1,2);
        idx= 0;

        for(int i= 0; i < outbreakData.length/2;i++)
        {
            int[] outbreaks= new int[4];
            for(int row= 0; row < 2; row++)
            {
                Object[] line= outbreakData[idx++];
                if(row != 0)
                {
                    outbreaks[0]= getPokemon((String) line[0]);
                    outbreaks[1]= getPokemon((String) line[1]);
                    outbreaks[2]= getPokemon((String) line[2]);
                    outbreaks[3]= getPokemon((String) line[3]);
                }
            }
            massOutbreakEncounterList.add(outbreaks);
        }

        ArrayList<int[]> hoennSoundList= new ArrayList<>();
        ArrayList<int[]> sinnohSoundList= new ArrayList<>();

        soundData= ArrayModifier.trim(soundData,1,2);
        idx= 0;

        for(int i= 0; i < soundData.length/3; i++)
        {
            int[] hoenn= new int[2];
            int[] sinnoh= new int[2];
            for(int row= 0; row < 3; row++)
            {
                Object[] line= soundData[idx++];
                if(row != 0)
                {
                    hoenn[row-1]= getPokemon((String) line[0]);
                    sinnoh[row-1]= getPokemon((String) line[1]);
                }
            }
            hoennSoundList.add(hoenn);
            sinnohSoundList.add(sinnoh);
        }

        for(int i= 0; i < rateList.size(); i++)
        {
            BinaryWriter writer= new BinaryWriter(new File(outputPath + separator + i + ".bin"));
            int[] rates= rateList.get(i);
            int[] fieldLevels= fieldLevelList.get(i);
            int[] fieldMorning= fieldMorningList.get(i);
            int[] fieldDay= fieldDayList.get(i);
            int[] fieldNight= fieldNightList.get(i);

            int[] hoenn= hoennSoundList.get(i);
            int[] sinnoh= sinnohSoundList.get(i);

            int[] surfMins= surfLevelMins.get(i);
            int[] surfMaxs= surfLevelMaxs.get(i);
            int[] surfArr= surfEncounterList.get(i);

            int[] smashMins= smashLevelMins.get(i);
            int[] smashMaxs= smashLevelMaxs.get(i);
            int[] smashArr= smashEncounterList.get(i);

            int[] oldMins= oldLevelMins.get(i);
            int[] oldMaxs= oldLevelMaxs.get(i);
            int[] oldArr= oldEncounterList.get(i);

            int[] goodMins= goodLevelMins.get(i);
            int[] goodMaxs= goodLevelMaxs.get(i);
            int[] goodArr= goodEncounterList.get(i);

            int[] superMins= superLevelMins.get(i);
            int[] superMaxs= superLevelMaxs.get(i);
            int[] superArr= superEncounterList.get(i);

            int[] outbreaks= massOutbreakEncounterList.get(i);

            writer.writeBytes(rates);
            writer.writeShort((short)0);
            writer.writeBytes(fieldLevels);
            writer.writeShorts(fieldMorning);
            writer.writeShorts(fieldDay);
            writer.writeShorts(fieldNight);
            writer.writeShorts(hoenn);
            writer.writeShorts(sinnoh);
            for(int x= 0; x < surfMins.length; x++)
            {
                writer.writeByte((byte)surfMins[x]);
                writer.writeByte((byte)surfMaxs[x]);
                writer.writeShort((short) surfArr[x]);
            }
            for(int x= 0; x < smashArr.length; x++)
            {
                writer.writeByte((byte)smashMins[x]);
                writer.writeByte((byte)smashMaxs[x]);
                writer.writeShort((short)smashArr[x]);
            }
            for(int x= 0; x < oldMins.length; x++)
            {
                writer.writeByte((byte)oldMins[x]);
                writer.writeByte((byte)oldMaxs[x]);
                writer.writeShort((short)oldArr[x]);
            }
            for(int x= 0; x < goodMins.length; x++)
            {
                writer.writeByte((byte)goodMins[x]);
                writer.writeByte((byte)goodMaxs[x]);
                writer.writeShort((short)goodArr[x]);
            }
            for(int x= 0; x < superMins.length; x++)
            {
                writer.writeByte((byte)superMins[x]);
                writer.writeByte((byte)superMaxs[x]);
                writer.writeShort((short)superArr[x]);
            }
            //writer.writeShorts(reverseBytes(outbreaks));
            writer.writeShorts(reverseBytes(intToShort(outbreaks)));
            writer.close();
        }
    }


    private void sort (File arr[])
    {
        Arrays.sort(arr, Comparator.comparingInt(JohtoEncounterEditor::fileToInt));
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
