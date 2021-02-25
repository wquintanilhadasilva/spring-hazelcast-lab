package com.example.demo.sardine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ProxySelector;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.github.sardine.impl.SardineImpl;
import com.github.sardine.impl.handler.VoidResponseHandler;

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
//            this.sardine.delete("https://webdav.4shared.com/2022");
//            this.sardine.createDirectory("https://webdav.4shared.com/2023/Teste/Teste/");
            this.sardine.put(url, arquivo);
            System.out.println("=====> OK <======");
        }
        catch (IOException e) {
        	e.printStackTrace();
            throw new IllegalArgumentException("integracao.webdav", e);
        }
	}
	
	public void criar(String caminho, File file) {
		try {
			
			InputStream fis = new FileInputStream(file);
			
			String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), caminho);
			
//			this.sardine.createDirectory(url);
			Map<String, String> header = new HashMap<>();
//			header.put("Expect", "100-continue");
			header.put("Expect", "100-continue"); // [Expect: 100-continue] [Expect: 100-continue, Content-Type: ISO-8859-1]
			header.put("Content-Type", "ISO-8859-1");
//			this.sardine.put(url, fis, "application/octet-stream", true, file.length());
			this.sardine.put(url, fis, header);
			
            System.out.println("=====> OK <======");
        }
        catch (IOException e) {
        	e.printStackTrace();
            throw new IllegalArgumentException("integracao.webdav", e);
        }
	}
	public void criarMyImple(String caminho, File file) {
		try {
			
			String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), caminho);
			
			Path p = file.toPath();
            String mimeType = Files.probeContentType(p);
            
            FileInputStream is = new FileInputStream(file);
            byte[] arquivo = is.readAllBytes();
			
//			this.sardine.createDirectory(url);
			Map<String, String> header = new HashMap<>();
//			header.put("Expect", "100-continue");
//			header.put("Expect", "100-continue"); // [Expect: 100-continue] [Expect: 100-continue, Content-Type: ISO-8859-1]
//			header.put("Content-Type", "ISO-8859-1");
//			header.put("Content-Type", mimeType);
//			this.sardine.put(url, fis, "application/octet-stream", true, file.length());
			
			MySardineImpl custom = MySardineImpl.instance(applicationProperties.getUserName(), applicationProperties.getPassword());
			custom.put(url, arquivo, header);
			
            System.out.println("=====> OK <======");
        }
        catch (IOException e) {
        	e.printStackTrace();
            throw new IllegalArgumentException("integracao.webdav", e);
        }
	}
//	
//	
	public void criarMySplit(String path, byte[] arquivo) {
		
        try {
        	
        	String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), path);

            int TAMANHO_CHUNK = 1024 * 250;
            int ultimaPosicao = 0;
            int tamanhoTotal = arquivo.length;
            byte[] pedaco;
            
            sardine.enablePreemptiveAuthentication(applicationProperties.getUrl());


            for (int i = 0; i < tamanhoTotal; i += TAMANHO_CHUNK) {
            	HttpHeaders headers = new HttpHeaders();
//            	headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            	
                ultimaPosicao = Math.min(tamanhoTotal - 1, i + TAMANHO_CHUNK);
                pedaco = Arrays.copyOfRange(arquivo, i, ultimaPosicao);
                
//                header.put(HttpHeaders.CONTENT_RANGE, "bytes " + from + "-" + to + "/" + to ) ;
                headers.put(HttpHeaders.CONTENT_RANGE, Collections.singletonList("bytes " + i + "-" + ultimaPosicao + "/" + tamanhoTotal));
                
                sardine.put(url, pedaco);
            }
        }
        catch (IOException e) {
        	e.printStackTrace();
        	throw new IllegalArgumentException("integracao.webdav", e);
        }
    }
	
	public void criarSplit(String caminho, File file) {
		
        try {
        	
        	String uriString = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), caminho);

            Map<String, String> header = new HashMap<>();
