package com.linseven.handler;



import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.linseven.cache.CacheCenter;
import com.linseven.client.NettyClient;
import com.linseven.model.Friend;
import com.linseven.model.Header;
import com.linseven.model.LoginInfo;
import com.linseven.model.Message;
import com.linseven.model.MessageType;
import com.linseven.model.UserInfo;
import com.linseven.sender.MessageSender;
import com.linseven.session.Session;
import com.linseven.window.JJQQ;
import com.linseven.window.MainWindow;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

	private volatile ScheduledFuture heartBeat;
	public  static Integer timeoutCount=0;
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //���ͻ��˸������TCP�������ֳɹ�֮���ɿͻ��˹�������������Ϣ���͸������
        //ctx.writeAndFlush(buildLoginReq());
       MessageSender.getInstance().setContext(ctx);
        
    }

    // ����������֮�󣬰���Э��淶���������Ҫ��������Ӧ����Ϣ��
	 @Override
	 public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;

        // ���������Ӧ����Ϣ����Ҫ�ж��Ƿ���֤�ɹ�
        //������Ӧ����Ϣ���д��������ж���Ϣ�Ƿ�������Ӧ����Ϣ��
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP) 
        {
            UserInfo user = (UserInfo) message.getBody();
            if (user==null)
            {
                // ���������Ӧ����Ϣ�����Ӧ���������жϣ������0��˵����֤ʧ�ܣ��ر���·�����·������ӡ�
                // ����ʧ�ܣ��ر�����
                ctx.close();
            } 
           else 
           {

		    	Platform.runLater(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub

						if(JJQQ.stage!=null)
						{
							MainWindow mainWindow = new MainWindow(user);
							mainWindow.setVisible(true);
							mainWindow.addFriend(user.getFriends());
							Scene scene = new Scene(mainWindow.getRoot());
							JJQQ.stage.setScene(scene);
						}
						
					}});
		    	
		    	//������
		    	 heartBeat = ctx.executor().scheduleAtFixedRate(
		                    new LoginAuthReqHandler.HeartBeatTask(ctx), 0, 50000,
		                    TimeUnit.MILLISECONDS);
		            CacheCenter.getInstance().setCurrentUser(user);
		           
           }
        }
        else 
        {
            // ������ǣ�ֱ��͸���������ChannelHandler���д���
            ctx.fireChannelRead(msg);
        }
    }

    
	 private class HeartBeatTask implements Runnable 
	    {
	        private final ChannelHandlerContext ctx;

	        public HeartBeatTask(final ChannelHandlerContext ctx) {
	            this.ctx = ctx;
	        }

	        @Override
	        public void run()
	        {
	        	synchronized(timeoutCount){
	        	if(timeoutCount>5)
	        	{
	        		System.out.println("ʧȥ���ӣ��볢����������");
	        		ctx.channel().close();
	        	}
	        	else
	        	{
		            Message heatBeat = buildHeatBeat();
		            ctx.writeAndFlush(heatBeat);
		            timeoutCount++;
	        	}
	            
	        	}
	        	
	        }

	        private Message buildHeatBeat() 
	        {
	            Message message = new Message();
	            Header header = new Header();
	            header.setType(MessageType.HEART_BEAT_MSG);
	            header.setSendTime(new Date().getTime());
	            message.setHeader(header);
	            message.setBody("ping");
	            return message;
	        }
	    }
	 
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
    {
        ctx.fireExceptionCaught(cause);
        System.out.println("ʧȥ�����ˣ��볢����������");
    }

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);

	}
    
}
