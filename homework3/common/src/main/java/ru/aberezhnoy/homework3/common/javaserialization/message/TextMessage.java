package ru.aberezhnoy.homework3.common.javaserialization.message;

public class TextMessage extends Message {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
