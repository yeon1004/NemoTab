package com.example.iiny1.nemotab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends FullScreenActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //일정 시간 후 메인으로 넘기기
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);//지연 시간: 2초
    }
}