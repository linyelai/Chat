package com.linseven.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import com.linseven.component.ChatDialog;
import com.linseven.model.Friend;
import com.linseven.model.Message;
import com.linseven.model.UserInfo;

public class CacheCenter 
{
   private  static UserInfo currentUser;
  private  static CacheCenter instance = new CacheCenter();
  private static boolean isConnected = false;
  private static LinkedBlockingQueue<Message> msgQueue = new LinkedBlockingQueue<Message>();
  private static Map<Long,ChatDialog> currentDialog = new HashMap<Long,ChatDialog>();
  private static Map<Long,List<Message>>  messageCache = new HashMap<>();
  public static CacheCenter getInstance()
  {
	  if(instance==null)
	  {
		  synchronized(instance)
		  {
			  instance = new CacheCenter();
		  }
	  }
	  return instance;
  }


  public void addMessage(Message msg){
  	Long userId = msg.getHeader().getSourceUserId();
	  List<Message> messages = messageCache.get(userId);
	  if(messages==null){
	  	messages = new ArrayList<>();
	  }
	  messages.add(msg);
	  messageCache.put(userId,messages);
  }
  public List<Message> getMessages(Long userId){

  	return messageCache.get(userId);
  }
   
public  UserInfo getCurrentUser()
{
	return currentUser;
}

public  void setCurrentUser(UserInfo currentUser)
{
	CacheCenter.currentUser = currentUser;
}

public static boolean isConnected() {
	return isConnected;
}

public static void setConnected(boolean isConnected) {
	CacheCenter.isConnected = isConnected;
}

public Message pollMessage()
{
	return msgQueue.poll();
}

public void putMessage(Message msg)
{
	try 
	{
		msgQueue.put(msg);
		synchronized(CacheCenter.class)
		{
			CacheCenter.class.notifyAll();
		}
	}
	catch (InterruptedException e) 
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
 public  ChatDialog getDialogByUserId(Long userId){
	 
	 return currentDialog.get(userId);
 }
 
 public void addDialog(Long userId,ChatDialog dialog)
 {
	 currentDialog.put(userId,dialog);
 }
 
 public   String getFriendName(Long friendId)
 {
	 String friendName = null;
	 List<Friend> friends = currentUser.getFriends();
	 for(Friend friend:friends)
	 {
		 if(friend.getId().equals(friendId))
		 {
			 friendName = friend.getName();
			 break;
		 }
	 }
	 return friendName;
 }
 
 public   Long getFriendId(String friendName)
 {
	 Long friendId = null;
	 List<Friend> friends = currentUser.getFriends();
	 for(Friend friend:friends)
	 {
		 if(friend.getName()!=null&&friend.getName().equals(friendName))
		 {
			 friendId = friend.getId();
			 break;
		 }
	 }
	 return friendId;
 }

 public String getFriendAvatar(Long friendId){

  	List<Friend> friends = currentUser.getFriends();
  	if(friends!=null){
  		for(Friend friend :friends){
  			if(friend.getId().equals(friendId)){
  				return friend.getAvatar();
			}
		}
	}
	return null;
 }
 
}
