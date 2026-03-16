package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		// Basic context loading test
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void keyBeansAreLoaded() {
		// Verify key service beans are loaded
		assertThat(applicationContext.containsBean("jwtService")).isTrue();
		assertThat(applicationContext.containsBean("userInfoService")).isTrue();
		assertThat(applicationContext.containsBean("personService")).isTrue();

		// Verify repository beans are loaded
		assertThat(applicationContext.containsBean("personRepository")).isTrue();
		assertThat(applicationContext.containsBean("userRepository")).isTrue();

		// Verify controller beans are loaded
		assertThat(applicationContext.containsBean("demoController")).isTrue();
		assertThat(applicationContext.containsBean("authController")).isTrue();
	}

	@Test
	void securityConfigurationLoads() {
		// Verify security configuration is loaded
		assertThat(applicationContext.containsBean("securityConfiguration")).isTrue();
	}
}
