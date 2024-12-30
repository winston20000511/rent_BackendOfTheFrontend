package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.example.demo.helper.SerialOrderNoUtil;


@SpringBootApplication
@EnableScheduling
public class DemoApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		SerialOrderNoUtil util = new SerialOrderNoUtil();

//		String dbHost = System.getenv("azure_db_host");
//		String dbUser = System.getenv("azure_db_username");
//		String dbPassword = System.getenv("azure_db_password");
//
//		if (dbHost == null || dbUser == null || dbPassword == null) {
//			System.out.println("環境變數未設置或讀取失敗！");
//		} else {
//			System.out.println("環境變數成功讀取：");
//			System.out.println("DB Host: " + dbHost);
//			System.out.println("DB User: " + dbUser);
//			System.out.println("DB Password: " + dbPassword);
//		}

	}


}
