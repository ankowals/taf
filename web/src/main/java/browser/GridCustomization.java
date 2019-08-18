package browser;

import com.github.dockerjava.api.model.Volume;
import io.restassured.http.ContentType;
import lombok.extern.log4j.Log4j2;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.await;

@Log4j2
public class GridCustomization {

    private GenericContainer setupGirdHub(){
        return new GenericContainer<>("selenium/hub")
                .withNetwork(Network.SHARED)
                .withNetworkAliases("grid-hub"); //assign alias DNS name
    }

    private List<GenericContainer> setupGridNodes(int numberOfNodes, GenericContainer hub){
        List<GenericContainer> nodes = new ArrayList<>();

        Map<String, String> env = new HashMap<>();
        env.put("NODE_MAX_INSTANCES", "2");
        env.put("HUB_HOST", "grid-hub");
        env.put("NODE_MAX_SESSION", "2");

        for (int i = 0; i < numberOfNodes; i++){
            nodes.add(new GenericContainer<>("selenium/node-chrome-debug")
                    .withNetwork(hub.getNetwork())
                    .withCreateContainerCmdModifier(it -> it.withVolumes(new Volume("/dev/shm:/dev/shm")))
                    .withEnv(env));
        }

        return nodes;
    }

    public String setupGird(int numberOfNodes){
        log.info("Setting up a grid using testcontainers");

        GenericContainer hub = setupGirdHub();
        List<GenericContainer> nodes = setupGridNodes(numberOfNodes, hub);

        hub.start();
        for(GenericContainer node : nodes){
            node.start();
        }

        log.info("hub address is " + hub.getContainerIpAddress());
        log.info("hub port number is " + hub.getMappedPort(4444));
        log.info("hub image name is " + hub.getDockerImageName());
        String url = "http://"+hub.getContainerIpAddress()+":"+hub.getMappedPort(4444)+"/wd/hub";
        log.info("hub url is " + url);
        log.info("hub console url is " + "http://"+hub.getContainerIpAddress()+":"+hub.getMappedPort(4444)+"/grid/console");

        awaitForGirdReadiness(url);

        return url;
    }

    private void awaitForGirdReadiness(String url){
        await().atMost(30, TimeUnit.SECONDS)
                .with()
                .pollInterval(3, TimeUnit.SECONDS)
                .ignoreExceptions()
                .until(() -> getGridStatus(url));
    }

    private boolean getGridStatus(String url){

        return given()
                .contentType(ContentType.JSON)
                .when()
                .get(url + "/status")
                .then()
                .extract()
                .path("value.ready");
    }

}
