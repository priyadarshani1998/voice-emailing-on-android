package com.bhanu.mini;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public abstract class ClickListener implements View.OnClickListener {

    public static void inputClick(EditText editText, final Activity Activity, final int code) {

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    Activity.startActivityForResult(intent, code);

                } catch (ActivityNotFoundException a) {

                    Toast.makeText(Activity.getApplicationContext(),
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
