package com.codingshuttle.project.uber.uberApp;

import com.codingshuttle.project.uber.uberApp.services.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UberAppApplicationTests {

	@Autowired
	private EmailSenderService emailSenderService;

	@Test
	void contextLoads() {
		emailSenderService.sendEmail("dinod88370@coloruz.com","This is a testing mail 1", "Body of my testing email 1");
	}

	@Test
	void sendEmailMultiple() {
		String emails[] = {
				"dinod88370@coloruz.com",
				"aimaimankhan@gmail.com"
		};

		emailSenderService.sendEmail(emails,"This is a testing mail 2", "Body of my testing email 2");

	}

}