//            this.sardine.enablePreemptiveAuthentication(uriString);
            
            int from = 0;
            
            int SIZE = 10000 ;
            
            FileInputStream f = new FileInputStream( file );
            FileChannel ch = f.getChannel( );

            MappedByteBuffer mappedBuffer = ch.map( MapMode.READ_ONLY, 0L, ch.size( ) );
            mappedBuffer.position(from);
            
            byte[] byteArray ; // = new byte[SIZE];
            int nGet;
            int to = from = mappedBuffer.position();
            int remCnt = 0;
            
            
            this.sardine.enablePreemptiveAuthentication(applicationProperties.getUrl());
            
            while( mappedBuffer.hasRemaining( ) )
            {
	            nGet = Math.min( mappedBuffer.remaining( ), SIZE );
	            byteArray = new byte[nGet];
	            mappedBuffer.get( byteArray, 0, nGet );
	            to+=nGet;
	            header.put(HttpHeaders.CONTENT_RANGE, "bytes " + from + "-" + to + "/" + to ) ; // total size is mandatory; works on tomcat local
	            // header.put(HttpHeaders.CONTENT_TYPE, "application/octet-stream"); // or try thid
	            // header.put(HttpHeaders.CONTENT_TYPE, "multipart/byteranges"); // or this
	            // header.put(HttpHeaders.TRANSFER_ENCODING, "chunked"); // doesnot work Transfer-encoding header already present
	            remCnt = mappedBuffer.remaining()/ SIZE;
	            sardine.put(uriString, new ByteArrayInputStream(byteArray) , header);
	            from = to;
            }
            ch.close();
            f.close();
            
        }
        catch (IOException e) {
        	e.printStackTrace();
        	throw new IllegalArgumentException("integracao.webdav", e);
        }
    }
	
//	public void lastTest(String path, byte[] arquivo) {
//		
//		try {
//        	
//        	String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), path);
//
//            int TAMANHO_CHUNK = 1024 * 250;
//            int ultimaPosicao = 0;
//            int tamanhoTotal = arquivo.length;
//            byte[] pedaco;
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//            
////            sardine.enablePreemptiveAuthentication(applicationProperties.getUrl(), 80, 443);
//
//            for (int i = 0; i < tamanhoTotal; i += TAMANHO_CHUNK) {
//            	
//                ultimaPosicao = Math.min(tamanhoTotal - 1, i + TAMANHO_CHUNK);
//                pedaco = Arrays.copyOfRange(arquivo, i, ultimaPosicao);
//
//                headers.put(HttpHeaders.CONTENT_RANGE, Collections.singletonList("bytes " + i + "-" + ultimaPosicao + "/" + tamanhoTotal));
//                InputStream targetStream = new ByteArrayInputStream(pedaco);
//                
//                sardine.put(url, targetStream, headers.toSingleValueMap());
//            }
//        }
//        catch (IOException e) {
//        	e.printStackTrace();
//        	throw new IllegalArgumentException("integracao.webdav", e);
//        }
//    }
	
	
	public void criarMySplit2(String path, String arquivo) {

		
        try {
        	
        	String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), path);
        	
        	File file = new File(arquivo);
        	FileInputStream fis = new FileInputStream(file);
        	
        	int cont;
        	
        	byte[] particao = new byte[1024 * 100];
        	
        	FileOutputStream saida = new FileOutputStream("/home/basis/Downloads/file-teste.pdf");
        	
        	long tamanhoTotal = file.length();
        	int start = 0;
        	int end = 0;
        	
        	sardine.enablePreemptiveAuthentication(applicationProperties.getUrl());

        	                        
        	while ( ( cont = fis.read( particao ) ) != -1 ) {
        		
        	     HttpHeaders headers = new HttpHeaders();
        	     end += particao.length; 
                 headers.put(HttpHeaders.CONTENT_RANGE, Collections.singletonList("bytes " + start + "-" + end + "/" + tamanhoTotal));
                 
                 sardine.put(url, new ByteArrayInputStream(particao), headers.toSingleValueMap());
                 
                 saida.write(particao); 
                 
                 start = end + 1;
                 
                 System.out.println(MessageFormat.format("Start {0} - end {1} / total {2}", start, end, tamanhoTotal));
        	     
        	}
        	
        }
        catch (IOException e) {
        	e.printStackTrace();
        	throw new IllegalArgumentException("integracao.webdav", e);
        }
    }
	
	public void criarMySplit3(String path, String arquivo) {

		
        try {
        	
        	String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), path);
        	
        	File file = new File(arquivo);
        	FileInputStream fis = new FileInputStream(file);
        	
        	SardineImpl s = new SardineImpl(applicationProperties.getUserName(),
                                                applicationProperties.getPassword());
        	
        	RepetableInputStreamEntity repeatable = new RepetableInputStreamEntity(file, 100);
        	
        	Path p = file.toPath();
            String mimeType = "multipart/form-data"; //Files.probeContentType(p);
        	
        	s.put(url, repeatable, mimeType, false);
        	
        	
        }
        catch (IOException e) {
        	e.printStackTrace();
        	throw new IllegalArgumentException("integracao.webdav", e);
        }
    }
	
	public void criarMySplit4(String caminho, String arquivo) {
        try {
        	
            String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), caminho);
            
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(applicationProperties.getUserName(),
                    applicationProperties.getPassword());
            
            credentialsProvider.setCredentials(AuthScope.ANY, usernamePasswordCredentials);
            
            HttpClient client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();

