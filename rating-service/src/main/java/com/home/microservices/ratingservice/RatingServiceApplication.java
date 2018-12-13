package com.home.microservices.ratingservice;

import com.home.microservices.ratingservice.entities.Rating;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("/rating")
public class RatingServiceApplication {

    public static final Logger LOGGER = LoggerFactory.getLogger(RatingServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RatingServiceApplication.class, args);
    }

    private List<Rating> ratings = Arrays.asList(
            new Rating(1L, 1L, 2),
            new Rating(3L, 2L, 4),
            new Rating(4L, 3L, 5)
    );

    @GetMapping
    public List<Rating> findAllRatings() {
        return ratings;
    }

    @GetMapping("/{bookId}")
    public Integer findRatingByBookId(@PathVariable Long bookId) {
        Rating r = ratings.stream().filter(rating -> rating.getBookId().equals(bookId)).findFirst().orElseThrow(NoSuchElementException::new);
        kafkaTemplate().send("rating", r.toString());
        return r.getStars();
    }

    @Bean
    Map<String, Object> producerConfigs() {
        HashMap<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return configs;
    }

    @Bean
    ProducerFactory<String, String> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
