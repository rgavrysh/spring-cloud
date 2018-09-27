package com.home.microservices.gateway.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class BookServiceFallbackProvider implements FallbackProvider {

    private final String bookServiceRoute = "book-service";

    @Override
    public ClientHttpResponse fallbackResponse(Throwable cause) {
        return fallbackResponse;
    }

    @Override
    public String getRoute() {
        return bookServiceRoute;
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return fallbackResponse;
    }

    ClientHttpResponse fallbackResponse = new ClientHttpResponse() {
        @Override
        public HttpStatus getStatusCode() throws IOException {
            return HttpStatus.OK;
        }

        @Override
        public int getRawStatusCode() throws IOException {
            return 200;
        }

        @Override
        public String getStatusText() throws IOException {
            return "OK";
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream("We can not reach the library at the moment. Please try again later."
                    .getBytes());
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            return httpHeaders;
        }
    };
}
