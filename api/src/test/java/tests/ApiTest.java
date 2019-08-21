package tests;

import base.ApiBaseTest;
import data.TestDataFactory;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import lombok.extern.log4j.Log4j2;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.openapitools.client.model.Category;
import org.openapitools.client.model.Pet;
import org.openapitools.client.model.Tag;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.qala.datagen.RandomValue.between;
import static io.restassured.RestAssured.given;
import static org.jeasy.random.FieldPredicates.*;

@Log4j2
public class ApiTest extends ApiBaseTest {

    @Test
    public void firstTest(){
        System.out.println("firstTest");
        log.debug("new message written to the log file");
    }

    @Test
    public void secondTest(){
        log.debug("secondTest");
        assert false;
    }

    @Test
    public void thirdTest(){
        log.debug("thirdTest");

        //setup mock
        String mockResponseBody = "[\n" +
                "  {\n" +
                "    \"id\": -973,\n" +
                "    \"category\": {\n" +
                "      \"id\": 45,\n" +
                "      \"name\": \"eue65U6Fqr4ET9y_\"\n" +
                "    },\n" +
                "    \"name\": \"doggie\",\n" +
                "    \"photoUrls\": [\n" +
                "      \"A81KwZjxEbWpNmxV\"\n" +
                "    ],\n" +
                "    \"tags\": [\n" +
                "      {\n" +
                "        \"id\": -771,\n" +
                "        \"name\": \"XF2QMoSVOFrMLmLZ\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"status\": \"sold\"\n" +
                "  }]";

        mockServer.stubFor(get(urlEqualTo("/pet/findByStatus?status=sold"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

        String baseUrl = "http://localhost:" + mockServer.port();

        given().filter(new RequestLoggingFilter())
                .filter(new ResponseLoggingFilter())
                .when()
                .get(baseUrl + "/pet/findByStatus?status=sold")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void fourthTest(){
        log.debug("fourthTest");

        //setup mock
        String mockResponseBody = "{\n" +
                "  \"id\": 9199424981609321000,\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"dog\"\n" +
                "  },\n" +
                "  \"name\": \"Lena\",\n" +
                "  \"photoUrls\": [],\n" +
                "  \"tags\": []\n" +
                "}";

        mockServer.stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

        Pet pet = new Pet();
        pet.category(new Category().name("dog")).name("Lena").id(1l);
        apiClient.pet()
                .addPet()
                    .body(pet)
                .execute(r -> r.prettyPeek());
    }

    @Test
    public void fifthTest(){
        log.debug("fifthTest");
        apiClient.pet().addPet();

        Category dogCategoy = TestDataFactory.aDefaultCategory()
                .name("dog")
                .build();

        Pet dog = TestDataFactory.aDefaultPet()
                .name("Lena")
                .category(dogCategoy)
                .build();

        //setup mock
        String mockResponseBody = "{\n" +
                "  \"id\": " + dog.getId() + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": " + dogCategoy.getId() + ",\n" +
                "    \"name\": \"dog\"\n" +
                "  },\n" +
                "  \"name\": \"Lena\",\n" +
                "  \"photoUrls\": [],\n" +
                "  \"tags\": []\n" +
                "}";

        mockServer.stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

        apiClient.pet()
                .addPet()
                .body(dog)
                .execute(r -> r.prettyPeek());
    }

    @Test
    public void sixthTest(){
        log.debug("sixthTest");

        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(named("id"), () -> between(0, Long.MAX_VALUE).Long())
                .randomize(named("name").and(inClass(Pet.class)), () -> "Lena")
                .randomize(named("name").and(inClass(Category.class)), () -> "dog");

        EasyRandom easyRandom = new EasyRandom(parameters);

        Pet randomPet = easyRandom.nextObject(Pet.class);

        StringBuilder sb = new StringBuilder();
        for (Tag tag: randomPet.getTags()){
            sb.append( "{\n" )
              .append( "\"id\": \"" + tag.getId() + "\",\n" )
              .append( "\"name\": \"" + tag.getName() + "\"\n" )
              .append( "},\n" );
        }
        sb.deleteCharAt(sb.length()-2); //remove last comma from the list
        String sTags = sb.toString();

        //setup mock
        String mockResponseBody = "{\n" +
                "  \"id\": " + randomPet.getId() + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": " + randomPet.getCategory().getId() + ",\n" +
                "    \"name\": \"dog\"\n" +
                "  },\n" +
                "  \"name\": \"Lena\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"A81KwZjxEbWpNmxV\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                sTags +
                "  ],\n" +
                " \"status\": \"" + randomPet.getStatus().getValue() + "\"\n" +
                "}";

        mockServer.stubFor(post(urlEqualTo("/pet"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(mockResponseBody)));

        apiClient.pet()
                .addPet()
                .body(randomPet)
                .execute(r -> r.prettyPeek());
    }

}
