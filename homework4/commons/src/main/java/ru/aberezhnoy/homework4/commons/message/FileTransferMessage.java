package ru.aberezhnoy.homework4.commons.message;

public class FileTransferMessage extends Message {
    private byte[] content;
    private long startPosition;
//    private boolean endOfFile = false;

//    public boolean getEndOfFile() {
//        return endOfFile;
//    }
//
//    public void setEndOfFile(boolean endOfFile) {
//        this.endOfFile = endOfFile;
//    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }
}
