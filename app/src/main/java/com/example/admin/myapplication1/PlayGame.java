package com.example.admin.myapplication1;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class PlayGame extends AppCompatActivity implements OnClickListener {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    private Bitmap image;
    private int counter = 0;
    ShareButton shareButton = null;


    private int level = 0, answer = 0, operator = 0, operand1 = 0, operand2 = 0;
    private final int ADD_OPERATOR = 0, SUBTRACT_OPERATOR = 1, MULTIPLY_OPERATOR = 2, DIVIDE_OPERATOR = 3;


    private String[] operators = {"+", "-", "x", "/"};
    private int[][] levelMin = {
            {1, 11, 21},
            {1, 5, 10},
            {2, 5, 10},
            {2, 3, 5}};
    private int[][] levelMax = {
            {10, 25, 50},
            {10, 20, 30},
            {5, 10, 15},
            {10, 50, 100}};
    private Random random;
    private TextView question, answerTxt, scoreTxt;
    private ImageView response;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, enterBtn, clearBtn;
    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ArithmeticFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        shareButton = (ShareButton)findViewById(R.id.fb_share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postPicture();
            }

        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playgame);
        gamePrefs = getSharedPreferences(GAME_PREFS, 0);
        question =  (TextView)findViewById(R.id.question);
        answerTxt = (TextView)findViewById(R.id.answer);
        response =  (ImageView)findViewById(R.id.response);
        scoreTxt =  (TextView)findViewById(R.id.score);
        response.setVisibility(View.INVISIBLE);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
        enterBtn = (Button)findViewById(R.id.enter);
        clearBtn = (Button)findViewById(R.id.clear);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);
        btn8.setOnClickListener(this);
        btn9.setOnClickListener(this);
        btn0.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        if(savedInstanceState!=null){
            //restore state
            level=savedInstanceState.getInt("level");
            int exScore = savedInstanceState.getInt("score");
            scoreTxt.setText("Score: "+exScore);
        }
        else{
            Bundle extras = getIntent().getExtras();
            if(extras !=null)
            {
                int passedLevel = extras.getInt("level", -1);
                if(passedLevel>=0) level = passedLevel;
            }
        }
        random = new Random();
        chooseQuestion();
    }
    public void postPicture() {
        //callbackManager = CallbackManager.Factory.create();
       // shareDialog = new ShareDialog(this);
        //check counter
        if(counter == 0) {
            //save the screenshot
            View rootView = findViewById(android.R.id.content).getRootView();
            rootView.setDrawingCacheEnabled(true);
            // creates immutable clone of image
            image = Bitmap.createBitmap(rootView.getDrawingCache());
            // destroy
            rootView.destroyDrawingCache();

            //share dialog
            AlertDialog.Builder shareDialog = new AlertDialog.Builder(this);
            shareDialog.setTitle("Share Screen Shot");
            shareDialog.setMessage("Share image to Facebook?");
            shareDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //share the image to Facebook
                    SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
                    SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
                    shareButton.setShareContent(content);
                    counter = 1;
                    shareButton.performClick();
                }
            });
            shareDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            shareDialog.show();
        }
        else {
            counter = 0;
            shareButton.setShareContent(null);
        }
    }
    private void chooseQuestion(){
//get a question
        answerTxt.setText("= ?");
        operator = random.nextInt(operators.length);
        operand1 = getOperand();
        operand2 = getOperand();
        if(operator == SUBTRACT_OPERATOR){
            while(operand2>operand1){
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }
        else if(operator==DIVIDE_OPERATOR){
            while((((double)operand1/(double)operand2)%1 > 0) || (operand1==operand2))
            {
                operand1 = getOperand();
                operand2 = getOperand();
            }
        }
        switch(operator)
        {
            case ADD_OPERATOR:
                answer = operand1+operand2;
                break;
            case SUBTRACT_OPERATOR:
                answer = operand1-operand2;
                break;
            case MULTIPLY_OPERATOR:
                answer = operand1*operand2;
                break;
            case DIVIDE_OPERATOR:
                answer = operand1/operand2;
                break;
            default:
                break;
        }
        question.setText(operand1+" "+operators[operator]+" "+operand2);
    }
    private int getOperand(){
//return operand number
        return random.nextInt(levelMax[operator][level] - levelMin[operator][level] + 1)
                + levelMin[operator][level];
    }
    @Override
    public void onClick(View view) {
        //button clicked

        if(view.getId()==R.id.enter){
            //enter button
            String answerContent = answerTxt.getText().toString();
            if(!answerContent.endsWith("?"))
            {
                //we have an answer
                int enteredAnswer = Integer.parseInt(answerContent.substring(2));
                int exScore = getScore();
                if(enteredAnswer==answer){
                    //correct
                    scoreTxt.setText("Score: "+(exScore+1));
                    response.setImageResource(R.drawable.tick);
                    response.setVisibility(View.VISIBLE);
                }
                else{
                    //incorrect
                    setHighScore();
                    scoreTxt.setText("Score: 0");
                    response.setImageResource(R.drawable.cross);
                    response.setVisibility(View.VISIBLE);
                }
                chooseQuestion();

            }
        }
        else if(view.getId()==R.id.clear){
            //clear button
            answerTxt.setText("= ?");
        }
        else {
            //number button
            int enteredNum = Integer.parseInt(view.getTag().toString());
            response.setVisibility(View.INVISIBLE);
            if(answerTxt.getText().toString().endsWith("?"))
                answerTxt.setText("= "+enteredNum);
            else
                answerTxt.append(""+enteredNum);
        }
    }
    private int getScore(){
        String scoreStr = scoreTxt.getText().toString();
        return Integer.parseInt(scoreStr.substring(scoreStr.lastIndexOf(" ")+1));
    }

    private void setHighScore(){
        //set high score
        int exScore = getScore();
        if(exScore>0){

            //we have a valid score
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            DateFormat dateForm = new SimpleDateFormat("dd MMMM yyyy");
            String dateOutput = dateForm.format(new Date());
            String scores = gamePrefs.getString("highScores", "");
            if(scores.length()>0){
                //we have existing scores
                List<Score> scoreStrings = new ArrayList<Score>();
                String[] exScores = scores.split("\\|");
                for(String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));

                }
                Score newScore = new Score(dateOutput, exScore);
                scoreStrings.add(newScore);
                Collections.sort(scoreStrings);
                StringBuilder scoreBuild = new StringBuilder("");
                for(int s=0; s<scoreStrings.size(); s++){
                    if(s>=10) break;//only want ten
                    if(s>0) scoreBuild.append("|");//pipe separate the score strings
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                //write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.apply();

            }
            else{
                //no existing scores
                scoreEdit.putString("highScores", ""+dateOutput+" - "+exScore);
                scoreEdit.apply();
            }

        }

    }
    protected void onDestroy(){
        setHighScore();
        super.onDestroy();
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
//save state
        int exScore = getScore();
        savedInstanceState.putInt("score", exScore);
        savedInstanceState.putInt("level", level);
        super.onSaveInstanceState(savedInstanceState);
    }
}

