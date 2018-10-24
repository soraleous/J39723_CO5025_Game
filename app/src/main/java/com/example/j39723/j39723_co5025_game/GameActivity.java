package com.example.j39723.j39723_co5025_game;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import android.content.SharedPreferences;

import com.example.j39723.j39723_co5025_game.model.Score;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables
    private ImageButton[] buttons = new ImageButton[10];
    private int count = 0;
    private int[] myImageArr = new int[]{R.drawable.facebook128x128, R.drawable.facebook128x128, R.drawable.google128x128, R.drawable.google128x128,
                                            R.drawable.tumblr128x128, R.drawable.tumblr128x128, R.drawable.youtube128x128, R.drawable.youtube128x128};
    private Object selectedButton1 = null;
    private Object selectedButton2 = null;
    public int time;
    private Timer timer;
    private TextView timerTextView;
    private boolean startTime = true;

    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ScoreFile";

    public String userName;
    // End of Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        // Get Intent Bundle
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userName = bundle.getString("username");
        System.out.println(userName);

        gamePrefs = getSharedPreferences(GAME_PREFS, 0);

        // FloatingActionButton used for closing Activity
        FloatingActionButton fab = findViewById(R.id.closeFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timer.purge();
                timerTextView.setText(R.string.timer_number);
                finish();
            }
        });

        // ImageButtons creation
        randomize(myImageArr, myImageArr.length);
        buttons[1] = findViewById(R.id.one);
        buttons[2] = findViewById(R.id.two);
        buttons[3] = findViewById(R.id.three);
        buttons[4] = findViewById(R.id.four);
        buttons[5] = findViewById(R.id.five);
        buttons[6] = findViewById(R.id.six);
        buttons[7] = findViewById(R.id.seven);
        buttons[8] = findViewById(R.id.eight);
        // Since Button 9 would not be used
        // buttons[9] = findViewById(R.id.nine);

        // Set OnClick Listener for all eight usable buttons
        for (int i = 1; i <= 8 ; i++) {
            buttons[i].setOnClickListener(this);
        }
        // clearGrid to reset grid buttons to default drawables
        clearGrid();
        // End of ImageButtons creation

        timerTextView = findViewById(R.id.timerNum);
        //Start timer and delays here
        if (startTime){
            time = 0;
            startTimer();
            // enableGrid to enable the buttons for use
            enableGrid();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //Only setup for eight buttons as ninth is disabled/unused
            case R.id.one :
                count++;
                makeMove(1);
                if (count == 2){
                    flipBack();
                }
                break;
            case R.id.two :
                count++;
                makeMove(2);
                if (count == 2){
                    flipBack();
                }
                break;
            case R.id.three :
                count++;
                makeMove(3);
                if (count == 2){
                    flipBack();
                }
                break;
            case R.id.four :
                count++;
                makeMove(4);
                if (count == 2){
                    flipBack();
                }
                break;
            case R.id.five :
                count++;
                makeMove(5);
                if (count == 2){
                    flipBack();
                }
                break;
            case R.id.six :
                count++;
                makeMove(6);
                if (count == 2){
                    flipBack();
                }
                break;
            case R.id.seven :
                count++;
                makeMove(7);
                if (count == 2){
                    flipBack();
                }
                break;
            case R.id.eight :
                count++;
                makeMove(8);
                if (count == 2){
                    flipBack();
                }
                break;
        }
    }

    public void clearGrid() {
        for(int i = 1; i <= 8; i++) {
            buttons[i].setBackgroundResource(R.drawable.question128x128);
        }
        count = 0;
    }

    public void enableGrid(){
        for(int i = 1; i <= 8; i++) {
            buttons[i].setEnabled(true);
        }
    }

    // Start of makeMove method
    public void makeMove(int i){
        buttons[i].setEnabled(false);
        buttons[i].setBackgroundResource(myImageArr[i-1]);
        // Give selected button a tag for future comparison
        String btnTag = Integer.toString(myImageArr[i-1]);
        buttons[i].setTag(btnTag);
        System.out.println("this is test1 " + btnTag);

        // Assign tag to two objects for easier comparison
        if (selectedButton1 == null){
            selectedButton1 = buttons[i].getTag();
            System.out.println("this is button1 " + selectedButton1);
        }else{
            selectedButton2 = buttons[i].getTag();
            System.out.println("this is button2 " + selectedButton2);
        }
    }
    // End of makeMove method

    public void flipBack() {
        // flipBack method compares the selected buttons to see if they match or not
        // If selected buttons matches, count and selectedButtons are reset to defaults
        if (selectedButton1.toString().equals(selectedButton2.toString())){
            selectedButton1 = null;
            selectedButton2 = null;
            count = 0;
            // checkEndGame called here as final button match would just result in game ending
            checkEndGame();
            // System.out.println("ITS EQUAL + RESET");
        // Else selectedButtons are reset and buttons are flipped back
        } else {
            for (int i = 1; i <= 8; i++) {
                if (selectedButton1 == buttons[i].getTag() || selectedButton2 == buttons[i].getTag()){
                    buttons[i].setEnabled(true);
                    buttons[i].setBackgroundResource(R.drawable.question128x128);
                    System.out.println("Button Resetted");
                }
            }
            System.out.println("ITS NOT EQUAL + RESET");
            selectedButton1 = null;
            selectedButton2 = null;
            count = 0;
        }
    }

    // Start of randomize method
    // This is to randomize 'myImageArr' Array to ensure the game loads images differently every time
    public void randomize(int arr[], int n){
        Random r = new Random();
        for (int i = n-1; i>0; i--){
            int j = r.nextInt(i);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    // End of randomize method

    // Start of startTimer
    // Code adapted from https://v4all123.blogspot.com/2013/01/timer.html
    public void startTimer(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timerTextView.setText(String.format(Locale.getDefault(), "%d", time));
                        if (time >= 0){
                            time += 1;
                        } else {
                            timerTextView.setText(R.string.timer_number);
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }
    // End of adapted Code
    // End of startTimer

    // Probably make a method to pass intent info for results
    // checkEndGame method
    public void checkEndGame(){
        if(     !buttons[1].isEnabled() && !buttons[2].isEnabled() && !buttons[3].isEnabled() &&
                !buttons[4].isEnabled() && !buttons[5].isEnabled() && !buttons[6].isEnabled() &&
                !buttons[7].isEnabled() && !buttons[8].isEnabled()){
            timer.cancel();
            System.out.println("Timer Stopped");
            //Probably setup intent here for ScoreActivity
            setHighScore();
            System.out.println("HighScore COMPLETED");
        }
    }

    // For saving scores
    // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-high-scores-and-state-data--mobile-18825
    private void setHighScore(){
        // Sets high score
        List<Score> scoreStrings = new ArrayList<Score>();
        int exTime = getTime();
        if (exTime > 0){
            // Valid time score
            String username = userName;
            SharedPreferences.Editor scoreEdit = gamePrefs.edit();
            String scores = gamePrefs.getString("highScores", "");
            String[] exScores = scores.split("\\|");

            if (scores.length()>0){
                // There's existing scores
                for (String eSc : exScores){
                    String[] parts = eSc.split(" - ");
                    scoreStrings.add(new Score(parts[0], Integer.parseInt(parts[1])));
                }
                Score newScore = new Score(username, exTime);
                scoreStrings.add(newScore);
                Collections.sort(scoreStrings);

                StringBuilder scoreBuild = new StringBuilder("");
                for (int s = 0; s<scoreStrings.size(); s++){
                    if (s>=10) break; // Only show ten
                    if (s>0) scoreBuild.append("|"); // Pipe separate the score strings
                    scoreBuild.append(scoreStrings.get(s).getScoreText());
                }
                // Write to prefs
                scoreEdit.putString("highScores", scoreBuild.toString());
                scoreEdit.commit();
                System.out.println("IF SCORE Submitted");
                finish();
            } else {
                // No existing scores
                scoreEdit.putString("highScores", "" + username + " - " + exTime);
                scoreEdit.commit();
                System.out.println("Else SCORE COMPLETED");
                // Test using finish(); first to see if it records scores properlytest
                finish();
            }

        }
    }
    // End of adapted code

    // Get Time score
    // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-gameplay-logic--mobile-18614
    private int getTime(){
        String scoreTime = timerTextView.getText().toString();
        return Integer.parseInt(scoreTime);
    }
    // End of adapted code

}
