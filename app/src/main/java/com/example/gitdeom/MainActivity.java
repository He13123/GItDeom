package com.example.gitdeom;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.gitdeom.net.OkHttpLo;
import com.example.gitdeom.net.OkHttpTo;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    String TAG = "aa";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //网络访问GET , POST ,PUT

        OkHttpTo okHttpTo = new OkHttpTo();
        okHttpTo.setUrl("put/token")
                .setType(3)
                .setJSONObject("type",1)
                .setHeader("")
                .setJSONObject("type",1)
                .setOkHttpLo(new OkHttpLo() {
                    @Override
                    public void onResponse(Object obj) {
                        Log.i(TAG,"onResponse:"+obj);
                    }

                    @Override
                    public void onFailure(IOException obj) {

                    }
                }).start();
        }
}
