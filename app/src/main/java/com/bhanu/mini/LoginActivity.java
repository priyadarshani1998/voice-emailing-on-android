package com.bhanu.mini;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import javax.mail.*;

public class LoginActivity extends AppCompatActivity {

    final int EMAIL_VOICE_CODE = 100;
    final int PASSWORD_VOICE_CODE = 200;
    private Button btnSubmit;
    public EditText editTxtFrom, editTxtPwd;
    LinearLayout loginForm;
    String from = null, password = null;
    Activity loginActivity;
    SendMailSSL SendMailSSL;
    View view;

    Session authSession = null;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(com.bhanu.mini.R.layout.activity_login);
        loginActivity = LoginActivity.this;
        SendMailSSL = new SendMailSSL();
        view = findViewById(R.id.login_form);

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        loginForm = (LinearLayout) findViewById(R.id.login_form);

        //initialize text fields
        editTxtFrom = (EditText) findViewById(com.bhanu.mini.R.id.txt_usr);
        editTxtPwd = (EditText) findViewById(com.bhanu.mini.R.id.txt_pwd);
        //initialize login button
        btnSubmit = (Button) findViewById(com.bhanu.mini.R.id.btn_sbt);

        // Check if UserResponse is Already Logged In
        if (SendMailSSL.session != null && SaveSharedPreference.getLoggedStatus(getApplicationContext())) {
            SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
            Toast.makeText(getApplicationContext(), "Already Logged in.!!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), VoiceEmailActivity.class);
            startActivity(intent);
        } else
            loginForm.setVisibility(View.VISIBLE);


        ClickListener.inputClick(editTxtFrom, loginActivity, EMAIL_VOICE_CODE);
        ClickListener.inputClick(editTxtPwd, loginActivity, PASSWORD_VOICE_CODE);


        btnSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Snackbar.make(view, "Authenticating Your Login...", Toast.LENGTH_LONG).show();

                if (editTxtFrom.getText().toString().contains("@") && (editTxtFrom.getText().toString().toLowerCase().contains("gmail.com") || editTxtFrom.getText().toString().toLowerCase().contains("yahoo.co"))) {
                    if (!editTxtPwd.getText().toString().equalsIgnoreCase("")) {

                        from = editTxtFrom.getText().toString();
                        password = editTxtPwd.getText().toString();

                        authSession = SendMailSSL.authenticate(loginActivity, from, password);

                        SendMailSSL.session = authSession;
                        SendMailSSL.fromEmail = from;
                        Snackbar.make(view, "Authenticating, We respect your privacy", Toast.LENGTH_LONG).show();
                        try {

                            if (authSession != null) {
                                Intent mailActivity = new Intent(LoginActivity.this, VoiceEmailActivity.class);
                                mailActivity.putExtra("FROM", from);
                                startActivity(mailActivity);
                            }

                        } catch (Exception e) {

                            Snackbar.make(view, "Invalid Id/Password", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Snackbar.make(view, "Enter Password", Toast.LENGTH_LONG).show();

                    }

                } else {

                    Snackbar.make(view, "Invalid/not supported Email ID", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EMAIL_VOICE_CODE:
                ClickListener.setVoiceResult(loginActivity, editTxtFrom, resultCode, data);
                from = editTxtFrom.getText().toString();
                break;

            case PASSWORD_VOICE_CODE:
                ClickListener.setVoiceResult(loginActivity, editTxtPwd, resultCode, data);
                password = editTxtPwd.getText().toString();
                break;
        }
    }

}
