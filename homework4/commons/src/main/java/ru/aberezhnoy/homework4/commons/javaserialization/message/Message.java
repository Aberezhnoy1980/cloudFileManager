package ru.aberezhnoy.homework4.commons.javaserialization.message;

import java.io.Serializable;

public  abstract class Message implements Serializable {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
