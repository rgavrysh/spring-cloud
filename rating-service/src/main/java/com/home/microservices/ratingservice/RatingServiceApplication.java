package com.home.microservices.ratingservice;

import com.home.microservices.ratingservice.entities.Rating;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

@SpringBootApplication
@EnableEurekaClient
@RestController
@RequestMapping("/rating")
public class RatingServiceApplication {

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
        return r.getStars();
    }
}
