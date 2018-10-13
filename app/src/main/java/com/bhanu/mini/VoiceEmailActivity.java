package com.bhanu.mini;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;


public class VoiceEmailActivity extends AppCompatActivity {

    Activity vmActivity;
    SendMailSSL mailSSL;
    private FloatingActionButton btnSpeakTO;
    private boolean backClickedTwice = false;

    private EditText editTxtTO;
    private EditText editTxtCC;
    private EditText editTxtBCC;
    private EditText editTxtSub;
    private EditText editTxtEB;
    private String subjectEmail = null;
    private String bodyEmail = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.bhanu.mini.R.layout.activity_email);
        vmActivity = VoiceEmailActivity.this;
        mailSSL = new SendMailSSL();

        getSupportActionBar().setIcon(com.bhanu.mini.R.drawable.ic_action_name2);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll()
                    .build();
            StrictMode.setThreadPolicy(policy);
        }

        //initialize text fields
        editTxtTO = (EditText) findViewById(R.id.to_email);
        editTxtCC = (EditText) findViewById(R.id.cc_email);
        editTxtBCC = (EditText) findViewById(R.id.bcc_email);
        editTxtSub = (EditText) findViewById(R.id.subject_email);
        editTxtEB = (EditText) findViewById(R.id.body_email);
        //initialize send button
        btnSpeakTO = (FloatingActionButton) findViewById(com.bhanu.mini.R.id.send_button);

        //add click input click listener
        ClickListener.inputClick(editTxtTO, vmActivity, 1);
        ClickListener.inputClick(editTxtCC, vmActivity, 2);
        ClickListener.inputClick(editTxtBCC, vmActivity, 3);
        ClickListener.inputClick(editTxtSub, vmActivity, 4);
        ClickListener.inputClick(editTxtEB, vmActivity, 5);

        //add click to send button
        btnSpeakTO.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String toEmail = editTxtTO.getText().toString();
                String ccEmail = editTxtCC.getText().toString();
                String bccEmail = editTxtBCC.getText().toString();

                subjectEmail = editTxtSub.getText().toString();
                bodyEmail = editTxtEB.getText().toString();

                mailSSL.sendEmail(
                        vmActivity,
                        toEmail,
                        ccEmail,
                        bccEmail,
                        subjectEmail,
                        bodyEmail);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_email, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {
            mailSSL.logout(vmActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (backClickedTwice) {
            mailSSL.logout(vmActivity);
            return;
        }

        this.backClickedTwice = true;
        Toast.makeText(this, "Please click BACK(<-)again to exit/logout", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backClickedTwice=false;
            }
        }, 2000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                ClickListener.setVoiceResult(vmActivity, editTxtTO, resultCode, data);
                break;
            case 2:
                ClickListener.setVoiceResult(vmActivity, editTxtCC, resultCode, data);
                break;
            case 3:
                ClickListener.setVoiceResult(vmActivity, editTxtBCC, resultCode, data);
                break;
            case 4:
                ClickListener.setVoiceResult(vmActivity, editTxtSub, resultCode, data);
                break;
            case 5:
                ClickListener.setVoiceResult(vmActivity, editTxtEB, resultCode, data);
                break;
        }
    }
}
