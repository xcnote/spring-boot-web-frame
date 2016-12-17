package com.system.http;

import lombok.Setter;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Setter
public class HttpClientManager {
	@Value("${httpclient.proxyHost}")
	private String proxyHost;
	@Value("${httpclient.proxyPort}")
	private Integer proxyPort;
	@Value("${httpclient.maxConnections}")
	private int maxConnections;
	@Value("${httpclient.socketTimeOut}")
	private int socketTimeOut;
	@Value("${httpclient.connectionTimeOut}")
	private int connectionTimeOut;
	
	@Bean(name="defaultHttpClient")
	public HttpClient getHttpClient(){
		
		HttpClientFactory factory = new HttpClientFactory();
		HttpClientConfig config = new HttpClientConfig();

		config.setMaxConnections(maxConnections);
		config.setSocketTimeOut(socketTimeOut);
		config.setConnectionTimeOut(connectionTimeOut);
		return factory.createHttpClient(config);
	}
	
	@Bean(name="proxyHttpClient")
	public HttpClient getProxyHttpClient(){
		HttpClientFactory factory = new HttpClientFactory();
		HttpClientConfig config = new HttpClientConfig();
		if(useProxy()){
			config.setProxyHost(proxyHost);
			config.setProxyPort(proxyPort);
		}
		config.setMaxConnections(maxConnections);
		config.setSocketTimeOut(socketTimeOut);
		config.setConnectionTimeOut(connectionTimeOut);
		return factory.createHttpClient(config);
	}
	
	public boolean useProxy(){
		if(StringUtils.isNotBlank(proxyHost)&&proxyPort > 0){
			return true;
		}
		return  false;
	}
}
