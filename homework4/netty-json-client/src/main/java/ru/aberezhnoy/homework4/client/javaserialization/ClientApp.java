package ru.aberezhnoy.homework4.client.javaserialization;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.aberezhnoy.homework4.commons.javaserialization.message.DateMessage;
import ru.aberezhnoy.homework4.commons.javaserialization.message.Message;
import ru.aberezhnoy.homework4.commons.javaserialization.message.NumberMessage;
import ru.aberezhnoy.homework4.commons.javaserialization.message.TextMessage;


import java.util.Date;

public class ClientApp {
    public static void main(String[] args) {
        try {
            new ClientApp().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() throws InterruptedException {
        final NioEventLoopGroup worker = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(512,0,2,0,2),
                                    new LengthFieldPrepender(2),
                                    new ObjectEncoder(),
                                    new ObjectDecoder(ClassResolvers.cacheDisabled(null)),
                                    new SimpleChannelInboundHandler<Message>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
//                                            System.out.println("Receive message" + msg);
                                        }
                                    }
                            );
                        }
                    });
            System.out.println("Client started");

            Channel channel = bootstrap.connect("localhost", 9000).sync().channel();
            try {
                while (true) {
                    TextMessage textMessage = new TextMessage();
                    textMessage.setText("Some text");
                    System.out.println("Try to send message: " + textMessage.getText());
                    channel.writeAndFlush(textMessage);

                    DateMessage dateMessage = new DateMessage();
                    dateMessage.setDate(new Date());
                    System.out.println("Try to send message: " + dateMessage.getDate());
                    channel.writeAndFlush(dateMessage);

                    NumberMessage numberMessage = new NumberMessage();
                    numberMessage.setNumber(45);
                    System.out.println("Try to send message: " + numberMessage.getNumber());
                    channel.writeAndFlush(numberMessage);

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                System.out.println("The message wasn't sent");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }
}

