package com.example.j39723.j39723_co5025_game;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.j39723.j39723_co5025_game.model.DBTools;
import com.example.j39723.j39723_co5025_game.model.User;

/**
 *  Uses Android Studio's default Login Activity Template, modified to fit project
 *  Removed the implemented LoaderManager<Cursor> as its currently unnecessary
 */
public class LoginActivity extends AppCompatActivity {

    // Variables
    private User myUser;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    // End of Variables

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = findViewById(R.id.username);
        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();
        // System.out.println(username + " " + password); (For Testing)

        boolean cancel = false;
        View focusView = null;

        // Checks for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }


        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password, this);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        // Since included account 'test' has more than 3 letters
        return username.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        // Since included account 'test' password has more than 3 letters
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }




    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    // Code adapted from https://stackoverflow.com/a/22209047
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        private final Context mContext;

        UserLoginTask(String email, String password, Context context) {
            mUsername = email;
            mPassword = password;
            mContext= context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DBTools dbTools=null;
            try{
                dbTools = new DBTools(mContext);
                myUser = dbTools.getUser(mUsername);

                if (myUser.userId>0) {
                    // Account exists, check password.
                    return myUser.password.equals(mPassword);
                } else {
                    myUser.password=mPassword;
                    return true;
                }
            } finally{
                if (dbTools!=null)
                    dbTools.close();
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            String testUser = "test";
            String testPass = "1234";

            if (success) {
                if (myUser.userId>0) {
                    finish();
                    Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
                    myIntent.putExtra("username", myUser.username);
                    LoginActivity.this.startActivity(myIntent);

                } else if (myUser.username.equals(testUser) && myUser.password.equals(testPass)) {
                    // Adds Test Account immediately upon typing the correct details
                    // Will only run once as Test User will then be added and will have an ID which will run the first IF statement afterwards instead
                    DBTools dbTools=null;
                    try {
                        finish();
                        dbTools = new DBTools(mContext);
                        myUser=dbTools.insertUser(myUser);
                        Intent myIntent = new Intent(LoginActivity.this,MainActivity.class);
                        myIntent.putExtra("username", myUser.username);
                        LoginActivity.this.startActivity(myIntent);
                    } finally {
                        if (dbTools!=null)
                            dbTools.close();
                    }
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    // Registers new user
                                    DBTools dbTools=null;
                                    try{
                                        finish();
                                        dbTools = new DBTools(mContext);
                                        myUser=dbTools.insertUser(myUser);
                                        Toast myToast = Toast.makeText(mContext,R.string.updatingReport, Toast.LENGTH_SHORT);
                                        myToast.show();
                                        Intent myIntent = new Intent(LoginActivity.this,MainActivity.class);
                                        myIntent.putExtra("username", myUser.username);
                                        LoginActivity.this.startActivity(myIntent);
                                    } finally {
                                        if (dbTools!=null)
                                            dbTools.close();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    // Display Toast message of cancelling registration
                                    Toast myToast = Toast.makeText(mContext,R.string.registerCancel, Toast.LENGTH_SHORT);
                                    myToast.show();
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(this.mContext);
                    builder.setMessage(R.string.confirm_registry).setPositiveButton(R.string.yes, dialogClickListener)
                            .setNegativeButton(R.string.no, dialogClickListener).show();
                }
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        // End of adapted code

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}



