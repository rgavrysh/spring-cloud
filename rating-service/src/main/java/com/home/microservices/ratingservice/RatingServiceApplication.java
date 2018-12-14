package com.home.microservices.ratingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.microservices.ratingservice.entities.BookDTO;
import com.home.microservices.ratingservice.entities.Rating;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("/rating")
@EnableKafka
public class RatingServiceApplication {

    final Logger logger = LoggerFactory.getLogger(RatingServiceApplication.class);

    private List<Rating> ratings = Arrays.asList(
            new Rating(1L, 1L, 2),
            new Rating(3L, 2L, 4),
            new Rating(4L, 3L, 5)
    );

    @Autowired
    private KafkaTemplate kafkaTemplate;
    @Autowired
    private ObjectMapper mapper;

    public static void main(String[] args) {
        SpringApplication.run(RatingServiceApplication.class, args);
    }

    @GetMapping
    public List<Rating> findAllRatings() {
        logger.info("Return all available ratings.");
        return ratings;
    }

    @GetMapping("/{bookId}")
    public Integer findRatingByBookId(@PathVariable Long bookId) {
        Rating rating = getRating(bookId);
        logger.info("Found rating {} by book id {}", rating, bookId);
        return rating.getStars();
    }

    private Rating getRating(Long bookId) {
        return ratings.stream().filter(rating -> rating.getBookId().equals(bookId)).findFirst().orElseThrow(NoSuchElementException::new);
    }

    @KafkaListener(groupId = "request", topics = {"books"})
    @SuppressWarnings("unchecked")
    public void sendRaiting(byte[] kafkaMsg) throws IOException {
        BookDTO bookDTO = mapper.readValue(kafkaMsg, BookDTO.class);
        logger.info("Got message from kafka {}", bookDTO.toString());
        logger.info("Send rating to kafka topic.");
        kafkaTemplate.send("rating", getRating(bookDTO.getId()).toString());
    }
}
