package config;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Config;
import util.CryptoUtil;

@Config.DecryptorClass( CryptoUtil.class )
@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({ "classpath:env.${active.environment}.properties",
                  "classpath:selenium.properties"})
public interface EnvironmentConfig extends Accessible {

    @Key("db.url")
    String dbUrl();

    @Key("app.port")
    int appPort();

    @Key("web.url")
    String webUrl();

    @Key("chrome.driver.path")
    String chromeDriverPath();

    @Key("chrome.driver.testContainers.use")
    boolean chromeDriverUseTestContainers();

    @Key("chrome.driver.testContainers.numberOfNodes")
    int chromeDriverNumberOfNodes();

    @EncryptedValue
    @Key("db.pass")
    String dbPass();

}
