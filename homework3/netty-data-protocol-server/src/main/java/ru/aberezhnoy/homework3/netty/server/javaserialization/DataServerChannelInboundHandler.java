package ru.aberezhnoy.homework3.netty.server.javaserialization;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ru.aberezhnoy.homework3.common.javaserialization.message.DateMessage;
import ru.aberezhnoy.homework3.common.javaserialization.message.Message;
import ru.aberezhnoy.homework3.common.javaserialization.message.NumberMessage;
import ru.aberezhnoy.homework3.common.javaserialization.message.TextMessage;

public class DataServerChannelInboundHandler extends SimpleChannelInboundHandler<Message> {

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
            var message = (TextMessage) msg;
            System.out.println("Incoming text message from client: " + message.getText());
        }

        if (msg instanceof DateMessage) {
            var message = (DateMessage) msg;
            System.out.println("Incoming date message from client: " + message.getDate());
        }
        if (msg instanceof NumberMessage) {
            var message = (NumberMessage) msg;
            System.out.println("Incoming number message from client: " + message.getNumber());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Catch cause " + cause.getMessage());
        ctx.close();
    }
}
