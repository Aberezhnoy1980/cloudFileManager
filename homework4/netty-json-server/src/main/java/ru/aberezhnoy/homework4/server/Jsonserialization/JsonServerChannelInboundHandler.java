package ru.aberezhnoy.homework4.server.Jsonserialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.aberezhnoy.homework4.commons.message.DownloadFileRequestMessage;
import ru.aberezhnoy.homework4.commons.message.FileMessage;
import ru.aberezhnoy.homework4.commons.message.Message;


import java.io.IOException;
import java.io.RandomAccessFile;

public class JsonServerChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

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
