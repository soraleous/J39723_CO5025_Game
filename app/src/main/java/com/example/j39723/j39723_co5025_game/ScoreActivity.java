package com.example.j39723.j39723_co5025_game;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.content.SharedPreferences;
import android.widget.TextView;

import java.util.Arrays;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        // Uses custom actionbar to display 'Up' button to prevent home page from crashing due to lack of intent info for username
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);

        // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-high-scores-and-state-data--mobile-18825
        TextView scoreView = findViewById(R.id.high_scores_list);
        SharedPreferences scorePrefs = getSharedPreferences(GameActivity.GAME_PREFS, 0);
        String[] savedScores = scorePrefs.getString("highScores", "").split("\\|");

        // Iterate through scores, appending them into a single string with new lines between them except when array is empty
        // System.out.println(Arrays.toString(savedScores));
        if (Arrays.toString(savedScores).equals("[]")) {
            scoreView.setText(R.string.score_empty);
        } else {
            StringBuilder scoreBuild = new StringBuilder("");
            int i = 1;
            for (String score : savedScores){
                scoreBuild.append(i).append(". ").append(score).append(" Seconds").append("\n");
                i++;
            }
            scoreView.setText(scoreBuild.toString());
        }
        // End of adapted code
    }
    // End of onCreate

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
