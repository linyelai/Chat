package com.rance.chatui.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linseven.chatroom.model.FriendOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.adapter.FriendAdapter;
import com.rance.chatui.adapter.SearchFriendAdapter;
import com.rance.chatui.ui.activity.IMActivity;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class SearchFriendsViewHolder extends BaseViewHolder<FriendOuterClass.Friend> {
    private static final String TAG = "SearchFriendsViewHolder";
    TextView friendName;
    ImageView friendImg;
    String friendUsername;
    Button addFriendBtn;
    private SearchFriendAdapter.onItemClickListener onItemClickListener;
    private Handler handler;
    private Context mContext;
    public SearchFriendsViewHolder(ViewGroup parent, SearchFriendAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_friend_result_item, parent, false));
        findViewByIds(itemView);
        setItemLongClick();
        setItemClick();
        this.mContext = parent.getContext();
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
    }

    private void findViewByIds(View itemView) {
        friendName = (TextView) itemView.findViewById(R.id.search_friend_name);
        friendImg = (ImageView) itemView.findViewById(R.id.search_friend_image);
        addFriendBtn = itemView.findViewById(R.id.addFriendBtn);

    }

    @Override
    public void setData(FriendOuterClass.Friend friend) {
        friendName.setText(friend.getNickName());
        Glide.with(mContext).load(friend.getAvatar()).into(friendImg);
        friendUsername = friend.getUsername();

    }

    public void setItemLongClick() {

    }

    public void setItemClick() {

        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendBtn.setText("已申请");
            }
        });


    }
}
