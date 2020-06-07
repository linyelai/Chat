package com.rance.chatui.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linseven.chatroom.model.FriendOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.adapter.ChatAdapter;
import com.rance.chatui.adapter.FriendAdapter;
import com.rance.chatui.enity.IMContact;
import com.rance.chatui.enity.Link;
import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.ui.activity.FriendListActivity;
import com.rance.chatui.ui.activity.IMActivity;
import com.rance.chatui.util.Constants;
import com.rance.chatui.util.FileUtils;
import com.rance.chatui.util.Utils;
import com.rance.chatui.widget.BubbleImageView;
import com.rance.chatui.widget.BubbleLinearLayout;
import com.rance.chatui.widget.GifTextView;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class FriendsViewHolder extends BaseViewHolder<FriendOuterClass.Friend> {
    private static final String TAG = "FriendsViewHolder";
    TextView friendName;
    ImageView friendImg;
    String friendUsername;
    private FriendAdapter.onItemClickListener onItemClickListener;
    private Handler handler;
    private Context mContext;
    public FriendsViewHolder(ViewGroup parent, FriendAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false));
        findViewByIds(itemView);
        setItemLongClick();
        setItemClick();
        this.mContext = parent.getContext();
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext , IMActivity.class);
                i.putExtra("friendName",friendName.getText());
                i.putExtra("friendUsername",friendUsername);
                i.setAction(Intent.ACTION_SEND);
                i.setDataAndType(Uri.parse("*"),"*");
                mContext.startActivity(i);
            }
        });
       // layoutParams = (RelativeLayout.LayoutParams) chatItemLayoutContent.getLayoutParams();
    }

    private void findViewByIds(View itemView) {
        friendName = (TextView) itemView.findViewById(R.id.friend_name);
        friendImg = (ImageView) itemView.findViewById(R.id.friend_image);

    }

    @Override
    public void setData(FriendOuterClass.Friend friend) {
        friendName.setText(friend.getNickName());
        Glide.with(mContext).load(friend.getAvatar()).into(friendImg);
        friendUsername = friend.getFriendUsername();

    }

    public void setItemLongClick() {

    }

    public void setItemClick() {
        friendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i=new Intent(mContext , IMActivity.class);
                i.putExtra("friendName",friendName.getText());
                i.putExtra("friendUsername",friendUsername);
                i.setAction(Intent.ACTION_SEND);
                i.setDataAndType(Uri.parse("*"),"*");
                mContext.startActivity(i);*/
            }
        });
    }
}
