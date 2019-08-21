package base;

import com.github.tomakehurst.wiremock.WireMockServer;
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

    @Autowired
    protected WireMockServer mockServer;

    @BeforeSuite
    public void beforeSuite(ITestContext testContext){
        log.info("SUITE " + testContext.getSuite().getName() +  " STARTED");
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        log.info("TEST " + method.getName() + " STARTED");
        mockServer.start();
        apiClient = apiClientService.getApiClient("http://localhost:" + mockServer.port(), environmentConfig.webUrl() + "/swagger.json");
    }

    @AfterMethod
    public void afterMethod(Method method){
        log.info("TEST " + method.getName() + " ENDED");
        mockServer.stop();
    }

    @AfterSuite
    public void afterSuite(ITestContext testContext) {
        log.info("SUITE " + testContext.getSuite().getName() +  " ENDED");
    }
}
