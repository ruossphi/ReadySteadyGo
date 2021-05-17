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

public class GamePlayActivity extends Activity {

    private TextView gameText;
    private TextView gameTimeText;
    private FrameLayout touchFrame;
    private final int DELAY_LENGTH = 1500;
    private final int DELAY_LENGTH_LONG = 4000;
    private final int REACTION_TIME = 350;

    private Boolean roundRunningStatus = false;
    private Boolean earlyPress = false;
    private Boolean gameOver = false;

    Handler handler;
    private int playerLife;
    private int enemyLife;

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
        setContentView(R.layout.play_activity);

        gameText = findViewById(R.id.playGameTxt);
        gameTimeText = findViewById(R.id.playGameTimeTxt);
        touchFrame = findViewById(R.id.gamePlayTouchFrame);

        heartEnemyOne = findViewById(R.id.heartEnemy1);
        heartEnemyTwo = findViewById(R.id.heartEnemy2);
        heartEnemyThree = findViewById(R.id.heartEnemy3);

        heartPlayerOne = findViewById(R.id.heartPlayer1);
        heartPlayerTwo = findViewById(R.id.heartPlayer2);
        heartPlayerThree = findViewById(R.id.heartPlayer3);

        handler = new Handler() ;

        mpShot = MediaPlayer.create(this, R.raw.gun_shot);
        mpFail = MediaPlayer.create(this, R.raw.buzzer);
        mpSuccess = MediaPlayer.create(this, R.raw.yee_haw_sound);

        touchFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mpShot.start();
                if(!roundRunningStatus){
                    earlyPress = true;
                }
                roundRunningStatus = false;
                evaluateResult();
            }
        });

        startGame();
    }

    public void startGame(){
        playerLife = 3;
        enemyLife = 3;
        startCountdown();
    }

    public void startCountdown(){
        gameTimeText.setText("00:000");
        roundRunningStatus = false;
        earlyPress = false;
        MilliSeconds = 0;
        displayCountdown();
    }

    public void displayCountdown(){

        if(!earlyPress && !roundRunningStatus && !gameOver){
            gameText.setText(getResources().getString(R.string.play_game_ready_text));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!earlyPress && !roundRunningStatus && !gameOver){
                        gameText.setText(getResources().getString(R.string.play_game_steady_text));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(!earlyPress && !roundRunningStatus && !gameOver) {
                                    gameText.setText(getResources().getString(R.string.play_game_go_text));
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
            earlyPress = false;
        }
    }

    public void evaluateResult(){

        if(UpdateTime <= REACTION_TIME && !earlyPress){
            enemyLife -= 1;
            gameText.setText(getResources().getString(R.string.play_game_your_point_text));
        }else {
            playerLife -= 1;
            gameText.setText(getResources().getString(R.string.play_game_enemy_point_text));
        }

        updateUI();

        if(enemyLife == 0){
            mpSuccess.start();
            updateGameOverScreen(true);
        }else if(playerLife == 0) {
            mpFail.start();
            updateGameOverScreen(false);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startCountdown();
                }
            }, DELAY_LENGTH);
        }
    }

    public void updateGameOverScreen(boolean won){
        gameOver = true;
        if(won){
            gameText.setText(getResources().getString(R.string.play_game_won_text));
        }else {
            gameText.setText(getResources().getString(R.string.play_game_lost_text));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                launchEvaluationActivity();
            }
        }, DELAY_LENGTH_LONG);
    }

    public void updateUI(){

        switch (playerLife) {
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

        switch (enemyLife) {
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
                gameTimeText.setText(""
                        + String.format("%02d", Seconds) + ":"
                        + String.format("%03d", MilliSeconds));
                handler.post(this);
            }


        }
    };

    public void launchEvaluationActivity(){
        SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);

        switch (prefs.getString("monetization", "")) {
            case "premium":
                startActivity(new Intent(GamePlayActivity.this, GameMenuActivity.class));
                finish();
                break;
            case "ads":
                startActivity(new Intent(GamePlayActivity.this, GameAdActivity.class));
                finish();
                break;
            case "store":
                startActivity(new Intent(GamePlayActivity.this, GameStoreActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GamePlayActivity.this, GameMenuActivity.class));
        finish();
    }

}