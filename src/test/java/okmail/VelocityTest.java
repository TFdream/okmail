package okmail;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

/**
 * @author Ricky Fung
 */
public class VelocityTest {

    @Test
    @Ignore
    public void testApp() throws IOException {

        Properties props = new Properties();
        props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("velocity.properties"));
        VelocityEngine ve = new VelocityEngine(props);
        ve.init();

        Template t = ve.getTemplate("/templates/registry.vm");
        VelocityContext context = new VelocityContext();

        context.put("username", "ricky");
        context.put("url", "http://www.thymeleaf.org");
        context.put("email", "ricky_feng@163.com");

        StringWriter writer = new StringWriter(1024);
        t.merge(context, writer);

        String output = writer.toString();
        System.out.println(output);
    }
}
