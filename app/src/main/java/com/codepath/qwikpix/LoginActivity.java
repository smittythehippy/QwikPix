package com.codepath.qwikpix;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.FragmentTransaction;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.w3c.dom.Text;

//parse-dashboard --appId qwik-pix --masterKey KeyForFastPix --serverURL "https://qwik-pix.herokuapp.com/parse"
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Context context;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                login(username, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Signup clicked");
                showSignUp(context);
            }
        });
    }

    private void showSignUp(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        final EditText etEmail = dialog.findViewById(R.id.etEmail);
        final EditText etUsername = dialog.findViewById(R.id.etUsername);
        final EditText etPassword = dialog.findViewById(R.id.etPassword);
        final Button btnSignUp = dialog.findViewById(R.id.btnSignUp);
        final Button btnCancel = dialog.findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create the Parse user and set core properties
                ParseUser user = new ParseUser();
                user.setEmail(etEmail.getText().toString());
                user.setUsername(etUsername.getText().toString());
                user.setPassword(etPassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            //Success
                            Toast.makeText(getApplicationContext(), "User Profile created!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            e.printStackTrace();
                        }
                        dialog.dismiss();

                    }
                });

            }
        });

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.90);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.90);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();

    }
    private void login(String username, String password){
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            //todo show loading icon until main activity is reached.
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    //todo: better handling
                    Log.e(TAG, "Issue with login");
                    e.printStackTrace();
                    return;
                }
                //Navigate to new activity IF the user has signed in properly
                goMainActivity();
            }
        });
    }

    private void goMainActivity(){
        Log.d(TAG, "logging to main activity");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
