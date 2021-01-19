package com.turtleisaac.pokeditor.editors.trainers.gen4;

public interface TrainerPokemonData
{
    short getIvs();
    short getAbility();

    int getLevel();
    int getPokemon();
    int getAltForm();

    short getBallCapsule();

    interface TrainerPokemonData1 extends TrainerPokemonData
    {
        int getMove1();
        int getMove2();
        int getMove3();
        int getMove4();
    }

    interface TrainerPokemonData2 extends TrainerPokemonData
    {
        int getItem();
    }

    interface TrainerPokemonData3 extends TrainerPokemonData1
    {
        int getItem();
    }
}
