package com.example.gitdeom.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.Http2Connection;

public class OkHttpTo extends Thread{
    private String Url = "http://192.168.1.110:8080/";
    private JSONObject jsonObject = new JSONObject();
    private OkHttpLo okHttpLo;
    private boolean isLoop;
    private int time;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (progressDialog!=null&&progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (msg.what == 1){
                okHttpLo.onResponse(msg.obj);
            }else if (msg.what == 2){
                okHttpLo.onFailure((IOException)msg.obj);
            }
            return false;
        }
    });
    public OkHttpTo setUrl(String url){
        Url += url;
        return this;
    }
    public OkHttpTo setJSONObject(String k, Object v){
        try {
            jsonObject.put(k, v);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  this;
    }
    public OkHttpTo setOkHttpLo (OkHttpLo okHttpLo){
        this.okHttpLo = okHttpLo;
        return this;
    }
    public OkHttpTo setLoop(boolean loop){
        isLoop = loop;
        return this;
    }
    public OkHttpTo setTime(int time){
        this.time = time;
        return this;
    }
    public OkHttpTo setDialong(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("网络请求中。。。");
        progressDialog.show();
        return this;
    }
    private int type;

    public OkHttpTo setType(int type){
        this.type = type;
        return this;
    }
    String token = null;
    public OkHttpTo setHeader(String token){
        this.token = token;
        return this;
    }

    @Override
    public void run() {
        super.run();
        do {
            OkHttpClient client = new OkHttpClient();
            RequestBody body =RequestBody.create(MediaType.get("application/json;charset=utf-8"),jsonObject.toString());
            Request.Builder builder = new Request.Builder()
                    .url(Url);
            switch (type){
                case 1:
                    builder.get();
                    break;
                case 2:
                    builder.post(body);
                    break;
                case 3:
                    builder.put(body);
                    break;
            }
            if (token != null){
                builder.header("Authorization",token);
            }
            final Request request = builder.build();
            client.newCall(request)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message message = new Message();
                            message.what = 2 ;
                            message.obj = e;
                            if (progressDialog != null){
                                handler.sendMessageDelayed(message,1000);
                            }else {
                                handler.sendMessage(message);
                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message message = new Message();
                            message.what = 1;
                            message.obj = response.body().string();
                            if (progressDialog != null){
                                handler.sendMessageDelayed(message,1000);
                            }else {
                                handler.sendMessage(message);
                            }
                        }
                    });
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }while (isLoop);
    }
}
