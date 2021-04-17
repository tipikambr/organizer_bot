package organizer.mail;

import organizer.bot.BotOrganizer;
import organizer.data.base.connection.utils.Mail;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

public class MailCheck {
    private static String chooseImapHost(String mail){
        return switch (mail.split("@")[1]){
            case "gmail.com" -> "imap.gmail.com";
            case "yandex.ru" ->"imap.yandex.ru";
            default -> null;
        };
    }

    public static ArrayList<String> checkMail(String email, String password, String[] words, Date lastCheck) throws MessagingException  {
        ArrayList<String> answerMessages = new ArrayList<>(0);
        Properties properties = new Properties();
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(properties);
        Store store = null;
        try {
            store = session.getStore("imap");
            store.connect(chooseImapHost(email), 993, email, password);
            Folder inbox = null;
            try {

                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);

                SearchTerm newerThen = new ReceivedDateTerm(ComparisonTerm.GT, lastCheck);
                Message[] messages = inbox.search(newerThen);
                for (Message message : messages){
                    for (var word : words)
                        if (message.getReceivedDate().after(lastCheck) && message.getSubject() != null && (message.getSubject().toLowerCase().contains(word.toLowerCase()) || message.getFrom()[0].toString().toLowerCase().contains(word.toLowerCase()))) {
                            answerMessages.add(message.getSubject());
                            break;
                        }
                }
            } finally {
                if (inbox != null)
                    inbox.close(false);
            }
        } catch (AuthenticationFailedException ignored) {
        } finally {
            if (store != null)
                store.close();
        }
        return answerMessages;
    }

    public static boolean isAlive(String mail, String passwd) throws MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(properties);
        Store store = null;
        try {
            store = session.getStore("imap");
            store.connect(chooseImapHost(mail), 993, mail, passwd);
            return store.isConnected();
        } catch (Exception e){
            if (store != null)
                store.close();
            return false;
        }
    }
}
