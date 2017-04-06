package com.karbaros.userregistrationusingsqlite;

/**
 * Created by shanu on 04-Apr-17.
 */


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


//Class is extending AsyncTask because this class is going to perform a networking operation
public class SendMail extends AsyncTask<Void, Void, Void> {

    //Declaring Variables
    private Context context;
    private View view;
    private Session session;

    //Information to send email
    private String email;
    private String subject;
    private String message;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    Config config = new Config();

    //Class Constructor
    public SendMail(View view, Context context, String email, String subject, String message) {
        //Initializing variables
        this.view = view;
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        // progressDialog = ProgressDialog.show(context, "Sending message", "Please wait...", false, false);
    }


    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getEMAIL(), config.getPASSWORD());
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mimeMessage = new MimeMessage(session);

            //Setting sender address
            mimeMessage.setFrom(new InternetAddress(config.getEMAIL()));
            //Adding receiver
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mimeMessage.setSubject(subject);
            //Adding message
            mimeMessage.setText(message);

            //Sending email
            Transport.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        //progressDialog.dismiss();
        //Showing a success message
        //Toast.makeText(context, "check your mail to continue...", Toast.LENGTH_LONG).show();

        /*final Snackbar snackbar = Snackbar
                .make(view, "Registration Success! Check Mail For Password", Snackbar.LENGTH_INDEFINITE);
        // Changing message text color
        snackbar.setActionTextColor(Color.RED);
        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        snackbar.show();*/
    }

}

