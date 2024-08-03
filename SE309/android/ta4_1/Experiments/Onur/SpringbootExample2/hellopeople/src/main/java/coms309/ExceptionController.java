package coms309;

/**
 * Controller used to showcase what happens when an exception is thrown
 *
 * @author Onur Onal
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ExceptionController {

    // Endpoint to trigger a sample exception for testing purposes.
    @RequestMapping(method = RequestMethod.GET, path = "/oops")
    public String triggerException() {
        throw new RuntimeException("An exception was thrown");
    }

}
