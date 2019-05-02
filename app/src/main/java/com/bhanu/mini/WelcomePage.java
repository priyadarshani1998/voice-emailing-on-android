package com.bhanu.mini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class WelcomePage extends Activity {

    Activity welcomeActivity;
    Button goTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.bhanu.mini.R.layout.activity_launch_page);

        goTo = (Button) findViewById(R.id.goToLogin);

    }

    public void goToLogin(View View) {
        Intent login = new Intent(WelcomePage.this, LoginActivity.class);

        startActivity(login);
    }

}
