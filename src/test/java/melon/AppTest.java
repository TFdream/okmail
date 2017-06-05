package melon;

import org.junit.Before;
import org.junit.Test;
import javax.mail.MessagingException;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private Melon melon;

    @Before
    public void before() throws Exception {
        this.melon = Melon.newBuilder()
                .host("smtp.gmail.com")
                .port(465)
                .username("your.email@gmail.com")
                .password("your@password")
                .auth(true)
                .ssl(true)
                .build();
    }

    @Test
    public void testSendText() throws MessagingException {

        Mail mail = Mail.newBuilder().from("your.email@gmail.com")
                .to("ricky_feng@163.com")
                .subject("测试邮件[普通文本]")
                .text("信件内容")
                .build();

        melon.send(mail);
    }

    @Test
    public void testSendHtml() throws MessagingException {

        Mail mail = Mail.newBuilder().from("your.email@gmail.com","VIP")
                .to("ricky_feng@163.com")
                .subject("测试邮件[HTML]")
                .html("HTML")
                .build();

        melon.send(mail);
    }

}
