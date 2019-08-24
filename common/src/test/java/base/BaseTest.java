package base;

import config.EnvironmentConfig;
import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.io.IoBuilder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;

@Log4j2
@Listeners(BaseListener.class)
public class BaseTest extends AbstractTestNGSpringContextTests {

    protected static EnvironmentConfig environmentConfig ;


    //a class initializer used to initialize logger.
    static {
        //redirect StdOut and StdErr to the logger
        System.setOut(
                IoBuilder.forLogger(LogManager.getLogger())
                        .setLevel(Level.DEBUG).buildPrintStream()
        );
        System.setErr(IoBuilder.forLogger(LogManager.getLogger())
                .setLevel(Level.WARN).buildPrintStream()
        );

        //load environment properties
        environmentConfig = ConfigFactory.create(EnvironmentConfig.class);
    }

}
