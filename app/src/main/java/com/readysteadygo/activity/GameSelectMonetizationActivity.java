package com.readysteadygo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.readysteadygo.R;

public class GameSelectMonetizationActivity extends Activity implements View.OnClickListener {

    private Button monetizationBtnOne;
    private Button monetizationBtnTwo;
    private Button monetizationBtnThree;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_monetization_activity);

        monetizationBtnOne = findViewById(R.id.moOneBtn);
        monetizationBtnTwo = findViewById(R.id.moTwoBtn);
        monetizationBtnThree = findViewById(R.id.moThreeBtn);

        monetizationBtnOne.setOnClickListener(this);
        monetizationBtnTwo.setOnClickListener(this);
        monetizationBtnThree.setOnClickListener(this);
    }

    public void storeMonetizationInfo(String modus){
        prefs = getApplicationContext().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString("monetization", modus);
        editor.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moOneBtn:
                storeMonetizationInfo("premium");
                startActivity(new Intent(GameSelectMonetizationActivity.this, GameMenuActivity.class));
                finish();
                break;
            case R.id.moTwoBtn:
                storeMonetizationInfo("ads");
                startActivity(new Intent(GameSelectMonetizationActivity.this, GameMenuActivity.class));
                finish();
                break;
            case R.id.moThreeBtn:
                storeMonetizationInfo("store");
                startActivity(new Intent(GameSelectMonetizationActivity.this, GameMenuActivity.class));
                finish();
                break;

        }
    }
}