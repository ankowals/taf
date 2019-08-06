package base;

import config.EnvConfig;
import config.IoCRestConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.WebDriverRunner.setWebDriver;

@ContextConfiguration(classes={IoCRestConfig.class})
public class UiBaseTest extends BaseTest {

    protected EnvConfig config;

    @BeforeSuite
    public void beforeSuite(){
        ApplicationContext ctx = new AnnotationConfigApplicationContext(EnvConfig.class);
        config = ctx.getBean(EnvConfig.class);
    }

    @BeforeTest
    public void beofreTest(){
        ChromeOptions options = prepareChromeOptions();
        WebDriver driver = createChromeDriver(options);
        setWebDriver(driver);
    }

    @AfterTest
    public void afterTest(){
        getWebDriver().quit();
    }

    private ChromeOptions prepareChromeOptions(){
        System.setProperty("webdriver.chrome.driver", getPathToResourceFile("chromedriver.exe"));
        ChromeOptions options = new ChromeOptions();
        //disable user prompt if extensions installation is blocked by admin
        options.setExperimentalOption("useAutomationExtension", false);
        //disable extensions and hide infobars
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");

        return options;
    }

    private WebDriver createChromeDriver(ChromeOptions options){
        return new ChromeDriver(options);
    }

    private String getPathToResourceFile(String resourceName){
        URL url = this.getClass().getClassLoader().getResource(resourceName);
        File file = null;
        try {
            file = new File(url.toURI());
        } catch (URISyntaxException e) {
            file = new File(url.getPath());
        } finally {
            return file.getAbsolutePath();
        }
    }

}
