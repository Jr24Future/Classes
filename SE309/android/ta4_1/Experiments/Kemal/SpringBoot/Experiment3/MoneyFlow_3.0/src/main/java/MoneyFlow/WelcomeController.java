package MoneyFlow;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MoneyFlow Insight Welcome Controller
 * Developed by group TA4_1.
 * @author Kemal Yavuz
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to MoneyFlow Insight";
    }

    @GetMapping("/version") //Updated Version
    public String getVersion() {
        return "MoneyFlow Insight Version 3.0";
    }

    @GetMapping("/feature") //Added new Features
    public String getFeature() {
        return "1. Version Check Feature Added.\n2. Added User Operations to the App.\n3. Added Financial Features.\n";
    }
}
