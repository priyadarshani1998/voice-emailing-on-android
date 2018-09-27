package com.bhanu.mini;

import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class VoiceEmailActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    String packageName = "com.google.android.gm";

    private FloatingActionButton btnSpeakTO;

    public EditText editTxtTO, editTxtCC, editTxtBCC, editTxtSub, editTxtEB;
    String editTxtFrom = null, password = null, txt_sub = null, txt_eb = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bhanu.mini.R.layout.activity_email);
        getSupportActionBar().setIcon(com.bhanu.mini.R.drawable.ic_action_name2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTxtFrom = this.getIntent().getStringExtra("FROM");
        password = this.getIntent().getStringExtra("PWD");

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        editTxtTO = (EditText) findViewById(R.id.to_email);
        editTxtCC = (EditText) findViewById(R.id.cc_email);
        editTxtBCC = (EditText) findViewById(R.id.bcc_email);
        editTxtSub = (EditText) findViewById(R.id.subject_email);
        editTxtEB = (EditText) findViewById(R.id.body_email);

        btnSpeakTO = (FloatingActionButton) findViewById(com.bhanu.mini.R.id.send_button);


        inputOnClickListener(editTxtTO, RESULT_SPEECH);
        inputOnClickListener(editTxtCC, RESULT_SPEECH);
        inputOnClickListener(editTxtBCC, RESULT_SPEECH);
        inputOnClickListener(editTxtSub, RESULT_SPEECH);
        inputOnClickListener(editTxtEB, RESULT_SPEECH);


        btnSpeakTO.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                sendEmail();
            }
        });
    }


    protected void sendEmail() {

        String toEmail = editTxtTO.getText().toString();
        String ccEmail = editTxtCC.getText().toString();
        String bccEmail = editTxtBCC.getText().toString();

        String[] TO = toEmail.split("\\,");
        String[] CC = ccEmail.split("\\,");
        String[] BCC = bccEmail.split("\\,");

        txt_sub = editTxtSub.getText().toString();
        txt_eb = editTxtEB.getText().toString();


        try {


            Session session = authenticate(editTxtFrom, password);

            //compose message

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(editTxtFrom));//change accordingly

            for (String email : TO) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }

            for (String email : CC) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
            }
            for (String email : BCC) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email));
            }

            message.setSubject(txt_sub);
            message.setText(txt_eb);

            //send message to
            Transport.send(message);
            Toast.makeText(getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG).show();

            Intent mailIntent = getPackageManager().getLaunchIntentForPackage(packageName);

            if (mailIntent != null)
                startActivity(mailIntent);

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Email Could not be Sent" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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

    private Session authenticate(final String userName, final String password) {


        Session session = null;

        try {
            Properties props = System.getProperties();

            if (userName.contains("gmail")) {
                //Get the session object
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

            } else if (userName.contains("yahoo")) {

                packageName = "com.yahoo.mobile.client.android.mail";

                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                props.put("mail.smtp.socketFactory.port", "587");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");
            }

            session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);//change accordingly
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Email Could authenticated" + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return session;
    }
}
