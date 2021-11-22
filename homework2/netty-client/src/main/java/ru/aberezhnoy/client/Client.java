package ru.aberezhnoy.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            new Client().run();
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
                                    new LineBasedFrameDecoder(128), // пакет определятся по переносу строки
//                                    new LengthFieldBasedFrameDecoder(512,0,2,0,2),
//                                    new LengthFieldPrepender(2),
//                                    new ByteArrayDecoder(), // массив байтов в ByteBuf
//                                    new ByteArrayEncoder(),
//                                    new EchoServerStringDecoder(), // собственный декодер для преобразования байтов в строку
//                                    new EchoServerStringEncoder(), // собственный Encoder для преобразования строк в байты
                                    new StringEncoder(),
                                    new StringDecoder(),
                                    new SimpleChannelInboundHandler<String>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
                                            System.out.println(msg);
                                        }
                                    }
                            );
                        }
                    });
            System.out.println("Client started");

            Channel channel = bootstrap.connect("localhost", 9000).sync().channel(); // Sync потому что Future не гарантирует мгновенное исполнение и ставит в очередь задач

            try {
                while (true) {
                    String message = new Scanner(System.in).nextLine();
                    if (message.equalsIgnoreCase("/Exit")) {
                        break;
                    }
                    channel.writeAndFlush(message + System.lineSeparator());
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
