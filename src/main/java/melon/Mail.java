package melon;

import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
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
    private final String subject;
    private final String text;
    private final String html;
    private final MimeMultipart multipart;

    public Mail(Builder builder) {
        this.from = builder.from;
        this.nickname = builder.nickname;
        this.to = builder.to;
        this.cc = builder.cc;
        this.subject = builder.subject;
        this.text = builder.text;
        this.html = builder.html;
        this.multipart = builder.multipart;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getFrom() {
        return from;
    }

    public String[] getTo() {
        return to;
    }

    public String[] getCc() {
        return cc;
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

    public MimeMultipart getMultipart() {
        return multipart;
    }

    public String getNickname() {
        return nickname;
    }

    static class Builder {
        private String from;
        private String nickname;
        private String[] to;
        private String[] cc;
        private String subject;
        private String text;
        private String html;
        private List<MimeBodyPart> parts;
        private MimeMultipart multipart;

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

        public Builder attach(File file, String filename) throws IOException, MessagingException {
            if(this.parts==null) {
                this.parts = new ArrayList<MimeBodyPart>();
            }
            this.parts.add(createAttachment(file, filename, null));
            return this;
        }

        public Builder attach(File file, String filename, String desc) throws IOException, MessagingException {
            if(this.parts==null) {
                this.parts = new ArrayList<MimeBodyPart>();
            }
            this.parts.add(createAttachment(file, filename, desc));
            return this;
        }

        public Mail build() {
            return new Mail(this);
        }

        private MimeBodyPart createAttachment(File file, String filename, String desc) throws IOException, MessagingException {
            MimeBodyPart mbp = new MimeBodyPart();
            mbp.attachFile(file);
            mbp.setDescription(desc);
            mbp.setFileName(filename);
            return mbp;
        }
    }
}
