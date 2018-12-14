package com.home.microservices.bookservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.microservices.bookservice.entities.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableEurekaClient
@RestController
@ManagedResource(objectName = "bean:name=bookService", description = "BookService MBean")
@EnableKafka
public class BookServiceApplication {

    private final Logger logger = LoggerFactory.getLogger(BookServiceApplication.class);
    private volatile long latency = 100;

    private static String serverId;
    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ObjectMapper mapper;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BookServiceApplication.class, args);
        serverId = context.getId();
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
            logger.warn(e.getMessage());
            Thread.currentThread().interrupt();
        }
        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
    }

    @GetMapping("/books/{id}/rating")
    @SuppressWarnings("unchecked")
    public Integer getBookWithRating(@PathVariable Long id, RestTemplate restTemplate) throws JsonProcessingException {
        kafkaTemplate.send("books", mapper.writeValueAsBytes(getBook(id)));
        return restTemplate.getForObject("http://gateway:8080/rating-service/rating/" + id, Integer.class);
    }

    @GetMapping("/")
    public String getIndex() {
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to library!\n");
        sb.append("You are serving by:\t").append(serverId);
        return sb.toString();
    }

    @KafkaListener(topics = "rating")
    String getKafkaMessage(String msg) {
        logger.info(msg);
        return msg;
    }

    private Book getBook(@PathVariable Long id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst().orElseThrow(NoSuchElementException::new);
    }

    public long getLatency() {
        return latency;
    }

    @ManagedOperation(description = "Set latency for getBookById simulating service timeout.")
    public void setLatency(long latency) {
        this.latency = latency;
    }

}