//            HttpEntity httpEntity = new ByteArrayEntity(arquivo);
//            HttpPut httpPut = new HttpPut(url);
//            httpPut.addHeader(null);
//            httpPut.setEntity(httpEntity);
//            HttpResponse response = client.execute(httpPut);
//            log.debug("enviado com sucesso! status code: " + response.getStatusLine().getStatusCode());
            
            
        	
        	File file = new File(arquivo);
        	FileInputStream fis = new FileInputStream(file);
        	
        	int cont;
        	
        	byte[] particao = new byte[1024 * 100];
        	
//        	FileOutputStream saida = new FileOutputStream("/home/basis/Downloads/file-teste.pdf");
        	
        	long tamanhoTotal = file.length();
        	int start = 0;
        	int end = 0;
            
            
//    		while ( ( cont = fis.read( particao ) ) != -1 ) {
        		
//	       	     end += particao.length; 
	       	  
//	             HttpEntity httpEntity = new ByteArrayEntity(particao);
	             HttpEntity httpEntity = new ByteArrayEntity(fis.readAllBytes());
	             HttpPut httpPut = new HttpPut(url);
//	             httpPut.addHeader(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + tamanhoTotal);
	             httpPut.setEntity(httpEntity);
	             HttpResponse response = client.execute(httpPut);
	             
//	            saida.write(particao); 
	            
//	            start = end + 1;
	            
	            System.out.println(MessageFormat.format("Start {0} - end {1} / total {2}", start, end, tamanhoTotal));
                
       	     
//            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public void criarMySplit5(String caminho, String arquivo) {
		try {
        	
        	String url = MessageFormat.format("{0}/{1}", applicationProperties.getUrl(), caminho);
        	
        	File file = new File(arquivo);
        	FileInputStream fis = new FileInputStream(file);
        	
        	SardineImpl s = new SardineImpl(applicationProperties.getUserName(),
                                                applicationProperties.getPassword());
        	
        	RepetableInputStreamEntity2 repeatable = new RepetableInputStreamEntity2(fis);
        	
        	Path path = file.toPath();
            String mimeType = "multipart/form-data"; //Files.probeContentType(path);
        	
        	s.put(url, repeatable, mimeType ,false);
        	
        	
        }
        catch (IOException e) {
        	e.printStackTrace();
        	throw new IllegalArgumentException("integracao.webdav", e);
        }
    }
	
	
	// https://github.com/lookfirst/sardine/issues/215
	
//	HttpClientBuilder builder = HttpClientBuilder.create();
	
