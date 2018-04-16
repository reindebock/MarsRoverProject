package com.reindebock.projects.configuration;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory tls12RequestFactory) {
        return new RestTemplate(tls12RequestFactory);
    }

    @Bean
    public ClientHttpRequestFactory tls12RequestFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLSv1.2");
            context.init(null, null, null);

            CloseableHttpClient httpClient = HttpClientBuilder
                    .create()
                    .setSSLContext(context)
                    .build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
            return factory;
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            return null;
        }
    }
}
