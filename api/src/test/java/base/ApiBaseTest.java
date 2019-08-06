package base;

import config.EnvConfig;
import config.IoCRestConfig;
import org.openapitools.client.ApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import service.ApiClientService;

@ContextConfiguration(classes={IoCRestConfig.class})
public class ApiBaseTest extends BaseTest {

    protected EnvConfig config;
    protected ApiClient apiClient;

    @Autowired
    ApiClientService apiClientService;

    @BeforeSuite
    public void beforeSuite(){
        System.out.println("BeforeSuite");
        ApplicationContext ctx = new AnnotationConfigApplicationContext(EnvConfig.class);
        config = ctx.getBean(EnvConfig.class);
    }

    @BeforeMethod
    public void beforeMethod(){
        System.out.println("BeforeMethod");
        apiClient = apiClientService.getApiClient(config.getWebUrl());
    }

    @AfterMethod
    public void afterMethod(){System.out.println("AfterMEthod");
    }

    @AfterSuite
    public void afterSuite() {System.out.println("AfterSuite");}
}
