package com.linseven.sender;

import java.util.Date;
import java.util.Scanner;

import com.linseven.cache.CacheCenter;
import com.linseven.model.Header;
import com.linseven.model.LoginInfo;
import com.linseven.model.Message;
import com.linseven.model.MessageType;
import com.linseven.model.UserInfo;
import com.linseven.session.Session;

import io.netty.channel.ChannelHandlerContext;

public class MessageSender
{
	private static ChannelHandlerContext ctx;
	private static  MessageSender instance;
    private MessageSender(){

	}
	public static  MessageSender getInstance(){

    	if(instance==null){
    		synchronized (MessageSender.class){
    			if(instance==null){
    				instance = new MessageSender();
				}
			}
		}
    	return instance;
	}

	public void setContext(ChannelHandlerContext context){
    	ctx = context;
	}
	public void sendMessage(Message msg){
    	if(ctx!=null) {
			ctx.writeAndFlush(msg);
		}
	}



}
