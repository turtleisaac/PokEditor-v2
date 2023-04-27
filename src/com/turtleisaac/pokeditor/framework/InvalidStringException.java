package com.turtleisaac.pokeditor.framework;

public class InvalidStringException extends RuntimeException
{
    public InvalidStringException(String error) {
        super(error);
    }
}
