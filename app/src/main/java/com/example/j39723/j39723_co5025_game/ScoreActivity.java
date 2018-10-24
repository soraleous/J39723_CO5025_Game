package com.example.j39723.j39723_co5025_game;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        // Parent Activity should be MainActivity, need pass back username there?

        // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-high-scores-and-state-data--mobile-18825
        TextView scoreView = findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(GameActivity.GAME_PREFS, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");

        // Iterate through scores, appending them into a single string with new lines between them
        StringBuilder scoreBuild = new StringBuilder("");
        for (String score : savedScores){
            scoreBuild.append(score + "\n");
        }
        scoreView.setText(scoreBuild.toString());


    }
    // End of onCreate

}
