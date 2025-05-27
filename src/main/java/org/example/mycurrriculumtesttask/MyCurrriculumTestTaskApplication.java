package org.example.mycurrriculumtesttask;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyCurrriculumTestTaskApplication {

    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .directory("src/main/resources")
                .load();
        System.setProperty("DATABASE_URL", dotenv.get("DATABASE_URL"));
        System.setProperty("DATABASE_OWNER", dotenv.get("DATABASE_OWNER"));
        System.setProperty("DATABASE_PASSWORD", dotenv.get("DATABASE_PASSWORD"));

        SpringApplication.run(MyCurrriculumTestTaskApplication.class, args);
    }

}
