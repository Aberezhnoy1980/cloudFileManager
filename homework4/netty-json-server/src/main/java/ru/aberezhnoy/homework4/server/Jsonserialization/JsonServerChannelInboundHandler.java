package ru.aberezhnoy.homework4.server.Jsonserialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.aberezhnoy.homework4.commons.message.*;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.SQLOutput;

public class JsonServerChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

    private static final String FILE_NAME = "/Users/alex/Documents/Учеба/IDEA/network-storage/cloudFileManager/Apache_OpenOffice_4.1.11_MacOS_x86-64_langpack_ru.dmg";

    private static final int BUFFER_SIZE = 1024 * 64;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("Channel is registered");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.out.println("Channel is unregistered");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Channel is active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("Channel is inactive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        if (msg instanceof TextMessage) {
            TextMessage message = (TextMessage) msg;
            System.out.println("Incoming text message from client: " + message.getText());
            ctx.writeAndFlush(msg);
        }
        if (msg instanceof DateMessage) {
            var message = (DateMessage) msg;
            System.out.println("Incoming date message from client: " + message.getDate());
            ctx.writeAndFlush(msg);
        }
        if (msg instanceof RequestFileMessage) {
            try (var randomAccessFile = new RandomAccessFile(FILE_NAME, "r")) {
                final long fileLength = randomAccessFile.length();
                do {
                    var position = randomAccessFile.getFilePointer();

                    final long availableBytes = fileLength - position;
                    byte[] bytes;
//                    boolean lastFrame = false;

                    if (availableBytes >= BUFFER_SIZE) {
                        bytes = new byte[BUFFER_SIZE];
                    } else {
                        bytes = new byte[(int) availableBytes];
//                        lastFrame = true;
                    }

                    randomAccessFile.read(bytes);

                    final FileTransferMessage message = new FileTransferMessage();
                    message.setContent(bytes);
                    message.setStartPosition(position);
//                    message.setEndOfFile(lastFrame);

                    ctx.writeAndFlush(message);

                } while (randomAccessFile.getFilePointer() < fileLength);

                ctx.writeAndFlush(new EndFileTransferMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (msg instanceof DownloadFileRequestMessage) {
            var message = (DownloadFileRequestMessage) msg;
            try (RandomAccessFile accessFile = new RandomAccessFile(message.getPath(), "r")) {
                final FileMessage fileMessage = new FileMessage();
                byte[] content = new byte[(int) accessFile.length()];
                accessFile.read(content);
                fileMessage.setContent(content);
                ctx.writeAndFlush(fileMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Catch cause " + cause.getMessage());
        ctx.close();
    }
}
