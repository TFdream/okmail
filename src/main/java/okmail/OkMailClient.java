package okmail;

import okmail.internal.Constants;
import okmail.internal.util.StringUtils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * JavaMail: https://javaee.github.io/javamail/
 *
 * @author Ricky Fung
 */
public class OkMailClient {
    private final String host;
    private final int port;
    private final String protocol;
    private final String username;
    private final String password;
    private final Boolean auth;
    private final Boolean ssl;
    private final Boolean debug;
    private final String mailer;

    private final Session session;

    private OkMailClient(Builder builder) {
        this.host = builder.host;
        this.port = builder.port;
        this.protocol = builder.protocol;
        this.username = builder.username;
        this.password = builder.password;
        this.auth = builder.auth;
        this.ssl = builder.ssl;
        this.debug = builder.debug;
        this.mailer = builder.mailer;
        this.session = Session.getInstance(getConfig(), new DefaultAuthenticator(this.username, this.password));
        if (debug) {
            this.session.setDebug(true);
        }
    }

    public void send(Mail mail) throws MessagingException {
        Message msg = convertToMessage(mail);
        Transport.send(msg);
    }

    private Message convertToMessage(Mail mail) throws MessagingException {
        MimeMessage msg = new MimeMessage(session);
        String from = mail.getFrom()==null ? username: mail.getFrom();
        if(StringUtils.isNotEmpty(mail.getNickname())) {
            try {
                String nickName = MimeUtility.encodeText(mail.getNickname());
                msg.setFrom(new InternetAddress(String.format("%s<%s>", nickName, from)));
            } catch (UnsupportedEncodingException e) {
                throw new MessagingException("encode nickname failure:"+mail.getNickname(), e);
            }
        } else {
            msg.setFrom(new InternetAddress(from));
        }

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
        if(mail.getBcc()!=null && mail.getBcc().length>0) {
            InternetAddress[] address = new InternetAddress[mail.getBcc().length];
            for(int i=0; i<mail.getBcc().length; i++) {
                address[i] = new InternetAddress(mail.getBcc()[i]);
            }
            msg.setRecipients(Message.RecipientType.BCC, address);
        }

        msg.setSubject(mail.getSubject());

        MimeMultipart cover = new MimeMultipart();
        if(mail.getHtml()!=null) {
            cover.addBodyPart(buildHtmlPart(mail.getHtml()));
        } else {
            cover.addBodyPart(buildTextPart(mail.getText()));
        }

        boolean hasAttachments = mail.getAttachments() != null && mail.getAttachments().size()>0;
        MimeMultipart content = cover;
        if (hasAttachments) {   //有附件
            content = new MimeMultipart();
            MimeBodyPart wrap = new MimeBodyPart();
            wrap.setContent(cover);
            content.addBodyPart(wrap);
            //添加附件
            for (MimeBodyPart attachment : mail.getAttachments()) {
                content.addBodyPart(attachment);
            }
        }
        msg.setContent(content);
        msg.setHeader(Constants.HEADER_X_MAILER, this.mailer);
        msg.setSentDate(new Date());
        return msg;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    private MimeBodyPart buildTextPart(String text) throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(text);
        return bodyPart;
    }

    private MimeBodyPart buildHtmlPart(String html) throws MessagingException {
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(html, Constants.MEDIA_TYPE_HTML);
        return bodyPart;
    }

    private Properties getConfig() {
        Properties props = new Properties();
        props.put(Constants.MAIL_SMTP_AUTH, auth.toString());
        props.put(Constants.MAIL_SMTP_SSL_ENABLE, ssl.toString());
        props.put(Constants.MAIL_TRANSPORT_PROTOCOL, protocol);
        props.put(Constants.MAIL_DEBUG, debug);
        props.put(Constants.MAIL_SMTP_HOST, host);
        props.put(Constants.MAIL_SMTP_PORT, port);
        return props;
    }

    public static class Builder {
        private String host;
        private int port;
        private String protocol;  //默认smtp
        private String username;
        private String password;
        private Boolean auth;
        private Boolean ssl;
        private Boolean debug;
        public String mailer;

        public Builder() {
            this.protocol = Constants.PROTOCOL_SMTP;
            this.auth = Boolean.TRUE;
            this.ssl = Boolean.TRUE;
            this.debug = Boolean.FALSE;
            this.mailer =  Constants.DEFAULT_MAILER;
        }

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

        public Builder mailer(String mailer) {
            this.mailer = mailer;
            return this;
        }

        public OkMailClient build() {
            return new OkMailClient(this);
        }
    }

}
