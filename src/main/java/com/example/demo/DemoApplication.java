package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.demo.helper.SerialOrderNoUtil;


@SpringBootApplication
@EnableScheduling
@Slf4j
public class DemoApplication implements CommandLineRunner{
	@Autowired
	private Environment env;
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		SerialOrderNoUtil util = new SerialOrderNoUtil();
		String dbUsername = env.getProperty("azure.db.username");
		log.info("dbUsername"+dbUsername);
		
	}

  
}
