package com.rance.chatui.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.linseven.chatroom.model.FriendOuterClass;
import com.linseven.chatroom.model.FriendsRequestOuterClass;
import com.linseven.chatroom.model.LoginResponseMsgOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.adapter.FriendAdapter;
import com.rance.chatui.reciever.MsgSender;
import com.rance.chatui.reciever.context.ChatContext;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;

public class FriendListActivity extends AppCompatActivity {

    private List<FriendOuterClass.Friend> friendList =new ArrayList<>();
    private FriendAdapter adapter ;
    RecyclerView listView;
    private LinearLayoutManager layoutManager;
    private Button addFriendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无效

        setContentView(R.layout.activity_friend_list);
        EventBus.getDefault().register(this);
        initFriends();
        adapter =new FriendAdapter(friendList);
        listView  =(RecyclerView)findViewById(R.id.list_View);
        addFriendBtn = findViewById(R.id.addFriendButton);
        listView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        //
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(FriendListActivity.this,SearchFriendActivity.class);
                startActivity(i);
            }
        });
    }

    private void initFriends(){

        getFriendList(ChatContext.getContext().getUsername());
    }

    public  void getFriendList(String username){
        ChannelHandlerContext ctx = ChatContext.getContext().getChannelHandlerContext();
        MsgSender<FriendsRequestOuterClass.FriendsRequest> sender = new MsgSender<>(ctx);
        FriendsRequestOuterClass.FriendsRequest friendsRequest = FriendsRequestOuterClass.FriendsRequest.newBuilder()
                .setUsername(username).setType(1).build();
        sender.send(friendsRequest);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final FriendOuterClass.FriendList  friendList1) {

        if(friendList1!=null){

            friendList.addAll(friendList1.getFriendListList());
            adapter.notifyDataSetChanged();
        }
    }

}
