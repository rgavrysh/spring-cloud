package com.home.microservices.ratingservice;

import com.home.microservices.ratingservice.entities.Rating;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"server.port:8090"})
@AutoConfigureMockMvc
public class RatingServiceApplicationTests {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @MockBean
    KafkaTemplate kafkaTemplate;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void whenGetRatingByBookIdThenCorrectFound() throws Exception {
        Rating rating = new Rating(1L, 1L, 2);
        Mockito.when(kafkaTemplate.send("rating", rating)).then(invocation -> rating);
        mockMvc.perform(MockMvcRequestBuilders.get("/rating/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isNotEmpty());
    }

}
