package ru.aberezhnoy.homework4.client.jsonserialization;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.aberezhnoy.homework4.commons.handler.JsonDecoder;
import ru.aberezhnoy.homework4.commons.handler.JsonEncoder;
import ru.aberezhnoy.homework4.commons.message.DownloadFileRequestMessage;
import ru.aberezhnoy.homework4.commons.message.FileMessage;
import ru.aberezhnoy.homework4.commons.message.Message;

import java.io.IOException;
import java.io.RandomAccessFile;

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
            Bootstrap bootstrap = new Bootstrap().group(worker).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 2, 0, 2), new LengthFieldPrepender(2), new JsonEncoder(), new JsonDecoder(), new SimpleChannelInboundHandler<Message>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
                            if (msg instanceof FileMessage) {
                                var message = (FileMessage) msg;
                                try (RandomAccessFile randomAccessFile = new RandomAccessFile("Task3", "rw")) {
                                    randomAccessFile.write(message.getContent());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                ctx.close();
                            }
                        }
                    });
                }
            });
            System.out.println("Client started");

            Channel channel = bootstrap.connect("localhost", 9000).sync().channel();

            DownloadFileRequestMessage message = new DownloadFileRequestMessage();
            message.setPath("/Users/mac/Documents/Учеба/IDEA/network-storage/cloudFileManager/Lorem.html");
            channel.writeAndFlush(message);

        } catch (InterruptedException e) {
            worker.shutdownGracefully();
        }
    }
}

