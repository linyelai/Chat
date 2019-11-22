package com.linseven.server;

import java.io.IOException;
import com.linseven.constant.NettyConstant;
import com.linseven.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;



public class NettyServer {

    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    //  .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer() {
                        @Override
                        public void initChannel(Channel ch) throws IOException {
                            ch.pipeline().addLast(new MessageDecoder(1024 * 1024, 4, 4));
                             ch.pipeline().addLast(new MessageEncoder());
                            ch.pipeline().addLast(new LoginAuthRespHandler());
                            // ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                            // ch.pipeline().addLast(new LoginAuthRespHandler());
                             ch.pipeline().addLast("HeartBeatHandler",new HeartBeatRespHandler());
                                ch.pipeline().addLast("ChatMsgHandler",new ChatMsgHandler());

                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)          // (5)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

            // 绑定端口，同步等待成功
            ChannelFuture f = b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
            System.out.println("Netty server start ok : " + (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
            f.channel().closeFuture().sync();
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception
    {
        new NettyServer().bind();
    }
}