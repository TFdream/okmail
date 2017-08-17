package okmail;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private String from = "java_mail_001@163.com";
    private String to = "java_mail_002@163.com";

    private OkMailClient client;

    @Before
    @Ignore
    public void before() throws Exception {
        this.client = OkMailClient.newBuilder()
                .host("smtp.163.com")
                .port(465)
                .protocol("smtp")
                .username(from)
                .password("javamail")
                .debug(true)
                .auth(true)
                .ssl(true)
                .build();
    }

    @Test
    @Ignore
    public void testSendText() throws MessagingException, UnsupportedEncodingException {

        Mail mail = Mail.newBuilder().from(from)
                .to(to)
                .cc("java_mail_002@163.com", "java_mail_004@163.com")
                .subject("测试邮件[普通文本]")
                .text("信件内容")
                .build();

        client.send(mail);
    }

    @Test
    @Ignore
    public void testSendHtml() throws MessagingException, UnsupportedEncodingException {

        Mail mail = Mail.newBuilder().from(from, "ricky fung")
                .to(to)
                .subject("测试邮件[HTML]")
                .html("<h1 font=red>信件内容</h1>")
                .build();

        client.send(mail);
    }

    @Test
    @Ignore
    public void testSendAttach() throws MessagingException, IOException {

        Mail mail = Mail.newBuilder().from(from, "ricky fung")
                .to(to)
                .subject("测试邮件[Attachment]")
                .html("<h1 font=red>信件内容</h1>")
                .attach(new File("F:/github/okmail/README.md"), "测试README.md")
                .build();

        client.send(mail);
    }

    @Test
    @Ignore
    public void testThymeleafTemplate() throws IOException, MessagingException {

        ClassLoaderTemplateResolver templateResolver =
                new ClassLoaderTemplateResolver();

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));

        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(true);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context ctx = new Context();
        ctx.setLocale(Locale.ENGLISH);
        ctx.setVariable("username", "ricky");
        ctx.setVariable("url", "http://www.thymeleaf.org");
        ctx.setVariable("email", "ricky_feng@163.com");

        StringWriter sw = new StringWriter(1024);
        templateEngine.process("registry", ctx, sw);

        String output = sw.toString();
        System.out.println(output);

        Mail mail = Mail.newBuilder().from(from,"ricky fung")
                .to(to)
                .subject("测试邮件[模板邮件-Thymeleaf]")
                .html(output)
                .build();

        client.send(mail);
    }

    @Test
    @Ignore
    public void testVelocityTemplate() throws IOException, MessagingException {

        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("velocity.properties"));
        VelocityEngine ve = new VelocityEngine(props);
        ve.init();

        Template t = ve.getTemplate("/templates/registry.vm");
        VelocityContext context = new VelocityContext();

        context.put("username", "ricky");
        context.put("url", "http://www.thymeleaf.org");
        context.put("email", "ricky_feng@163.com");

        StringWriter sw = new StringWriter(1024);
        t.merge(context, sw);

        String output = sw.toString();
        System.out.println(output);

        Mail mail = Mail.newBuilder().from(from,"ricky fung")
                .to(to)
                .subject("测试邮件[模板邮件-Velocity]")
                .html(output)
                .build();

        client.send(mail);
    }
}
