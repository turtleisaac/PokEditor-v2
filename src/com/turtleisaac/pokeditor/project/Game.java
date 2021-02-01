package com.turtleisaac.pokeditor.project;

public enum Game
{
    Diamond(
            new String[] {"Personal","TM Learnsets","Level-Up Learnsets","Evolutions","Tutor Move List","Tutor Move Compatibility","Baby Forms","Trainer Data","Trainer Pokemon","Moves","Items","Field Encounters","Water Encounters","Swarm/ Day/ Night Encounters","Poke Radar Encounters","Dual-Slot Mode Encounters"},
            new String[] {"Personal","Learnsets","Evolutions","Tutors","Babies","Trainers","Moves","Items","Encounters"}),

    Pearl(new String[] {"Personal","TM Learnsets","Level-Up Learnsets","Evolutions","Tutor Move List","Tutor Move Compatibility","Baby Forms","Trainer Data","Trainer Pokemon","Moves","Items","Field Encounters","Water Encounters","Swarm/ Day/ Night Encounters","Poke Radar Encounters","Dual-Slot Mode Encounters"},
            new String[] {"Personal","Learnsets","Evolutions","Tutors","Babies","Trainers","Moves","Items","Encounters"}),

    Platinum(new String[] {"Personal","TM Learnsets","Level-Up Learnsets","Evolutions","Tutor Move List","Tutor Move Compatibility","Baby Forms","Trainer Data","Trainer Pokemon","Moves","Items","Field Encounters","Water Encounters","Swarm/ Day/ Night Encounters","Poke Radar Encounters","Dual-Slot Mode Encounters"},
            new String[] {"Personal","Learnsets","Evolutions","Tutors","Babies","Trainers","Moves","Items","Encounters"}),

    HeartGold(new String[] {"Personal","TM Learnsets","Level-Up Learnsets","Evolutions","Baby Forms","Trainer Data","Trainer Pokemon","Moves","Items","Field Encounters","Water Encounters","Rock Smash Encounters","Mass-Outbreak Encounters","Sound Encounters"},
            new String[] {"Personal","Learnsets","Evolutions","Babies","Trainers","Moves","Items","Encounters"}),

    SoulSilver(new String[] {"Personal","TM Learnsets","Level-Up Learnsets","Evolutions","Baby Forms","Trainer Data","Trainer Pokemon","Moves","Items","Field Encounters","Water Encounters","Rock Smash Encounters","Mass-Outbreak Encounters","Sound Encounters"},
            new String[] {"Personal","Learnsets","Evolutions","Babies","Trainers","Moves","Items","Encounters"}),

    Black(new String[] {},
            new String[] {}),

    White(new String[] {},
            new String[] {}),

    Black2(new String[] {},
            new String[] {}),

    White2(new String[] {},
            new String[] {});


    public final String[] sheetList;
    public final String[] editorList;

    Game(String[] sheetList, String[] editorList)
    {
        this.sheetList= sheetList;
        this.editorList= editorList;
    }
}
