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

    public SendMailSSL(final String from, final String password, String toEmails, String ccEmail, String bccEmail, String subject, String body) throws Exception {

        String packageName = "com.google.android.gm";


        String[] TO = toEmails.split("\\,");
        String[] CC = ccEmail.split("\\,");
        String[] BCC = bccEmail.split("\\,");


        try

        {
            Properties props = System.getProperties();

            if (from.contains("gmail")) {
                //Get the session object
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
            } else if (from.contains("yahoo")) {

                packageName = "com.yahoo.mobile.client.android.mail";

                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                props.put("mail.smtp.socketFactory.port", "587");
                props.put("mail.smtp.socketFactory.class",
                        "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");
            }

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);//change accordingly
                }
            });

            //compose message

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));//change accordingly

            for (String email : TO) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            }

            for (String email : CC) {
                message.addRecipient(Message.RecipientType.CC, new InternetAddress(email));
            }
            for (String email : BCC) {
                message.addRecipient(Message.RecipientType.BCC, new InternetAddress(email));
            }

            message.setSubject(subject);
            message.setText(body);

            //send messageti
            Transport.send(message);
//            Toast.makeText(getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG).show();

//            if (from.contains("gmail")) {
//                Intent mailIntent = Activity.getPackageManager().getLaunchIntentForPackage(packageName);
//
//                if (mailIntent != null)
//                    startActivity(mailIntent);
//            }

        } catch(Exception e) {
            e.printStackTrace();
//            Toast.makeText(getApplicationContext(), "Email Could not be Sent" + e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }


//    public Session authenticate(final String fromEmail, final String password) {
//
//        Properties props = new Properties();
//        if (session == null) {
//            if (fromEmail.toLowerCase().contains("gmail")) {
//
//                //Get the session object
//                props.put("mail.smtp.auth", "true");
//                props.put("mail.smtp.starttls.enable", "true");
//                props.put("mail.smtp.host", "smtp.gmail.com");
//                props.put("mail.smtp.port", "587");
//
//
//            } else if (fromEmail.toLowerCase().contains("yahoo")) {
//                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
//                props.put("mail.smtp.socketFactory.port", "587");
//                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//                props.put("mail.smtp.auth", "true");
//                props.put("mail.smtp.port", "587");
//            }
//
//            session = Session.getInstance(props, new Authenticator() {
//                protected PasswordAuthentication getPasswordAuthentication() {
//
//                    return new PasswordAuthentication(fromEmail, password);//change accordingly
//                }
//            });
//        } else {
//            session = Session.getDefaultInstance(props);
//        }
//        return session;
//
//    }


//    public void sendMail(final String fromEmail, final String password) {
//
//
//        MimeMessage message = new MimeMessage(authenticate(fromEmail, password));
//        try {
//            message.setFrom(new InternetAddress(fromEmail));
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(fromEmail));
//            message.setSubject("Test");
//            Transport.send(message);
//
//        }catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
}
