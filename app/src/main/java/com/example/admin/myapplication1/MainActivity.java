package com.example.admin.myapplication1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button playBtn, helpBtn, highBtn;
    private String[] levelNames = {"Easy", "Medium", "Hard"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playBtn = (Button) findViewById(R.id.play_btn);
        helpBtn = (Button) findViewById(R.id.help_btn);
        highBtn = (Button) findViewById(R.id.high_btn);
        playBtn.setOnClickListener(this);
        helpBtn.setOnClickListener(this);
        highBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

//respond to clicks
        if (view.getId() == R.id.play_btn) {
            //play button
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Choose a level")
                    .setSingleChoiceItems(levelNames, 0, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            //start gameplay
                            startPlay(which);
                        }
                    });
            AlertDialog ad = builder.create();
            ad.show();
        } else if (view.getId() == R.id.help_btn) {
            //how to play button
            Intent helpIntent = new Intent(this, HowToPlay.class);
            this.startActivity(helpIntent);
        } else if (view.getId() == R.id.high_btn) {
            //high scores button
            Intent highIntent = new Intent(this, HighScores.class);
            this.startActivity(highIntent);
        }
    }
    private void startPlay(int chosenLevel)
    {
        //start gameplay
        Intent playIntent = new Intent(this, PlayGame.class);
        playIntent.putExtra("level", chosenLevel);
        this.startActivity(playIntent);
    }
}


