package com.rance.chatui.adapter;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.rance.chatui.R;
import com.rance.chatui.adapter.holder.BaseViewHolder;
import com.rance.chatui.adapter.holder.NewMessageViewHolder;
import com.rance.chatui.domain.NewMessage;

import com.rance.chatui.reciever.context.ChatContext;
import com.rance.chatui.ui.activity.IMActivity;

import java.util.ArrayList;
import java.util.List;


public class NewMessageAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private NewMessageAdapter.onItemClickListener onItemClickListener;

    private List<NewMessage> newMessages;
    public Handler handler;
    public NewMessageAdapter(List<NewMessage> objects){
        this.newMessages = objects;
    }
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder viewHolder = null;
        viewHolder = new NewMessageViewHolder(parent, onItemClickListener, handler);
        return viewHolder;
    }
    public void addItemClickListener(NewMessageAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.setData(newMessages.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        if (newMessages == null) {
            return 0;
        } else {
            return newMessages.size();
        }
    }

    public void addAll(List<NewMessage> msgs) {
        if (newMessages == null) {
            newMessages = msgs;
        } else {
            newMessages.addAll(msgs);
        }
        notifyDataSetChanged();
    }

    public void add(NewMessage msg) {
        if (newMessages == null) {
            newMessages = new ArrayList<>();
        }

        newMessages.add(msg);

        notifyDataSetChanged();
    }

    public interface onItemClickListener {
        void onClick(View view,String friendName);
    }

}