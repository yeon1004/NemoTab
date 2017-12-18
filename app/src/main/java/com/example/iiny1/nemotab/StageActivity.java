package com.example.iiny1.nemotab;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

public class StageActivity extends BasicGameActivity {

    String stage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvGameModeInfo.setText("하얀 블럭을 모두 없애주세요!");
        Intent intent = getIntent();
        stage = intent.getStringExtra("selectedStage");

        InputStream is;
        switch (stage){
            case "stage1":
                is = getResources().openRawResource(R.raw.stage1);
                break;
            case "stage2":
                is = getResources().openRawResource(R.raw.stage2);
                break;
            case "stage3":
                is = getResources().openRawResource(R.raw.stage3);
                break;
            case "stage4":
                is = getResources().openRawResource(R.raw.stage4);
                break;
            case "stage5":
                is = getResources().openRawResource(R.raw.stage5);
                break;
            case "stage6":
                is = getResources().openRawResource(R.raw.stage6);
                break;
            case "stage7":
                is = getResources().openRawResource(R.raw.stage7);
                break;
            case "stage8":
                is = getResources().openRawResource(R.raw.stage8);
                break;
            case "stage9":
                is = getResources().openRawResource(R.raw.stage9);
                break;
            default :
                is = getResources().openRawResource(R.raw.stage10);
                break;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String tempString = null;
        try{
            int i = is.read();
            while(i != -1)
            {
                baos.write(i);
                i = is.read();
            }
            tempString = baos.toString();
        }catch(IOException e){}
        finally {
            try{is.close();}catch(IOException e){};
        }
        String[] data1 = tempString.split("\r\n");
        for(int i=0; i < data1.length; i++){
            String[] data2 = data1[i].split("\t");
            for(int j = 0; j < data2.length; j++){
                if(!data2[j].toString().equals("0") && j != 0)
                {
                    BoardArr[i][j] = 10;
                    cellArr[i][j].setBackgroundResource(R.drawable.block_stage);
                }
            }
        }
    }

