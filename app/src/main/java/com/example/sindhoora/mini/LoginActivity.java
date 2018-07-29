package com.example.sindhoora.mini;

import java.util.ArrayList;

import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
public class LoginActivity extends AppCompatActivity {

    private Button btnsubmit;
    public EditText editTxtFrom,editTxtPwd;
    String From=null,Pwd=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(Build.VERSION.SDK_INT>9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

            editTxtFrom = (EditText) findViewById(R.id.txt_usr);
            editTxtPwd = (EditText) findViewById(R.id.txt_pwd);

            btnsubmit = (Button) findViewById(R.id.btn_sbt);
            Toolbar t = (Toolbar) findViewById(R.id.tob);
            t.setNavigationIcon(R.drawable.ic_action_name4);
            t.setTitle("Login");
            getSupportActionBar().setIcon(R.drawable.ic_action_name2);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            // getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnsubmit.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Authenticating Your Login...",Toast.LENGTH_LONG).show();

                if( editTxtFrom.getText().toString().contains("@")&& (editTxtFrom.getText().toString().toLowerCase().contains("gmail.com") || editTxtFrom.getText().toString().toLowerCase().contains("yahoo.co"))){
                   if(!editTxtPwd.getText().toString().equalsIgnoreCase(""))
                   { From = editTxtFrom.getText().toString();
                       Pwd = editTxtPwd.getText().toString();

                       try {
                           Properties props = new Properties();
                  //        Toast.makeText(getApplicationContext(),From+" "+Pwd+" Entered Port Authentication",Toast.LENGTH_LONG).show();

                           if (From.toLowerCase().contains("gmail")) {
//Get the session object
                               //  Toast.makeText(getApplicationContext(),"Entered Port Authentication",Toast.LENGTH_LONG).show();
                               props.put("mail.smtp.auth", "true");
                               props.put("mail.smtp.starttls.enable", "true");
                               props.put("mail.smtp.host", "smtp.gmail.com");
                               props.put("mail.smtp.port", "587");

                           } else if (From.toLowerCase().contains("yahoo")) {
                               props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                               props.put("mail.smtp.socketFactory.port", "587");
                               props.put("mail.smtp.socketFactory.class",
                                       "javax.net.ssl.SSLSocketFactory");
                               props.put("mail.smtp.auth", "true");
                               props.put("mail.smtp.port", "587");
                           }


                           //   Properties props = new Properties();




                           Session session = Session.getInstance(props,
                                       new Authenticator() {
                                           protected PasswordAuthentication getPasswordAuthentication() {
                                               return new PasswordAuthentication(From,Pwd);//change accordingly
                                           }
                                       });

//compose message

                               MimeMessage message = new MimeMessage(session);
                               message.setFrom(new InternetAddress(From));//change accordingly
                               message.addRecipient(Message.RecipientType.TO, new InternetAddress(From));
                               message.addRecipient(Message.RecipientType.CC, new InternetAddress(From));
                               message.setSubject("Voice Based Mail Login.");
                               message.setText("We respect Your Privacy.");
                           //
                               //send message
                               Transport.send(message);
                   //        Toast.makeText(getApplicationContext(),"Authenticating Your Login!!!",Toast.LENGTH_LONG).show();

                           Intent i=new Intent(LoginActivity.this,VoiceEmailActivity.class);
                           i.putExtra("FROM",From);
                           i.putExtra("PWD",Pwd);
                           startActivity(i);
                       }catch(Exception e){


                           Toast.makeText(getApplicationContext(),"Invalid Id/Password",Toast.LENGTH_LONG).show();

                       }





                      } else{ Toast.makeText(getApplicationContext(),"Enter Password" ,Toast.LENGTH_LONG).show();;

                   }

                }else{ Toast.makeText(getApplicationContext(),"Invalid/not supported Email ID" ,Toast.LENGTH_LONG).show();;

            }}
        });
    }
}
