package com.rance.chatui.reciever.handler;


import com.linseven.chatroom.model.MessageOuterClass;
import com.rance.chatui.reciever.context.ChatContext;

import org.greenrobot.eventbus.EventBus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author linseven
 * @Date 2020/5/18
 */
public class ChatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception
    {
        if(msg instanceof MessageOuterClass.Message){
            MessageOuterClass.Message  message =(MessageOuterClass.Message) msg;
            String friendUsername = message.getFrom();
            String currentChatFrienName = ChatContext.getContext().getCurrentChatFriendName();
            if(currentChatFrienName!=null&&currentChatFrienName.equals(friendUsername)) {
                System.out.println("from:" + message.getFrom() + ",to:" + message.getTo() + ",msg:" + message.getMsg());
                EventBus.getDefault().post(message);
            }else{
                ChatContext.getContext().addUnreadMsg(friendUsername,message);
            }

        }else{
            ctx.fireChannelRead(msg);
        }
    }

}