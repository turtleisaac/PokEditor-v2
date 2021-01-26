package com.turtleisaac.pokeditor.editors.trainers.gen4;

public interface TrainerDataGen4
{
    short getFlag();
    short getTrainerClass();
    short getBattleType();
    short getNumPokemon();

    int getItem1();
    int getItem2();
    int getItem3();
    int getItem4();

    long getAI();

    short getBattleType2();

    short getUnknown1();
    short getUnknown2();
    short getUnknown3();

    static TrainerDataGen4 create(String[] arr)
    {
        return new TrainerDataGen4()
        {
            @Override
            public short getFlag()
            {
                return 0;
            }

            @Override
            public short getTrainerClass()
            {
                return 0;
            }

            @Override
            public short getBattleType()
            {
                return 0;
            }

            @Override
            public short getNumPokemon()
            {
                return 0;
            }

            @Override
            public int getItem1()
            {
                return 0;
            }

            @Override
            public int getItem2()
            {
                return 0;
            }

            @Override
            public int getItem3()
            {
                return 0;
            }

            @Override
            public int getItem4()
            {
                return 0;
            }

            @Override
            public long getAI()
            {
                return 0;
            }

            @Override
            public short getBattleType2()
            {
                return 0;
            }

            @Override
            public short getUnknown1()
            {
                return 0;
            }

            @Override
            public short getUnknown2()
            {
                return 0;
            }

            @Override
            public short getUnknown3()
            {
                return 0;
            }
        };
    }
}
