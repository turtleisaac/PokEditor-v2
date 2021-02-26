package com.turtleisaac.pokeditor.utilities;

import com.turtleisaac.pokeditor.editors.encounters.johto.JohtoEncounterEditor;
import com.turtleisaac.pokeditor.editors.encounters.johto.JohtoEncounterReturn;
import com.turtleisaac.pokeditor.editors.evolutions.gen4.EvolutionEditor;
import com.turtleisaac.pokeditor.editors.narctowl.Narctowl;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerEditorGen4;
import com.turtleisaac.pokeditor.editors.trainers.gen4.TrainerReturnGen4;
import com.turtleisaac.pokeditor.project.Game;
import com.turtleisaac.pokeditor.project.Project;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class RandomizerUtils
{
    private final Project project;
    private final Game game;
    private final String dataPath;

    private int numSpecies;
    private final ArrayList<File> toDelete;

    public RandomizerUtils(Project project)
    {
        this.project= project;
        this.game= project.getBaseRom();
        dataPath= project.getProjectPath().getAbsolutePath() + File.separator + project.getName() + File.separator + "data";
        toDelete= new ArrayList<>();

        numSpecies= Narctowl.getNumFiles(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4");

        if(numSpecies > 508) //Basic Gen 4 amount
        {
            numSpecies= Integer.parseInt(JOptionPane.showInputDialog("What is the highest index number used by pokemon in your ROM? (Mikelan's Gen V dex expansion patch has a maximum value of 699)")) + 1;
        }
    }

    public void randomizeEncounters() throws IOException
    {
        Narctowl narctowl= new Narctowl(true);
        JohtoEncounterEditor editor= new JohtoEncounterEditor(project, dataPath);
        JohtoEncounterReturn encounterReturn;
        String narcPath;
        String dirPath;

        switch (game)
        {
            case HeartGold:
                narcPath=  File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7";
                dirPath= File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "7_";
                break;

            case SoulSilver:
                narcPath= File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6";
                dirPath= File.separator + "a" + File.separator + "1" + File.separator + "3" + File.separator + "6_";
                break;

            default:
                throw new RuntimeException("Game not supported yet");
        }

        narctowl.unpack(dataPath + narcPath,dataPath + dirPath);
        encounterReturn= editor.encountersToSheet(dirPath);

        Object[][] field= encounterReturn.getField();
        Object[] fieldHeader= field[0];
        field= Arrays.copyOfRange(field,1,field.length);

        Object[][] water= encounterReturn.getWater();
        Object[] waterHeader= water[0];
        water= Arrays.copyOfRange(water,1,water.length);

        Object[][] smash= encounterReturn.getSmash();
        Object[] smashHeader= smash[0];
        smash= Arrays.copyOfRange(smash,1,smash.length);

        Object[][] outbreak= encounterReturn.getOutbreak();
        Object[] outbreakHeader= outbreak[0];
        outbreak= Arrays.copyOfRange(outbreak,1,outbreak.length);

        Object[][] sound= encounterReturn.getSound();
        Object[] soundHeader= sound[0];
        sound= Arrays.copyOfRange(sound,1,sound.length);


        for(int row= 0; row < field.length; row++)
        {
            if(row % 13 != 0)
            {
                field[row][3]= "" + getRandom();
                field[row][4]= "" + getRandom();
                field[row][5]= "" + getRandom();
            }
        }

        for(int row= 0; row < water.length; row++)
        {
            if(row % 6 != 0)
            {
                water[row][3]= "" + getRandom();
                water[row][6]= "" + getRandom();
                water[row][9]= "" + getRandom();
                water[row][12]= "" + getRandom();
            }
        }

        for(int row= 0; row < smash.length; row++)
        {
            if(row % 3 != 0)
            {
                smash[row][3]= "" + getRandom();
            }
        }

        for(int row= 0; row < outbreak.length; row++)
        {
            if(row % 2 != 0)
            {
                outbreak[row][2]= "" + getRandom();
                outbreak[row][3]= "" + getRandom();
                outbreak[row][4]= "" + getRandom();
                outbreak[row][5]= "" + getRandom();
            }
        }

        for(int row= 0; row < sound.length; row++)
        {
            if(row % 3 != 0)
            {
                sound[row][2]= "" + getRandom();
                sound[row][3]= "" + getRandom();
            }
        }

        Object[][] fieldTable= new Object[field.length+1][];
        fieldTable[0]= fieldHeader;
        System.arraycopy(field,0,fieldTable,1, field.length);

        Object[][] waterTable= new Object[water.length+1][];
        waterTable[0]= waterHeader;
        System.arraycopy(water,0,waterTable,1, water.length);

        Object[][] smashTable= new Object[smash.length+1][];
        smashTable[0]= smashHeader;
        System.arraycopy(smash,0,smashTable,1, smash.length);

        Object[][] outbreakTable= new Object[outbreak.length+1][];
        outbreakTable[0]= outbreakHeader;
        System.arraycopy(outbreak,0,outbreakTable,1, outbreak.length);

        Object[][] soundTable= new Object[sound.length+1][];
        soundTable[0]= soundHeader;
        System.arraycopy(sound,0,soundTable,1, sound.length);

        editor.sheetsToEncounters(fieldTable,waterTable,smashTable,outbreakTable,soundTable,"");
        narctowl.pack(dataPath + dirPath,"",dataPath + narcPath);
        toDelete.add(new File(dataPath + dirPath));

        for(File file : toDelete)
            clearDirs(file);

        toDelete.clear();
    }

    public void randomizeTrainerPokemon() throws IOException
    {
        Narctowl narctowl= new Narctowl(true);
        TrainerEditorGen4 trainerEditor= new TrainerEditorGen4(project,dataPath,game);

        String dataNarcPath=  File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "5";
        String dataDirPath= File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "5_";
        String teamNarcPath=  File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "6";
        String teamDirPath= File.separator + "a" + File.separator + "0" + File.separator + "5" + File.separator + "6_";

        narctowl.unpack(dataPath + dataNarcPath,dataPath + dataDirPath);
        narctowl.unpack(dataPath + teamNarcPath,dataPath + teamDirPath);
        toDelete.add(new File(dataPath + dataDirPath));
        toDelete.add(new File(dataPath + teamDirPath));

        TrainerReturnGen4 trainerReturn= trainerEditor.trainersToSheets(dataDirPath,teamDirPath);

        Object[][] data= trainerReturn.getTrainerData();
        Object[] dataHeader= data[0];
        data= Arrays.copyOfRange(data,1,data.length);

        Object[][] pokemon= trainerReturn.getTrainerPokemon();
        Object[] pokemonHeader= pokemon[0];
        pokemon= Arrays.copyOfRange(pokemon,1,pokemon.length);

        for(int i= 0; i < data.length; i++)
        {
            data[i][2]= "FALSE";

            pokemon[i][5]= "" + getRandom();
            pokemon[i][6]= "" + 0;
            pokemon[i][12]= "" + (int) (Math.random()*28);

            pokemon[i][16]= "" + getRandom();
            pokemon[i][17]= "" + 0;
            pokemon[i][23]= "" + (int) (Math.random()*28);

            pokemon[i][27]= "" + getRandom();
            pokemon[i][28]= "" + 0;
            pokemon[i][34]= "" + (int) (Math.random()*28);

            pokemon[i][38]= "" + getRandom();
            pokemon[i][39]= "" + 0;
            pokemon[i][45]= "" + (int) (Math.random()*28);

            pokemon[i][49]= "" + getRandom();
            pokemon[i][50]= "" + 0;
            pokemon[i][56]= "" + (int) (Math.random()*28);

            pokemon[i][60]= "" + getRandom();
            pokemon[i][61]= "" + 0;
            pokemon[i][67]= "" + (int) (Math.random()*28);
        }

        Object[][] dataTable= new Object[data.length+1][];
        dataTable[0]= dataHeader;
        System.arraycopy(data,0,dataTable,1, data.length);

        Object[][] pokemonTable= new Object[pokemon.length+1][];
        pokemonTable[0]= pokemonHeader;
        System.arraycopy(pokemon,0,pokemonTable,1, pokemon.length);

        trainerEditor= new TrainerEditorGen4(project,dataPath,game);
        trainerEditor.sheetsToTrainers(dataTable,pokemonTable,File.separator + "a" + File.separator + "0" + File.separator + "5");

        narctowl.pack(dataPath + dataDirPath,"",dataPath + dataNarcPath);
        narctowl.pack(dataPath + teamDirPath,"",dataPath + teamNarcPath);


        for(File file : toDelete)
            clearDirs(file);

        toDelete.clear();
    }

    public void randomizeEvolutionsEveryLevel() throws IOException
    {
        EvolutionEditor editor= new EvolutionEditor(project, dataPath, game);
        Narctowl narctowl= new Narctowl(true);
        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_");
        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_"));

        Object[][] evolutions= editor.evolutionsToSheet(File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_",false);
        Object[] header= Arrays.copyOf(evolutions,1);
        evolutions= Arrays.copyOfRange(evolutions,1,evolutions.length-1);

        for(int row= 1; row < numSpecies; row++)
        {
            if((row < 494 || row > 543) && row != 132)
            {
                Object[] rowPrefix= Arrays.copyOf(evolutions[row],2);
                evolutions[row]= Arrays.copyOf(evolutions[0],evolutions[0].length);
                System.arraycopy(rowPrefix,0,evolutions[row],0,2);

                evolutions[row][2]= "Level Up";
                evolutions[row][3]= "1";
                evolutions[row][4]= "" + getRandom();

                evolutions[row][5]= "Use Item";
                evolutions[row][6]= "Thunderstone";
                evolutions[row][7]= "" + getRandom();
            }
            else if(row == 132) //Ditto
            {
                int idx= 2;

                evolutions[row][idx++]= "Use Item";
                evolutions[row][idx++]= "Thunderstone";
                evolutions[row][idx++]= "" + getRandom();

                evolutions[row][idx++]= "Use Item";
                evolutions[row][idx++]= "Water Stone";
                evolutions[row][idx++]= "" + getRandom();

                evolutions[row][idx++]= "Use Item";
                evolutions[row][idx++]= "Fire Stone";
                evolutions[row][idx++]= "" + getRandom();

                evolutions[row][idx++]= "Use Item";
                evolutions[row][idx++]= "Sun Stone";
                evolutions[row][idx++]= "" + getRandom();

                evolutions[row][idx++]= "Use Item";
                evolutions[row][idx++]= "Moon Stone";
                evolutions[row][idx++]= "" + getRandom();

                evolutions[row][idx++]= "Use Item";
                evolutions[row][idx++]= "Leaf Stone";
                evolutions[row][idx++]= "" + getRandom();

                evolutions[row][idx++]= "Level Up";
                evolutions[row][idx++]= "1";
                evolutions[row][idx]= "" + getRandom();
            }
        }

        Object[][] table= new Object[evolutions.length+1][];
        table[0]= header;
        System.arraycopy(evolutions,0,table,1,evolutions.length);


        editor= new EvolutionEditor(project, dataPath, game);
        editor.sheetToEvolutions(table,File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_");
        narctowl.pack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_","",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4");


        for(File file : toDelete)
            clearDirs(file);

        toDelete.clear();
    }

    public void randomizeEvolutions() throws IOException
    {
        EvolutionEditor editor= new EvolutionEditor(project, dataPath, game);
        Narctowl narctowl= new Narctowl(true);
        narctowl.unpack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_");
        toDelete.add(new File(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_"));

        Object[][] evolutions= editor.evolutionsToSheet(File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_",false);
        Object[] header= Arrays.copyOf(evolutions,1);
        evolutions= Arrays.copyOfRange(evolutions,1,evolutions.length-1);

        for(int row= 1; row < numSpecies; row++)
        {
            if((row < 494 || row > 543))
            {
                Object[] rowPrefix= Arrays.copyOf(evolutions[row],2);
                evolutions[row]= Arrays.copyOf(evolutions[0],evolutions[0].length);
                System.arraycopy(rowPrefix,0,evolutions[row],0,2);

                for(int col= 2; col < 23 && !evolutions[row][col].equals("None"); col+= 3)
                {
                    if(evolutions[row][col].equals("Trade"))
                    {
                        evolutions[row][col]= "Level Up";
                        evolutions[row][col+1]= "38";
                    }
                    else if(evolutions[row][col].equals("Trade (Item)"))
                    {
                        evolutions[row][col]= "Use Item (Day)";
                        evolutions[row][col+3]= "Use Item (Night)";
                        evolutions[row][col+4]= evolutions[row][col+1];
                        evolutions[row][col+5]= evolutions[row][col+2];
                    }

                    evolutions[row][col+2]= "" + getRandom();
                }
            }
        }

        Object[][] table= new Object[evolutions.length+1][];
        table[0]= header;
        System.arraycopy(evolutions,0,table,1,evolutions.length);


        editor= new EvolutionEditor(project, dataPath, game);
        editor.sheetToEvolutions(table,File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_");
        narctowl.pack(dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4_","",dataPath + File.separator + "a" + File.separator + "0" + File.separator + "3" + File.separator + "4");


        for(File file : toDelete)
            clearDirs(file);

        toDelete.clear();
    }

    /**
     * Function for generating a random number representing the index of a species in the personal narc
     * numSpecies - the number of files in the personal narc OR the maximum slot number to be allowed, depending on narc size and dex expansion status (see constructor)
     * @return a random number between 1 and numSpecies, inclusive; however, the range 494-543, inclusive, is not permittable due to the nature of current dex expansion techniques (is taken up by eggs, alt forms, and their sprites for now)
     */
    private int getRandom()
    {
        int species;
        do
        {
            species= (int) (Math.random()*numSpecies);
        }
        while (species == 0 || (species >= 494 && species <= 543));

        return species;
    }

    public static void clearDirs(File folder)
    {
        for(File f : Objects.requireNonNull(folder.listFiles()))
        {
            if(f.isDirectory())
                clearDirs(f);
            else
                f.delete();
        }
        folder.delete();
    }
}
