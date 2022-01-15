package ru.aberezhnoy.homework3.netty.server.Jsonserialization;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import ru.aberezhnoy.homework3.common.handler.JsonDecoder;
import ru.aberezhnoy.homework3.common.handler.JsonEncoder;

public class ServerApp {
    public static void main(String[] args) {
        try {
            new ServerApp().run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workersGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap()
                    .group(bossGroup, workersGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LengthFieldBasedFrameDecoder(1024 * 1024, 0, 2, 0, 2),
                                    new LengthFieldPrepender(2),
                                    new JsonEncoder(),
                                    new JsonDecoder(),
                                    new JsonServerChannelInboundHandler()
                            );
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            Channel channel = server.bind(9000).sync().channel();

            System.out.println("Server started");

            channel.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workersGroup.shutdownGracefully();
        }
    }
}

