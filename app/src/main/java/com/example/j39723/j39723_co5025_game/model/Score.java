package com.example.j39723.j39723_co5025_game.model;

import android.support.annotation.NonNull;

public class Score implements Comparable<Score> {

    private String scoreUser;
    public int scoreTime;

    public Score(String user, int time){
        scoreUser = user;
        scoreTime = time;
    }


    // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-high-scores-and-state-data--mobile-18825
    @Override
    public int compareTo(@NonNull Score o) {
        // Sorted in ascending order
        return Integer.compare(scoreTime, o.scoreTime);
    }

    public String getScoreText(){
        return scoreUser + " - " + scoreTime;
    }
    // End of adapted code

}
