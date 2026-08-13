package dccargo.dcargoservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient shipmentWebClient(AppConfig appConfig) {
        String baseUrl = "http://" + appConfig.getIp() + ":" + appConfig.getShipmentport();
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


}
