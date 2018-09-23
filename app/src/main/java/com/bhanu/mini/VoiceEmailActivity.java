package com.bhanu.mini;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class VoiceEmailActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    protected static final int SEND_CODE = 200;

    private ImageButton btnSpeakCC, btnSpeakSub, btnSpeakEB;
    private FloatingActionButton btnSpeakTO;
    private TextView txtText;
    public EditText editTxtTO, editTxtCC, editTxtBCC, editTxtSub, editTxtEB;
    // private Button btnSendE
    //SendMailSSL smssl;
    String fr = null, password = null, txt_cc = null, txt_sub = null, txt_eb = null, txt_to = null, dd = null, ddd = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bhanu.mini.R.layout.activity_email);
        getSupportActionBar().setIcon(com.bhanu.mini.R.drawable.ic_action_name2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //txtText = (TextView) findViewById(R.id.txtText);
        editTxtTO = (EditText) findViewById(R.id.to_email);
        editTxtCC = (EditText) findViewById(R.id.cc_email);
        editTxtBCC = (EditText) findViewById(R.id.bcc_email);
        editTxtSub = (EditText) findViewById(R.id.subject_email);
        editTxtEB = (EditText) findViewById(R.id.body_email);

        btnSpeakTO = (FloatingActionButton) findViewById(com.bhanu.mini.R.id.send_button);

        //btnSendEmail = (Button)findViewById(R.id.btnEmail
        ClickListener.inputOnClickListener(editTxtTO, RESULT_SPEECH);


        editTxtTO.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);
                    //txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        editTxtCC.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);


                    //txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        editTxtEB.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);


                    //txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

        editTxtSub.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, RESULT_SPEECH);


                    //txtText.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });


        btnSpeakTO.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                sendEmail();
            }
        });
    }


    protected void sendEmail() {

        String packageName = "com.google.android.gm";

        String toEmail = editTxtTO.getText().toString();
        String ccEmail = editTxtCC.getText().toString();
        String bccEmail = editTxtBCC.getText().toString();

        String[] TO = toEmail.split("\\,");
        String[] CC = ccEmail.split("\\,");
        String[] BCC = bccEmail.split("\\,");

        txt_sub = editTxtSub.getText().toString();
        txt_eb = editTxtEB.getText().toString();


        fr = this.getIntent().getStringExtra("FROM");
        password = this.getIntent().getStringExtra("PWD");

        try {
            Properties props = System.getProperties();

            if (fr.contains("gmail")) {
                //Get the session object
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
            } else if (fr.contains("yahoo")) {

                packageName ="com.yahoo.mobile.client.android.mail";

                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                props.put("mail.smtp.socketFactory.port", "587");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");
            }

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fr, password);//change accordingly
                }
            });

            //compose message
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(fr));//change accordingly

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

            //send messageti
            Transport.send(message);
            Toast.makeText(getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG).show();

            if (fr.contains("gmail")) {
                Intent mailIntent = getPackageManager().getLaunchIntentForPackage(packageName);

                if (mailIntent != null)
                    startActivity(mailIntent);
            }

        } catch (Exception e) {
            //e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Email Could not be Sent" + e.getMessage() ,Toast.LENGTH_LONG).show();;
        }
    }
}
