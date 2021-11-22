package ru.aberezhnoy.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoServer {
    public static void main(String[] args) throws InterruptedException {
        new EchoServer().run();
    }

    public void run() throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1); // инициация новых подключений
        NioEventLoopGroup workersGroup = new NioEventLoopGroup(); // Обработка подключенных клиентов. Количество Threads = двойному количеству полученных процессов
        try {
            ServerBootstrap server = new ServerBootstrap() // конфигуратор сервера
                    .group(bossGroup, workersGroup)
                    .channel(NioServerSocketChannel.class) // Тип канала: TCP
                    // Обработчик создается при новом подключении. Метод initChannel - конфигурация клиента, говорит о том как клиент будет обрабатываться. Pipeline - последовательность изменений полученных на сервер байтов
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) {
                            ch.pipeline().addLast(
                                    new LineBasedFrameDecoder(128), // пакет определятся по переносу строки
//                                new LengthFieldBasedFrameDecoder(512,0,2,0,2),
//                                new LengthFieldPrepender(2), // пакет определятся по длине сообщения
//                                new ByteArrayDecoder(), // массив байтов в ByteBuf
//                                new ByteArrayEncoder(),
//                                new EchoServerStringDecoder(), // собственный декодер для преобразования байтов в строку
//                                new EchoServerStringEncoder(), // собственный Encoder для преобразования строк в байты
                                    new StringDecoder(), // преобразует входящий трафик байтов в строки
                                    new StringEncoder(), // преобразует исходящий трафик байтов в строки
                                    new EchoServerChannelInboundHandler()
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
