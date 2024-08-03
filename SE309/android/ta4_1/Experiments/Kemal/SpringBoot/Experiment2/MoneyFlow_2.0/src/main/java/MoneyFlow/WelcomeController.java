package MoneyFlow; //Updated Package Name.

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

    @GetMapping("/version") //Added Version Check Feature
    public String getVersion() {
        return "MoneyFlow Insight Version 2.0";
    }

    @GetMapping("/feature") //Added Feature Check List
    public String getFeature() {
        return "1. Version Check Feature Added.\n2. Added User Operations to the App.\n";
    }
}
