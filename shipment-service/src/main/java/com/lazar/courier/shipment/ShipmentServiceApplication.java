package com.lazar.courier.shipment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
public class ShipmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShipmentServiceApplication.class, args);
    }
}
