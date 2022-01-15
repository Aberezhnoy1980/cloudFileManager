package ru.aberezhnoy.homework4.commons.javaserialization.message;

public class TextMessage extends Message {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
