package com.bhanu.mini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;


public class WelcomePage extends Activity {

    Button goTo;
    Activity welcomeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.bhanu.mini.R.layout.activity_launch_page);
        goTo = (Button)findViewById(R.id.gotoLogin);

        ClickListener.buttonClick(goTo, welcomeActivity, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                if(text.get(0).equalsIgnoreCase("login")) {

                    Intent goToLogin = new Intent(WelcomePage.this, LoginActivity.class);
                    startActivity(goToLogin);
                }
                break;
        }
    }
}
