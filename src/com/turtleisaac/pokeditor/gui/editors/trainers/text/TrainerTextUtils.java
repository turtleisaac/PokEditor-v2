package com.turtleisaac.pokeditor.gui.editors.trainers.text;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TrainerTextUtils
{
    private Project project;
    private String projectPath;
    private String dataPath;
    private static String resourcePath;
    private static String[] trainerNames;
    private static String[] trainerClassData;

    private Game baseRom;

    public TrainerTextUtils(Project project, String projectPath, Game baseRom) throws IOException
    {
        this.project= project;
        this.projectPath= projectPath;
        this.baseRom= baseRom;
        dataPath= projectPath;
        resourcePath= projectPath;
        System.out.println(projectPath);
        if(projectPath.endsWith(File.separator + "data"))
        {
            resourcePath= projectPath.substring(0,projectPath.lastIndexOf(File.separator));
            resourcePath= resourcePath.substring(0,resourcePath.lastIndexOf(File.separator));
        }
        resourcePath+= File.separator + "Program Files" + File.separator;

        switch(project.getBaseRom())
        {
            case Diamond:
            case Pearl:
                trainerNames= TextEditor.getBank(project,559);
                trainerClassData= TextEditor.getBank(project,560);
                break;

            case Platinum:
                trainerNames= TextEditor.getBank(project,618);
                trainerClassData= TextEditor.getBank(project,619);
                break;

            case HeartGold:
            case SoulSilver:
                trainerNames= TextEditor.getBank(project,729);
                trainerClassData= TextEditor.getBank(project,730);
                break;
        }
    }

    public ArrayList<TrainerText> getTrainerText(String trainerTextFile)
    {
        Buffer buffer = new Buffer(trainerTextFile);
        ArrayList<TrainerText> textActivationList = new ArrayList<>();

        for(int i= 0; i < buffer.getLength()/4; i++)
        {
            int trainerId= buffer.readShort();
            int condition= buffer.readByte();
            buffer.skipBytes(1);

            int finalI = i;
            textActivationList.add(new TrainerText()
            {
                @Override
                public int getTextId()
                {
                    return finalI;
                }

                @Override
                public int getTrainerId()
                {
                    return trainerId;
                }

                @Override
                public int getCondition()
                {
                    return condition;
                }
            });
        }

        return textActivationList;
    }
}
