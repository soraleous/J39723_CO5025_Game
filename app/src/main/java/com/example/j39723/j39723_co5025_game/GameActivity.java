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

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables
    private ImageButton[] buttons = new ImageButton[10];
    private int count = 0;
    private int[] myImageArr = new int[]{R.drawable.facebook128x128, R.drawable.facebook128x128, R.drawable.google128x128, R.drawable.google128x128,
            R.drawable.tumblr128x128, R.drawable.tumblr128x128, R.drawable.youtube128x128, R.drawable.youtube128x128};
    private Object selectedButton1 = null;
    private Object selectedButton2 = null;
    private int time = 0;
    private Timer timer;
    private TextView timerTextView;
    private boolean startTime = true;
    // End of Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // FloatingActionButton used for closing Activity
        FloatingActionButton fab = findViewById(R.id.closeFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                timer.purge();
                timerTextView.setText(R.string.timer_number);
                finish();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent); */
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
        // clearGrid to reset grid buttons to default drawables and enable for use
        clearGrid();
        // End of ImageButtons creation

        timerTextView = findViewById(R.id.timerNum);
        //Start timer and delays here
        if (startTime){
            time = 0;
            startTimer();
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
            buttons[i].setEnabled(true);
            buttons[i].setBackgroundResource(R.drawable.question128x128);
        }
        count = 0;
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
            System.out.println("ITS EQUAL + RESET");
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
            System.out.println("Timer Stopped");
            timer.cancel();
            //Probably setup intent here for ScoreActivity
        }
    }
}
