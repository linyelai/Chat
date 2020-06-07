package com.rance.chatui.reciever.context;

import android.app.Activity;
import android.content.Context;

import com.linseven.chatroom.model.FriendOuterClass;
import com.linseven.chatroom.model.MessageOuterClass;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.reciever.Client;
import com.rance.chatui.ui.activity.IMActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;

/**
 * @Author linseven
 * @Date 2020/5/17
 */
public class ChatContext {
    private static ChatContext context =  new ChatContext();
    private static ChannelHandlerContext ctx;
    private static FriendOuterClass.FriendList friendList;
    public  static Object isLoginLock = new Object();
    public  static  Object isGetFriendsLock = new Object();
    public  static  Object connectLock = new Object();
    private static boolean isconncted = false;
    private final static String  host= "192.168.43.17";
    private final static int port = 6666;
    private static Client client  =  new Client(host,port);
    private static Map<String, List<MessageOuterClass.Message>> unReadMsgs = new HashMap<String, List<MessageOuterClass.Message>>();
    private static String currentChatFriendName = "";
    private static Map<String,Activity> dialogs = new HashMap<>();
    private Context appContext;
    public  Client getClient(){
        return client;
    }



    private  static  String username ;
    private   ChatContext(){

    }
    public static ChatContext getContext(){
        return context;
    }

    public ChannelHandlerContext getChannelHandlerContext(){
        return ctx;
    }
    public void  setChannelHandlerContext(ChannelHandlerContext ctx1){
         ctx = ctx1;
    }
    public void setFriendList(FriendOuterClass.FriendList friendList1){

        friendList = friendList1;
    }
    public FriendOuterClass.FriendList getFriendList(){
        return friendList;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username1){
        username =  username1;
    }

    public boolean isConnected(){
        return isconncted;
    }
    public void setConnected(boolean connected){
        isconncted = connected;
    }

    public void addUnreadMsg(String friedName, MessageOuterClass.Message messageInfo){
        List<MessageOuterClass.Message> unreadMsg = unReadMsgs.get(friedName);
        if(unreadMsg==null){
            unreadMsg = new ArrayList<>();
        }
        unreadMsg.add(messageInfo);
        unReadMsgs.put(friedName,unreadMsg);
    }

    public List<MessageOuterClass.Message> getUnReadMsg(String friendUsername){

        return unReadMsgs.get(friendUsername);
    }
    public void clearUnReadMsg(String friendName){
        List<MessageOuterClass.Message> unreadMsgs =unReadMsgs.get(friendName);
        if(unreadMsgs!=null&&unreadMsgs.size()>0){
            unreadMsgs.clear();
        }

    }
    public void setCurrentChatFriendName(String friendName){
        currentChatFriendName = friendName;
    }
    public String getCurrentChatFriendName(){
        return currentChatFriendName;
    }

    public Context getAppContext(){
        return appContext;
    }
    public void setAppContext(Context context){
        appContext = context;
    }

    public void addDialog(String friendName,Activity activity){
        dialogs.put(friendName,activity );
    }
    public Activity getDialog(String friendName){

        return dialogs.get(friendName);
    }
}
