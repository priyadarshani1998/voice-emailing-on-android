package com.bhanu.mini;

import android.widget.Toast;

import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import android.widget.Toast.*;

public class SendMailSSL {


    private static String From, Pwd, To, Cc, Sub, Eb;
    private Session session;
//    public SendMailSSL(String FROM, String PWD, String TO, String CC, String SUB, String EB) throws IOException {
//        From = FROM;
//        Pwd = PWD;
//        To = TO;
//        Cc = CC;
//        Sub = SUB;
//        Eb = EB;
//        // Toast.makeText(this,From+", "+Pwd+", "+To+", "+Cc+Sub+", "+Eb,Toast.LENGTH_LONG).show();
//        String ok = "";
//        TO.replaceAll(" ", "");
//
//        String to = To;//change accordingly
//        To.toLowerCase();
//        Properties props = new Properties();
//
//        if (To.contains("gmail")) {
////Get the session object
//
//            props.put("mail.smtp.host", "smtp.gmail.com");
//
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.port", "465");
//        } else if (To.contains("yahoo")) {
//            props.put("mail.smtp.host", "smtp.mail.yahoo.com");
//            props.put("mail.smtp.socketFactory.port", "587");
//            props.put("mail.smtp.socketFactory.class",
//                    "javax.net.ssl.SSLSocketFactory");
//            props.put("mail.smtp.auth", "true");
//            props.put("mail.smtp.port", "587");
//        }
//
//
//        Session session = Session.getDefaultInstance(props,
//                new Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(From, Pwd);//change accordingly
//                    }
//                });
//
//        //compose message
//        try {
//            MimeMessage message = new MimeMessage(session);
//            message.setFrom(new InternetAddress(From));//change accordingly
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(To));
//            message.addRecipient(Message.RecipientType.CC, new InternetAddress(Cc));
//            message.setSubject(Sub);
//            message.setText(Eb);
//
//            //send message
//            Transport.send(message);
//            //Toast.makeText("Success",5);
//
//            //Toast.makeText(getApplicationContext(),"Email Sent Successfully" ,Toast.LENGTH_LONG).show();;
//            // System.out.println("Email sent successfully");
//
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

    public Session authenticate(final String fromEmail, final String password) {

        Properties props = new Properties();
        if (session == null) {
            if (fromEmail.toLowerCase().contains("gmail")) {

                //Get the session object
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");


            } else if (fromEmail.toLowerCase().contains("yahoo")) {
                props.put("mail.smtp.host", "smtp.mail.yahoo.com");
                props.put("mail.smtp.socketFactory.port", "587");
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.port", "587");
            }

            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {

                    return new PasswordAuthentication(fromEmail, password);//change accordingly
                }
            });
        } else {
            session = Session.getDefaultInstance(props);
        }
        return session;

    }


    public void sendMail(final String fromEmail, final String password) {


        MimeMessage message = new MimeMessage(authenticate(fromEmail, password));
        try {
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(fromEmail));
            message.setSubject("Test");
            Transport.send(message);

        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
