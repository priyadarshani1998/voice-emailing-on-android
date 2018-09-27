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

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class VoiceEmailActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    MimeMessage message = null;
    Session     session = null;


    String packageName = "com.google.android.gm";

    private FloatingActionButton btnSpeakTO;

    private EditText editTxtTO;
    private EditText editTxtCC;
    private EditText editTxtBCC;
    private EditText editTxtSub;
    private EditText editTxtEB;
    private String editTxtFrom  = null;
    private String password     = null;
    private String subjectEmail = null;
    private String bodyEmail    = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.bhanu.mini.R.layout.activity_email);
        getSupportActionBar().setIcon(com.bhanu.mini.R.drawable.ic_action_name2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTxtFrom = this.getIntent().getStringExtra("FROM");
        password = this.getIntent().getStringExtra("PWD");

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
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

        String toEmail  = editTxtTO.getText().toString();
        String ccEmail  = editTxtCC.getText().toString();
        String bccEmail = editTxtBCC.getText().toString();

        subjectEmail = editTxtSub.getText().toString();
        bodyEmail = editTxtEB.getText().toString();


        try {

            Session AuthSession = this.authenticate(editTxtFrom, password);

            //compose message
            MimeMessage message = this.composeMessage(AuthSession, toEmail, ccEmail, bccEmail);

//            MimeMessage message = new MimeMessage(session);
//
//
//            for (String email : toEmailList) {
//                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
//            }
//
//            for (String email : ccEmailList) {
//                message.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
//            }
//            for (String email : bccEmailList) {
//                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email));
//            }
//
//            message.setSubject(subjectEmail);
//            message.setText(bodyEmail);

            //send message to
            Transport.send(message);

            Toast.makeText(getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG)
                    .show();

            this.openEmailApp();

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Email Could not be Sent" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void openEmailApp() {

        Intent mailIntent = getPackageManager().getLaunchIntentForPackage(packageName);

        if (mailIntent != null)
            startActivity(mailIntent);

    }

    private MimeMessage composeMessage(Session session, String toEmail, String ccEmail, String bccEmail) {

        try {

            message = new MimeMessage(session);

            //setting who is sending an email
            message.setFrom(new InternetAddress(editTxtFrom));//change accordingly

            //adding all types of recipients of an email
            addRecipients(message, Message.RecipientType.TO, toEmail);
            addRecipients(message, Message.RecipientType.CC, ccEmail);
            addRecipients(message, Message.RecipientType.BCC, bccEmail);

            //setting subject and body of an email
            message.setSubject(subjectEmail);
            message.setText(bodyEmail);

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Could not compose message" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return message;
    }

    private void addRecipients(Message message, Message.RecipientType type, String recipients) {

        try {

            String[] recipientsList = recipients.split("\\,");

            for (String recipient : recipientsList) {
                message.addRecipient(type, new InternetAddress(recipient));
            }

        } catch (Exception e) {

            Toast.makeText(getApplicationContext(), "Could not add recipients" + type + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    private Session authenticate(final String userName, final String password) {

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
            Toast.makeText(getApplicationContext(), "Email Could authenticated" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return session;
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

                    Toast.makeText(getApplicationContext(), "Oops! Your device doesn't support Speech to Text", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 2: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    for (String result : results) {
                        if (!result.equalsIgnoreCase("clean")) {
                            try {

                                editTxtTO.setText(result);

                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "Couldn't enter email" + e.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            editTxtTO.setText("");
                        }

                    }

                }
                break;
            }
            case 3: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    for (String result : results) {
                        if (!result.equalsIgnoreCase("clean")) {
                            try {

                                editTxtTO.setText(result);

                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "Couldn't enter email" + e.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            editTxtTO.setText("");
                        }

                    }

                }
                break;
            }case 4: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    for (String result : results) {
                        if (!result.equalsIgnoreCase("clean")) {
                            try {

                                editTxtTO.setText(result);

                            } catch (Exception e) {

                                Toast.makeText(getApplicationContext(), "Couldn't enter email" + e.getMessage(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        } else {
                            editTxtTO.setText("");
                        }

                    }

                }
                break;
            }
        }
    }
}
