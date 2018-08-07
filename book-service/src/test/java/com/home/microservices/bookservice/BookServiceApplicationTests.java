package com.home.microservices.bookservice;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest({"server.port:8099"})
public class BookServiceApplicationTests {

	@Autowired
	private BookServiceApplication bookServiceApplication;

	@Test
	public void contextLoads() {
		Assert.assertTrue(bookServiceApplication != null);
	}

}
