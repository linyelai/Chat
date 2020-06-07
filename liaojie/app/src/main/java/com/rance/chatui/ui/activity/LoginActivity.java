package com.rance.chatui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.linseven.chatroom.model.FriendOuterClass;
import com.linseven.chatroom.model.FriendsRequestOuterClass;
import com.linseven.chatroom.model.LoginMsgOuterClass;
import com.linseven.chatroom.model.LoginResponseMsgOuterClass;
import com.linseven.chatroom.model.UnReadMsgRequestOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.reciever.MessageReciever;
import com.rance.chatui.reciever.MsgSender;
import com.rance.chatui.reciever.context.ChatContext;
import com.rance.chatui.tabview.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.netty.channel.ChannelHandlerContext;

public class LoginActivity extends AppCompatActivity {

    EditText usernameText;
    EditText passwordText;
    Button loginBtn ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无效

        setContentView(R.layout.login);
        EventBus.getDefault().register(this);
        usernameText = (EditText)findViewById(R.id.username);
        passwordText = (EditText)findViewById(R.id.password);
        loginBtn = (Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    public void login(){

        if(ChatContext.getContext().isConnected()) {
            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();
            LoginMsgOuterClass.LoginMsg loginMsg = LoginMsgOuterClass.LoginMsg.newBuilder().setUsername(username).setPassword(password).build();
            ChannelHandlerContext ctx = ChatContext.getContext().getChannelHandlerContext();
            ChatContext.getContext().setUsername(username);
            MsgSender sender = new MsgSender(ctx);
            sender.send(loginMsg);
        }else{
            Toast.makeText(this, "网络异常,请重新登录", Toast.LENGTH_SHORT).show();
            ChatContext.getContext().getClient().connect();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final LoginResponseMsgOuterClass.LoginResponseMsg  loginResponseMsg) {

        if(loginResponseMsg!=null&&loginResponseMsg.getIsSuccess()){
            getFriendList(usernameText.getText().toString());
            Intent i=new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
    public  void getFriendList(String username){
        ChannelHandlerContext ctx = ChatContext.getContext().getChannelHandlerContext();
        MsgSender<FriendsRequestOuterClass.FriendsRequest> sender = new MsgSender<>(ctx);
        FriendsRequestOuterClass.FriendsRequest friendsRequest = FriendsRequestOuterClass.FriendsRequest.newBuilder()
                .setUsername(username).setType(1).build();
        sender.send(friendsRequest);
    }


    public void getUnReadMsg(String username){

        ChannelHandlerContext ctx = ChatContext.getContext().getChannelHandlerContext();
        MsgSender<UnReadMsgRequestOuterClass.UnReadMsgRequest> sender = new MsgSender<>(ctx);
        UnReadMsgRequestOuterClass.UnReadMsgRequest unReadMsgRequest = UnReadMsgRequestOuterClass.
                UnReadMsgRequest.newBuilder().setUsername(username).setToken("token").build();
        sender.send(unReadMsgRequest);
    }

    public void  showFriendList(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        FriendOuterClass.FriendList friendList = ChatContext.getContext().getFriendList();
        System.out.println("好友列表");
        for(FriendOuterClass.Friend friend:friendList.getFriendListList()){
            System.out.println(friend.getUsername());
        }
    }

}
