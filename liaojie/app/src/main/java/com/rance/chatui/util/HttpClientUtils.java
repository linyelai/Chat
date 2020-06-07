package com.rance.chatui.util;

import android.net.http.HttpsConnection;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpClient4.3工具类
 * @author hang.luo
 */
public class HttpClientUtils
{


    /**
     * post请求传输json参数
     * @param url  url地址
     * @param
     * @return
     */
    public static JSONObject httpPost(String url, JSONObject jsonParam)
    {
        // post请求返回结果
        HttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost httpPost = new HttpPost(url);
        // 设置请求和传输超时时间
        
        try
        {
            if (null != jsonParam)
            {
                // 解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(httpPost);
            // 请求发送成功，并得到响应
            if (result.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                String str = "";
                try
                {
                    // 读取服务器返回过来的json字符串数据
                    str = EntityUtils.toString(result.getEntity(), "utf-8");
                    // 把json字符串转换成json对象
                    jsonResult = JSONObject.parseObject(str);
                }
                catch (Exception e)
                {

                }
            }
        }
        catch (IOException e)
        {

        }
        
        return jsonResult;
    }

    /**
     * post请求传输String参数 例如：name=Jack&sex=1&type=2
     * Content-type:application/x-www-form-urlencoded
     * @param url            url地址
     * @param
     * @return
     */
    public static JSONObject httpPost(String url) throws  Exception
    {
        // post请求返回结果

        URL url1 = new URL(url);
        //得到connection对象。
        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
        connection.setRequestMethod("POST");
        String result =null;
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
           result = is2String(inputStream);//将流转换为字符串。
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",result);
        return jsonObject ;
    }

    /**
     * 发送get请求
     * @param url 路径
     * @return
     */
    public static JSONObject httpGet(String url)
    {
        // get请求返回结果
        JSONObject jsonResult = null;
        HttpClient client = new DefaultHttpClient();
        // 发送get请求
        HttpGet request = new HttpGet(url);
        try
        {
            HttpResponse response = client.execute(request);

            // 请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
            {
                // 读取服务器返回过来的json字符串数据
                HttpEntity entity = response.getEntity();
                String strResult = EntityUtils.toString(entity, "utf-8");
                // 把json字符串转换成json对象
                jsonResult = JSONObject.parseObject(strResult);
            }
            else
            {

            }
        }
        catch (IOException e)
        {

        }

        return jsonResult;
    }

    public static  void test(){
        JSONObject param=new JSONObject();
        param.put("username","15977983383");
        param.put("password","123456");
        param.put("nickName","15977983383");
        param.put("avatar","http://img0.imgtn.bdimg.com/it/u=401967138,750679164&fm=26&gp=0.jpg");
        try {
            httpPost("http://localhost:8762/register?username=15977983383&password=123456&nickname=15977983383&avatar=http://img0.imgtn.bdimg.com/it/u=401967138,750679164&fm=26&gp=0.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  static  void main(String [] args){
        test();
    }
    public static String is2String(InputStream is) throws  Exception{

        //连接后，创建一个输入流来读取response
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(is,"utf-8"));
        String line = "";
        StringBuilder stringBuilder = new StringBuilder();
        String response = "";
        //每次读取一行，若非空则添加至 stringBuilder
        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }
        //读取所有的数据后，赋值给 response
        //String response = stringBuilder.toString().trim();
        return stringBuilder.toString().trim();


    }

}