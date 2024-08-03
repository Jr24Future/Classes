package ta4_1.MoneyFlow_Backend;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@TestConfiguration
@Profile({"dev", "prod"})
public class WebSocketTestConfig {

    @Bean
    @Primary
    public ServerEndpointExporter serverEndpointExporter() {
        // We're returning null here to effectively disable WebSocket server configuration for tests.
        return null;
    }
}
