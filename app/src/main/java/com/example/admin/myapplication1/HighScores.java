package com.example.admin.myapplication1;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.internal.WebDialog;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

public class HighScores extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high);
       // FacebookSdk.sdkInitialize(getApplicationContext());



        TextView scoreView = (TextView)findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(PlayGame.GAME_PREFS, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");
        StringBuilder scoreBuild = new StringBuilder("");
        for(String score : savedScores){
            scoreBuild.append(score+"\n");
        }
        scoreView.setText(scoreBuild.toString());

    }


}