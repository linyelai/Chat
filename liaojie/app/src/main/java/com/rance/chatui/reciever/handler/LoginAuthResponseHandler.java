package com.rance.chatui.reciever.handler;

import com.linseven.chatroom.model.LoginResponseMsgOuterClass;
import com.rance.chatui.reciever.context.ChatContext;

import org.greenrobot.eventbus.EventBus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Author linseven
 * @Date 2020/5/17
 */
public class LoginAuthResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception
    {
        if(msg instanceof LoginResponseMsgOuterClass.LoginResponseMsg){
            LoginResponseMsgOuterClass.LoginResponseMsg  loginMsg =(LoginResponseMsgOuterClass.LoginResponseMsg) msg;
            //校验是否登录成功
            EventBus.getDefault().post(loginMsg);

        }else{
            ctx.fireChannelRead(msg);
        }
    }

}