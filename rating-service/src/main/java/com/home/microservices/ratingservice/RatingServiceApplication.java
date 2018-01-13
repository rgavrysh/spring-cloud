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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
			new Rating(2L, 1L, 3),
			new Rating(3L, 2L, 4),
			new Rating(4L, 3L, 5)
	);

	@GetMapping
	public List<Rating> findAllRatings() {
		return ratings;
	}

	@GetMapping("/{bookId}")
	public List<Rating> findRatingByBookId(@PathVariable Long bookId) {
		return bookId == null || bookId.equals(0L) ? Collections.EMPTY_LIST :
				ratings.stream().filter(rating -> rating.getBookId().equals(bookId)).collect(Collectors.toList());
	}
}
