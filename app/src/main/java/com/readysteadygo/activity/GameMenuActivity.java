package com.readysteadygo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.readysteadygo.R;

public class GameMenuActivity extends Activity implements View.OnClickListener {

    private Button menuPlayBtn;
    private Button menuVsPlayBtn;
    private Button evaluationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);

        menuPlayBtn = findViewById(R.id.playGameBtn);
        menuVsPlayBtn = findViewById(R.id.playVsGameBtn);
        evaluationBtn = findViewById(R.id.evaluationMenuBtn);

        menuPlayBtn.setOnClickListener(this);
        menuVsPlayBtn.setOnClickListener(this);
        evaluationBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playGameBtn:
                startActivity(new Intent(GameMenuActivity.this, GamePlayActivity.class));
                finish();
                break;
            case R.id.playVsGameBtn:
                startActivity(new Intent(GameMenuActivity.this, GamePlayVsActivity.class));
                finish();
                break;
            case R.id.evaluationMenuBtn:
                startActivity(new Intent(GameMenuActivity.this, GameEvaluationActivity.class));
                finish();
                break;
        }
    }
}