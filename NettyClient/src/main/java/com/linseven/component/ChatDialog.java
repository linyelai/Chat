package com.linseven.component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.linseven.cache.CacheCenter;
import com.linseven.factory.MessageFactory;
import com.linseven.model.Message;

import com.linseven.sender.MessageSender;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class ChatDialog {
	
	private Long dstUserId;//锟斤拷锟酵碉拷锟矫伙拷锟斤拷
	private Stage stage;//锟斤拷锟斤拷
	private WebView webView;
	private boolean isShow;
    private static ChatWindowController1 controller1;
	public ChatDialog(long dstUserId,String friendName)
	{
		this.dstUserId = dstUserId;
		//锟斤拷始锟斤拷锟斤拷锟斤拷曰锟斤拷锟�
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/linseven/component/chatWindow1.fxml"));
		try {
			AnchorPane root = fxmlLoader.load();
			ObservableList<Node> child = root.getChildren();
		    webView = (WebView) child.get(0);
		    
			URL url = ChatDialog.class.getResource("index.html");
			String urlStr = url.toExternalForm();
			WebEngine engine  = webView.getEngine();
			webView.setCache(true);
			engine.load(urlStr);

         /*   engine.getLoadWorker().stateProperty().addListener(
                    (ObservableValue<? extends Worker.State> ov, Worker.State oldState,
                     Worker.State newState) -> {

                        if (newState == Worker.State.SUCCEEDED) {
                            JSObject window = (JSObject) engine.executeScript("window");
                            window.setMember("controller", new ChatWindowController1(dstUserId));
                            window.setMember("friendName","rose");
                            window.setMember("userName",CacheCenter.getInstance().getCurrentUser().getUsername());
                            window.setMember("userAvatar",CacheCenter.getInstance().getCurrentUser().getAvatar());
                            window.setMember("friendAvatar",CacheCenter.getInstance().getFriendAvatar(dstUserId));
                        }
                    });*/

            controller1 =  new ChatWindowController1(dstUserId);
			JSObject window = (JSObject) engine.executeScript("window");
			window.setMember("controller",controller1);
			window.setMember("friendName",friendName);
			window.setMember("userName",CacheCenter.getInstance().getCurrentUser().getUsername());
			window.setMember("userAvatar",CacheCenter.getInstance().getCurrentUser().getAvatar());
			window.setMember("friendAvatar",CacheCenter.getInstance().getFriendAvatar(dstUserId));

			stage = new Stage();
			Scene chatScene = new Scene(root);
			stage.setScene(chatScene);
			stage.setTitle(String.valueOf(dstUserId));
			stage.show();
			isShow = true;
			//灏嗘湭鏌ョ湅鐨勪俊鎭繘琛屽睍绀�
			List<Message> messageList = CacheCenter.getInstance().getMessages(dstUserId);
			readMsgList(messageList);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void show(){

		if(stage!=null) {
			stage.sizeToScene();
			stage.toFront();
			stage.show();
			//灏嗘湭鏌ョ湅鐨勪俊鎭繘琛屽睍绀�
			List<Message> messageList = CacheCenter.getInstance().getMessages(dstUserId);
			readMsgList(messageList);
		}
	}
	public void sendMsg(String msg)
	{
		Message<String> message = MessageFactory.buildTextMsg(dstUserId,msg);
		MessageSender.getInstance().sendMessage(message);

	}
	public void reciveMsg(Message msg)
	{
		if(msg==null){
			return ;
		}
		Long destUserId  = msg.getHeader().getSourceUserId();
		ChatDialog dialog = CacheCenter.getInstance().getDialogByUserId(destUserId);
		dialog.readMsg(msg);

	}

	public void readMsg(Message msg){

		if(msg!=null){
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					if(webView!=null) {
						WebEngine webEngine = webView.getEngine();
						JSObject win = (JSObject) webEngine.executeScript("window");
						String msgStr = msg.getBody().toString();
						Long userId = msg.getHeader().getSourceUserId();
						String name = CacheCenter.getInstance().getFriendName(userId);
						win.eval("addSendMsg('" + msgStr.trim() + "')");
						System.out.println(msgStr);
					}

				}});

		}
	}

	public void readMsgList(List<Message> messageList){

		if(messageList!=null) {
			messageList.forEach(msg -> {
				readMsg(msg);
			});
		}
	}

	public boolean isShowing(){
		return this.isShow;
	}

	public static class ChatWindowController1 {

		private Long destUserId;


		private ChatWindowController1(Long destUserId){
			this.destUserId = destUserId;
		}
		public void sendMsg(String msg)
		{
			Message<String> message = MessageFactory.buildTextMsg(this.destUserId,msg);
			MessageSender.getInstance().sendMessage(message);

		}
	}

}
