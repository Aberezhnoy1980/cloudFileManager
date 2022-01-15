package ru.aberezhnoy.homework4.commons.message;

public class FileMessage extends Message {
    private byte [] content;

    public byte[] getContent() {
        return content;
    }

    public void setContent (byte[] content) {
        this.content = content;
    }
}
