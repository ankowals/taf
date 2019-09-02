package base;

import com.github.tomakehurst.wiremock.WireMockServer;
import config.IoCRestConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
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
        //pass log dir name and log file name to the Log4j2 context
        ThreadContext.put("logFileName", method.getName() + "_" + System.nanoTime() + "_" + Thread.currentThread().getId());
        ThreadContext.put("logDirName", method.getName());

        log.info("TEST " + method.getName() + " STARTED");
        mockServer.start();
        apiClient = apiClientService.getApiClient(environmentConfig.webUrl() + ":" + mockServer.port(), "https://petstore.swagger.io/v2/swagger.json");
    }

    @AfterMethod
    public void afterMethod(Method method){
        log.info("TEST " + method.getName() + " ENDED");
        attachLogToReport();
        mockServer.stop();
    }

    @AfterSuite
    public void afterSuite(ITestContext testContext) {
        log.info("SUITE " + testContext.getSuite().getName() +  " ENDED");
    }
}
