package com.bhanu.mini;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import android.widget.Toast.*;

public class SendMailSSL {

    MimeMessage message = null;
    Session session = null;
    String packageName = "com.google.android.gm";

    protected void sendEmail(final Activity Activity,Session AuthSession, String toEmail,String ccEmail, String bccEmail,String subjectEmail, String bodyEmail) {

        try {

            String editTextFrom = Activity.getIntent().getStringExtra("FROM");
            //compose message
            MimeMessage message = this.composeMessage(Activity, AuthSession, toEmail, ccEmail, bccEmail, editTextFrom, subjectEmail, bodyEmail);

            //send message to
            Transport.send(message);

            Toast.makeText(Activity.getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG)
                    .show();

        } catch (Exception e) {

            Toast.makeText(Activity.getApplicationContext(), "Email Could not be Sent" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }



    private MimeMessage composeMessage(final Activity Activity,Session session, String toEmail, String ccEmail, String bccEmail, String editTxtFrom, String subjectEmail, String bodyEmail) {

        try {

            message = new MimeMessage(session);

            //setting who is sending an email
            message.setFrom(new InternetAddress(editTxtFrom));//change accordingly

            //adding all types of recipients of an email
            if(toEmail != null && !toEmail.isEmpty())
                addRecipients(Activity,message, Message.RecipientType.TO, toEmail);
            if(ccEmail != null && !ccEmail.isEmpty())
                addRecipients(Activity,message, Message.RecipientType.CC, ccEmail);
            if(bccEmail != null && !bccEmail.isEmpty())
                addRecipients(Activity,message, Message.RecipientType.BCC, bccEmail);

            //setting subject and body of an email
            message.setSubject(subjectEmail);
            message.setText(bodyEmail);

        } catch (Exception e) {

            Toast.makeText(Activity.getApplicationContext(), "Could not compose message" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return message;
    }

    private void addRecipients(final Activity Activity, Message message, Message.RecipientType type, String recipients) {

        String[] recipientsList = recipients.split("\\,");

        try {

            for (String recipient : recipientsList) {
                message.addRecipient(type, new InternetAddress(recipient));
            }
        } catch (Exception e) {

            Toast.makeText(Activity.getApplicationContext(), "Could not add recipients" + type + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    private Session authenticate(final Activity Activity, final String userName, final String password) {

        try {
            Properties props = System.getProperties();

            if (userName.contains("gmail")) {
                //Get the session object
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

            } else if (userName.contains("yahoo")) {

                packageName = "com.yahoo.mobile.client.android.mail";

                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                props.put("mail.smtp.socketFactory.port", "587");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");
            }

            session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);//change accordingly
                }
            });
        } catch (Exception e) {
            Toast.makeText(Activity.getApplicationContext(), "Email Could not authenticated" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return session;
    }
}
