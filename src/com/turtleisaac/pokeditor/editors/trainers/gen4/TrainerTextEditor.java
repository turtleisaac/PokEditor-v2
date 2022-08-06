package com.turtleisaac.pokeditor.editors.trainers.gen4;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Project;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class TrainerTextEditor
{
    TrainerTextUtils trainerTextRetriever;
    private ArrayList<TrainerText> trainerTexts;
    private String trainerTextAssignmentFile;
    private String trainerTextAssignmentUnpacked;
    private ArrayList<File> toDelete;
    private String[] messages;

    private final Project project;

    public TrainerTextEditor(Project project) throws IOException
    {
        toDelete = new ArrayList<>();

        this.project= project;
        trainerTextRetriever = new TrainerTextUtils(project, project.getProjectPath().getAbsolutePath(), project.getBaseRom());
        trainerTextAssignmentFile = project.getDataPath() + File.separator;
        trainerTextAssignmentUnpacked = project.getProjectPath().getAbsolutePath() + File.separator + "temp" + File.separator + "trainer_msg";


        switch(project.getBaseRom())
        {
            case Platinum:
//                trainerTextBankId = 728;
                break;

            case HeartGold:
            case SoulSilver:
                trainerTextAssignmentFile += "a" + File.separator + "0" + File.separator + "5" + File.separator + "7";
                break;
        }

        if(!new File(trainerTextAssignmentUnpacked).exists())
        {
            Narctowl narctowl = new Narctowl(true);
            narctowl.unpack(trainerTextAssignmentFile, trainerTextAssignmentUnpacked);
        }
        trainerTexts = trainerTextRetriever.getTrainerText(trainerTextAssignmentUnpacked + File.separator + "0.bin");
        toDelete.add(new File(trainerTextAssignmentUnpacked));
    }

    public ArrayList<TrainerText> getTrainerTexts()
    {
        return trainerTexts;
    }
}
