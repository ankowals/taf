package tests;

import base.ApiBaseTest;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openapitools.client.model.Category;
import org.openapitools.client.model.Pet;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;


public class ApiTest extends ApiBaseTest {

    Logger logger = LogManager.getLogger();

    @Test
    public void firstTest(){
        System.out.println("firstTest");
        logger.debug("new message written to the log file");
    }

    @Test
    public void secondTest(){
        logger.debug("secondTest");
        assert false;
    }

    @Test
    public void thirdTest(){
        logger.debug("thirdTest");

        String baseUrl = config.getWebUrl();

        given().filter(new RequestLoggingFilter()).filter(new ResponseLoggingFilter()).
                when().
                get(baseUrl + "/pet/findByStatus?status=available").
                then().
                assertThat().
                statusCode(200);
    }

    @Test
    public void fourthTest(){
        logger.debug("fourthTest");
        apiClient.pet().addPet();

        Pet pet = new Pet();
        pet.category(new Category().name("dog")).name("Lena").id(1l);
        apiClient.pet()
                .addPet()
                    .body(pet)
                .execute(r -> r.prettyPeek());
    }

}
