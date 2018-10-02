package com.bhanu.mini;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public abstract class ClickListener implements View.OnClickListener {

    public static void inputClick(EditText editText, final Activity Activity, final int code) {

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent voiceInput = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                voiceInput.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    Activity.startActivityForResult(voiceInput, code);

                } catch (ActivityNotFoundException a) {

                    Snackbar.make(view,
                            "Ops! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void setVoiceResult(final Activity activity, EditText editText,int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && null != data) {

            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

            for (String result : results) {

                if (!result.equalsIgnoreCase("clean")) {
                    try {

                        editText.setText(result);

                    } catch (Exception e) {

                        Toast.makeText(activity.getApplicationContext(), "Couldn't convert voice to text" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    editText.setText("");
                }

            }

        }

    }
}
