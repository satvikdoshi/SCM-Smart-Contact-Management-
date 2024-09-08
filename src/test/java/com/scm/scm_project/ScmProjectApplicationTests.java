package com.scm.scm_project;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.scm.scm_project.services.EmailService;

@SpringBootTest
class ScmProjectApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private EmailService service;

	@Test
	void sendEmailTest(){
		service.sendEmail("satvikdoshi27@gmail.com", "Testing", "SCM");
	}

}
