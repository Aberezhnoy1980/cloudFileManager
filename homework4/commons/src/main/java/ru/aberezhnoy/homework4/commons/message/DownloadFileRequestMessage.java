package ru.aberezhnoy.homework4.commons.message;

public class DownloadFileRequestMessage extends Message {

    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
