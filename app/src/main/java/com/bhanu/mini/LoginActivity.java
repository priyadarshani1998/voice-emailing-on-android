package com.bhanu.mini;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class LoginActivity extends AppCompatActivity {

    final int EMAIL_VOICE_CODE = 100;
    final int PASSWORD_VOICE_CODE = 200;
    private Button btnSubmit;
    public EditText editTxtFrom, editTxtPwd;
    String from = null, Pwd = null;
    Activity loginActivity;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(com.bhanu.mini.R.layout.activity_login);
        loginActivity = LoginActivity.this;

        if (Build.VERSION.SDK_INT > 9) {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        editTxtFrom = (EditText) findViewById(com.bhanu.mini.R.id.txt_usr);
        editTxtPwd = (EditText) findViewById(com.bhanu.mini.R.id.txt_pwd);
        btnSubmit = (Button) findViewById(com.bhanu.mini.R.id.btn_sbt);


        ClickListener.inputOnClickListener(editTxtFrom, loginActivity,EMAIL_VOICE_CODE);
        ClickListener.inputOnClickListener(editTxtPwd,loginActivity, PASSWORD_VOICE_CODE);

        textChange(editTxtFrom);
        textChange(editTxtPwd);

        btnSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Authenticating Your Login...", Toast.LENGTH_LONG).show();

                if (editTxtFrom.getText().toString().contains("@") && (editTxtFrom.getText().toString().toLowerCase().contains("gmail.com") || editTxtFrom.getText().toString().toLowerCase().contains("yahoo.co"))) {
                    if (!editTxtPwd.getText().toString().equalsIgnoreCase("")) {

                        from = editTxtFrom.getText().toString();
                        Pwd = editTxtPwd.getText().toString();


                        Properties props = new Properties();
                        //Toast.makeText(getApplicationContext(),from+" "+Pwd+" Entered Port Authentication",Toast.LENGTH_LONG).show();

                        if (from.toLowerCase().contains("gmail")) {

                            //Get the session object
                            //  Toast.makeText(getApplicationContext(),"Entered Port Authentication",Toast.LENGTH_LONG).show();
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.starttls.enable", "true");
                            props.put("mail.smtp.host", "smtp.gmail.com");
                            props.put("mail.smtp.port", "587");


                        } else if (from.toLowerCase().contains("yahoo")) {
                            props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                            props.put("mail.smtp.socketFactory.port", "587");
                            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                            props.put("mail.smtp.auth", "true");
                            props.put("mail.smtp.port", "587");
                        }

                        Session session = Session.getInstance(props, new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {

                                return new PasswordAuthentication(from, Pwd);//change accordingly
                            }
                        });
                        try {
                            Toast.makeText(getApplicationContext(), "Authenticating, We respect your privacy", Toast.LENGTH_LONG).show();

                            Intent i = new Intent(LoginActivity.this, VoiceEmailActivity.class);
                            i.putExtra("FROM", from);
                            i.putExtra("PWD", Pwd);
                            startActivity(i);

                        } catch (Exception e) {


                            Toast.makeText(getApplicationContext(), "Invalid Id/Password", Toast.LENGTH_LONG).show();

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Invalid/not supported Email ID", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void textChange(final EditText eText) {

        eText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                eText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(com.bhanu.mini.R.drawable.microphone), null);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {


                if (charSequence.length() != 0) {

                    eText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(com.bhanu.mini.R.drawable.icon_close), null);

                } else {

                    eText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(com.bhanu.mini.R.drawable.microphone), null);

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                eText.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(com.bhanu.mini.R.drawable.icon_close), null);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EMAIL_VOICE_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    for (String result : results) {
                        if (!result.equalsIgnoreCase("clean")) {
                            try {

                                editTxtFrom.setText(result);

                                from = editTxtFrom.getText().toString();

                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "Couldn't enter email" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            editTxtFrom.setText("");
                        }

                    }

                }
                break;
            }
            case PASSWORD_VOICE_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    for (String result : results) {
                        if (!result.equalsIgnoreCase("clear")) {
                            try {

                                editTxtPwd.setText(result);
                                Pwd = editTxtPwd.getText().toString();

                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "Couldn't enter password" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {

                            editTxtPwd.setText("");
                        }

                    }

                }
                break;
            }

        }
    }

}
