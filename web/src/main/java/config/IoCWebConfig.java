package config;

import browser.BrowserCuztomization;
import browser.GridCustomization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;

import static org.testcontainers.containers.BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL;

@Configuration
@ComponentScan(value = "browser")
public class IoCWebConfig {

    @Autowired
    BrowserCuztomization browserCustomization;

    @Lazy
    @Bean
    //@Scope("prototype")
    public BrowserWebDriverContainer chromeContainer() {
        return new BrowserWebDriverContainer()
                .withCapabilities(browserCustomization.prepareChromeOptions())
                .withRecordingMode(RECORD_ALL, new File("target"));
    }

    @Lazy
    @Bean
    //@Scope("prototype")
    public GridCustomization gridCustomization() {
        return new GridCustomization();
    }

}