    protected void GameOver(){
        mp.stop();
        mSoundManager.playSound(4);
        cdt.cancel();
        TempBoard.setOnTouchListener(null);
        curBlockBox.setOnTouchListener(null);
        tvGameModeTitle.setText("GameOver");
        tvGameModeInfo.setText("게임 실패!");
        layoutStatBtns.removeAllViews();
        LinearLayout.LayoutParams stateBtn1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,  1);
        LinearLayout.LayoutParams stateBtn2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,  2);
        Button btnHome = new Button(this);
        Button btnReplay = new Button(this);

        btnReplay.setLayoutParams(stateBtn1);
        btnReplay.setText("다시하기");
        btnReplay.setBackgroundColor(getResources().getColor(R.color.ThemeRed));
        btnReplay.setTextColor(getResources().getColor(R.color.ThemeBeige));
        btnReplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                finish();
                finish();
                Intent intent = new Intent(getApplicationContext(), StageActivity.class);
                intent.putExtra("selectedStage", stage);
                startActivityForResult(intent, 0);
            }
        });

        btnHome.setLayoutParams(stateBtn2);
        btnHome.setBackgroundColor(getResources().getColor(R.color.ThemeNavy));
        btnHome.setTextColor(getResources().getColor(R.color.ThemeBeige));
        btnHome.setText("홈으로");
        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                cdt.cancel();
                mp.stop();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        layoutStatBtns.addView(btnHome);
        layoutStatBtns.addView(btnReplay);
        layoutGameStateBg.setVisibility(View.VISIBLE);
    }

    private void GameClear(){
        cdt.cancel();
        TempBoard.setOnTouchListener(null);
        curBlockBox.setOnTouchListener(null);
        tvGameModeTitle.setText("GameClear");
        tvGameModeInfo.setText("게임 성공!");
        layoutStatBtns.removeAllViews();
        LinearLayout.LayoutParams stateBtn1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,  1);
        LinearLayout.LayoutParams stateBtn2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,  2);
        Button btnHome = new Button(this);
        Button btnReplay = new Button(this);

        btnReplay.setLayoutParams(stateBtn1);
        btnReplay.setText("다음 스테이지");
        btnReplay.setBackgroundColor(getResources().getColor(R.color.ThemeRed));
        btnReplay.setTextColor(getResources().getColor(R.color.ThemeBeige));
        btnReplay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                mp.stop();
                finish();
                if(!stage.equals("stage10")) {
                    Intent intent = new Intent(getApplicationContext(), StageActivity.class);
                    switch (stage){
                        case "stage1":
                            intent.putExtra("selectedStage", "stage2");
                            break;
                        case "stage2":
                            intent.putExtra("selectedStage", "stage3");
                            break;
                        case "stage3":
                            intent.putExtra("selectedStage", "stage4");
                            break;
                        case "stage4":
                            intent.putExtra("selectedStage", "stage5");
                            break;
                        case "stage5":
                            intent.putExtra("selectedStage", "stage6");
                            break;
                        case "stage6":
                            intent.putExtra("selectedStage", "stage7");
                            break;
                        case "stage7":
                            intent.putExtra("selectedStage", "stage8");
                            break;
                        case "stage8":
                            intent.putExtra("selectedStage", "stage9");
                            break;
                        case "stage9":
                            intent.putExtra("selectedStage", "stage10");
                            break;
                        default :
                            intent.putExtra("selectedStage", stage);
                            break;
                    }
                    startActivityForResult(intent, 0);
                }else{
                    Toast.makeText(getApplicationContext(), "마지막 스테이지입니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        btnHome.setLayoutParams(stateBtn2);
        btnHome.setBackgroundColor(getResources().getColor(R.color.ThemeNavy));
        btnHome.setTextColor(getResources().getColor(R.color.ThemeBeige));
        btnHome.setText("홈으로");
        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                cdt.cancel();
                mp.stop();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        layoutStatBtns.addView(btnHome);
        layoutStatBtns.addView(btnReplay);
        layoutGameStateBg.setVisibility(View.VISIBLE);
    }

    protected void reOrder()
    {
        int idx = 0;
        int term = 1;
        if(hLineCnt > 0) {
            for(int i = hLineArr[idx]; i < iBoardSize - term; i++) { //막줄빼고
                if(i == hLineArr[idx+1] - 1) {
                    term++;
                    if(idx < hLineCnt-1) idx++;
                }
                for(int j = 0; j < iBoardSize - term; j++){
                    BoardArr[i][j] = BoardArr[i+term][j];
                    switch (BoardArr[i+term][j])
                    {
                        case 1:
                            cellArr[i][j].setBackgroundResource(R.drawable.block1);
                            break;
                        case 2:
                            cellArr[i][j].setBackgroundResource(R.drawable.block2);
                            break;
                        case 3:
                            cellArr[i][j].setBackgroundResource(R.drawable.block3);
                            break;
                        case 4:
                            cellArr[i][j].setBackgroundResource(R.drawable.block4);
                            break;
                        case 5:
                            cellArr[i][j].setBackgroundResource(R.drawable.block5);
                            break;
                        case 6:
                            cellArr[i][j].setBackgroundResource(R.drawable.block6);
                            break;
                        case 7:
                            cellArr[i][j].setBackgroundResource(R.drawable.block7);
                            break;
                        case 10:
                            cellArr[i][j].setBackgroundResource(R.drawable.block_stage);
                            break;
                        default:
                            cellArr[i][j].setBackgroundResource(R.drawable.noblock);
                    }
                }
            }
            for(int j = 0; j < iBoardSize; j++) {
                for(int i = 0; i < term; i++){
                    BoardArr[iBoardSize-i-1][j] = 0;
                    cellArr[iBoardSize-i-1][j].setBackgroundResource(R.drawable.noblock);
                }
            }
        }

        idx = 0;
        term = 1;
        if(vLineCnt > 0) {
            for(int i = vLineArr[idx]; i < iBoardSize - term; i++) { //막줄빼고
                if(i == vLineArr[idx+1] - 1) {
                    term++;
                    if(idx < vLineCnt-1) idx++;
                }
                for(int j = 0; j < iBoardSize - term; j++){
                    BoardArr[j][i] = BoardArr[j][i+term];
                    switch (BoardArr[j][i+term])
                    {
                        case 1:
                            cellArr[j][i].setBackgroundResource(R.drawable.block1);
                            break;
                        case 2:
                            cellArr[j][i].setBackgroundResource(R.drawable.block2);
                            break;
                        case 3:
                            cellArr[j][i].setBackgroundResource(R.drawable.block3);
                            break;
                        case 4:
                            cellArr[j][i].setBackgroundResource(R.drawable.block4);
                            break;
                        case 5:
                            cellArr[j][i].setBackgroundResource(R.drawable.block5);
                            break;
                        case 6:
                            cellArr[j][i].setBackgroundResource(R.drawable.block6);
                            break;
                        case 7:
                            cellArr[j][i].setBackgroundResource(R.drawable.block7);
                            break;
                        case 10:
                            cellArr[j][i].setBackgroundResource(R.drawable.block_stage);
                            break;
                        default:
                            cellArr[j][i].setBackgroundResource(R.drawable.noblock);
                    }
                }
            }
            for(int j = 0; j < iBoardSize; j++) {
                for(int i = 0; i < term; i++){
                    BoardArr[j][iBoardSize-i-1] = 0;
                    cellArr[j][iBoardSize-i-1].setBackgroundResource(R.drawable.noblock);
                }
            }
        }

        //하얀 블럭 체크
        int sBlock = 0;
        for(int i = 0; i < iBoardSize; i++){
            for(int j = 0; j < iBoardSize; j++)
                if(BoardArr[i][j] >= 10) sBlock++;
        }
        if(sBlock <= 0) GameClear();
    }

    protected void DropBlock()
    {
        int[][] block = blocks[0].getBlock(); //현재 블럭 가져옴

        for(int row = 0; row < blocks[0].getRows(); row++) {
            for (int col = 0; col < blocks[0].getCols(); col++) {
                if(block[row][col] > 0 && BoardArr[TouchedCellRow + row][TouchedCellCol + col] != 0) {
                    RemoveBlock();
                    return;
                }
            }
        }

        for(int row = 0; row < blocks[0].getRows(); row++)
        {
            for(int col = 0; col < blocks[0].getCols(); col++)
            {
                if(block[row][col] > 0) {
                    //임시 보드판은 투명처리
                    tempCellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundColor(Color.TRANSPARENT);

                    //실제 게임판에 블럭 놓기
                    BoardArr[TouchedCellRow + row][TouchedCellCol + col] = block[row][col];

                    switch(block[row][col])
                    {
                        case 1:
                            cellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundResource(R.drawable.block1);
                            break;
                        case 2:
                            cellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundResource(R.drawable.block2);
                            break;
                        case 3:
                            cellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundResource(R.drawable.block3);
                            break;
                        case 4:
                            cellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundResource(R.drawable.block4);
                            break;
                        case 5:
                            cellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundResource(R.drawable.block5);
                            break;
                        case 6:
                            cellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundResource(R.drawable.block6);
                            break;
                        default:
                            cellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundResource(R.drawable.block7);
                            break;
                    }

                }
            }
        }
        TouchedCellCol = TouchedCellRow = -1;
        //점수 증가
        int score = 10;                    //이번 턴 점수
        int curScore = Integer.parseInt(tvScore.getText().toString());   //기존 점수
        tvScore.setText(Integer.toString(curScore + score));
        getNextBlock();
        CheckLine();
    }
}
