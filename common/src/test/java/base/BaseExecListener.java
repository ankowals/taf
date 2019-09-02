package base;

import config.EnvironmentConfig;
import lombok.extern.log4j.Log4j2;
import org.aeonbits.owner.ConfigFactory;
import org.testng.IExecutionListener;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Log4j2
public class BaseExecListener implements IExecutionListener {
    @Override
    public void onExecutionFinish() {
        try {
            String path = System.getProperty("user.dir") + "/target/allure-results/environment.properties";
            new File(path).createNewFile();

            OutputStream out = Files.newOutputStream(Paths.get(path), StandardOpenOption.WRITE);
            EnvironmentConfig cfg = ConfigFactory.create(EnvironmentConfig.class);
            cfg.store(out, path);
        } catch (IOException e){
            log.error(e.getMessage());
        }
    }

}