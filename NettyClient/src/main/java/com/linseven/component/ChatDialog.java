package com.linseven.component;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import com.linseven.cache.CacheCenter;
import com.linseven.controller.ChatWindowController;
import com.linseven.factory.MessageFactory;
import com.linseven.message.SendMessage;
import com.linseven.model.Message;

import com.linseven.sender.MessageSender;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import netscape.javascript.JSObject;

public class ChatDialog {
	
	private long dstUserId;//���͵��û���
	private Stage stage;//����
	private WebView webView;
	public ChatDialog(long dstUserId)
	{
		this.dstUserId = dstUserId;
		//��ʼ������Ի���
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/linseven/component/chatWindow1.fxml"));
		try {
			
			AnchorPane root = fxmlLoader.load();
			ObservableList<Node> child = root.getChildren();
		    webView = (WebView) child.get(0);
			URL url = ChatDialog.class.getResource("index.html");
			String urlStr = url.toExternalForm();
			WebEngine engine  = webView.getEngine();
			engine.load(urlStr);
			JSObject window = (JSObject) engine.executeScript("window");
			window.setMember("chatWindow", this);
			stage = new Stage();
			Scene chatScene = new Scene(root);
			stage.setScene(chatScene);
			stage.setTitle(String.valueOf(dstUserId));
			stage.show();
			//将未查看的信息进行展示
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
			//将未查看的信息进行展示
			List<Message> messageList = CacheCenter.getInstance().getMessages(dstUserId);
			readMsgList(messageList);
		}
	}
	public void sendMsg(Integer dstUserId,String msg)
	{
		Message<String> message = MessageFactory.buildTextMsg(dstUserId,msg);
		MessageSender.getInstance().sendMessage(message);

	}
	public void reciveMsg(Message msg)
	{
		CacheCenter.getInstance().addMessage(msg);
		/*if(msg!=null){
			Platform.runLater(new Runnable(){
				@Override
				public void run() {
					if(webView!=null) {
						WebEngine webEngine = webView.getEngine();
						JSObject win = (JSObject) webEngine.executeScript("window");
						String msgStr = msg.getBody().toString();
						win.eval("addMsg(" + msgStr + "')");
					}

				}});
			
		}*/
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
						win.eval("addMsg(" + msgStr + "')");
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

}
