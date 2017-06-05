package melon;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * ${DESCRIPTION}
 *
 * @author Ricky Fung
 */
public class DefaultAuthenticator extends Authenticator {
    private final String username;
    private final String password;

    public DefaultAuthenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
    }
}
