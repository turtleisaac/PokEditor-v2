package com.turtleisaac.pokeditor.editors.text;

import com.turtleisaac.pokeditor.project.Game;

public enum TextBank
{
//    DP__NAMES(),
//    PLAT__NAMES(),
//    HGSS__NAMES()

    NULL_ENTRY(0),

    DP_ITEM_NAMES(344),
    PLAT_ITEM_NAMES(392),
    HGSS_ITEM_NAMES(222),

    DP_SPECIES_NAMES(362),
    PLAT_SPECIES_NAMES(412),
    HGSS_SPECIES_NAMES(237),

    DP_ABILITY_NAMES(552),
    PLAT_ABILITY_NAMES(610),
    HGSS_ABILITY_NAMES(720),

    DP_MOVE_NAMES(588),
    PLAT_MOVE_NAMES(647),
    HGSS_MOVE_NAMES(750),

    DP_TRAINER_NAMES(559),
    PLAT_TRAINER_NAMES(618),
    HGSS_TRAINER_NAMES(729),

    DP_TRAINER_CLASS_NAMES(560),
    PLAT_TRAINER_CLASS_NAMES(619),
    HGSS_TRAINER_CLASS_NAMES(730),

    DP_TRAINER_TEXT(0), //TODO change
    PLAT_TRAINER_TEXT(617), //TODO change
    HGSS_TRAINER_TEXT(728)
    ;

    public final int value;

    TextBank(int value)
    {
        this.value = value;
    }

    public static TextBank getBankID(TextBankTypes textBankType, Game game)
    {
        switch(game)
        {
            case Diamond:
            case Pearl:
                switch(textBankType)
                {
                    case ITEM_NAMES:
                        return DP_ITEM_NAMES;
                    case SPECIES_NAMES:
                        return DP_SPECIES_NAMES;
                    case ABILITY_NAMES:
                        return DP_ABILITY_NAMES;
                    case MOVE_NAMES:
                        return DP_MOVE_NAMES;
                    case TRAINER_NAMES:
                        return DP_TRAINER_NAMES;
                    case TRAINER_CLASS_NAMES:
                        return DP_TRAINER_CLASS_NAMES;
                    case TRAINER_TEXT:
                        return DP_TRAINER_TEXT;
                }
            case Platinum:
                switch(textBankType)
                {
                    case ITEM_NAMES:
                        return PLAT_ITEM_NAMES;
                    case SPECIES_NAMES:
                        return PLAT_SPECIES_NAMES;
                    case ABILITY_NAMES:
                        return PLAT_ABILITY_NAMES;
                    case MOVE_NAMES:
                        return PLAT_MOVE_NAMES;
                    case TRAINER_NAMES:
                        return PLAT_TRAINER_NAMES;
                    case TRAINER_CLASS_NAMES:
                        return PLAT_TRAINER_CLASS_NAMES;
                    case TRAINER_TEXT:
                        return PLAT_TRAINER_TEXT;
                }
            case HeartGold:
            case SoulSilver:
                switch(textBankType)
                {
                    case ITEM_NAMES:
                        return HGSS_ITEM_NAMES;
                    case SPECIES_NAMES:
                        return HGSS_SPECIES_NAMES;
                    case ABILITY_NAMES:
                        return HGSS_ABILITY_NAMES;
                    case MOVE_NAMES:
                        return HGSS_MOVE_NAMES;
                    case TRAINER_NAMES:
                        return HGSS_TRAINER_NAMES;
                    case TRAINER_CLASS_NAMES:
                        return HGSS_TRAINER_CLASS_NAMES;
                    case TRAINER_TEXT:
                        return HGSS_TRAINER_TEXT;
                }
            default:
                return NULL_ENTRY;
        }
    }

    public enum TextBankTypes
    {
        ITEM_NAMES,
        SPECIES_NAMES,
        ABILITY_NAMES,
        MOVE_NAMES,
        TRAINER_NAMES,
        TRAINER_CLASS_NAMES,
        TRAINER_TEXT
    }
}
