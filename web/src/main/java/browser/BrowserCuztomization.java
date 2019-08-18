package browser;

import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;

@Component
public class BrowserCuztomization {

    public ChromeOptions prepareChromeOptions(){
        ChromeOptions options = new ChromeOptions();
        //disable user prompt if extensions installation is blocked by admin
        options.setExperimentalOption("useAutomationExtension", false);
        //disable extensions and hide infobars
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-infobars");

        return options;
    }

}