//	SardineImpl s = new SardineImpl();
//	s.put
	
	class RepetableInputStreamEntity extends AbstractHttpEntity {
		
		private final File file;
	    private final InputStream is;
	    private final int bufferSize;
	    
	    private int current;
	    private List<byte[]> blocks = new LinkedList<>();
	    
	    public RepetableInputStreamEntity(File file, int bufferSize) throws IOException {
	    	this.file = file;
	    	this.is = new FileInputStream(file);
	    	this.bufferSize = bufferSize;
	    	this.current = 0;
	    	
	    	this.readBytes();
	    	
	    }

	    @Override
	    public boolean isRepeatable() {
	        return true;
	    }

	    @Override
	    public long getContentLength() {
	        return file.length();
	    }

	    @Override
	    public InputStream getContent() throws IOException, UnsupportedOperationException {
	        return is;
	    }

	    @Override
	    public void writeTo(OutputStream outstream) throws IOException {
	    	
	    	if((this.blocks != null && this.blocks.size() > 0) && this.current < this.blocks.size()) {
	    		byte[] bcurrent = this.blocks.get(this.current);
	    		outstream.write(bcurrent);
	    		this.current++;
	    	}
	    	
	    }

	    @Override
	    public boolean isStreaming() {
	        return true;
	    }
	    
	    private void readBytes() throws IOException {
	    	
	    	long tamanhoTotal = file.length();
        	int start = 0;
        	int end = 0;
        	int cont;
        	
        	byte[] particao = new byte[1024 * this.bufferSize];
	    	
	    	while ( ( cont = this.is.read( particao ) ) != -1 ) {
       	     	end += particao.length; 
       	     	this.blocks.add(particao);
                start = end + 1;
                System.out.println(MessageFormat.format("Start {0} - end {1} / total {2}", start, end, tamanhoTotal));
	    	}
	    	
	    }
		
	}
}


class RepetableInputStreamEntity2 extends AbstractHttpEntity {
	
	final MultipartFile file;//just example which works for me. You can use whatever you want just make sure your entity is repetable and you can create new instance of input stream on each getContent call.
	
	RepetableInputStreamEntity2(FileInputStream fis) throws IOException{
		this.file = new MockMultipartFile("filename", fis.readAllBytes());
	}

    @Override
    public boolean isRepeatable() {
        return true;
    }

    @Override
    public long getContentLength() {
        return file.getSize();
    }

    @Override
    public InputStream getContent() throws IOException, UnsupportedOperationException {
        return file.getInputStream();
    }

    @Override
    public void writeTo(OutputStream outstream) throws IOException {
        try (InputStream content = getContent()) {
            IOUtils.copy(content, outstream);
        }
    }

    @Override
    public boolean isStreaming() {
        return true;
    }
	
}


class MySardineImpl extends SardineImpl {
	
	private MySardineImpl(String username, String password) {
		super(username, password);
	}
	
	public static MySardineImpl instance(String username, String password)
	{
		return new MySardineImpl(username, password);
	}
	
	public void put(String url, byte[] data, Map<String, String> headers) throws IOException
	{
		List<Header> list = new ArrayList<Header>();
		for (Map.Entry<String, String> h : headers.entrySet())
		{
			list.add(new BasicHeader(h.getKey(), h.getValue()));
		}
		
		ByteArrayEntity entity = new ByteArrayEntity(data);
		
		this.put(url, entity, list);
	}
	
//	@Override
//	public void put(String url, HttpEntity entity, List<Header> headers) throws IOException
//	{
//		this.put(url, entity, headers, new VoidResponseHandler());
//	}

	@Override
	public <T> T put(String url, HttpEntity entity, List<Header> headers, ResponseHandler<T> handler) throws IOException
	{
		HttpPut put = new HttpPut(url);
		put.setEntity(entity);
		for (Header header : headers)
		{
			put.addHeader(header);
		}
		try
		{
			return this.execute(put, handler);
		}
		catch (HttpResponseException e)
		{
			if (e.getStatusCode() == HttpStatus.SC_EXPECTATION_FAILED)
			{
				// Retry with the Expect header removed
				put.removeHeaders(HTTP.EXPECT_DIRECTIVE);
				if (entity.isRepeatable())
				{
					return this.execute(put, handler);
				}
			}
			throw e;
		}
	}
}


