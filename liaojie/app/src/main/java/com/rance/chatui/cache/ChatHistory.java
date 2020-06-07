package com.rance.chatui.cache;

import com.rance.chatui.enity.MessageInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatHistory {

    private static ChatHistory chatHistory = new ChatHistory();
    private static Map<String, List<MessageInfo>> historyCache = new HashMap<>();

    private  ChatHistory(){

    }
    public static ChatHistory getInstance(){
        return chatHistory;
    }
    public void addMessege(String friendUsername,MessageInfo message){
        List<MessageInfo> history = historyCache.get(friendUsername);
        if(history==null){
            history = new ArrayList<>();
        }
        history.add(message);
        historyCache.put(friendUsername,history);
    }
    public List<MessageInfo> getChatHistory(String friendUsername){

        List<MessageInfo> messageInfos = historyCache.get(friendUsername);
        if(messageInfos==null){
            messageInfos = new ArrayList<>();
        }
        historyCache.put(friendUsername,messageInfos);
        return messageInfos;
    }

}
