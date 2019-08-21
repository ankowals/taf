package tests;

import base.ApiBaseTest;
import lombok.extern.log4j.Log4j2;
import org.openapitools.client.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import service.ApiClientService;

import java.lang.reflect.Method;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.qala.datagen.RandomValue.between;
import static io.qala.datagen.RandomValue.length;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
public class ApiTestDes extends ApiBaseTest {

    @Autowired
    ApiClientService apiClientService;

    @Override
    @BeforeMethod
    public void beforeMethod(Method method){
        log.debug("TEST " + method.getName() + " STARTED");
        mockServer.start();
        apiClient = apiClientService.getApiClientWithoutValidationFilter("http://localhost:" + mockServer.port());
    }

    @Test
    public void deserializationTest(){
       log.debug("deserializationTest");

        //setup mock
        StringBuilder sb = new StringBuilder();
        sb.append( "[\n" );
        for (int i = 0; i < 6; i++){
            sb.append("  {\n" +
                    "    \"id\": " + between(Long.MIN_VALUE, Long.MAX_VALUE).Long() + ",\n" +
                    "    \"category\": {\n" +
                    "      \"id\": " + between(Long.MIN_VALUE, Long.MAX_VALUE).Long() + ",\n" +
                    "      \"name\": \"" + length(16).alphanumeric() + "\"\n" +
                    "    },\n" +
                    "    \"name\": \"doggie\",\n" +
                    "    \"photoUrls\": [\n" +
                    "      \"" + length(16).alphanumeric() + "\"\n" +
                    "    ],\n" +
                    "    \"tags\": [\n" +
                    "      {\n" +
                    "        \"id\": " + between(Long.MIN_VALUE, Long.MAX_VALUE).Long() + ",\n" +
                    "        \"name\": \"" + length(17).alphanumeric() + "\"\n" +
                    "      }\n" +
                    "    ],\n" +
                    "    \"status\": \"sold\"\n" +
                    "  },\n" );
        }
        sb.append( "]" );
        sb.deleteCharAt(sb.length()-2); //remove last new line character from the list
        sb.deleteCharAt(sb.length()-2); //remove last comma from the list
        String mockResponseBody = sb.toString();

        mockServer.stubFor(get(urlEqualTo("/pet/findByStatus?status=sold"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

       Pet[] pets = apiClient.pet()
               .findPetsByStatus().statusQuery("sold")
               .execute(r -> r.prettyPeek()).andReturn().as(Pet[].class);

       assertThat(pets[0].getStatus().getValue()).isEqualToIgnoringCase("sold");
       assertThat(pets.length).isEqualTo(6);
    }
}