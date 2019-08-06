package base;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.apache.logging.log4j.status.StatusLogger;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;

@Listeners(BaseListener.class)
public class BaseTest extends AbstractTestNGSpringContextTests {
    Logger logger = LogManager.getLogger();

    //a class initializer used to initialize logger.
    static {
        //turn off warning related to missing configuration
        StatusLogger.getLogger().setLevel(Level.OFF);

        //redirect StdOut and StdErr to the logger so we can catch logs written by other tools like Selenium, RestAssured etc.
        System.setOut(
                IoBuilder.forLogger(LogManager.getLogger())
                        .setLevel(Level.DEBUG).buildPrintStream()
        );
        System.setErr(IoBuilder.forLogger(LogManager.getLogger())
                .setLevel(Level.WARN).buildPrintStream()
        );
    }

}
