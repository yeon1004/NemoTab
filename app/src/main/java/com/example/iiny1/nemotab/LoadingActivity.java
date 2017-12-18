package com.example.iiny1.nemotab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class LoadingActivity extends FullScreenActivity {
    TextView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        logo = (TextView)findViewById(R.id.tvLogo);
        Animation anima = AnimationUtils.loadAnimation(this, R.anim.alpha);
        logo.startAnimation(anima);

        //일정 시간 후 메인으로 넘기기
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);//지연 시간: 2초
    }
}