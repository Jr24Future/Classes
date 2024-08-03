package ta4_1.MoneyFlow_Backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

// The @SpringBootTest annotation tells Spring Boot to look for the main configuration class
// (one with @SpringBootApplication, for instance) and use that to start a Spring application context.
@SpringBootTest
@ActiveProfiles("test")
public class MoneyFlowBackendApplicationTests {

	// Autowire the ApplicationContext to ensure that injection works properly
	@Autowired
	private ApplicationContext context;

	// A simple sanity test to ensure that the context loads without errors.
	@Test
	void contextLoads() {
		// The test passes if the application context loads successfully, which means all beans are created without error
		assert(context != null); // or use assertTrue, depending on your test needs
	}
}
