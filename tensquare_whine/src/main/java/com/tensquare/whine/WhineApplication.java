package com.tensquare.whine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import util.IdWorker;

@SpringBootApplication
@EnableEurekaClient
public class WhineApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhineApplication.class);
    }

    @Bean
    public IdWorker idWorker() {
        return new IdWorker();
    }
}
