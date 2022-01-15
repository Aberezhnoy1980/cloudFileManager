package ru.aberezhnoy.homework3.common.javaserialization.message;

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
