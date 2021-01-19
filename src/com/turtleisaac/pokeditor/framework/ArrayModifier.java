package com.turtleisaac.pokeditor.framework;

import java.util.Arrays;

public class ArrayModifier
{
    public static Object[][] trim(Object[][] arr, int rows, int cols)
    {
        arr= Arrays.copyOfRange(arr,rows,arr.length);

        for(int i= 0; i < arr.length; i++)
        {
            System.out.println(Arrays.toString(arr[i]));
            arr[i]= Arrays.copyOfRange(arr[i], cols, arr[i].length);
            System.out.println(Arrays.toString(arr[i]) + "\n");
        }

        return arr;
    }
}
