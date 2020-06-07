package com.rance.chatui.reciever;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author linseven
 * @Date 2020/5/17
 */
public class MsgSender<T>  {

    private ChannelHandlerContext ctx ;

    public MsgSender(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }
    public void send(T msg){
        ctx.channel().writeAndFlush(msg);
    }
}
