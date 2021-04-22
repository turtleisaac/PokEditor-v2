package com.turtleisaac.pokeditor.gui.projects.projectwindow.sheets;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.project.Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FormatGenerator
{
    private static final String[] typeArr= {"Normal", "Fighting", "Flying", "Poison", "Ground", "Rock", "Bug", "Ghost", "Steel", "???", "Fire", "Water","Grass","Electric","Psychic","Ice","Dragon","Dark"};
    private static final String[] eggGroupArr= {"~","Monster","Water 1","Bug","Flying","Field","Fairy","Grass","Human-Like","Water 3","Mineral","Amorphous","Water 2","Ditto","Dragon","Undiscovered"};
    private static final String[] growthTableIdArr= {"Medium Fast","Erratic","Fluctuating","Medium Slow","Fast","Slow","Medium Fast","Medium Fast"};

    private static String[] nameData;
    private static String[] moveData;
    private static String[] itemData;
    private static String[] abilityData;
    private static String[] evolutionMethodArr;
    private static String[] effects;

    private static List<List<Object>> sheet;


    public static List<List<Object>> updateFormatSheet(Project project) throws IOException
    {
        sheet= new ArrayList<>();

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                nameData= TextEditor.getBank(project,362);
                moveData= TextEditor.getBank(project,588);
                itemData= TextEditor.getBank(project,344);
                abilityData= TextEditor.getBank(project,552);
                break;

            case Platinum:
                nameData= TextEditor.getBank(project,412);
                moveData= TextEditor.getBank(project,647);
                itemData= TextEditor.getBank(project,392);
                abilityData= TextEditor.getBank(project,610);
                break;

            case HeartGold:
            case SoulSilver:
                nameData= TextEditor.getBank(project,237);
                moveData= TextEditor.getBank(project,750);
                itemData= TextEditor.getBank(project,222);
                abilityData= TextEditor.getBank(project,720);
                break;
        }

        String resourcePath= project.getProjectPath().getAbsolutePath() + File.separator + "Program Files" + File.separator;

        BufferedReader reader= new BufferedReader(new FileReader(resourcePath + "EvolutionMethodsGen4.txt"));
        String line;
        ArrayList<String> evolutionList= new ArrayList<>();
        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            evolutionList.add(line);
        }
        evolutionMethodArr= evolutionList.toArray(new String[0]);
        reader.close();

        reader= new BufferedReader(new FileReader(resourcePath + "Effects.txt"));
        ArrayList<String> effectList= new ArrayList<>();

        while((line= reader.readLine()) != null)
        {
            line= line.trim();
            effectList.add(line);
        }
        effects= effectList.toArray(new String[0]);
        reader.close();


        String[] targets=new String[12];
        targets[0]= "One opponent";
        targets[1]= "Automatic";
        targets[2]= "Random";
        targets[3]= "Both opponents";
        targets[4]= "Both opponents and ally";
        targets[5]= "User";
        targets[6]= "User's side of field";
        targets[7]= "Entire field";
        targets[8]= "Opponent's side of field";
        targets[9]= "Automatic (fails if there is no ally)";
        targets[10]= "User or ally";
        targets[11]= "One opponent (fails if target faints)";


        generateReferenceList("Items",0);
        setColumn(evolutionMethodArr,1);
        setColumn(abilityData,2);
        setColumn(effects,3);
        setColumn(new String[] {"Physical","Special","Status"},4);
        setColumn(targets,5);
        setColumn(typeArr,6);
        setColumn(growthTableIdArr,7);
        generateReferenceList("Moves",8);

        return sheet;
    }

    private static void generateReferenceList(String sheetName, int colNum)
    {
        ArrayList<String> list= new ArrayList<>();
        for(int i= 0; i < 1000; i++)
        {
            list.add("=" + sheetName + "!B" + (i+2));
        }

        setColumn(list.toArray(new String[0]), colNum);
    }

    private static void setColumn(String[] column, int colNum)
    {
        while(sheet.size() < column.length)
        {
            sheet.add(new ArrayList<>());
        }

        for(int row= 0; row < column.length; row++)
        {
            while (sheet.get(row).size() < colNum+1)
            {
                sheet.get(row).add("");
            }

            sheet.get(row).set(colNum,column[row]);
        }
    }
}
