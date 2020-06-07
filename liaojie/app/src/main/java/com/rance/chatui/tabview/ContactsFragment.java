package com.rance.chatui.tabview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.linseven.chatroom.model.FriendOuterClass;
import com.linseven.chatroom.model.FriendsRequestOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.adapter.BaseRecyclerViewAdapter;
import com.rance.chatui.adapter.FriendAdapter;
import com.rance.chatui.base.BaseSwipeRefreshLayout;
import com.rance.chatui.domain.NewMessage;
import com.rance.chatui.reciever.MsgSender;
import com.rance.chatui.reciever.context.ChatContext;
import com.rance.chatui.ui.activity.FriendListActivity;
import com.rance.chatui.ui.activity.IMActivity;
import com.rance.chatui.ui.activity.SearchFriendActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by yx on 16/4/3.
 */
public class ContactsFragment extends BaseFragment implements ITabClickListener {

    private List<FriendOuterClass.Friend> friendList =new ArrayList<>();
    private FriendAdapter adapter ;
    RecyclerView listView;
    private LinearLayoutManager layoutManager;
    private Button addFriendBtn;
    @Override
    public void fetchData() {
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.activity_friend_list, container, false);
        initFriends();
        adapter =new FriendAdapter(friendList);
        listView  =view.findViewById(R.id.list_View);
        addFriendBtn = view.findViewById(R.id.addFriendButton);
        listView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listView.setLayoutManager(layoutManager);
        adapter.addItemClickListener(new FriendAdapter.onItemClickListener(){
            @Override
            public void onClick(View view, int position) {
                FriendOuterClass.Friend friend =friendList.get(position);
                //打开聊天面板
                //IMActivity imActivity = new IMActivity();
                Intent i=new Intent(ContactsFragment.this.getContext(), IMActivity.class);
                i.putExtra("friendName",friend.getNickName());
                i.putExtra("friendUsername",friend.getUsername());
                i.setAction(Intent.ACTION_SEND);
                i.setDataAndType(Uri.parse("*"),"*");
                startActivity(i);
            }
        });
        //
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ContactsFragment.this.getContext(), SearchFriendActivity.class);
                startActivity(i);
            }
        });
        return view;
    }

    private void initFriends(){

        getFriendList(ChatContext.getContext().getUsername());
        if(ChatContext.getContext().getFriendList()!=null) {
            friendList.addAll(ChatContext.getContext().getFriendList().getFriendListList());
            adapter =new FriendAdapter(friendList);
            adapter.notifyDataSetChanged();
        }
        /*for (int i=0;i<2;i++){
            FriendOuterClass.Friend friend = FriendOuterClass.Friend.newBuilder()
                    .setUsername("10000"+i).setNickName("周瑜"+i).setAvatar("http://img5.imgtn.bdimg.com/it/u=2531384262,3699915741&fm=26&gp=0.jpg").build();
            friendList.add(friend);
        }*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final FriendOuterClass.FriendList  friendList1) {

        if(friendList1!=null){

            friendList.addAll(friendList1.getFriendListList());
            adapter.notifyDataSetChanged();
        }
    }
    public  void getFriendList(String username){
        ChannelHandlerContext ctx = ChatContext.getContext().getChannelHandlerContext();
        MsgSender<FriendsRequestOuterClass.FriendsRequest> sender = new MsgSender<>(ctx);
        FriendsRequestOuterClass.FriendsRequest friendsRequest = FriendsRequestOuterClass.FriendsRequest.newBuilder()
                .setUsername(username).setType(1).build();
        sender.send(friendsRequest);
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


    class ConstactsAdapter extends BaseRecyclerViewAdapter {
        public ConstactsAdapter() {
            super(R.layout.contact_item);
        }

        @Override
        public void onBindViewData(BaseRecyclerViewAdapter.ViewHolder var1, int var2) {
            var1.getTextView(R.id.contact).setText("联系人");
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }



}
