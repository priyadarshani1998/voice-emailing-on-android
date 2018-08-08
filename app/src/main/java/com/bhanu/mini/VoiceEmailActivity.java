package com.bhanu.mini;

import java.util.ArrayList;

import android.os.Build;
import android.os.StrictMode;
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

    protected ClickListener clickListener = new ClickListener();

    private ImageButton btnSpeakTO, btnSpeakCC, btnSpeakSub, btnSpeakEB;
    private TextView txtText;
    public EditText editTxtTO, editTxtCC, editTxtSub, editTxtEB;
    // private Button btnSendE
    //SendMailSSL smssl;
    String fr = null, pwd = null, txt_cc = null, txt_sub = null, txt_eb = null, txt_to = null, dd = null, ddd = null;

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
        editTxtTO = (EditText) findViewById(com.bhanu.mini.R.id.txt_to);
        editTxtCC = (EditText) findViewById(com.bhanu.mini.R.id.txt_cc);
        editTxtSub = (EditText) findViewById(com.bhanu.mini.R.id.txt_sub);
        editTxtEB = (EditText) findViewById(com.bhanu.mini.R.id.txt_bdy);
        btnSpeakTO = (ImageButton) findViewById(com.bhanu.mini.R.id.voice_btn);

        //btnSendEmail = (Button)findViewById(R.id.btnEmail


        clickListener.clickEvent(editTxtTO, RESULT_SPEECH);
        clickListener.clickEvent(editTxtTO, RESULT_SPEECH);
        clickListener.clickEvent(editTxtCC, RESULT_SPEECH);
        clickListener.clickEvent(editTxtEB, RESULT_SPEECH);
        clickListener.clickEvent(editTxtSub, RESULT_SPEECH);

        clickListener.onImageButtonClick(btnSpeakTO, RESULT_SPEECH);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {

                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (text.get(0).equalsIgnoreCase("send email")) {
                        try {
                            Toast.makeText(getApplicationContext(), "Page Loading!!!", Toast.LENGTH_LONG).show();
                            ;

                            fr = this.getIntent().getStringExtra("FROM");
                            pwd = this.getIntent().getStringExtra("PWD");
                            txt_to = editTxtTO.getText().toString();
                            txt_cc = editTxtCC.getText().toString();
                            txt_sub = editTxtSub.getText().toString();
                            txt_eb = editTxtEB.getText().toString();
                            // Toast.makeText(getApplicationContext(),fr+", "+pwd+", "+txt_to+", "+txt_cc+", "+
                            //       txt_sub+", "+txt_eb,Toast.LENGTH_LONG).show();
                            //smssl = new SendMailSSL(fr,pwd, txt_to, txt_cc, txt_sub,txt_eb);
                            //sendEmail();


                            Properties props = new Properties();

                            if (fr.contains("gmail")) {
                                //Get the session object
                                Toast.makeText(getApplicationContext(), "Entered Port Authentication", Toast.LENGTH_LONG).show();
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.starttls.enable", "true");
                                props.put("mail.smtp.host", "smtp.gmail.com");
                                props.put("mail.smtp.port", "587");
                            } else if (fr.contains("yahoo")) {
                                Toast.makeText(getApplicationContext(), "Entering Port Authentication", Toast.LENGTH_LONG).show();

                                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                                props.put("mail.smtp.socketFactory.port", "587");
                                props.put("mail.smtp.socketFactory.class",
                                        "javax.net.ssl.SSLSocketFactory");
                                props.put("mail.smtp.auth", "true");
                                props.put("mail.smtp.port", "587");
                            }


                            Session session = Session.getDefaultInstance(props,
                                    new Authenticator() {
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new PasswordAuthentication(fr, pwd);//change accordingly
                                        }
                                    });

                            //compose message
                            txt_to = txt_to.replaceAll(" ", "");
                            txt_cc = txt_cc.replaceAll(" ", "");
                            txt_eb = txt_eb.replaceAll("full stop", ".");
                            txt_eb = txt_eb.replaceAll("dot", ".");
                            txt_eb = txt_eb.replaceAll("Dot", ".");
                            txt_eb = txt_eb.replaceAll("question mark", "?");
                            txt_eb = txt_eb.replaceAll("questionmark", "?");
                            txt_eb = txt_eb.replaceAll("semicolon", ";");
                            txt_eb = txt_eb.replaceAll("semi colon", ";");
                            txt_eb = txt_eb.replaceAll("exclamation", "!");
                            txt_eb = txt_eb.replaceAll("colon", ":");
                            txt_eb = txt_eb.replaceAll("hyphen", "-");
                            txt_eb = txt_eb.replaceAll("dash", "-");
                            txt_eb = txt_eb.replaceAll("Dash", "-");
                            txt_eb = txt_eb.replaceAll("underscore", "_");
                            txt_eb = txt_eb.replaceAll("next line", "\n");
                            txt_eb = txt_eb.replaceAll("comma", ",");
                            if (txt_cc == null) {
                                txt_cc = " ";
                            }

                            Toast.makeText(getApplicationContext(), fr + " From Address " + txt_to + " To Address", Toast.LENGTH_LONG).show();

                            MimeMessage message = new MimeMessage(session);
                            message.setFrom(new InternetAddress(fr));//change accordingly
                            message.addRecipient(Message.RecipientType.TO, new InternetAddress(txt_to));
                            if (txt_cc.trim().equals("")) {

                            } else {

                                message.addRecipient(Message.RecipientType.CC, new InternetAddress(txt_cc));
                            }

                            message.setSubject(txt_sub);
                            message.setText(txt_eb);

                            //send message to
                            Transport.send(message);


                            Toast.makeText(getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG).show();


                            if (fr.contains("gmail")) {
                                Intent gmailIntent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");

                                if (gmailIntent != null)
                                    startActivity(gmailIntent);
                            } else  if (fr.contains("yahoo")) {

                                Intent yahooIntent = getPackageManager().getLaunchIntentForPackage("com.yahoo.mobile.client.android.mail");

                                if (yahooIntent != null)
                                    startActivity(yahooIntent);
                            }
                        } catch (Exception e) {

                            Toast.makeText(getApplicationContext(), "Email Could not be Sent" + e.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    } else if (editTxtTO.findFocus() == editTxtTO) {
                        editTxtTO.append(text.get(0));
                        break;

                    } else if (editTxtCC.findFocus() == editTxtCC) {
                        editTxtCC.append(text.get(0));
                        break;
                    } else if (editTxtSub.findFocus() == editTxtSub) {
                        editTxtSub.append(text.get(0));
                        break;
                    } else if (editTxtEB.findFocus() == editTxtEB) {
                        editTxtEB.append(text.get(0));
                        break;
                    }

                    //editTxt.append(text.get(0));

                }
                break;
            }

        }
    }

    protected void sendEmail() {
        /*Log.i("Send email", "");
        String[] TO = {editTxtTO.getText().toString()};
        String[] CC = {editTxtCC.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, editTxtSub.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, editTxtEB.getText().toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(VoiceEmailActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }*/
        try {
            Properties props = new Properties();

            if (fr.contains("gmail")) {
                //Get the session object
                Toast.makeText(getApplicationContext(), "Entered Port Authentication", Toast.LENGTH_LONG).show();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
            } else if (fr.contains("yahoo")) {
                Toast.makeText(getApplicationContext(), "Entering Port Authentication", Toast.LENGTH_LONG).show();

                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                props.put("mail.smtp.socketFactory.port", "587");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");
            }


            Session session = Session.getDefaultInstance(props,
                    new Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(fr, pwd);//change accordingly
                        }
                    });

            //compose message
            txt_to = txt_to.replaceAll(" ", "");
            txt_cc = txt_cc.replaceAll(" ", "");
            Toast.makeText(getApplicationContext(), fr + " From Address " + txt_to + " To Address", Toast.LENGTH_LONG).show();
            /*MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fr));//change accordingly
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(txt_to));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(txt_cc));
            message.setSubject(txt_sub);
            message.setText(txt_eb);

            //send messageti
            Transport.send(message);*/

            Intent iemail = new Intent(Intent.ACTION_SEND);
            iemail.putExtra(Intent.EXTRA_EMAIL, txt_to);
            iemail.putExtra(Intent.EXTRA_CC, txt_cc);
            iemail.putExtra(Intent.EXTRA_SUBJECT, txt_sub);
            iemail.putExtra(Intent.EXTRA_TEXT, txt_eb);
            startActivity(Intent.createChooser(iemail, ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}