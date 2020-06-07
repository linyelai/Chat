package com.rance.chatui.reciever.handler;


import com.rance.chatui.reciever.context.ChatContext;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author linseven
 * @Date 2020/5/17
 */
public class ConnectionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        //将ctx加入缓存用于发送登录信息
        //登录
        ChatContext chatContext = ChatContext.getContext();
        chatContext.setChannelHandlerContext(ctx);
        synchronized (ChatContext.connectLock){
            ChatContext.getContext().setConnected(true);
        }

    }
}