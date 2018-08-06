package com.home.microservices.bookservice;

import com.home.microservices.bookservice.entities.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("/books")
public class BookServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookServiceApplication.class, args);
    }

    private List<Book> books = Arrays.asList(
            new Book(1L, "Origin", "Dan Brown"),
            new Book(2L, "Inception", "Dan Brown"),
            new Book(3L, "It", "Stephen King")
    );

    @GetMapping("")
    public List<Book> findAllBooks() {
        return books;
    }

    @GetMapping("/{bookId}")
    public Book findBookById(@PathVariable Long bookId) {
        return books.stream().filter(book -> book.getId().equals(bookId)).findFirst().orElse(null);
    }

}