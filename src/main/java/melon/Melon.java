package melon;

import melon.util.StringUtils;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * JavaMail: https://javaee.github.io/javamail/
 *
 * @author Ricky Fung
 */
public class Melon {
    private final String host;
    private final int port;
    private final String protocol;
    private final String username;
    private final String password;
    private final Boolean auth;
    private final Boolean ssl;
    private final Boolean debug;
    private final int timeout;

    private final Session session;

    private Melon(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.protocol = builder.protocol;
        this.username = builder.username;
        this.password = builder.password;
        this.auth = builder.auth;
        this.ssl = builder.ssl;
        this.debug = builder.debug;
        this.timeout = builder.timeout;

        this.session = Session.getInstance(getConfig(), new DefaultAuthenticator(username, password));
        if (debug) {
            session.setDebug(true);
        }
    }

    public void send(Mail mail) throws MessagingException {
        Message msg = convertToMessage(mail);
        Transport.send(msg);
    }

    private Message convertToMessage(Mail mail) throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(mail.getFrom()));
        if(mail.getTo()!=null && mail.getTo().length>0) {
            InternetAddress[] address = new InternetAddress[mail.getTo().length];
            for(int i=0; i<mail.getTo().length; i++) {
                address[i] = new InternetAddress(mail.getTo()[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, address);
        }
        if(mail.getCc()!=null && mail.getCc().length>0) {
            InternetAddress[] address = new InternetAddress[mail.getCc().length];
            for(int i=0; i<mail.getCc().length; i++) {
                address[i] = new InternetAddress(mail.getCc()[i]);
            }
            msg.setRecipients(Message.RecipientType.CC, address);
        }

        msg.setSubject(mail.getSubject());
        if (mail.getMultipart() != null) {
            msg.setContent(mail.getMultipart());
        } else {
            msg.setText(mail.getText());
        }
        msg.setSentDate(new Date());
        return msg;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private Properties getConfig() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", auth.toString());
        props.put("mail.smtp.ssl.enable", ssl.toString());
        props.put("mail.transport.protocol", protocol);
        props.put("mail.debug", debug);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.timeout", timeout);
        return props;
    }

    static class Builder {
        private String host;
        private int port;
        private String protocol;  //默认smtp
        private String username;
        private String password;
        private Boolean auth;
        private Boolean ssl;
        private Boolean debug;
        private int timeout;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }
        public Builder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }
        public Builder username(String username) {
            this.username = username;
            return this;
        }
        public Builder password(String password) {
            this.password = password;
            return this;
        }
        public Builder auth(boolean auth) {
            this.auth = auth;
            return this;
        }
        public Builder ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }
        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }
        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Melon build() {
            if(StringUtils.isBlank(this.protocol)){
                this.protocol = "smtp";
            }
            if(this.auth==null) {
                this.auth = Boolean.TRUE;
            }
            if(this.ssl==null) {
                this.ssl = Boolean.TRUE;
            }
            if(this.debug==null) {
                this.debug = Boolean.FALSE;
            }
            return new Melon(this);
        }
    }

}
