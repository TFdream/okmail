# Melon
Melon built on top of the Java Mail API, aims to provide a simplify API for sending email.

## Features
* Built on top of the Java Mail API, aims to provide a simplify API for sending email.
* Supports text based emails/ HTML formatted emails/ text message with attachments .
* Supports CC/BCC
* Supports HTML Email Template(use Thymeleaf or Velocity as Template Engine)

## Quick Start
1. maven dependency
```
<dependency>
  <groupId>com.mindflow</groupId>
  <artifactId>melon</artifactId>
  <version>1.0.0</version>
</dependency>
```

2. Example
```
public class AppTest {

    private String from = "java_mail_001@163.com";
    private String password = "javamail";
    private String to = "java_mail_003@163.com";

    private Melon melon;

    @Before
    public void before() throws Exception {
        this.melon = Melon.newBuilder()
                .host("smtp.163.com")
                .port(465)
                .protocol("smtp")
                .username(from)
                .password(password)
                .debug(true)
                .auth(true)
                .ssl(true)
                .build();
    }

    @Test
    public void testSendText() throws MessagingException {

        Mail mail = Mail.newBuilder().from(from)
                .to(to)
                .cc("java_mail_002@163.com", "java_mail_004@163.com")
                .subject("测试邮件[普通文本]")
                .text("信件内容")
                .build();

        melon.send(mail);
    }

    @Test
    public void testSendHtml() throws MessagingException {

        Mail mail = Mail.newBuilder().from(from, "ricky fung")
                .to(to)
                .subject("测试邮件[HTML]")
                .html("<h1 font=red>信件内容</h1>")
                .build();

        melon.send(mail);
    }

    @Test
    public void testSendAttach() throws MessagingException, IOException {

        Mail mail = Mail.newBuilder().from(from, "ricky fung")
                .to(to)
                .subject("测试邮件[Attachment]")
                .html("<h1 font=red>信件内容</h1>")
                .attach(new File("F:/github/melon/README.md"), "测试README.md")
                .build();

        melon.send(mail);
    }

    @Test
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

        melon.send(mail);
    }

    @Test
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

        melon.send(mail);
    }
}
```

