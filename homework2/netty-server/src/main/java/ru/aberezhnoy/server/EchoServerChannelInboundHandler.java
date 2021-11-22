package ru.aberezhnoy.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class EchoServerChannelInboundHandler extends SimpleChannelInboundHandler<String> {

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
    protected void channelRead0(ChannelHandlerContext ctx, String msg) {
//        var incomingMessage = (String) msg; // Если наследуемся от ChannelInboundHandlerAdapter
        ctx.writeAndFlush("Echo: " + msg + "" + new Date() + "\n");
//        ctx.write(msg);
//        ctx.write(" ");
//        ctx.write(new Date().toString());
//        ctx.flush();
//        ReferenceCountUtil.release(msg); // Если наследуемся от ChannelInboundHandlerAdapter

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("Catch cause " + cause.getMessage());
        ctx.close();
    }
}
