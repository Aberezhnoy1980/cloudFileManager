package ru.aberezhnoy.homework4.commons.javaserialization.message;

public class NumberMessage extends Message{
    private int number;

    public int getNumber () {
        return number;
    }

    public void setNumber (int number) {
        this.number = number;
    }
}
