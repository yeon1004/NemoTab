package com.example.iiny1.nemotab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends FullScreenActivity {
    Button btnLeft, btnRight, btnStart;
    TextView tvMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLeft = (Button)findViewById(R.id.btnLeft);
        btnRight = (Button)findViewById(R.id.btnRight);
        btnStart = (Button)findViewById(R.id.btnStart);
        tvMode = (TextView)findViewById(R.id.tvMode);
        tvMode.setText("Basic");
        /*
        //클릭 이벤트 기본형
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

            }
        });
        */

        btnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(tvMode.getText().equals("Basic"))
                    tvMode.setText("Stage");
                else
                    tvMode.setText("Basic");
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if(tvMode.getText().equals("Basic"))
                    tvMode.setText("Stage");
                else
                    tvMode.setText("Basic");
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                Intent intent;
                if(tvMode.getText().equals("Basic"))
                    intent = new Intent(getApplicationContext(), BasicGameActivity.class);
                else
                    intent = new Intent(getApplicationContext(), SelectStageActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }
}
