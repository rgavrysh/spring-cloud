package com.home.microservices.bookservice;

import com.home.microservices.bookservice.entities.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class BookServiceApplication {

    private static ConfigurableApplicationContext context;

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
            java.util.concurrent.TimeUnit.MILLISECONDS.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
    }

    @GetMapping("/")
    public String getIndex() {
        String port = context.getEnvironment().getProperty("server.port");
        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to library! ");
        sb.append("You are serving by:\t").append(context.getId());
        return sb.toString();
    }

}