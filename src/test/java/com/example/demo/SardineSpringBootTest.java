package com.example.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.example.demo.sardine.SardineService;

@SpringBootTest
public class SardineSpringBootTest {
	
//	private static final String FILE_NAME = "/home/basis/Downloads/tmp/VIO/Vinay-K.-Ingle-John-G.-Proakis-Digital-Signal-Processing-Using-MATLAB-3rd-Edition-Cengage-Learning-2011.pdf";
	private static final String FILE_NAME = "/home/basis/Downloads/tmp/VIO/teste.txt";
	private static final String FOLDER_NAME = "2022/Abr";
	
	@Autowired
	private SardineService service;
	
	@Test
	void sendFilePath () throws IOException {
		
		byte[] array = Files.readAllBytes(Paths.get(FILE_NAME));
		
		this.service.criar(FOLDER_NAME, array);
		
	}
	@Test
	void sendFilePathFile () throws IOException {
		
		File f = new File(FILE_NAME);
		
		this.service.criar(FOLDER_NAME, f);
		
	}
	@Test
	void sendMySardineImpl () throws IOException {
		
//		byte[] array = Files.readAllBytes(Paths.get(FILE_NAME));
		
		File f = new File(FILE_NAME);
		
		this.service.criarMyImple(FOLDER_NAME, "teste3.txt", f);
		
	}
	
//	@Test
//	void sendFileMySplit () throws IOException {
//		
//		byte[] array = Files.readAllBytes(Paths.get(FILE_NAME));
//		
////		this.service.criarMySplit(FOLDER_NAME, array);
//		
//	}
//	
//	@Test
//	void sendLastTest () throws IOException {
//		
//		byte[] array = Files.readAllBytes(Paths.get(FILE_NAME));
//		
//		this.service.lastTest(FOLDER_NAME, array);
//		
//	}
//	
//	@Test
//	void sendFile () throws IOException {
//		
//		File file = new File(FILE_NAME);
//		
//		this.service.criar(FOLDER_NAME, file);
//	}
	/*
	@Test
	void sendFileSplit() throws IOException {
		
		File file = new File(FILE_NAME);
		
//		this.service.criarSplit(FOLDER_NAME, file);
	}	
	
	@Test
	void criarMySplit2() throws IOException {
		
		this.service.criarMySplit2(FOLDER_NAME, FILE_NAME);
		
	}
	
		*/
//	
//	@Test
//	void criarMySplit3() throws IOException {
//		
//		this.service.criarMySplit3(FOLDER_NAME, FILE_NAME);
//		
//	}

//	@Test
//	void criarMySplit4() throws IOException {
//		
//		this.service.criarMySplit4(FOLDER_NAME, FILE_NAME);
//		
//	}
//	
//	@Test
//	void criarMySplit5() throws IOException {
//		
//		this.service.criarMySplit5(FOLDER_NAME, FILE_NAME);
//		
//	}
	
	@Test
	void splitTest() {
		
//		String sourcePath = FILE_NAME;
//		if(FILE_NAME.substring(0, 1).equals("/")) {
//			sourcePath = FILE_NAME.substring(1);
//		}
//		List<String> paths = Arrays.asList(sourcePath.split("/"));
//		System.out.println(FILE_NAME);
//		System.out.println(sourcePath);
//		
//		for(String p : paths) {
//			System.out.println(p);
//		}
		
		this.montarDiretorio("/2020/Fev/Joao Figueiredo/Entradas", "/home/basis/Downloads/tmp/VIO/teste");
	}
	
	
	public void montarDiretorio(String caminho, String url) {
		
        StringBuilder urlPortal = new StringBuilder(url);
        
        String sourcePath = caminho;
		if(caminho.substring(0, 1).equals("/")) {
			sourcePath = caminho.substring(1); // remove a primeira barra
		}
		List<String> paths = Arrays.asList(sourcePath.split("/"));
		
		for(String part : paths) {
			urlPortal.append("/").append(part);
//			HttpMkcol httpMkcol = new HttpMkcol(urlPortal.toString());
//			try {
//                HttpResponse response = client.execute(httpMkcol);
//                log.debug("Diretório criado com sucesso: {}", response);
//            } catch (IOException e) {
//                log.debug(e.getMessage(), e);
//                throw new ParametrizedMessageException("integracao.webdav", ConstantsUtil.ERROR_TITLE);
//            }
			
		}
		
    }
}
