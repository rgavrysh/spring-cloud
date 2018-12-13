package com.home.microservices.bookservice;

import com.home.microservices.bookservice.entities.Book;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableEurekaClient
@RestController
@ManagedResource(objectName = "bean:name=bookService", description = "BookService MBean")
@EnableKafka
public class BookServiceApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceApplication.class);

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

    private String kafkaMessage;

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

    @KafkaListener(topics = "rating")
    String getKafkaMessage(String msg) {
        LOGGER.info(msg);
        this.kafkaMessage = msg;
        return msg;
    }

    @Bean
    ConsumerFactory<String, String> consumerFactory() {
        HashMap<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "book-service-consumer");
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory(configs);
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(3);
        factory.getContainerProperties().setPollTimeout(3000);
        return factory;
    }

}