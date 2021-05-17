package com.readysteadygo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.readysteadygo.R;

public class GameStoreActivity extends Activity implements View.OnClickListener{

    private Button backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_activity);

        backbtn = findViewById(R.id.storeBackBtn);
        backbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(GameStoreActivity.this, GameMenuActivity.class));
        finish();
    }
}