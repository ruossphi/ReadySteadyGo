package com.readysteadygo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.readysteadygo.R;
import com.readysteadygo.entity.Survey;
import com.readysteadygo.handler.UploadSurveyData;
import com.readysteadygo.interfaces.AsynchResponse;
import com.readysteadygo.interfaces.AsynchResponseParent;

public class GameEvaluationActivity extends Activity implements View.OnClickListener, AsynchResponseParent {

    private Button sendEvaluationBtn;
    private RatingBar ratingBarOne;
    private RatingBar ratingBarTwo;
    private RatingBar ratingBarThree;
    private RatingBar ratingBarFour;
    private RatingBar ratingBarFive;
    private SharedPreferences prefs;
    private Survey survey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.evaluation_activity);

        sendEvaluationBtn = findViewById(R.id.evaluationSendBtn);
        sendEvaluationBtn.setOnClickListener(this);

        ratingBarOne = findViewById(R.id.ratingBar);
        ratingBarTwo = findViewById(R.id.ratingBar2);
        ratingBarThree = findViewById(R.id.ratingBar3);
        ratingBarFour = findViewById(R.id.ratingBar4);
        ratingBarFive = findViewById(R.id.ratingBar5);
    }

    public boolean checkAreAllRatingBarsSet(){
        if(ratingBarOne.getRating() != 0.0 && ratingBarTwo.getRating() != 0.0 && ratingBarThree.getRating() != 0.0 && ratingBarFour.getRating() != 0.0 && ratingBarFive.getRating() != 0.0){
            survey = new Survey();
            prefs = getApplicationContext().getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            survey.setMonetization(prefs.getString("monetization", ""));

            survey.setAnswer1((int) (ratingBarOne.getRating() * 20.0));
            survey.setAnswer2((int) (ratingBarTwo.getRating() * 20.0));
            survey.setAnswer3((int) (ratingBarThree.getRating() * 20.0));
            survey.setAnswer4((int) (ratingBarFour.getRating() * 20.0));
            survey.setAnswer5((int) (ratingBarFive.getRating() * 20.0));

            return true;
        }else return false;
    }

    @Override
    public void onClick(View v) {
        // Send Data to Server
        if(checkAreAllRatingBarsSet()){

            UploadSurveyData uploadSurveyData = new UploadSurveyData(getApplicationContext());
            uploadSurveyData.delegate2 = this;
            uploadSurveyData.executeAsyncSurveyUpload(survey);

        }else Toast.makeText(this,  getResources().getString(R.string.evaluation_missing_field), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processFinish() {
        if(prefs.getBoolean("Upload_OK", false)){
            Toast.makeText(this, getResources().getString(R.string.evaluation_feedback_success), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(GameEvaluationActivity.this, GameSelectMonetizationActivity.class));
            finish();
        }
    }
}