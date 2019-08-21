package service;

import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.openapitools.client.ApiClient;

import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.openapitools.client.GsonObjectMapper.gson;

public class ApiClientService {

    public ApiClient getApiClient(String baseUrl, String specUrl){
        return ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> new RequestSpecBuilder().setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())))
                        .addFilter(new RequestLoggingFilter())
                        .addFilter(new ResponseLoggingFilter())
                        .addFilter(new OpenApiValidationFilter(specUrl))
                        .setBaseUri(baseUrl)));
    }

    public ApiClient getApiClientWithoutValidationFilter(String baseUrl){
        return ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> new RequestSpecBuilder().setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())))
                        .addFilter(new RequestLoggingFilter())
                        .addFilter(new ResponseLoggingFilter())
                        .setBaseUri(baseUrl)));
    }

}
