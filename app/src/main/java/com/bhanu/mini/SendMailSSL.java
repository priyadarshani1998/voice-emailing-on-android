package com.bhanu.mini;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class SendMailSSL {

    MimeMessage message = null;
    static Session session = null;
    static String fromEmail = null;
    String packageName = "com.google.android.gm";

    /**
     * @param Activity
     * @param toEmail
     * @param ccEmail
     * @param bccEmail
     * @param subjectEmail
     * @param bodyEmail
     */
    protected void sendEmail(final Activity Activity, String toEmail, String ccEmail, String bccEmail, String subjectEmail, String bodyEmail) {

        try {
            //compose message
            MimeMessage message = this.composeMessage(Activity, this.session, toEmail, ccEmail, bccEmail, subjectEmail, bodyEmail);

            //send message to
            Transport.send(message);
            //open app
            this.openEmailApp(Activity);

            Toast.makeText(Activity.getApplicationContext(), "Email Sent Successfully", Toast.LENGTH_LONG)
                    .show();

        } catch (Exception e) {

            Toast.makeText(Activity.getApplicationContext(), "Email Could not be Sent" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    /**
     * @param Activity
     */
    private void openEmailApp(Activity Activity) {

        Intent mailIntent = Activity.getPackageManager().getLaunchIntentForPackage(packageName);

        if (mailIntent != null)
            Activity.startActivity(mailIntent);
    }

    /**
     * @param Activity
     * @param session
     * @param toEmail
     * @param ccEmail
     * @param bccEmail
     * @param subjectEmail
     * @param bodyEmail
     * @return
     */
    protected MimeMessage composeMessage(final Activity Activity, Session session, String toEmail, String ccEmail, String bccEmail, String subjectEmail, String bodyEmail) {

        try {

            message = new MimeMessage(session);

            //setting who is sending an email
            message.setFrom(new InternetAddress(this.fromEmail));//change accordingly

            //adding all types of recipients of an email
            if (toEmail != null && !toEmail.isEmpty())
                addRecipients(Activity, message, Message.RecipientType.TO, toEmail);
            if (ccEmail != null && !ccEmail.isEmpty())
                addRecipients(Activity, message, Message.RecipientType.CC, ccEmail);
            if (bccEmail != null && !bccEmail.isEmpty())
                addRecipients(Activity, message, Message.RecipientType.BCC, bccEmail);

            //setting subject and body of an email
            message.setSubject(subjectEmail);
            message.setText(formatBodyText(bodyEmail));

        } catch (Exception e) {

            Toast.makeText(Activity.getApplicationContext(), "Could not compose message" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

        return message;
    }

    /**
     * @param Activity
     * @param message
     * @param type
     * @param recipients
     */
    private void addRecipients(final Activity Activity, Message message, Message.RecipientType type, String recipients) {

        String[] recipientsList = recipients.split("\\,");

        try {

            for (String recipient : recipientsList) {
                message.addRecipient(type, new InternetAddress(formattingEmail(recipient)));
            }
        } catch (Exception e) {

            Toast.makeText(Activity.getApplicationContext(), "Could not add recipients" + type + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }

    /**
     * @param Activity
     * @param userName
     * @param password
     * @return Session
     */
    protected Session authenticate(final Activity Activity, final String userName, final String password) {

        try {
            Properties props = System.getProperties();

            if (userName.contains("gmail")) {

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

            session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);//change accordingly
                }
            });
        } catch (Exception e) {
            Toast.makeText(Activity.getApplicationContext(), "Email Could not authenticated" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        //Get the session object
        return session;
    }

    /**
     * @param bodyText
     * @return formatted email body
     */
    private String formatBodyText(String bodyText) {

        bodyText = bodyText.replaceAll("full stop",".");
        bodyText = bodyText.replaceAll("dot",".");
        bodyText = bodyText.replaceAll("Dot",".");
        bodyText = bodyText.replaceAll("question mark","?");
        bodyText = bodyText.replaceAll("questionmark","?");
        bodyText = bodyText.replaceAll("semicolon",";");
        bodyText = bodyText.replaceAll("semi colon",";");
        bodyText = bodyText.replaceAll("exclamation","!");
        bodyText = bodyText.replaceAll("colon",":");
        bodyText = bodyText.replaceAll("hyphen","-");
        bodyText = bodyText.replaceAll("dash","-");
        bodyText = bodyText.replaceAll("Dash","-");
        bodyText = bodyText.replaceAll("underscore","_");
        bodyText = bodyText.replaceAll("next line","\n");
        bodyText = bodyText.replaceAll("comma",",");

        return bodyText;
    }

    /**
     * @param emailText
     * @return formatted email
     */
    private String formattingEmail(String emailText) {

        emailText = emailText.replaceAll(" ","");
        emailText = emailText.replaceAll(" ","");
        emailText = emailText.replaceAll("at","@");
        emailText = emailText.replaceAll("At","@");
        emailText = emailText.replaceAll("At the rate","@");
        emailText = emailText.replaceAll("at the rate","@");
        emailText = formatBodyText(emailText);

        return emailText;
    }

    /**
     * @param Activity
     */
    public void logout(Activity Activity) {
        session = null;
        Activity.finish();
    }
}

