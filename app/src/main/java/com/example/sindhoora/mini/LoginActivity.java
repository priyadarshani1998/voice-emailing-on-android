package com.example.sindhoora.mini;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class LoginActivity extends AppCompatActivity {

    final int EMAIL_VOICE_CODE = 100;
    final int PASSWORD_VOICE_CODE = 200;
    private Button btnSubmit;
    public EditText editTxtFrom, editTxtPwd;
    String From = null, Pwd = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        editTxtFrom = (EditText) findViewById(R.id.txt_usr);
        editTxtPwd = (EditText) findViewById(R.id.txt_pwd);

        btnSubmit = (Button) findViewById(R.id.btn_sbt);
        Toolbar t = (Toolbar) findViewById(R.id.tob);
        t.setNavigationIcon(R.drawable.ic_action_name4);
        t.setTitle("Login");
        getSupportActionBar().setIcon(R.drawable.ic_action_name2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setDisplayShowHomeEnabled(true);

        inputOnClickListener(editTxtFrom,EMAIL_VOICE_CODE);
        inputOnClickListener(editTxtPwd,PASSWORD_VOICE_CODE);

        btnSubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Authenticating Your Login...", Toast.LENGTH_LONG).show();

                if (editTxtFrom.getText().toString().contains("@") && (editTxtFrom.getText().toString().toLowerCase().contains("gmail.com") || editTxtFrom.getText().toString().toLowerCase().contains("yahoo.co"))) {
                    if (!editTxtPwd.getText().toString().equalsIgnoreCase("")) {
                        From = editTxtFrom.getText().toString();
                        Pwd = editTxtPwd.getText().toString();

                        try {
                            Properties props = new Properties();
                            //Toast.makeText(getApplicationContext(),From+" "+Pwd+" Entered Port Authentication",Toast.LENGTH_LONG).show();

                            if (From.toLowerCase().contains("gmail")) {
                                //Get the session object
                                //  Toast.makeText(getApplicationContext(),"Entered Port Authentication",Toast.LENGTH_LONG).show();
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.starttls.enable", "true");
                                props.put("mail.smtp.host", "smtp.gmail.com");
                                props.put("mail.smtp.port", "587");

                            } else if (From.toLowerCase().contains("yahoo")) {
                                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                                props.put("mail.smtp.socketFactory.port", "587");
                                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.port", "587");
                            }


                            //   Properties props = new Properties();


                            Session session = Session.getInstance(props, new Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(From, Pwd);//change accordingly
                                }
                            });

                            //compose message

                            MimeMessage message = new MimeMessage(session);
                            message.setFrom(new InternetAddress(From));//change accordingly
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(From));
                            message.addRecipient(Message.RecipientType.CC, new InternetAddress(From));
                            message.setSubject("Voice Based Mail Login.");
                            message.setText("We respect Your Privacy.");
                            //
                            //send message
                            Transport.send(message);
                            //        Toast.makeText(getApplicationContext(),"Authenticating Your Login!!!",Toast.LENGTH_LONG).show();

                            Intent i = new Intent(LoginActivity.this, VoiceEmailActivity.class);
                            i.putExtra("FROM", From);
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


    private void inputOnClickListener(EditText eText, final int code) {

        eText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, code);
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
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
                        if (!result.equalsIgnoreCase("clear")) {
                            try {

                                editTxtFrom.setText(result);

                                From = editTxtFrom.getText().toString();

                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "Couldn't enter email" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
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
                        }

                    }

                }
                break;
            }

        }
    }


}
