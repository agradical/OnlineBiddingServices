package email;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    public Email(String email, String message_str) {
    	//System.out.println("in eamil");
        final String username = "gobidding404@gmail.com";
        final String password = "@gobidding404";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.required","true");
        //java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gobidding404@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(email));
            message.setSubject("Update on your Bid");
            message.setText(message_str);
            //System.out.println("mail sent");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com","gobidding404", "@gobidding404");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            //System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}