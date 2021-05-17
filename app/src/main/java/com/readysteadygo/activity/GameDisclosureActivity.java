package com.readysteadygo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.readysteadygo.R;

public class GameDisclosureActivity extends Activity implements View.OnClickListener{

    private Button disclosureAgreeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.disclosure_activity);

        disclosureAgreeBtn = findViewById(R.id.disclosureAgreeBtn);
        disclosureAgreeBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(GameDisclosureActivity.this, GameSelectMonetizationActivity.class));
        finish();
    }
}