package com.cl.oc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = "classpath:dubbo-consumer.xml")
public class OcApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcApplication.class, args);
    }

}
