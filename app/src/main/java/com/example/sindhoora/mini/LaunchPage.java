package com.example.sindhoora.mini;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class LaunchPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch_page);
    }

    public void goToLogin(View view) {

        Intent goToLogin = new Intent(LaunchPage.this, LoginActivity.class);

        startActivity(goToLogin);

    }


}