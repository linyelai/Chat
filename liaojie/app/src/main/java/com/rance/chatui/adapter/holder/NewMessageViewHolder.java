package com.rance.chatui.adapter.holder;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.linseven.chatroom.model.FriendOuterClass;
import com.rance.chatui.R;
import com.rance.chatui.adapter.FriendAdapter;
import com.rance.chatui.adapter.NewMessageAdapter;
import com.rance.chatui.domain.NewMessage;
import com.rance.chatui.reciever.context.ChatContext;
import com.rance.chatui.ui.activity.IMActivity;

/**
 * 作者：Rance on 2016/11/29 10:47
 * 邮箱：rance935@163.com
 */
public class NewMessageViewHolder extends BaseViewHolder<NewMessage> {
    private static final String TAG = "FriendsViewHolder";
    TextView friendName;
    TextView friendUsername;
    ImageView friendImg;
    TextView newMsg;
    private NewMessageAdapter.onItemClickListener onItemClickListener;
    private Handler handler;
    private Context mContext;
    public NewMessageViewHolder(ViewGroup parent, NewMessageAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.new_msg_item, parent, false));
        findViewByIds(itemView);
        setItemLongClick();
        this.mContext = parent.getContext();
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
        setItemClick();
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), IMActivity.class);
                i.putExtra("friendName",friendName.getText().toString());
                i.putExtra("friendUsername",friendUsername.getText().toString());
                i.setAction(Intent.ACTION_SEND);
                i.setDataAndType(Uri.parse("*"), "*");
                Activity activity = ChatContext.getContext().getDialog(friendName.getText().toString());
                v.getContext().startActivity(i);
               /* if(activity==null) {
                    v.getContext().startActivity(i);
                }else {
                    ActivityManager am = (ActivityManager) v.getContext().getSystemService(Context.ACTIVITY_SERVICE);
                    am.addAppTask(activity, i, null, null);
                }*/
            }
        });

        // layoutParams = (RelativeLayout.LayoutParams) chatItemLayoutContent.getLayoutParams();
    }

    private void findViewByIds(View itemView) {
        friendName = (TextView) itemView.findViewById(R.id.friend_name);
        friendImg = (ImageView) itemView.findViewById(R.id.friend_image);
        friendUsername = (TextView) itemView.findViewById(R.id.friend_Username);
        newMsg = itemView.findViewById(R.id.newMsg);
    }

    @Override
    public void setData(NewMessage msg) {
        friendName.setText(msg.getFriendNickName());
        Glide.with(mContext).load(msg.getAvatar()).into(friendImg);
       newMsg.setText(msg.getLastMsg());
       friendUsername.setText(msg.getFriendUsername());

    }

    public void setItemLongClick() {

    }

    public void setItemClick() {

 friendImg.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         Intent i = new Intent(v.getContext(), IMActivity.class);
         i.putExtra("friendName",friendName.getText().toString());
         i.setAction(Intent.ACTION_SEND);
         i.setDataAndType(Uri.parse("*"), "*");
         v.getContext().startActivity(i);
     }
 });
    }
}
