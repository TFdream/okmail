package melon;

import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * @author Ricky Fung
 */
public class ThymeleafTest {

    @Test
    public void testApp() throws IOException {

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
        ctx.setVariable("url", "http://www.thymeleaf.org");
        ctx.setVariable("email", "ricky_feng@163.com");

        StringWriter sw = new StringWriter(1024);
        templateEngine.process("registry", ctx, sw);

        String output = sw.toString();
        System.out.println(output);
    }
}
