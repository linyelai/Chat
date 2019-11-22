package com.linseven.client;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.linseven.constant.NettyConstant;
import com.linseven.handler.ChatMsgHandler;
import com.linseven.handler.HeartBeatReqHandler;
import com.linseven.handler.LoginAuthReqHandler;
import com.linseven.handler.MessageDecoder;
import com.linseven.handler.MessageEncoder;
import com.linseven.sender.MessageSender;
import com.linseven.session.Session;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettyClient 
{
	

	public void connect(int port,String host)throws Exception
	{
		    EventLoopGroup group = new NioEventLoopGroup();
			Bootstrap boot = new Bootstrap();
			boot.group(group)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY,true)
			.handler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ch.pipeline().addLast(new MessageDecoder(1024*1024, 4, 4))
					.addLast("MessageEncoder", new MessageEncoder())
					//ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(5000));
					.addLast("LoginAuthReqHandler",new LoginAuthReqHandler() );
					ch.pipeline().addLast("HeartBeatReqHandler",new HeartBeatReqHandler());
					ch.pipeline().addLast("chatMsgHandler",new ChatMsgHandler());
				}
				
			});
			ChannelFuture future = boot.connect(new InetSocketAddress(host, port)).sync();
			if(future.isSuccess()){

				future.channel().closeFuture().sync();
				
				//return true;
			}


	}

}
