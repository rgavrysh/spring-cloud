package com.home.microservices.bookservice;

import com.home.microservices.bookservice.entities.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableEurekaClient
@RestController
@ManagedResource(objectName = "bean:name=bookService", description = "BookService MBean")
public class BookServiceApplication {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    private static ConfigurableApplicationContext context;
    private volatile long latency = 100;

    public long getLatency() {
        return latency;
    }

    @ManagedOperation(description = "Set latency for getBookById simulating service timeout.")
    public void setLatency(long latency) {
        this.latency = latency;
    }

    public static void main(String[] args) {
        context = SpringApplication.run(BookServiceApplication.class, args);
    }

    private List<Book> books = Arrays.asList(
            new Book(1L, "Origin", "Dan Brown"),
            new Book(2L, "Inception", "Dan Brown"),
            new Book(3L, "It", "Stephen King")
    );

    @GetMapping("/books")
    public List<Book> findAllBooks() {
        return books;
    }

    @GetMapping("/books/{bookId}")
    public Book findBookById(@PathVariable Long bookId) {
        try {
            TimeUnit.MILLISECONDS.sleep(latency);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
    }

    @GetMapping("/books/{id}/rating")
    public Integer getBookWithRating(@PathVariable Long id, RestTemplate restTemplate) {
        Book b = books.stream().filter(book -> book.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
        Integer bookRate = restTemplate.getForObject("http://gateway:8080/rating-service/rating/" + id, Integer.class);
        return bookRate;
    }

    @GetMapping("/")
    public String getIndex() {
        String port = context.getEnvironment().getProperty("server.port");
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to library!\n");
        sb.append("You are serving by:\t").append(context.getId());
        return sb.toString();
    }

}