package com.readysteadygo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.readysteadygo.R;
import java.util.Random;

public class GamePlayVsActivity extends Activity {

    private TextView gameTextP1;
    private TextView gameTimeTextP1;
    private TextView gameTextP2;
    private TextView gameTimeTextP2;
    private FrameLayout touchFrameP1;
    private FrameLayout touchFrameP2;
    private final int DELAY_LENGTH = 1500;
    private final int DELAY_LENGTH_LONG = 4000;

    private Boolean roundRunningStatus = false;
    private Boolean earlyPressP1 = false;
    private Boolean earlyPressP2 = false;
    private Boolean gameOver = false;

    private Boolean pressStatusP1 = false;
    private Boolean pressStatusP2 = false;

    Handler handler;

    private int p1Life;
    private int p2Life;

    private ImageView heartPlayerOne;
    private ImageView heartPlayerTwo;
    private ImageView heartPlayerThree;

    private ImageView heartEnemyOne;
    private ImageView heartEnemyTwo;
    private ImageView heartEnemyThree;

    private MediaPlayer mpShot;
    private MediaPlayer mpFail;
    private MediaPlayer mpSuccess;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
    int Seconds, MilliSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_vs_activity);

        gameTextP1 = findViewById(R.id.playGameTxtVsP1);
        gameTextP2 = findViewById(R.id.playGameTxtVsP2);
        gameTimeTextP1 = findViewById(R.id.playGameTimeTxtVsP1);
        gameTimeTextP2 = findViewById(R.id.playGameTimeTxtVsP2);
        touchFrameP1 = findViewById(R.id.gamePlayTouchFrameVsP1);
        touchFrameP2 = findViewById(R.id.gamePlayTouchFrameVsP2);

        heartEnemyOne = findViewById(R.id.heartEnemy1Vs);
        heartEnemyTwo = findViewById(R.id.heartEnemy2Vs);
        heartEnemyThree = findViewById(R.id.heartEnemy3Vs);

        heartPlayerOne = findViewById(R.id.heartPlayer1Vs);
        heartPlayerTwo = findViewById(R.id.heartPlayer2Vs);
        heartPlayerThree = findViewById(R.id.heartPlayer3Vs);

        handler = new Handler() ;

        mpShot = MediaPlayer.create(this, R.raw.gun_shot);
        mpFail = MediaPlayer.create(this, R.raw.buzzer);
        mpSuccess = MediaPlayer.create(this, R.raw.yee_haw_sound);

        touchFrameP1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpShot.start();
                if(!roundRunningStatus){
                    earlyPressP1 = true;
                }
                roundRunningStatus = false;
                if(!pressStatusP2){
                    pressStatusP1 = true;
                    evaluateResult();
                }
            }
        });

        touchFrameP2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpShot.start();
                if(!roundRunningStatus){
                    earlyPressP2 = true;
                }
                roundRunningStatus = false;
                if(!pressStatusP1){
                    pressStatusP2 = true;
                    evaluateResult();
                }
            }
        });

        startGame();
    }

    public void startGame(){
        p1Life = 3;
        p2Life = 3;
        startCountdown();
    }

    public void startCountdown(){
        gameTimeTextP1.setText("00:000");
        gameTimeTextP2.setText("00:000");
        roundRunningStatus = false;
        earlyPressP1 = false;
        earlyPressP2 = false;
        pressStatusP1 = false;
        pressStatusP2 = false;
        MilliSeconds = 0;
        displayCountdown();
    }

    public void displayCountdown(){

        if(!earlyPressP1 && !earlyPressP2 && !roundRunningStatus && !gameOver){
            gameTextP1.setText(getResources().getString(R.string.play_game_ready_text));
            gameTextP2.setText(getResources().getString(R.string.play_game_ready_text));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!earlyPressP1 && !earlyPressP2 && !roundRunningStatus && !gameOver){
                        gameTextP1.setText(getResources().getString(R.string.play_game_steady_text));
                        gameTextP2.setText(getResources().getString(R.string.play_game_steady_text));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!earlyPressP1 && !earlyPressP2 && !roundRunningStatus && !gameOver) {
                                    gameTextP1.setText(getResources().getString(R.string.play_game_go_text));
                                    gameTextP2.setText(getResources().getString(R.string.play_game_go_text));
                                    roundRunningStatus = true;
                                    startTimer();
                                }
                            }
                        }, (long)  1500 + new Random().nextInt(2000));
                    }
                }
            }, DELAY_LENGTH);
        }
    }

    public void startTimer(){
        if(roundRunningStatus){
            StartTime = SystemClock.uptimeMillis();
            runnable.run();
            earlyPressP1 = false;
            earlyPressP2 = false;
        }
    }

    public void evaluateResult(){

        if(!earlyPressP1 && pressStatusP1){
            p2Life -= 1;
            gameTextP1.setText(getResources().getString(R.string.play_game_your_point_text));
            gameTextP2.setText(getResources().getString(R.string.play_game_lost_text));
        }else if(!earlyPressP2 && pressStatusP2){
            p1Life -= 1;
            gameTextP2.setText(getResources().getString(R.string.play_game_your_point_text));
            gameTextP1.setText(getResources().getString(R.string.play_game_lost_text));
        }else if(earlyPressP1){
            p1Life -= 1;
            mpFail.start();
        }else if(earlyPressP2){
            p2Life -= 1;
            mpFail.start();
        }

        updateUI();

        if(p1Life == 0){
            updateGameOverScreen(false);
        }else if(p2Life == 0) {
            updateGameOverScreen(true);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCountdown();
                }
            }, DELAY_LENGTH);
        }
    }

    public void updateGameOverScreen(boolean winner){
        gameOver = true;
        if(winner){
            gameTextP1.setText(getResources().getString(R.string.play_game_won_text));
            gameTextP2.setText(getResources().getString(R.string.play_game_lost_text));
        }else {
            gameTextP1.setText(getResources().getString(R.string.play_game_lost_text));
            gameTextP2.setText(getResources().getString(R.string.play_game_won_text));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchEvaluationActivity();
            }
        }, DELAY_LENGTH_LONG);
    }

    public void updateUI(){

        switch (p1Life) {
            case 3:
                heartPlayerOne.setBackgroundResource(R.drawable.heart_full);
                heartPlayerTwo.setBackgroundResource(R.drawable.heart_full);
                heartPlayerThree.setBackgroundResource(R.drawable.heart_full);
                break;
            case 2:
                heartPlayerOne.setBackgroundResource(R.drawable.heart_full);
                heartPlayerTwo.setBackgroundResource(R.drawable.heart_full);
                heartPlayerThree.setBackgroundResource(R.drawable.heart_empty);
                break;
            case 1:
                heartPlayerOne.setBackgroundResource(R.drawable.heart_full);
                heartPlayerTwo.setBackgroundResource(R.drawable.heart_empty);
                heartPlayerThree.setBackgroundResource(R.drawable.heart_empty);
                break;
            case 0:
                heartPlayerOne.setBackgroundResource(R.drawable.heart_empty);
                heartPlayerTwo.setBackgroundResource(R.drawable.heart_empty);
                heartPlayerThree.setBackgroundResource(R.drawable.heart_empty);
                break;
        }

        switch (p2Life) {
            case 3:
                heartEnemyOne.setBackgroundResource(R.drawable.heart_full);
                heartEnemyTwo.setBackgroundResource(R.drawable.heart_full);
                heartEnemyThree.setBackgroundResource(R.drawable.heart_full);
                break;
            case 2:
                heartEnemyOne.setBackgroundResource(R.drawable.heart_full);
                heartEnemyTwo.setBackgroundResource(R.drawable.heart_full);
                heartEnemyThree.setBackgroundResource(R.drawable.heart_empty);
                break;
            case 1:
                heartEnemyOne.setBackgroundResource(R.drawable.heart_full);
                heartEnemyTwo.setBackgroundResource(R.drawable.heart_empty);
                heartEnemyThree.setBackgroundResource(R.drawable.heart_empty);
                break;
            case 0:
                heartEnemyOne.setBackgroundResource(R.drawable.heart_empty);
                heartEnemyTwo.setBackgroundResource(R.drawable.heart_empty);
                heartEnemyThree.setBackgroundResource(R.drawable.heart_empty);
                break;
        }
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Seconds = Seconds % 60;
            MilliSeconds = (int) (UpdateTime % 1000);

            if(roundRunningStatus){
                gameTimeTextP1.setText(""
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
                gameTimeTextP2.setText(""
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
                handler.post(this);
            }
        }
    };

    public void launchEvaluationActivity(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        switch (prefs.getString("monetization", "")) {
            case "paid":
                startActivity(new Intent(GamePlayVsActivity.this, GamePlayActivity.class));
                finish();
                break;
            case "ads":
                startActivity(new Intent(GamePlayVsActivity.this, GameAdActivity.class));
                finish();
                break;
            case "store":
                startActivity(new Intent(GamePlayVsActivity.this, GameStoreActivity.class));
                finish();
                break;
            case "premium":
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GamePlayVsActivity.this, GameMenuActivity.class));
        finish();
    }

}