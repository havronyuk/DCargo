package dccargo.dcargoservice.component;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ShipmentClient {

    private final WebClient shipmentClient;


    public ShipmentClient(
            @Qualifier("shipmentWebClient") WebClient shipmentClient
    ) {
        this.shipmentClient = shipmentClient;
    }






}
