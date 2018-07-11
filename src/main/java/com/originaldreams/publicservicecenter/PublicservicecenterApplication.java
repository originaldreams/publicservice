package com.originaldreams.publicservicecenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class PublicservicecenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(PublicservicecenterApplication.class, args);
    }
}
