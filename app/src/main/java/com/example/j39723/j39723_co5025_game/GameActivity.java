package com.example.j39723.j39723_co5025_game;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.content.SharedPreferences;

import com.example.j39723.j39723_co5025_game.model.Score;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables
    private ImageButton[] buttons = new ImageButton[10];
    private int count = 0;
    //private int[] myImageArr = new int[]{R.drawable.facebook512x512, R.drawable.facebook512x512, R.drawable.google512x512, R.drawable.google512x512, R.drawable.tumblr512x512, R.drawable.tumblr512x512, R.drawable.youtube512x512, R.drawable.youtube512x512};
    private int[] myImageArr;
    private Object selectedButton1 = null;
    private Object selectedButton2 = null;
    public int time;
    private Timer timer;
    private TextView timerTextView;
    private boolean startTime = true;

    private SharedPreferences gamePrefs;
    public static final String GAME_PREFS = "ScoreFile";

    public String userName;
    public int gameType;

    private boolean isBusy = false;

    private Handler mHandler = new Handler();
    Runnable buttonHold;
    // initialStartDelay may be used for future purposes
    Runnable initialStartDelay;

    MediaPlayer mp;
    MediaPlayer mp2;
    // End of Variables

    // Start of onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Get Intent Bundle
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userName = bundle.getString("username");
        gameType = bundle.getInt("arrayNo");
        System.out.println("GameType = " + gameType);
        if (gameType == 0){
            myImageArr = new int[]{R.drawable.facebook512x512, R.drawable.facebook512x512, R.drawable.google512x512, R.drawable.google512x512,
                    R.drawable.tumblr512x512, R.drawable.tumblr512x512, R.drawable.youtube512x512, R.drawable.youtube512x512};
        } else if (gameType == 1){
            myImageArr = new int[]{R.drawable.digg_512, R.drawable.digg_512, R.drawable.ebay_512, R.drawable.ebay_512,
                    R.drawable.ted_512, R.drawable.ted_512, R.drawable.youtube_512, R.drawable.youtube_512};
        } else {
            myImageArr = new int[]{R.drawable.green_num_1, R.drawable.green_num_1, R.drawable.green_num_2, R.drawable.green_num_2,
                    R.drawable.green_num_3, R.drawable.green_num_3, R.drawable.green_num_4, R.drawable.green_num_4};
        }


        // System.out.println(userName); (For Testing)

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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

        //Start timer and possibly delays here
        timerTextView = findViewById(R.id.timerNum);
        if (startTime){
            time = 0;
            startTimer();
            // enableGrid to enable the buttons for use
            enableGrid();
        }
    }
    // End of onCreate Method

    // Start of onClick Method
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Only setup for eight buttons as ninth is disabled/unused
            case R.id.one :
                count++;
                makeMove(1);
                if (count == 2){ flipBack(); }
                break;
            case R.id.two :
                count++;
                makeMove(2);
                if (count == 2){ flipBack(); }
                break;
            case R.id.three :
                count++;
                makeMove(3);
                if (count == 2){ flipBack(); }
                break;
            case R.id.four :
                count++;
                makeMove(4);
                if (count == 2){ flipBack(); }
                break;
            case R.id.five :
                count++;
                makeMove(5);
                if (count == 2){ flipBack(); }
                break;
            case R.id.six :
                count++;
                makeMove(6);
                if (count == 2){ flipBack(); }
                break;
            case R.id.seven :
                count++;
                makeMove(7);
                if (count == 2){ flipBack(); }
                break;
            case R.id.eight :
                count++;
                makeMove(8);
                if (count == 2){ flipBack(); }
                break;
        }
    }
    // End of onClick Method

    // flipIt method for flip animation
    // code http://www.edumobile.org/android/flip-your-viewsimage-buttontext-etc/
    private void flipIt(final View viewToFlip){
        ObjectAnimator flip = ObjectAnimator.ofFloat(viewToFlip, "rotationY", 0f, 360f);
        flip.setDuration(200);
        flip.start();
    }
    // End of adapted code

    // Start of screenTapped Method (Added android:onClick="screenTapped" to base layout)
    // Code adapted from https://stackoverflow.com/a/27602169
    public void screenTapped(View view){
        // End of adapted code
        // Decided to disable pending handlers here should user taps any other part of the screen other than the buttons
            //Code adapted from https://stackoverflow.com/a/15230215
            if (mHandler!= null){
                // Remove handler for resetting button image within 3 seconds
                mHandler.removeCallbacks(buttonHold);
                // End of adapted code

                // Then perform the disabled handler here instead to reset buttons
                if (count == 2){
                    mp2 = MediaPlayer.create(this, R.raw.mag_page_flip);
                    for (int i = 1; i <= 8; i++) {
                        if (selectedButton1 == buttons[i].getTag() || selectedButton2 == buttons[i].getTag()){
                            flipIt(buttons[i]);
                            buttons[i].setEnabled(true);
                            buttons[i].setBackgroundResource(R.drawable.question512x512);
                        }
                    }
                    mp2.start();
                    isBusy = false;
                    selectedButton1 = null;
                    selectedButton2 = null;
                    count = 0;
                }

            }

    }

    // clearGrid to setup buttons with the question mark image
    public void clearGrid() {
        for(int i = 1; i <= 8; i++) {
            buttons[i].setBackgroundResource(R.drawable.question512x512);
        }
        count = 0;
    }

    // enableGrid to make buttons usable
    public void enableGrid(){
        for(int i = 1; i <= 8; i++) {
            buttons[i].setEnabled(true);
        }
    }


    // Start of makeMove method
    public void makeMove(int i){
        if (!isBusy) {
            mp2 = MediaPlayer.create(this, R.raw.mag_page_flip);
            mp2.start();
            flipIt(buttons[i]);
            buttons[i].setEnabled(false);
            buttons[i].setBackgroundResource(myImageArr[i - 1]);

            // Give selected button a tag for future comparison
            String btnTag = Integer.toString(myImageArr[i - 1]);
            buttons[i].setTag(btnTag);
            System.out.println("this is test1 " + btnTag);

            // Assign tag to two objects for easier comparison
            if (selectedButton1 == null) {
                selectedButton1 = buttons[i].getTag();
                System.out.println("this is button1 " + selectedButton1);
            } else {
                selectedButton2 = buttons[i].getTag();
                System.out.println("this is button2 " + selectedButton2);
            }
        }
    }
    // End of makeMove method

    public void flipBack() {
        // flipBack method compares the selected buttons to see if they match or not
        // If selected buttons matches, count and selectedButtons are reset to default values
        if (selectedButton1.toString().equals(selectedButton2.toString())){
            mp = MediaPlayer.create(this, R.raw.grunz_success_low);
            mp.start();
            selectedButton1 = null;
            selectedButton2 = null;
            count = 0;
            // checkEndGame called here as final button match would just result in game ending
            checkEndGame();
            // System.out.println("ITS EQUAL + RESET");

        // Else selectedButtons are reset and buttons are flipped back (Within 3 seconds or tap detected)
        } else {
            // isBusy prevents our 'Enabled' buttons from pressing if its true
            mp = MediaPlayer.create(this, R.raw.lucario_error);
            mp2 = MediaPlayer.create(this, R.raw.mag_page_flip);
            mp.start();
            isBusy = true;
            buttonHold = new Runnable() {
                @Override
                public void run() {
                    for (int i = 1; i <= 8; i++) {
                        if (selectedButton1 == buttons[i].getTag() || selectedButton2 == buttons[i].getTag()){
                            flipIt(buttons[i]);
                            buttons[i].setEnabled(true);
                            buttons[i].setBackgroundResource(R.drawable.question512x512);
                        }
                    }
                    mp2.start();
                    isBusy = false;
                    selectedButton1 = null;
                    selectedButton2 = null;
                    count = 0;
                }
            }; mHandler.postDelayed(buttonHold, 3000);


        }
    }

    // Start of randomize method
    // This is to randomize 'myImageArr' Array to ensure the game loads images differently every time
    public void randomize(int arr[], int n){
        Random r = new Random();
        for (int i = n-1; i > 0; i--){
            int j = r.nextInt(i);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }
    // End of randomize method

    // Start of startTimer method
    public void startTimer(){
        // Code adapted from https://v4all123.blogspot.com/2013/01/timer.html
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
        // End of adapted Code
    }
    // End of startTimer method

    // Start of checkEndGame method to check whether all buttons are used then the game ends
    // (Works because it requires second last match to be correct hence the last final match will always be correct which results in all buttons being disabled)
    public void checkEndGame(){
        if(     !buttons[1].isEnabled() && !buttons[2].isEnabled() && !buttons[3].isEnabled() &&
                !buttons[4].isEnabled() && !buttons[5].isEnabled() && !buttons[6].isEnabled() &&
                !buttons[7].isEnabled() && !buttons[8].isEnabled()){
            timer.cancel();
            System.out.println("Timer Stopped");
            //Probably setup intent here for ScoreActivity
            setHighScore();
            // finish might be changed with dialog
            mp = MediaPlayer.create(this, R.raw.fins_success_1);
            mp.start();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Congratulations " + userName + "!");
            builder.setMessage("It only took you " + timerTextView.getText().toString() + " seconds");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timer.purge();
                    timerTextView.setText(R.string.timer_number);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.score_board_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timer.purge();
                    timerTextView.setText(R.string.timer_number);
                    finish();
                    startActivity(new Intent(GameActivity.this, ScoreActivity.class));
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            // finish();
        }
    }
    // End of checkEndGame method

    // Start of setHighScore method for Saving scores
    private void setHighScore(){
        // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-high-scores-and-state-data--mobile-18825
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
            } else {
                // No existing scores
                scoreEdit.putString("highScores", "" + username + " - " + exTime);
                scoreEdit.commit();
                System.out.println("Else SCORE COMPLETED");
            }
            // End of adapted code
        }
    }

    // Start of getTime method to get time as score
    private int getTime(){
        // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-gameplay-logic--mobile-18614
        String scoreTime = timerTextView.getText().toString();
        return Integer.parseInt(scoreTime);
        // End of adapted code
    }
    // End of getTime method

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.release();
            mp2.release();
        }
    }

}
