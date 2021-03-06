/*
 * Swagger Petstore
 * This is a sample server Petstore server.  You can find out more about Swagger at [http://swagger.io](http://swagger.io) or on [irc.freenode.net, #swagger](http://swagger.io/irc/).  For this sample, you can use the api key `special-key` to test the authorization filters.
 *
 * OpenAPI spec version: 1.0.0
 * Contact: apiteam@swagger.io
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client;

import io.restassured.builder.RequestSpecBuilder;
import org.openapitools.client.api.PetApi;
import org.openapitools.client.api.StoreApi;
import org.openapitools.client.api.UserApi;

import java.util.function.Supplier;

import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.openapitools.client.GsonObjectMapper.gson;

public class ApiClient {
    public static final String BASE_URI = "https://petstore.swagger.io/v2";

    private final Config config;

    private ApiClient(Config config) {
        this.config = config;
    }

    public static ApiClient api(Config config) {
        return new ApiClient(config);
    }

    public PetApi pet() {
        return PetApi.pet(config.baseReqSpec.get());
    }
    public StoreApi store() {
        return StoreApi.store(config.baseReqSpec.get());
    }
    public UserApi user() {
        return UserApi.user(config.baseReqSpec.get());
    }

    public static class Config {
        private Supplier<RequestSpecBuilder> baseReqSpec = () -> new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())));

        /**
         * Use common specification for all operations
         * @param supplier supplier
         * @return configuration
         */
        public Config reqSpecSupplier(Supplier<RequestSpecBuilder> supplier) {
            this.baseReqSpec = supplier;
            return this;
        }

        public static Config apiConfig() {
            return new Config();
        }
    }
}