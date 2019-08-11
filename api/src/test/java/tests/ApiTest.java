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
import org.testng.annotations.Test;

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
        log.debug("fourthTest");
        apiClient.pet().addPet();

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

        apiClient.pet()
                .addPet()
                .body(randomPet)
                .execute(r -> r.prettyPeek());
    }

}
