package com.readysteadygo.handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.readysteadygo.interfaces.AsynchResponse;
import com.readysteadygo.interfaces.AsynchResponseParent;
import com.readysteadygo.entity.Survey;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class UploadSurveyData implements AsynchResponse{

    private Context context;
    private  final String postReceiverUrl = "http://readysteadygo.simplecoding.ch/upload.php";
    private static final String TAG = "UploadSurvey.java";
    private Survey survey;
    public AsynchResponseParent delegate2 = null;

    public UploadSurveyData(Context mContext){
        this.context = mContext;
    }

    public void executeAsyncSurveyUpload(Survey survey){
        this.survey = survey;

        UploadSurveyAsyncTask uploadSurveyAsyncTask = new UploadSurveyAsyncTask();
        uploadSurveyAsyncTask.delegate = this;
        uploadSurveyAsyncTask.execute();
    }

    private class UploadSurveyAsyncTask extends AsyncTask<Void, Void, Void> {

        public AsynchResponse delegate = null;

        @Override
        protected Void doInBackground(Void... voids) {
            post();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            delegate.processFinish();
        }
    }

    private void post(){
        try{

            Log.v(TAG, "postURL: " + postReceiverUrl);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost( postReceiverUrl);

            // add data
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("modus", survey.getMonetization()));
            params.add(new BasicNameValuePair("answerOne", String.valueOf(survey.getAnswer1())));
            params.add(new BasicNameValuePair("answerTwo",  String.valueOf(survey.getAnswer2())));
            params.add(new BasicNameValuePair("answerThree", String.valueOf(survey.getAnswer3())));
            params.add(new BasicNameValuePair("answerFour", String.valueOf(survey.getAnswer4())));
            params.add(new BasicNameValuePair("answerFive", String.valueOf(survey.getAnswer5())));

            httpPost.setEntity(new UrlEncodedFormEntity(params));

            // execute HTTP post request
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {

                String responseStr = EntityUtils.toString(resEntity).trim();
                Log.v(TAG, "Response: " +  responseStr);

                String checksResponseStrConquer = "successfully";

                // if upload on Server was successfully
                if (responseStr.toLowerCase().contains(checksResponseStrConquer.toLowerCase()) == true) {
                    Log.v(TAG, "upload successful");
                    SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("Upload_OK", true);
                    editor.commit();
                }
                else {
                    Log.v(TAG, "could not Update in DB");
                    SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("Upload_OK", false);
                    editor.commit();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.v(TAG, "could not Update in DB");
            SharedPreferences prefs = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Upload_OK", false);
            editor.commit();
        }
    }

    @Override
    public void processFinish() {
        try{
            delegate2.processFinish();
        }catch (Exception e){}
    }
}
