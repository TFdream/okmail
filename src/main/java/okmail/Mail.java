package okmail;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class Mail {
    private final String from;
    private final String nickname;
    private final String[] to;
    private final String[] cc;
    private final String[] bcc;
    private final String subject;
    private final String text;
    private final String html;
    private List<MimeBodyPart> attachments ;

    public Mail(Builder builder) {
        this.from = builder.from;
        this.nickname = builder.nickname;
        this.to = builder.to;
        this.cc = builder.cc;
        this.bcc = builder.bcc;
        this.subject = builder.subject;
        this.text = builder.text;
        this.html = builder.html;
        this.attachments = builder.attachments;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getFrom() {
        return from;
    }

    public String getNickname() {
        return nickname;
    }

    public String[] getTo() {
        return to;
    }

    public String[] getCc() {
        return cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public String getHtml() {
        return html;
    }

    public List<MimeBodyPart> getAttachments() {
        return attachments;
    }

    static class Builder {
        private String from;
        private String nickname;
        private String[] to;
        private String[] cc;
        private String[] bcc;
        private String subject;
        private String text;
        private String html;
        private List<MimeBodyPart> attachments;

        public Builder from(String from) {
            this.from = from;
            return this;
        }
        public Builder from(String from, String nickname) {
            this.from = from;
            this.nickname = nickname;
            return this;
        }
        public Builder to(String... to) {
            this.to = to;
            return this;
        }
        public Builder cc(String... cc) {
            this.cc = cc;
            return this;
        }
        public Builder bcc(String... bcc) {
            this.bcc = bcc;
            return this;
        }
        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }
        public Builder text(String text) {
            this.text = text;
            return this;
        }
        public Builder html(String html) {
            this.html = html;
            return this;
        }

        public Builder attach(File file) throws IOException, MessagingException {
            return attach(file, file.getName(), null);
        }

        public Builder attach(File file, String filename) throws IOException, MessagingException {
            return attach(file, filename, null);
        }

        public Builder attach(File file, String filename, String desc) throws IOException, MessagingException {
            if(this.attachments==null) {
                this.attachments = new ArrayList<>();
            }
            this.attachments.add(createAttachment(file, filename, desc));
            return this;
        }

        public Mail build() {
            return new Mail(this);
        }

        private MimeBodyPart createAttachment(File file, String filename, String desc) throws IOException, MessagingException {
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.attachFile(file);
            FileDataSource fds = new FileDataSource(file);
            mbp.setDataHandler(new DataHandler(fds));
            mbp.setDescription(desc);
            mbp.setFileName(MimeUtility.encodeText(filename));
            return mbp;
        }
    }
}
