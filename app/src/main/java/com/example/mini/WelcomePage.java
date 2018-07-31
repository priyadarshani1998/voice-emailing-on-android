package com.example.mini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class WelcomePage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(com.example.mini.R.layout.activity_launch_page);
    }

    public void goToLogin(View view) {

        Intent goToLogin = new Intent(WelcomePage.this, LoginActivity.class);

        startActivity(goToLogin);

    }


}
