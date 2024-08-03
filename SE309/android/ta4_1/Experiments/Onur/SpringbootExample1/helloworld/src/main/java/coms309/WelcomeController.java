package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

/*
@author Onur Onal
 */

@RestController
class WelcomeController {

    private User user = new User("Onur", "Onal");
    private String name = user.getName();

    // Makes a GET request to return a string.
    // Request can be made in Postman with: http://localhost:8080/
    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to MoneyFlow Insight!";
    }

    // Makes a GET request to return a string based on the users name.
    // Request can be made in Postman with: http://localhost:8080/Name
    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to MoneyFlow Insight: " + name;
    }
}
