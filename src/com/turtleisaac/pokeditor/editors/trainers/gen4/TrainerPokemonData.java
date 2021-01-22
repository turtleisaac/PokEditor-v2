package com.turtleisaac.pokeditor.editors.trainers.gen4;

import java.util.ArrayList;

public interface TrainerPokemonData
{
    short getIvs();
    short getAbility();

    int getLevel();
    int getPokemon();
    int getAltForm();

    int getItem();

    int getMove1();
    int getMove2();
    int getMove3();
    int getMove4();

    short getBallCapsule();

//    public static ArrayList<TrainerPokemonData> create(String[] arr)
//    {
//        ArrayList<TrainerPokemonData> pokemonList= new ArrayList<>();
//
//
//    }
}
