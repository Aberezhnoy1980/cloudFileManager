package ru.aberezhnoy.homework3.common.javaserialization.message;

public class NumberMessage extends Message{
    private int number;

    public int getNumber () {
        return number;
    }

    public void setNumber (int number) {
        this.number = number;
    }
}
