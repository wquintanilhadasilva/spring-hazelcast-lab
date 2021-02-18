package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.demo.sardine.SardineService;
import com.github.sardine.Sardine;

@SpringBootTest
public class SardineSpringBootTest {
	
	@Autowired
	private SardineService service;
	
	@Autowired
	private Sardine sardine;
	
	@Test
	void injectService () {
		this.service.criar("xpto", null);
	}
	
	@Test
	void injectBean() {
		Assert.notNull(sardine, "Sardine instance");
	}

}
