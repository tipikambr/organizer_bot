package organizer.mail;

import org.glassfish.jersey.server.model.Suspendable;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.Date;
import java.util.Properties;

public class ReadEmail {
    public static void main(String[] args) throws MessagingException {
        Properties properties = new Properties();
        properties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getDefaultInstance(properties);
        Store store = null;
        try {
             store = session.getStore("imap");
            store.connect("imap.yandex.ru", 993, "tipikambr@yandex.ru", "yrpvxfkmpsaknzgg");
             Folder inbox = null;
            try {
                long time = new Date(new Date().getTime() - 48*60*60).getTime();
                Date start = new Date();
                System.out.println(start);

                inbox = store.getFolder("INBOX");
                inbox.open(Folder.READ_ONLY);
                int count = inbox.getMessageCount();
                Message[] messages = inbox.getMessages();
                int i = 0;
                for (Message message : messages) {
                    //От кого
                    i++;
//                    System.out.println(++i);
//                    if (message.getReceivedDate().getTime() > time){
//                        String from = ((InternetAddress) message.getFrom()[0]).getAddress();
//                        System.out.println();
//                        System.out.println("FROM: " + from + " " + message.getReceivedDate());
//                        System.out.println("SUBJECT: " + message.getSubject());
                    }
                System.out.println(i);
                System.out.println(new Date() + "-----" + new Date(new Date().getTime() - start.getTime()).getTime());
            } finally {
                if (inbox != null) {
                    //Не забываем закрыть собой папку сообщений.
                    inbox.close(false);
                }
            }

        } finally {
            if (store != null) {
                //И сам почтовый ящик тоже закрываем
                store.close();
            }
        }
    }
}