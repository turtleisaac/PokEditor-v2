package com.turtleisaac.pokeditor.editors.trainers.gen4;

import com.jidesoft.swing.ComboBoxSearchable;
import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.narctowl.Narctowl;
import com.turtleisaac.pokeditor.project.Project;
import org.apache.commons.io.FileUtils;

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
    private String trainerTextOffsetFile;
    private String trainerTextOffsetUnpacked;
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
        trainerTextOffsetFile = project.getDataPath() + File.separator;
        trainerTextOffsetUnpacked = project.getProjectPath().getAbsolutePath() + File.separator + "temp" + File.separator + "trainer_msg_tbl";


        switch(project.getBaseRom())
        {
            case Platinum:
                trainerTextAssignmentFile += "poketool" + File.separator + "trmsg" + File.separator + "trtbl.narc";
                trainerTextOffsetFile += "poketool" + File.separator + "trmsg" + File.separator + "trtblofs.narc";
                break;

            case HeartGold:
            case SoulSilver:
                trainerTextAssignmentFile += "a" + File.separator + "0" + File.separator + "5" + File.separator + "7";
                trainerTextOffsetFile += "a" + File.separator + "1" + File.separator + "3" + File.separator + "1";
                break;
        }

        toDelete.add(new File(trainerTextAssignmentUnpacked));
        toDelete.add(new File(trainerTextOffsetUnpacked));
    }

    public ArrayList<TrainerText> getTrainerTexts() throws IOException
    {
        clearDirs(new File(trainerTextAssignmentUnpacked));

        if(!new File(trainerTextAssignmentUnpacked).exists())
        {
            Narctowl narctowl = new Narctowl(true);
            narctowl.unpack(trainerTextAssignmentFile, trainerTextAssignmentUnpacked);
        }
        trainerTexts = trainerTextRetriever.getTrainerText(trainerTextAssignmentUnpacked + File.separator + "0.bin");

        return trainerTexts;
    }

    public void writeTrainerTexts(ArrayList<TrainerText> trainerTexts, int numTrainers) throws IOException
    {
        Narctowl narctowl = new Narctowl(true);
        if(!new File(trainerTextOffsetUnpacked).exists())
        {
            narctowl.unpack(trainerTextOffsetFile, trainerTextOffsetUnpacked);
        }

        trainerTextRetriever.writeTrainerText(trainerTexts, numTrainers, trainerTextAssignmentUnpacked, trainerTextOffsetUnpacked);

        narctowl.pack(trainerTextAssignmentUnpacked,"",trainerTextAssignmentFile);
        narctowl.pack(trainerTextOffsetUnpacked,"",trainerTextOffsetFile);
    }

    private static void clearDirs(File folder)
    {
        try
        {
            FileUtils.deleteDirectory(folder);
        }
        catch(IOException e)
        {
            System.err.println("\tFailed to delete folder: " + folder.getAbsolutePath());
            e.printStackTrace();
        }

        if(folder.exists())
        {
            System.err.println("\tFolder wasn't deleted?");
        }
    }
}
