# Melon
Melon aims to provide a simplify API for sending email.

## Features
* Built on top of the Java Mail API, aims to provide a simplify API for sending email.
* Supports text based emails/ HTML formatted emails/ text message with attachments .
* Supports CC/BCC
* Supports HTML Template( use Thymeleaf as Template Engine)

## Quick Start
1. maven dependency
```
<dependency>
  <groupId>com.mindflow</groupId>
  <artifactId>melon</artifactId>
  <version>1.0.0-SNAPSHOT</version>
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
                .subject("测试邮件[普通文本]")
                .text("信件内容")
                .build();

        melon.send(mail);
    }

    @Test
    public void testSendHtml() throws MessagingException {

        Mail mail = Mail.newBuilder().from(from,"VIP")
                .to(to)
                .subject("测试邮件[HTML]")
                .html("<h1 font=red>信件内容</h1>")
                .build();

        melon.send(mail);
    }

    @Test
    public void testSendAttach() throws MessagingException, IOException {

        Mail mail = Mail.newBuilder().from(from,"ricky fung")
                .to(to)
                .subject("测试邮件[Attachment]")
                .html("<h1 font=red>信件内容</h1>")
                .attach(new File("F:/github/melon/README.md"), "测试README.md")
                .build();

        melon.send(mail);
    }

    @Test
    public void testTemplate() throws IOException, MessagingException {

        //use thymeleaf
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
        ctx.setVariable("email", "ricky_feng@163.com");

        StringWriter sw = new StringWriter(1024);
        templateEngine.process("registry", ctx, sw);

        String output = sw.toString();
        System.out.println(output);

        Mail mail = Mail.newBuilder().from(from,"ricky fung")
                .to(to)
                .subject("测试邮件[Template]")
                .html(output)
                .build();

        melon.send(mail);
    }
}
```

