package com.rance.chatui.reciever.handler;


import com.google.protobuf.MessageLite;
import com.linseven.chatroom.model.FriendOuterClass;
import com.linseven.chatroom.model.FriendsRequestOuterClass;
import com.linseven.chatroom.model.LoginMsgOuterClass;
import com.linseven.chatroom.model.LoginResponseMsgOuterClass;
import com.linseven.chatroom.model.MessageOuterClass;
import com.linseven.chatroom.model.UnReadMsgRequestOuterClass;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 参考ProtobufVarint32FrameDecoder 和 ProtobufDecoder
 */

public class CustomProtobufDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 4) { // 如果可读长度小于包头长度，退出。
            in.markReaderIndex();

            // 获取包头中的body长度
            byte low = in.readByte();
            byte high = in.readByte();
            short s0 = (short) (low & 0xff);
            short s1 = (short) (high & 0xff);
            s1 <<= 8;
            short length = (short) (s0 | s1);

            // 获取包头中的protobuf类型
            in.readByte();
            byte dataType = in.readByte();

            // 如果可读长度小于body长度，恢复读指针，退出。
            if (in.readableBytes() < length) {
                in.resetReaderIndex();
                return;
            }

            // 读取body
            ByteBuf bodyByteBuf = in.readBytes(length);

            byte[] array;
            int offset;

            int readableLen= bodyByteBuf.readableBytes();
            if (bodyByteBuf.hasArray()) {
                array = bodyByteBuf.array();
                offset = bodyByteBuf.arrayOffset() + bodyByteBuf.readerIndex();
            } else {
                array = new byte[readableLen];
                bodyByteBuf.getBytes(bodyByteBuf.readerIndex(), array, 0, readableLen);
                offset = 0;
            }

            //反序列化
            MessageLite result = decodeBody(dataType, array, offset, readableLen);
            out.add(result);
        }
    }

    public MessageLite decodeBody(byte dataType, byte[] array, int offset, int length) throws Exception {
        if (dataType == 0x00) {
            return LoginMsgOuterClass.LoginMsg.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);

        } else if (dataType == 0x01) {
            return LoginResponseMsgOuterClass.LoginResponseMsg.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }
        else if (dataType == 0x02) {
            return FriendsRequestOuterClass.FriendsRequest.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }
        else if (dataType == 0x03) {
            return FriendOuterClass.FriendList.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }
        else if (dataType == 0x04) {
            return UnReadMsgRequestOuterClass.UnReadMsgRequest.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }
        else if (dataType == 0x05) {
            return MessageOuterClass.Message.getDefaultInstance().
                    getParserForType().parseFrom(array, offset, length);
        }



        return null; // or throw exception
    }
}