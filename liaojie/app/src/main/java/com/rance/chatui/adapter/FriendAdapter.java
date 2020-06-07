package com.rance.chatui.adapter;


import android.os.Handler;
import android.support.v7.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import com.linseven.chatroom.model.FriendOuterClass;
import com.rance.chatui.adapter.holder.BaseViewHolder;
import com.rance.chatui.adapter.holder.FriendsViewHolder;
import java.util.ArrayList;
import java.util.List;


public class FriendAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private FriendAdapter.onItemClickListener onItemClickListener;

    private List<FriendOuterClass.Friend> friends;
    public Handler handler;
    public FriendAdapter(List<FriendOuterClass.Friend> objects){
        this.friends = objects;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        viewHolder = new FriendsViewHolder(parent, onItemClickListener, handler);
        return viewHolder;
    }
    public void addItemClickListener(FriendAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(friends.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        if (friends == null) {
            return 0;
        } else {
            return friends.size();
        }
    }

    public void addAll(List<FriendOuterClass.Friend> friendList) {
        if (friends == null) {
            friends = friendList;
        } else {
            friends.addAll(friendList);
        }
        notifyDataSetChanged();
    }

    public void add(FriendOuterClass.Friend friendP) {
        if (friends == null) {
            friends = new ArrayList<>();
        }

        friends.add(friendP);

        notifyDataSetChanged();
    }

    public interface onItemClickListener {

        void onClick(View view, int position);


    }

}