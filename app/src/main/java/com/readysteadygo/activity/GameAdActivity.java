package com.readysteadygo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import com.readysteadygo.R;


public class GameAdActivity extends Activity implements View.OnClickListener{

    private Button skipBtn;
    private final int DELAY_LENGTH = 8000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_activity);

        skipBtn = findViewById(R.id.adBtnSkip);
        skipBtn.setOnClickListener(this);
        skipBtn.setVisibility(View.INVISIBLE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skipBtn.setVisibility(View.VISIBLE);
            }
        }, DELAY_LENGTH);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(GameAdActivity.this, GameMenuActivity.class));
        finish();
    }
}