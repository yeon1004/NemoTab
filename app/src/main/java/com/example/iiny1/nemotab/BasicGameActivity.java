package com.example.iiny1.nemotab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
//import android.os.Handler;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class BasicGameActivity extends FullScreenActivity {

    Boolean Game_clear;

    Block[] blocks;
    TableLayout GameBoard;
    TableLayout TempBoard;

    final int iBoardSize = 15; //보드 칸 수

    Random random;
    LinearLayout curBlockBox;
    LinearLayout curBlock;
    LinearLayout nextBlock1;
    LinearLayout nextBlock2;

    TableRow[] trArr;
    ImageView[][] cellArr;

    TableRow[] tempTrArr;
    ImageView[][] tempCellArr;

    int TouchedCellRow, TouchedCellCol;

    int[][] BoardArr;
    TextView tvScore;

    int[] vLineArr = new int[4];
    int[] hLineArr = new int[4];
    int vLineCnt = 0;
    int hLineCnt = 0;
    boolean LineChk;

    int targetScore; //현재 점수로부터 증가될 목표 점수

    //게임 상태 Layout (게임 시작, 게임 오버)
    LinearLayout layoutGameStateBg;
    LinearLayout layoutGameState;
    TextView tvGameModeTitle;
    TextView tvGameModeInfo;
    LinearLayout layoutStatBtns;
    Button btnGameState;

    //타이머
    ProgressBar pbTimer;
    CountDownTimer cdt;

    //음악
    MediaPlayer mp;
    SoundManager mSoundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_game);

        BoardArr = new int[iBoardSize][iBoardSize];
        for(int i = 0; i < iBoardSize; i++)
            for(int j = 0; j < iBoardSize; j++)
                BoardArr[i][j] = 0;

        GameBoard = (TableLayout)findViewById(R.id.GameBoard);
        FrameLayout.LayoutParams gbLp = new FrameLayout.LayoutParams(getLcdSizeWidth()-5, getLcdSizeWidth()-5);
        GameBoard.setLayoutParams(gbLp);

        trArr = new TableRow[iBoardSize];
        cellArr = new ImageView[iBoardSize][iBoardSize];

        //***LayoutParam 앞에는 상위 레이아웃을 적어줄 것
        TableLayout.LayoutParams trLp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1);
        LayoutParams cellLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1);
        cellLp.setMargins(2, 2, 2, 2);
        for(int i = 0; i < iBoardSize; i++)
        {
            trArr[i] = new TableRow(this);
            trArr[i].setLayoutParams(trLp);
            GameBoard.addView(trArr[i]);

            for(int j = 0; j < iBoardSize; j++)
            {
                cellArr[i][j] = new ImageView(this);
                cellArr[i][j].setLayoutParams(cellLp);
                cellArr[i][j].setScaleType(ImageView.ScaleType.FIT_XY);
                cellArr[i][j].setBackgroundResource(R.drawable.noblock);
                //cellArr[i][j].setBackgroundColor(getResources().getColor(R.color.GBfore));
                //cellArr[i][j].setId();
                trArr[i].addView(cellArr[i][j]);
            }
        }
        //게임판 그리기 끝

        TempBoard = (TableLayout)findViewById(R.id.TempBoard);
        TempBoard.setLayoutParams(gbLp);

        tempTrArr = new TableRow[iBoardSize];
        tempCellArr = new ImageView[iBoardSize][iBoardSize];

        for(int i = 0; i < iBoardSize; i++)
        {
            tempTrArr[i] = new TableRow(this);
            tempTrArr[i].setLayoutParams(trLp);
            TempBoard.addView(tempTrArr[i]);

            for(int j = 0; j < iBoardSize; j++)
            {
                tempCellArr[i][j] = new ImageView(this);
                tempCellArr[i][j].setLayoutParams(cellLp);
                tempCellArr[i][j].setBackgroundColor(Color.TRANSPARENT); //투명으로
                //cellArr[i][j].setId();
                tempTrArr[i].addView(tempCellArr[i][j]);
            }
        }
        //임시게임판 그리기 끝

        blocks = new Block[3]; //블럭 배열 생성

        //블럭 목록 보여줄 레이아웃 불러오기
        curBlockBox = (LinearLayout)findViewById(R.id.curBlockBox);
        curBlock = (LinearLayout)findViewById(R.id.curBlock);
        nextBlock1 = (LinearLayout)findViewById(R.id.nextBlock1);
        nextBlock2 = (LinearLayout)findViewById(R.id.nextBlock2);

        //블록 생성
        random = new Random();
        blocks[0] = new Block(random.nextInt(7)+1);
        blocks[1] = new Block(random.nextInt(7)+1);
        blocks[2] = new Block(random.nextInt(7)+1);

        curBlock.addView(DrawBlock(blocks[0], 1));
        nextBlock1.addView(DrawBlock(blocks[1], 2));
        nextBlock2.addView(DrawBlock(blocks[2], 3));

        tvScore = (TextView)findViewById(R.id.tvScore);
        tvScore.setText("0");

        //효과음 및 배경음악
        mSoundManager = new SoundManager();
        mSoundManager.initSounds(getBaseContext());
        mSoundManager.addSound(1, R.raw.block1);
        mSoundManager.addSound(2, R.raw.remove);
        mSoundManager.addSound(3, R.raw.score);
        mSoundManager.addSound(4, R.raw.fail);
        mp = MediaPlayer.create(this, R.raw.bgm);

        //타이머
        pbTimer = (ProgressBar)findViewById(R.id.pbTimer);
        pbTimer.setMax(200);
        pbTimer.setProgress(200);
        //게임 상태 창
        layoutGameStateBg = (LinearLayout)findViewById(R.id.layoutGameStateBg);
        layoutGameState = (LinearLayout)findViewById(R.id.layoutGameState);
        tvGameModeTitle = (TextView)findViewById(R.id.tvGameModeTitle);
        tvGameModeInfo = (TextView)findViewById(R.id.tvGameModeInfo);
        layoutStatBtns = (LinearLayout)findViewById(R.id.layoutStateBtns);
        btnGameState = (Button)findViewById(R.id.btnGameState);

        tvGameModeTitle.setText("Game \nStart");
        tvGameModeInfo.setText("최대한 많은 라인을 클리어해서\n높은 점수를 획득하세요!");
        btnGameState.setText("시작");
        btnGameState.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                layoutGameStateBg.setVisibility(View.INVISIBLE);

                TempBoard.setOnTouchListener(BoardTouchEvent);
                curBlockBox.setOnTouchListener(blockTouchEvent);

                cdt = new CountDownTimer(1000 * 100000, 1000){
                    public void onTick (long millisUntilFinished){
                        if(pbTimer.getProgress() > 0){
                            pbTimer.setProgress(pbTimer.getProgress() - 1);
                        }else{
                            pbTimer.setProgress(0);
                            GameOver();
                        }
                    }
                    public void onFinish(){

                    }
                }.start();
                mp.start();
                return true;
            }
        });
        layoutGameStateBg.setVisibility(View.VISIBLE);
    }

    protected void GameOver(){
        mp.stop();
        mSoundManager.playSound(4);
        cdt.cancel();
        TempBoard.setOnTouchListener(null);
        curBlockBox.setOnTouchListener(null);
        tvGameModeTitle.setText("GameOver");
        tvGameModeInfo.setText("제한시간 종료! \n최종 스코어는 "+tvScore.getText()+"점 입니다!");
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
                mp.stop();
                finish();
                startActivity(new Intent(BasicGameActivity.this, BasicGameActivity.class));
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
            }
        });
        layoutStatBtns.addView(btnHome);
        layoutStatBtns.addView(btnReplay);
        layoutGameStateBg.setVisibility(View.VISIBLE);

    }

    protected TableLayout DrawBlock(Block block, int order)
    {
        //블록 배열 받아오기
        int[][] blockArr = block.getBlock();

        //블록 표시할 레이아웃 만들기
        TableLayout tbBlock = new TableLayout(this);
        TableRow[] trBlock = new TableRow[block.getRows()];

        //블록에 사용할 layoutParams
        LayoutParams tbLp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        tbLp.setMargins(DpToPx.getPx(this, 1), DpToPx.getPx(this, 1), DpToPx.getPx(this, 1), DpToPx.getPx(this, 1));
        tbLp.gravity = Gravity.CENTER;

        TableLayout.LayoutParams trLp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
        LayoutParams blockLp;

        //현재 블럭과 다음 블럭의 사이즈 차이
        if(order <= 1) {
            blockLp = new LayoutParams(DpToPx.getPx(this, 20), DpToPx.getPx(this, 20));
            blockLp.setMargins(DpToPx.getPx(this, 1), DpToPx.getPx(this, 1), DpToPx.getPx(this, 1), DpToPx.getPx(this, 1));
            blockLp.setMargins(DpToPx.getPx(this, 1), DpToPx.getPx(this, 1), DpToPx.getPx(this, 1), DpToPx.getPx(this, 1));
        }
        else {
            blockLp = new LayoutParams(DpToPx.getPx(this, 10), DpToPx.getPx(this, 10));
            blockLp.setMargins(DpToPx.getPx(this, 1), DpToPx.getPx(this, (float)0.5), DpToPx.getPx(this, (float)0.5), DpToPx.getPx(this, (float)0.5));
        }

        tbBlock.setLayoutParams(tbLp);
        tbBlock.setGravity(Gravity.CENTER);
        for(int i = 0; i < block.getRows(); i++)
        {
            trBlock[i] = new TableRow(this);
            trBlock[i].setLayoutParams(trLp);
            for(int j = 0; j < block.getCols(); j++)
            {
                ImageView blockImg = new ImageView(this);
                blockImg.setLayoutParams(blockLp);

                if(blockArr[i][j] > 0)
                {
                    switch(blockArr[i][j])
                    {
                        case 1:
                            blockImg.setBackgroundColor(getResources().getColor(R.color.bc1));
                            break;
                        case 2:
                            blockImg.setBackgroundColor(getResources().getColor(R.color.bc2));
                            break;
                        case 3:
                            blockImg.setBackgroundColor(getResources().getColor(R.color.bc3));
                            break;
                        case 4:
                            blockImg.setBackgroundColor(getResources().getColor(R.color.bc4));
                            break;
                        case 5:
                            blockImg.setBackgroundColor(getResources().getColor(R.color.bc5));
                            break;
                        case 6:
                            blockImg.setBackgroundColor(getResources().getColor(R.color.bc6));
                            break;
                        default:
                            blockImg.setBackgroundColor(getResources().getColor(R.color.bc7));
                            break;
                    }
                }
                trBlock[i].addView(blockImg);
            }
            tbBlock.addView(trBlock[i]);
        }
        return tbBlock;
    }

    protected void getNextBlock()
    {
        int bCode;
        while(true)
        {
            bCode = random.nextInt(7)+1;
            if(bCode != blocks[1].getColorCode() || bCode != blocks[2].getColorCode()) break;
        }

        blocks[0] = blocks[1];
        blocks[1] = blocks[2];
        blocks[2] = new Block(bCode);

        curBlock.removeAllViews();
        nextBlock1.removeAllViews();
        nextBlock2.removeAllViews();
        curBlock.addView(DrawBlock(blocks[0], 1));
        nextBlock1.addView(DrawBlock(blocks[1], 2));
        nextBlock2.addView(DrawBlock(blocks[2], 3));
    }

    protected View.OnTouchListener BoardTouchEvent = new View.OnTouchListener()
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            int action=event.getActionMasked();

            if(action==MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE)
            {
                RemoveBlock();
                Point point = new Point((int) event.getX(), (int) event.getY());
                getTouchedCell(point);
                //cellArr[TouchedCellRow][TouchedCellCol].setBackgroundColor(getResources().getColor(R.color.GBback));
                MarkBlock();
            }
            else if(action==MotionEvent.ACTION_UP){
                RemoveBlock(); //임시 블록을 지운다
                Point point = new Point((int) event.getX(), (int) event.getY());
                //포인트 위치가 게임보드 안일 때만 블록 드롭
                if(GameBoard.getLeft() <= point.x && point.x <= GameBoard.getRight()
                        && GameBoard.getTop() <= point.y && point.y <= GameBoard.getBottom()){
                    mSoundManager.playSound(1);
                    DropBlock();
                }
                else mSoundManager.playSound(2);
            }
            return true;
        }
    };

    protected void getTouchedCell(Point point)
    {
        //터치 및 이동은 게임판 안에서만 움직인다
        int CellWidth = (GameBoard.getRight() - GameBoard.getLeft()) / 15;
        int CellHeight = (GameBoard.getBottom() - GameBoard.getTop()) / 15;

        //셀의 index가 범위를 벗어나지 않았는지 확인
        //index 세로 최소 : 0
        //index 세로 최대 : iBoardSize - block[0].getRows()
        //index 가로 최소 : 0
        //index 가로 최대 : iBoardSize - block[0].getCols()

        TouchedCellCol = point.x / CellWidth;
        if (TouchedCellCol < 0) TouchedCellCol = 0;
        if (TouchedCellCol > iBoardSize - blocks[0].getCols()) TouchedCellCol = iBoardSize - blocks[0].getCols();

        TouchedCellRow = point.y / CellHeight;
        if (TouchedCellRow < 0) TouchedCellRow = 0;
        if (TouchedCellRow > iBoardSize - blocks[0].getRows()) TouchedCellRow = iBoardSize - blocks[0].getRows();
    }

    protected void RemoveBlock()
    {
        if(TouchedCellCol < 0 || TouchedCellRow < 0) return;

        int[][] block = blocks[0].getBlock(); //현재 블럭 가져옴

        for(int row = 0; row < blocks[0].getRows(); row++)
        {
            for(int col = 0; col < blocks[0].getCols(); col++)
            {
                if(block[row][col] > 0) {
                    tempCellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
    }

    protected void MarkBlock()
    {
        int[][] block = blocks[0].getBlock(); //현재 블럭 가져옴

        //tempCellArr[TouchedCellRow][TouchedCellCol].setBackgroundColor(getResources().getColor(R.color.GBback));

        for(int row = 0; row < blocks[0].getRows(); row++)
        {
            for(int col = 0; col < blocks[0].getCols(); col++)
            {
                if(block[row][col] > 0) {
                    tempCellArr[TouchedCellRow + row][TouchedCellCol + col].setBackgroundColor(Color.argb(50, 0, 0, 0));
                }
            }
        }
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
        pbTimer.setProgress(pbTimer.getProgress()+2);
        getNextBlock();
        CheckLine();
    }

    protected View.OnTouchListener blockTouchEvent = new View.OnTouchListener()
    {
        public boolean onTouch(View v, MotionEvent event)
        {
            int action=event.getActionMasked();
            Point point = new Point((int) event.getX(), (int) event.getY());

            if(action==MotionEvent.ACTION_UP &&
                    curBlockBox.getLeft() <= point.x && point.x <= curBlockBox.getRight()
                    && curBlockBox.getTop() <= point.y && point.y <= curBlockBox.getBottom())
            {
                blocks[0].TurnBlock();
                curBlock.removeAllViews();
                curBlock.addView(DrawBlock(blocks[0], 1));

            }
            return true;
        }
    };

    protected void CheckLine()
    {
        hLineCnt = 0;
        vLineCnt = 0;

        //가로 라인 체크
        for(int i = 0; i < iBoardSize; i++)
        {
            LineChk = false;
            for(int j = 0; j < iBoardSize; j++)
            {
                if(BoardArr[i][j] != 0) LineChk = true;
                else {
                    LineChk = false;
                    break;
                }
            }
            if(LineChk) hLineArr[hLineCnt++] = i;
        }

        //세로 라인 체크
        for(int i = 0; i < iBoardSize; i++)
        {
            LineChk = false;
            for(int j = 0; j < iBoardSize; j++)
            {
                if(BoardArr[j][i] != 0) LineChk = true;
                else {
                    LineChk = false;
                    break;
                }
            }
            if(LineChk) vLineArr[vLineCnt++] = i;
        }

        if(hLineCnt > 0 || vLineCnt > 0)
        {
            //점수 증가
            int score = hLineCnt * 100 + vLineCnt * 100;                    //이번 턴 점수
            int curScore = Integer.parseInt(tvScore.getText().toString());   //기존 점수
            tvScore.setText(Integer.toString(curScore + score));
            mSoundManager.playSound(3);
            LineClear(true);
        }
    }

    protected void LineClear(boolean run)
    {
        if(hLineCnt > 0)
        {
            for(int i = hLineCnt ;i > 0; i--)
            {
                for(int j = 0; j < 15; j++)
                {
                    BoardArr[hLineArr[i-1]][j] = 0;
                    if(run)
                        cellArr[hLineArr[i-1]][j].setBackgroundResource(R.drawable.highlight);
                    else
                        cellArr[hLineArr[i-1]][j].setBackgroundResource(R.drawable.noblock);
                }
            }
        }
        if(vLineCnt > 0)
        {
            for(int i = vLineCnt ;i > 0; i--)
            {
                for(int j = 0; j < 15; j++)
                {
                    BoardArr[j][vLineArr[i-1]] = 0;
                    if(run)
                        cellArr[j][vLineArr[i-1]].setBackgroundResource(R.drawable.highlight);
                    else
                        cellArr[j][vLineArr[i-1]].setBackgroundResource(R.drawable.noblock);
                }
            }
        }
        if(!run) {
            reOrder();
            return;
        }
        /*
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            public void run() {
                LineClear(false);
            }
        }, 100);
        */
        LineClear(false);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            cdt.cancel();
            TempBoard.setOnTouchListener(null);
            curBlockBox.setOnTouchListener(null);
            tvGameModeTitle.setText("Pause");
            tvGameModeInfo.setText("게임이 일시정지 되었습니다!");
            layoutStatBtns.removeAllViews();
            LinearLayout.LayoutParams stateBtn1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,  1);
            LinearLayout.LayoutParams stateBtn2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT,  2);
            stateBtn1.setMargins(5, 5, 5, 5);
            stateBtn2.setMargins(5, 5, 5, 5);
            Button btnHome = new Button(this);
            Button btnReplay = new Button(this);

            btnReplay.setLayoutParams(stateBtn1);
            btnReplay.setBackgroundColor(getResources().getColor(R.color.ThemeRed));
            btnReplay.setTextColor(getResources().getColor(R.color.ThemeBeige));
            btnReplay.setText("다시시작");
            btnReplay.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v){
                    layoutGameStateBg.setVisibility(View.INVISIBLE);

                    TempBoard.setOnTouchListener(BoardTouchEvent);
                    curBlockBox.setOnTouchListener(blockTouchEvent);

                    cdt = new CountDownTimer(1000 * 100, 1000){
                        public void onTick (long millisUntilFinished){
                            if(pbTimer.getProgress() > 0){
                                pbTimer.setProgress(pbTimer.getProgress() - 1);
                            }
                        }
                        public void onFinish(){
                            pbTimer.setProgress(0);
                            GameOver();
                        }
                    }.start();
                    mp.start();
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
                }
            });
            layoutStatBtns.addView(btnHome);
            layoutStatBtns.addView(btnReplay);
            layoutGameStateBg.setVisibility(View.VISIBLE);
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    public int getLcdSizeWidth(){
        return ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }
}