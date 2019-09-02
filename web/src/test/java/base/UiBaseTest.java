package base;


import browser.BrowserCustomization;
import browser.GridCustomization;
import com.codeborne.selenide.logevents.SelenideLogger;
import config.IoCRestConfig;
import config.IoCWebConfig;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.lifecycle.TestDescription;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;


@Log4j2
@ContextConfiguration(classes={IoCRestConfig.class, IoCWebConfig.class})
public class UiBaseTest extends BaseTest {

    @Lazy
    @Autowired
    BrowserWebDriverContainer chromeContainer;

    @Autowired
    BrowserCustomization browserCustomization;

    private static String url;

    @BeforeSuite
    public void beforeSuite(ITestContext testContext){
        log.info("SUITE " + testContext.getSuite().getName() +  " STARTED");
    }

    @AfterSuite
    public void afterSuite(ITestContext testContext) {
        log.info("SUITE " + testContext.getSuite().getName() +  " ENDED");
    }

    @BeforeTest
    public void beforeTest() {
        if (environmentConfig.chromeDriverUseTestContainers() && environmentConfig.chromeDriverNumberOfNodes() > 0) {
            log.info("Starting selenium gird");

            //spring ctx is initialized in @BeforeClass so we need to extract our been manually
            ApplicationContext ctx = new AnnotationConfigApplicationContext(IoCWebConfig.class);
            GridCustomization gridCustomization = ctx.getBean(GridCustomization.class);

            url = gridCustomization.setupGird(environmentConfig.chromeDriverNumberOfNodes());
        }
    }

    @BeforeMethod
    public void beforeMethod(Method method) throws MalformedURLException {
        //pass log dir name and log file name to the Log4j2 context
        ThreadContext.put("logFileName", method.getName() + "_" + System.nanoTime() + "_" + Thread.currentThread().getId());
        ThreadContext.put("logDirName", method.getName());

        //allure - selenide integration
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide().screenshots(true).savePageSource(false));

        log.info("TEST " + method.getName() + " STARTED");
        if (!environmentConfig.chromeDriverUseTestContainers()){
            log.info("Starting web driver");
            System.setProperty("webdriver.chrome.driver", environmentConfig.chromeDriverPath() + File.separator + "chromedriver.exe");
            ChromeOptions options = browserCustomization.prepareChromeOptions();
            setWebDriver(createChromeDriver(options));
        }
        if (environmentConfig.chromeDriverUseTestContainers() && environmentConfig.chromeDriverNumberOfNodes() == 0) {
            log.info("Starting web driver in test container");
            chromeContainer.start();
            setWebDriver(chromeContainer.getWebDriver());
        }

        if (environmentConfig.chromeDriverUseTestContainers() && environmentConfig.chromeDriverNumberOfNodes() > 0) {
            log.info("Starting web driver in grid");

            RemoteWebDriver driver = new RemoteWebDriver(new URL(url), DesiredCapabilities.chrome());
            setWebDriver(driver);
        }

    }

    @AfterMethod
    public void afterMethod(Method method, ITestResult result){
        log.info("TEST " + method.getName() + " ENDED");
        attachLogToReport();
        if (environmentConfig.chromeDriverUseTestContainers() && environmentConfig.chromeDriverNumberOfNodes() == 0) {
            TestDescription description = new TestDescription() {
                @Override
                public String getTestId() {
                    return method.getName();
                }

                @Override
                public String getFilesystemFriendlyName() {
                    return method.getName();
                }
            };

            chromeContainer.afterTest(description, Optional.of(result)
                    .filter(r -> !r.isSuccess())
                    .map(__ -> new RuntimeException()));

            chromeContainer.stop();
        }

        if (environmentConfig.chromeDriverUseTestContainers() && environmentConfig.chromeDriverNumberOfNodes() > 0) {
            getWebDriver().quit();
        }

        if (!environmentConfig.chromeDriverUseTestContainers()) {
            getWebDriver().quit();
        }
    }

    private WebDriver createChromeDriver(ChromeOptions options){
        return new ChromeDriver(options);
    }

}