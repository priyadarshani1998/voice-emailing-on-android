package com.bhanu.mini;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;


public class WelcomePage extends Activity {

    Activity welcomeActivity;
    Button goTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.bhanu.mini.R.layout.activity_launch_page);

        goTo = (Button) findViewById(R.id.goToLogin);

//        goTo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent voiceInput = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                voiceInput.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
//                startActivityForResult(voiceInput, 300);
//            }
//        });

    }

    public void goToLogin(View View) {
        Intent login = new Intent(WelcomePage.this, LoginActivity.class);

        startActivity(login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
//        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//        Toast.makeText(getApplicationContext(), results.get(0), Toast.LENGTH_LONG).show();
//        System.out.println("login here"+results.get(0));

        switch (requestCode) {
            case 300:
                Intent goToLogin = new Intent(WelcomePage.this, LoginActivity.class);
                ClickListener.setVoiceNavigate(welcomeActivity, goToLogin, resultCode, data);
                break;
        }
    }
}
