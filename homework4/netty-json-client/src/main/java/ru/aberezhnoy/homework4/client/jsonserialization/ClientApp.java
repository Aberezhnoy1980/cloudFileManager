package ru.aberezhnoy.homework4.client.jsonserialization;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.aberezhnoy.homework4.commons.handler.JsonDecoder;
import ru.aberezhnoy.homework4.commons.handler.JsonEncoder;
import ru.aberezhnoy.homework4.commons.message.*;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.SocketChannel;

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
                                    new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 3, 0, 3),
                                    new LengthFieldPrepender(3),
                                    new JsonDecoder(),
                                    new JsonEncoder(),
                                    new SimpleChannelInboundHandler<Message>() {
                                        @Override
                                        protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
                                            if (msg instanceof TextMessage) {
                                                System.out.println("Incoming text message from server: " + ((TextMessage) msg).getText());
//                                                ctx.writeAndFlush(msg);
                                            }
                                            if (msg instanceof DateMessage) {
                                                System.out.println("Incoming date message from server: " + ((DateMessage) msg).getDate());
//                                                ctx.writeAndFlush(msg);
                                            }
                                            if (msg instanceof FileTransferMessage) {
                                                System.out.println("New incoming file transfer message");
                                                var message = (FileTransferMessage) msg;
                                                try (RandomAccessFile randomAccessFile = new RandomAccessFile("1", "rw")) {
                                                    randomAccessFile.seek(message.getStartPosition());
                                                    randomAccessFile.write(message.getContent());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (msg instanceof EndFileTransferMessage) {
                                                System.out.println("File transfer is finished");
                                                ctx.close();
                                            }
//                                            if (msg instanceof RequestFileMessage) {
//                                                try (var randomAccessFile = new RandomAccessFile(FILE_NAME, "r")) {
//                                                    final long fileLength = randomAccessFile.length();
//                                                    do {
//                                                        var position = randomAccessFile.getFilePointer();
//
//                                                        final long availableBytes = fileLength - position;
//                                                        byte[] bytes;
//
//                                                        if (availableBytes <= BUFFER_SIZE) {
//                                                            bytes = new byte[BUFFER_SIZE];
//                                                        } else {
//                                                            bytes = new byte[(int) availableBytes];
//                                                        }
//
//                                                        randomAccessFile.read(bytes);
//
//                                                        final FileTransferMessage message = new FileTransferMessage();
//                                                        message.setContent(bytes);
//                                                        message.setStartPosition(position);
//
//                                                        ctx.writeAndFlush(message);
//
//                                                    } while (randomAccessFile.getFilePointer() < fileLength);
//
//                                                    ctx.writeAndFlush(new EndFileTransferMessage());
//                                                } catch (IOException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }


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

            ChannelFuture channelFuture = bootstrap.connect("localhost", 9000).sync();
            channelFuture.channel().writeAndFlush(new RequestFileMessage());
            channelFuture.channel().closeFuture().sync();

//            DownloadFileRequestMessage message = new DownloadFileRequestMessage();
//            message.setPath("/Users/mac/Documents/Учеба/IDEA/network-storage/cloudFileManager/Lorem.html");
//            channel.writeAndFlush(message);

        } catch (InterruptedException e) {
            worker.shutdownGracefully();
        }
    }
}

