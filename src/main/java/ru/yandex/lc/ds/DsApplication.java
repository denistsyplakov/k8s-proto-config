package ru.yandex.lc.ds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DsApplication implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(DsApplication.class);

	@Value("${test.value}")
	private int testValue;

	public static void main(String[] args) {
		SpringApplication.run(DsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Docker secret test app started. Test value {}",testValue);
	}
}
