package com.home.microservices.bookservice;

import com.home.microservices.bookservice.entities.Book;
import com.home.microservices.bookservice.entities.Books;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableEurekaClient
@RestController
@ManagedResource(objectName = "bean:name=bookService", description = "BookService MBean")
public class BookServiceApplication {

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

    @RequestMapping(value = "/books", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
            MediaType.TEXT_HTML_VALUE, MediaType.TEXT_XML_VALUE},
            consumes = MediaType.ALL_VALUE)
    public Books findAllBooks() {
        Books books = new Books();
        books.setBooks(this.books);
        return books;
    }

    @RequestMapping(value = "/books/{bookId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = MediaType.ALL_VALUE)
    public Book findBookById(@PathVariable Long bookId) {
        try {
            TimeUnit.MILLISECONDS.sleep(latency);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
    }

    @RequestMapping("/home")
    public String getIndex() {
        String port = context.getEnvironment().getProperty("server.port");
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to library!\n");
        sb.append("You are serving by:\t").append(context.getId());
        return sb.toString();
    }

}