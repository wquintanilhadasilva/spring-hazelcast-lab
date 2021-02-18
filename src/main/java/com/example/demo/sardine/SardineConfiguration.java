package com.example.demo.sardine;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

@Configuration
public class SardineConfiguration {
	
	@Bean
	public Sardine getSardine(ApplicationProperties applicationProperties) {
            return SardineFactory.begin(applicationProperties.getUserName(),
                                                applicationProperties.getPassword());
    }

}
