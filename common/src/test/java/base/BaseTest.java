package base;

import config.EnvironmentConfig;
import io.qameta.allure.Attachment;
import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.io.IoBuilder;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Listeners;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Log4j2
@Listeners({BaseTestListener.class, BaseExecListener.class})
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

    @Attachment(value = "log", type = "text/plain")
    protected byte[] attachLogToReport(){
        String fileName = ThreadContext.get("logFileName");
        String dir = ThreadContext.get("logDirName");
        File file = new File(System.getProperty("user.dir") + "/target/logs/" + dir + "/" + fileName + ".log");
        try {
            return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (IOException e) {
            log.error( "File " + file.getAbsolutePath() + " not found! " + e.getMessage());
        }

        return null;
    }

}
