package com.rance.chatui.reciever;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import com.rance.chatui.reciever.handler.ChatHandler;
import com.rance.chatui.reciever.handler.ConnectionHandler;
import com.rance.chatui.reciever.handler.CustomProtobufDecoder;
import com.rance.chatui.reciever.handler.CustomProtobufEncoder;
import com.rance.chatui.reciever.handler.FriendsResponseHandler;
import com.rance.chatui.reciever.handler.LoginAuthResponseHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Author linseven
 * @Date 2020/5/17
 */
public class Client {

    private String host;
    private int port;
    public Client(String host,int port){

        this.host = host;
        this.port = port;
    }
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    EventLoopGroup group = new NioEventLoopGroup();
    public void connect()
    {

            Bootstrap boot = new Bootstrap();
            boot.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 100000)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // TODO Auto-generated method stub
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ConnectionHandler());
                            pipeline.addLast(new CustomProtobufDecoder());
                            pipeline.addLast(new CustomProtobufEncoder());
                            pipeline.addLast(new LoginAuthResponseHandler());
                            pipeline.addLast(new FriendsResponseHandler());
                            pipeline.addLast(new ChatHandler());
                        }

                    });

        try {
            ChannelFuture future = boot.connect(new InetSocketAddress(host, port)).sync();
            if (future.isSuccess()) {
                future.channel().closeFuture().sync();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //	return false;
    }


}
