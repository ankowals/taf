package base;

import config.IoCRestConfig;
import lombok.extern.log4j.Log4j2;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
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

    protected ApiClient apiClient;

    @Autowired
    ApiClientService apiClientService;

    @BeforeSuite
    public void beforeSuite(ITestContext testContext){
        log.info("SUITE " + testContext.getSuite().getName() +  " STARTED");
    }

    @BeforeMethod
    public void beforeMethod(Method method){
        log.info("TEST " + method.getName() + " STARTED");
        apiClient = apiClientService.getApiClient(environmentConfig.webUrl());
    }

    @AfterMethod
    public void afterMethod(Method method){
        log.info("TEST " + method.getName() + " ENDED");
    }

    @AfterSuite
    public void afterSuite(ITestContext testContext) {
        log.info("SUITE " + testContext.getSuite().getName() +  " ENDED");
    }
}
