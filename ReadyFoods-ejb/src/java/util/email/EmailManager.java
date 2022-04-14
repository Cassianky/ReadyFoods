package util.email;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailManager {

    private final String emailServerName = "smtp.gmail.com";
    private final String mailer = "JavaMailer";
    private String smtpAuthUser;
    private String smtpAuthPassword;
     
    public EmailManager() {
    }

    public EmailManager(String smtpAuthUser, String smtpAuthPassword) {
        this.smtpAuthUser = smtpAuthUser;
        this.smtpAuthPassword = smtpAuthPassword;
    }

    public Boolean email(String name, String fromEmailAddress, String toEmailAddress, String path) {
        String emailBody = "Dear " + name + ", we are excited to welcome you to ReadyFoods! Do check out ReadyFoods' features from recipes, mealkits to a personal food diary!";

        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", emailServerName);
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Dear " + name + ", Welcome to ReadyFoods!");
                msg.setText(emailBody);
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                // This mail has 2 part, the BODY and the embedded image
                MimeMultipart multipart = new MimeMultipart("related");

                // first part (the html)
                BodyPart messageBodyPart = new MimeBodyPart();
                String htmlText = "<H1>Thank you for joining us!</H1><img src=\"cid:image\">";
                messageBodyPart.setContent(htmlText, "text/html");
                // add it
                multipart.addBodyPart(messageBodyPart);

                // second part (the image)
                messageBodyPart = new MimeBodyPart();
                DataSource fds = new FileDataSource(path);
                
                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setHeader("Content-ID", "<image>");

                // add image to the multipart
                multipart.addBodyPart(messageBodyPart);

                // put everything together
                msg.setContent(multipart);
                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        
        
        
    }
    
    public Boolean emailOrderInvoice(String name, String fromEmailAddress, String toEmailAddress, String path) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", emailServerName);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", emailServerName);
            props.put("mail.smtp.debug", "true");
            javax.mail.Authenticator auth = new SMTPAuthenticator(smtpAuthUser, smtpAuthPassword);
            Session session = Session.getInstance(props, auth);
            session.setDebug(true);
            Message msg = new MimeMessage(session);

            if (msg != null) {
                msg.setFrom(InternetAddress.parse(fromEmailAddress, false)[0]);
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress, false));
                msg.setSubject("Dear " + name + ", your order at ReadyFoods has been confirmed");
                msg.setHeader("X-Mailer", mailer);
                Date timeStamp = new Date();
                msg.setSentDate(timeStamp);

                // This mail has 2 part, the BODY and the embedded image
                MimeMultipart multipart = new MimeMultipart("related");

                // first part (the html)
                BodyPart messageBodyPart = new MimeBodyPart();
                String htmlText = "<H1>View your order invoice below!</H1>";
                messageBodyPart.setContent(htmlText, "text/html");
                // add it
                multipart.addBodyPart(messageBodyPart);

                // second part (the image)
                messageBodyPart = new MimeBodyPart();
                DataSource fds = new FileDataSource(path);
                
                messageBodyPart.setDataHandler(new DataHandler(fds));
                messageBodyPart.setFileName("OrderInvoice.pdf");
                // add image to the multipart
                multipart.addBodyPart(messageBodyPart);

                // put everything together
                msg.setContent(multipart);
                Transport.send(msg);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return false;
        }
        
        
        
    }
}
