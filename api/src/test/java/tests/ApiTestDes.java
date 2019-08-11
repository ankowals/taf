package tests;

import base.ApiBaseTest;
import lombok.extern.log4j.Log4j2;
import org.openapitools.client.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.ApiClientService;

import java.lang.reflect.Method;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ApiTestDes extends ApiBaseTest {

    @Autowired
    ApiClientService apiClientService;

    @Override
    @BeforeMethod
    public void beforeMethod(Method method){
        log.debug("TEST " + method.getName() + " STARTED");
        apiClient = apiClientService.getApiClientWithoutValidationFilter(config.getWebUrl());
    }

    @Test
    public void deserializationTest(){
       log.debug("deserializationTest");

       Pet[] pets = apiClient.pet()
               .findPetsByStatus().statusQuery("sold")
               .execute(r -> r.prettyPeek()).andReturn().as(Pet[].class);

       assertThat(pets[0].getStatus().getValue()).isEqualToIgnoringCase("sold");
    }
}