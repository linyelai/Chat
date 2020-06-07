package com.rance.chatui.ui.activity;


import android.os.Bundle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linseven.chatroom.model.FriendOuterClass;
import com.linseven.chatroom.model.MessageOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.adapter.SearchFriendAdapter;
import com.rance.chatui.cache.ChatHistory;
import com.rance.chatui.domain.Member;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.util.Constants;
import com.rance.chatui.util.HttpClientUtils;
import com.rance.chatui.util.RegexUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendActivity extends AppCompatActivity {

    RecyclerView friendListView;
    SearchFriendAdapter adapter;
    private LinearLayoutManager layoutManager;
    private EditText searchFriendUsername;
    private Button searchFriendBtn;

    private List<FriendOuterClass.Friend> searchFriends  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无效

        setContentView(R.layout.activity_search_friend);
        friendListView = findViewById(R.id.searchFriendResultList);
        searchFriendBtn = findViewById(R.id.searchFriendBtn);
        searchFriendUsername = findViewById(R.id.searchFriendUsername);
        EventBus.getDefault().register(this);
        searchFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread =  new Thread(new Runnable() {
                    @Override
                    public void run() {
                        searchFriends.clear();
                        String usernameStr = searchFriendUsername.getText().toString();

                        if(usernameStr==null){
                            Toast.makeText(SearchFriendActivity.this, "请输入好友账号", Toast.LENGTH_SHORT).show();
                        }
                        if(!RegexUtils.checkMobile(usernameStr)){
                            Toast.makeText(SearchFriendActivity.this, "请输入手机号码作为用户名", Toast.LENGTH_SHORT).show();
                        }

                        String param = "username="+usernameStr;

                        JSONObject result = null;
                        try {
                            result = HttpClientUtils.httpPost("http://192.168.43.17:8762/findMembers?"+param);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(result!=null){
                            Toast.makeText(SearchFriendActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            List<Member> friends  = JSON.parseArray(result.get("data").toString(),Member.class);
                            for(Member friend: friends) {
                                FriendOuterClass.Friend friend1 = FriendOuterClass.Friend.newBuilder()
                                        .setAvatar(friend.getAvatar()).setUsername(friend.getUsername()).setNickName(friend.getNickname()).build();
                                searchFriends.add(friend1);
                            }
                            EventBus.getDefault().post(searchFriends);
                        }else{
                            Toast.makeText(SearchFriendActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                thread.start();
            }
        });
        init();
        adapter = new SearchFriendAdapter(searchFriends);
        friendListView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        friendListView.setLayoutManager(layoutManager);
        adapter.addItemClickListener(new SearchFriendAdapter.onItemClickListener(){
            @Override
            public void onClick(View view, int position) {

            }
        });

    }
    public void init(){
        searchFriends = new ArrayList<>();
        FriendOuterClass.Friend friend = FriendOuterClass.Friend.newBuilder()
                .setUsername("18312483564").setNickName("七爷").setAvatar("http://img0.imgtn.bdimg.com/it/u=401967138,750679164&fm=26&gp=0.jpg").build();
        FriendOuterClass.Friend friend1 = FriendOuterClass.Friend.newBuilder()
                .setUsername("18312483564").setNickName("七爷").setAvatar("http://img0.imgtn.bdimg.com/it/u=401967138,750679164&fm=26&gp=0.jpg").build();

        searchFriends.add(friend);
        searchFriends.add(friend1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void searchFriendResponse(final List<FriendOuterClass.Friend> friend) {
        adapter.notifyDataSetChanged();


    }

}
