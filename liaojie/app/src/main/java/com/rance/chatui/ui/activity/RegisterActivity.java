package com.rance.chatui.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.rance.chatui.R;
import com.rance.chatui.util.HttpClientUtils;
import com.rance.chatui.util.RegexUtils;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private EditText confirmPassword;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无效

        setContentView(R.layout.content_register);
        username = findViewById(R.id.phoneNum);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        button = findViewById(R.id.register_button);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Thread thread =  new Thread(new Runnable() {
                    @Override
                    public void run() {

                        String usernameStr = username.getText().toString();
                        String passwordStr = password.getText().toString();
                        String confirmPasswordStr = confirmPassword.getText().toString();
                        if(usernameStr==null||passwordStr==null||confirmPasswordStr==null){
                            Toast.makeText(RegisterActivity.this, "输入不完整", Toast.LENGTH_SHORT).show();
                        }
                        if(!RegexUtils.checkMobile(usernameStr)){
                            Toast.makeText(RegisterActivity.this, "请输入手机号码作为用户名", Toast.LENGTH_SHORT).show();
                        }
                        if(!passwordStr.equals(confirmPasswordStr)){
                            Toast.makeText(RegisterActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();

                        }
                        String param = "username="+usernameStr+"&password="+passwordStr+"&nickname="+usernameStr+"&avatar=http://img0.imgtn.bdimg.com/it/u=401967138,750679164&fm=26&gp=0.jpg";

                        JSONObject result = null;
                        try {
                            result = HttpClientUtils.httpPost("http://192.168.43.17:8762/register?"+param);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(result!=null){
                            Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(i);
                        }else{
                            Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                thread.start();


            }
        });

    }

}
