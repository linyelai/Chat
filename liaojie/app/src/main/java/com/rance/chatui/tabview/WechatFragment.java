package com.rance.chatui.tabview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linseven.chatroom.model.FriendOuterClass;
import com.linseven.chatroom.model.FriendsRequestOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.adapter.BaseRecyclerViewAdapter;
import com.rance.chatui.adapter.FriendAdapter;
import com.rance.chatui.adapter.NewMessageAdapter;
import com.rance.chatui.base.BaseSwipeRefreshLayout;
import com.rance.chatui.domain.NewMessage;
import com.rance.chatui.reciever.MsgSender;
import com.rance.chatui.reciever.context.ChatContext;
import com.rance.chatui.ui.activity.FriendListActivity;
import com.rance.chatui.ui.activity.IMActivity;
import com.rance.chatui.ui.activity.SearchFriendActivity;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;


/**
 * Created by yx on 16/4/3.
 */
public class WechatFragment extends BaseFragment implements ITabClickListener {
    private List<NewMessage> newMessages =new ArrayList<>();
    private NewMessageAdapter adapter ;
    RecyclerView listView;
    private LinearLayoutManager layoutManager;

    @Override
    public void fetchData() {
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_msg_list, container, false);
        initNewMsg();
        adapter =new NewMessageAdapter(newMessages);
        listView  =view.findViewById(R.id.newMsg_list_View);
        listView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        return view;
    }


    private void getData() {


    }

    @Override
    public void onMenuItemClick() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }


    class WechatAdapter extends BaseRecyclerViewAdapter {
        public WechatAdapter() {
            super(R.layout.contact_item);
        }

        @Override
        public void onBindViewData(ViewHolder var1, int var2) {
            var1.getTextView(R.id.contact).setText("你有新消息");
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

    private void initNewMsg(){

        //getFriendList(ChatContext.getContext().getUsername());
        newMessages.clear();
        for (int i=0;i<2;i++){
            NewMessage msg = new NewMessage();
            msg.setFriendUsername("10000"+i);
            msg.setFriendNickName("周瑜"+i);
            msg.setAvatar("http://img5.imgtn.bdimg.com/it/u=2531384262,3699915741&fm=26&gp=0.jpg");
            msg.setLastMsg("hell world");
            msg.setCreatTime("2020-05-22 00:00:00");
            newMessages.add(msg);
        }
    }
    public  void getFriendList(String username){
        ChannelHandlerContext ctx = ChatContext.getContext().getChannelHandlerContext();
        MsgSender<FriendsRequestOuterClass.FriendsRequest> sender = new MsgSender<>(ctx);
        FriendsRequestOuterClass.FriendsRequest friendsRequest = FriendsRequestOuterClass.FriendsRequest.newBuilder()
                .setUsername(username).setType(1).build();
        sender.send(friendsRequest);
    }
}
