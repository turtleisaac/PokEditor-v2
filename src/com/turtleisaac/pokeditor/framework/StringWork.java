package com.turtleisaac.pokeditor.framework;

public class StringWork
{
    public static String appendLeadingZeros(int num, int len)
    {
        StringBuilder strBuilder = new StringBuilder("" + num);
        while (strBuilder.length() != len)
        {
            strBuilder.insert(0, "0");
        }
        return strBuilder.toString();
    }
}
