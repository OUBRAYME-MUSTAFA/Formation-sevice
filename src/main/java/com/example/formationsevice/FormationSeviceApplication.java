package com.example.formationsevice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.CrossOrigin;

@SpringBootApplication
@EnableFeignClients
@CrossOrigin("http://localhost:4200/")
public class FormationSeviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormationSeviceApplication.class, args);
    }

}
