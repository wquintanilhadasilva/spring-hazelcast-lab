package com.example.demo.sardine;

import java.io.IOException;
import java.text.MessageFormat;

import org.springframework.stereotype.Service;

import com.github.sardine.Sardine;

@Service
public class SardineService {
	
	private final Sardine sardine;
	private final ApplicationProperties applicationProperties;
	
	public SardineService(Sardine sardine, ApplicationProperties applicationProperties){
		this.applicationProperties = applicationProperties;
		this.sardine = sardine;
		
	}
	
	public void criar(String caminho, byte[] arquivo) {
		try {
            String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), caminho);
            this.sardine.put(url, arquivo);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("integracao.webdav", e);
        }
	}
	
}
