package com.rance.chatui.reciever.handler;

import com.google.protobuf.MessageLite;
import com.linseven.chatroom.model.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 参考ProtobufVarint32LengthFieldPrepender 和 ProtobufEncoder
 */
@Sharable
public class CustomProtobufEncoder extends MessageToByteEncoder<MessageLite> {



    @Override
    protected void encode(ChannelHandlerContext ctx, MessageLite msg, ByteBuf out) throws Exception {


        byte[] body = msg.toByteArray();
        byte[] header = encodeHeader(msg, (short)body.length);

        out.writeBytes(header);
        out.writeBytes(body);

        return;
    }

    private byte[] encodeHeader(MessageLite msg, short bodyLength) {
        byte messageType = 0x0f;

        if (msg instanceof LoginMsgOuterClass.LoginMsg) {
            messageType = 0x00;
        } else if (msg instanceof LoginResponseMsgOuterClass.LoginResponseMsg) {
            messageType = 0x01;
        }else if (msg instanceof FriendsRequestOuterClass.FriendsRequest) {
            messageType = 0x02;
        }else if (msg instanceof FriendOuterClass.FriendList) {
            messageType = 0x03;
        }else if (msg instanceof UnReadMsgRequestOuterClass.UnReadMsgRequest) {
            messageType = 0x04;
        }else if (msg instanceof MessageOuterClass.Message) {
            messageType = 0x05;
        }

        byte[] header = new byte[4];
        header[0] = (byte) (bodyLength & 0xff);
        header[1] = (byte) ((bodyLength >> 8) & 0xff);
        header[2] = 0; // 保留字段
        header[3] = messageType;

        return header;

    }
}