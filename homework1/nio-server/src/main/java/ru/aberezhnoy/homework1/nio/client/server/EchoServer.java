package ru.aberezhnoy.homework1.nio.client.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        new EchoServer().start();
    }

    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress("Localhost", 9001));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started");

        while (true) {
            selector.select();
            System.out.println("New selector event");
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    System.out.println("New selector acceptable event");
                    register(selector, serverSocket);
                }

                if (selectionKey.isReadable()) {
                    System.out.println("New selector readable event");
                    readMessage(selectionKey);
                }
                iterator.remove();
            }
        }
    }

    public void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        System.out.println("New client is connected");
    }

    public void readMessage(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        client.read(byteBuffer);
//        String readMessage = new String(byteBuffer.array());
        client.write(byteBuffer.flip());
        byteBuffer.clear();
/*
        String writeMessage = "ECHO: " + readMessage + LocalDateTime.now() + "\n";
        byteBuffer.put(writeMessage.getBytes());
        byteBuffer.flip();
        client.write(byteBuffer);

        byteBuffer.put(("[ECHO] " + readMessage.trim() + "\n").getBytes());
        byteBuffer.flip();
        client.write(byteBuffer);
        byteBuffer.clear();
*/
    }
}
