package com.dal.asdc.reconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class ReconnectApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ReconnectApplication.class, args);
        String activeProfiles = String.join(", ", context.getEnvironment().getActiveProfiles());
        System.out.println("Active Profiles: " + activeProfiles);

    }


}
