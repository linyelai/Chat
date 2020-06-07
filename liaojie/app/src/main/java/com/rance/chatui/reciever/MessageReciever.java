package com.rance.chatui.reciever;

import android.content.Intent;
import android.net.Uri;

import com.rance.chatui.enity.MessageInfo;
import com.rance.chatui.reciever.context.ChatContext;
import com.rance.chatui.util.Constants;
import com.rance.chatui.util.FileUtils;

import org.greenrobot.eventbus.EventBus;

public class MessageReciever {
    public final static String MIME_TYPE_TEXT = "text/plain";

    public void start(){

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ChatContext.getContext().getClient().connect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }
}
