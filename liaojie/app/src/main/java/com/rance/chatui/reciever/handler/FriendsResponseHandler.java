package com.rance.chatui.reciever.handler;

import com.linseven.chatroom.model.FriendOuterClass;
import com.rance.chatui.reciever.context.ChatContext;

import org.greenrobot.eventbus.EventBus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author linseven
 * @Date 2020/5/17
 */
public class FriendsResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception
    {
        if(msg instanceof FriendOuterClass.FriendList){
            FriendOuterClass.FriendList  friendList =(FriendOuterClass.FriendList) msg;
            //放入上下文中
            ChatContext.getContext().setFriendList(friendList);
            EventBus.getDefault().post(friendList);

        }else{
            ctx.fireChannelRead(msg);
        }
    }

}