package com.projeto.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class UserserviceApplication {

    public static void main(String[] args) {

        // Carrega o .env do módulo user-service
        Dotenv dotenv = Dotenv.configure()
                              .directory("back-end/user-service") // caminho relativo à raiz do projeto
                              .ignoreIfMissing()
                              .load();

        // Define as propriedades do Spring
        System.setProperty("spring.datasource.url", dotenv.get("DB_URL"));
        System.setProperty("spring.datasource.username", dotenv.get("DB_USERNAME"));
        System.setProperty("spring.datasource.password", dotenv.get("DB_PASSWORD"));
        System.setProperty("spring.datasource.driver-class-name", "org.postgresql.Driver");

        SpringApplication.run(UserserviceApplication.class, args);
    }
}
