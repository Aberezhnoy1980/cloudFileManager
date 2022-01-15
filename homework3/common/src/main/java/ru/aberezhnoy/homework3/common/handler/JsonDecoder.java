package ru.aberezhnoy.homework3.common.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import ru.aberezhnoy.homework3.common.message.Message;

import java.util.List;

public class JsonDecoder extends MessageToMessageDecoder<ByteBuf> {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        byte[] bytes = ByteBufUtil.getBytes(msg);
        System.out.println("new message as String: " + new String(bytes));
        Message message = OBJECT_MAPPER.readValue(bytes, Message.class);
        out.add(message);
    }
}
