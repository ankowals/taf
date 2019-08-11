package base;

import config.EnvConfig;
import config.IoCRestConfig;
import lombok.extern.log4j.Log4j2;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import service.ApiClientService;

import java.lang.reflect.Method;

@Log4j2
@ContextConfiguration(classes={IoCRestConfig.class})
public class ApiBaseTest extends BaseTest {

    protected EnvConfig config;
    protected ApiClient apiClient;

    @Autowired
    ApiClientService apiClientService;

    @BeforeSuite
    public void beforeSuite(ITestContext testContext){
        log.debug("SUITE " + testContext.getSuite().getName() +  " STARTED");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(EnvConfig.class);
        config = ctx.getBean(EnvConfig.class);
    }

    @BeforeMethod
    public void beforeMethod(Method method){
        log.debug("TEST " + method.getName() + " STARTED");
        apiClient = apiClientService.getApiClient(config.getWebUrl());
    }

    @AfterMethod
    public void afterMethod(Method method){
        log.debug("TEST " + method.getName() + " ENDED");
    }

    @AfterSuite
    public void afterSuite(ITestContext testContext) {
        log.debug("SUITE " + testContext.getSuite().getName() +  " ENDED");
    }
}
