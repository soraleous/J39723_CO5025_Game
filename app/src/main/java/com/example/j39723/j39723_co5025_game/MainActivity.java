package com.example.j39723.j39723_co5025_game;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button newGameButton, scoreBoardButton, aboutButton, logoutButton;
    private String userName;
    private String[] gameImages = {"Images", "Words", "Numbers"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get Intent Bundle
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userName = bundle.getString("username");
        // System.out.println(userName); (For Testing)
        setTitle("Welcome, " + userName + "!");

        newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(this);
        scoreBoardButton = findViewById(R.id.score_board_button);
        scoreBoardButton.setOnClickListener(this);
        aboutButton = findViewById(R.id.about_button);
        aboutButton.setOnClickListener(this);
        logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_about :
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_logout :
                // Code adapted from https://stackoverflow.com/a/19310052
                startActivity(new Intent(this, LoginActivity.class));
                Toast myToast = Toast.makeText(this,R.string.logged_out_text, Toast.LENGTH_SHORT);
                myToast.show();
                finish();
                // End of adapted code
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()){
            case R.id.new_game_button:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Button's Format")
                        .setSingleChoiceItems(gameImages,-1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                // Call method startGame to start game activity here
                                startGame(which);
                            }
                        });
                AlertDialog ad = builder.create();
                ad.show();
                break;
            case R.id.score_board_button:
                i = new Intent(this, ScoreActivity.class);
                startActivity(i);
                break;
            case R.id.about_button:
                i = new Intent(this, AboutActivity.class);
                startActivity(i);
                break;
            case R.id.logout_button:
                startActivity(new Intent(this, LoginActivity.class));
                Toast myToast = Toast.makeText(this,R.string.logged_out_text, Toast.LENGTH_SHORT);
                myToast.show();
                finish();
                break;
        }
    }

    // Code adapted from https://code.tutsplus.com/tutorials/android-sdk-create-an-arithmetic-game-gameplay-logic--mobile-18614
    private void startGame(int chosenType){
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("username", userName);
        i.putExtra("arrayNo", chosenType);
        startActivity(i);
    }
    // End of adapted code

}
