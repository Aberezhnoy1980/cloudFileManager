package ru.aberezhnoy.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class EchoServerStringDecoder extends MessageToMessageDecoder<byte[]> {
    @Override
    protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) {
        out.add(new String(msg));
    }
}
