package com.turtleisaac.pokeditor.editors.trainers.gen4;

import com.turtleisaac.pokeditor.editors.text.TextEditor;
import com.turtleisaac.pokeditor.framework.BinaryWriter;
import com.turtleisaac.pokeditor.framework.Buffer;
import com.turtleisaac.pokeditor.framework.MemBuf;
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

        for (int i = 0; i < buffer.getLength()/4; i++)
        {
            int trainerId = buffer.readShort();
            int condition = buffer.readByte();
            buffer.skipBytes(1);

            int textId = i;
            textActivationList.add(new TrainerText()
            {
                @Override
                public int getTextId()
                {
                    return textId;
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

                @Override
                public String getText()
                {
                    return null;
                }
            });
        }

        return textActivationList;
    }

    public void writeTrainerText(ArrayList<TrainerText> trainerTexts, int numTrainers, String trainerTextDir, String trainerTextTableDir) throws IOException
    {
        MemBuf trainerTextBuf = MemBuf.create();
        MemBuf.MemBufWriter trainerTextWriter = trainerTextBuf.writer();

        MemBuf trainerTextOffsetBuf = MemBuf.create();
        MemBuf.MemBufWriter trainerTextOffsetWriter = trainerTextOffsetBuf.writer();

        for (TrainerText text : trainerTexts)
        {
            trainerTextWriter.writeShort((short) text.getTrainerId());
            trainerTextWriter.writeByte((byte) text.getCondition());
            trainerTextWriter.writeByte((byte) 0);
        }

        int lengthOfBuffer = trainerTextBuf.reader().getBuffer().length;

        ArrayList<Integer> alreadyWrittenTrainers = new ArrayList<>();
        MemBuf.MemBufReader trainerTextReader = trainerTextBuf.reader();

        for (int i = 0; i < numTrainers; i++)
        {
            trainerTextReader.setPosition(0);
            for (int j = 0; j < lengthOfBuffer / 4; j++)
            {
                int trainerId = trainerTextReader.readShort();
                trainerTextReader.skip(2);

                if (trainerId == i && !alreadyWrittenTrainers.contains(trainerId))
                {
                    alreadyWrittenTrainers.add(trainerId);
                    trainerTextOffsetWriter.writeShort((short) (trainerTextReader.getPosition() - 4));
                }
            }

            if (!alreadyWrittenTrainers.contains(i))
            {
                trainerTextOffsetWriter.writeShort((short) 0);
                alreadyWrittenTrainers.add(i);
            }
        }

        trainerTextReader.setPosition(0);

        BinaryWriter writer = new BinaryWriter(trainerTextDir + File.separator + "0.bin");
        writer.write(trainerTextReader.getBuffer());
        writer.close();

        writer = new BinaryWriter(trainerTextTableDir + File.separator + "0.bin");
        writer.write(trainerTextOffsetBuf.reader().getBuffer());
        writer.close();
    }
}
