package MoneyFlow; //Update the package name.

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Kemal Yavuz
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Welcome to MoneyFlow Insight";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello, " + name + "! Welcome to MoneyFlow Insight";
    }

    @GetMapping("/version") //Added Version Check Feature
    public String getVersion() {
        return "MoneyFlow Insight Version 1.0";
    }

    @GetMapping("/feature") //Added Feature Check List
    public String getFeature() {
        return "1. Version Check Feature Added.\n";
    }
}