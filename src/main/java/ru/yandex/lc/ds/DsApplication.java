package ru.yandex.lc.ds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SpringBootApplication
public class DsApplication implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(DsApplication.class);

    @Value("${test.value}")
    private int testValue;

    private static void externalizeConfig() throws IOException {
        var extConfig = new File("/config/application.properties");
        if (extConfig.exists()) {
            log.info("Found externalized config file {}, copying it to /application.properties", extConfig.toPath());
            Files.copy(extConfig.toPath(), new File("/application.properties").toPath());
        }else{
            log.info("Externalized config {} not found",extConfig.toPath());
        }
    }

    public static void main(String[] args) throws IOException {
        externalizeConfig();
        SpringApplication.run(DsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Docker secret test app started. Test value {}", testValue);
    }
}
