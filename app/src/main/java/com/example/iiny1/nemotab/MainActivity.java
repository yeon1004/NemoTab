package com.example.iiny1.nemotab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FullScreenActivity {
    Button btnSetting, btnLeft, btnRight, btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSetting = (Button)findViewById(R.id.btnSetting);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnRight = (Button)findViewById(R.id.btnRight);
        btnStart = (Button)findViewById(R.id.btnStart);

        /*
        //클릭 이벤트 기본형
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });
        */

        btnSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), BasicGameActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
