package com.swodd.interactmap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swodd.interactmap.entities.Apartment;
import com.swodd.interactmap.entities.Image;
import com.swodd.interactmap.entities.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class InteractmapApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    public void testApartment() throws Exception {
        //test add
        List<Image> images = new ArrayList<>();
        Image image = new Image("www.example_image.png");
        images.add(image);
        Point coordinates = new Point(0, 0);
        Apartment apartment = new Apartment(4, 100.0, "Big and nice", images, "Kiev", coordinates);
        ResponseEntity<Apartment> responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/apartments", apartment, Apartment.class);
        assertEquals(HttpStatus.CREATED.value(), responseEntity.getStatusCodeValue());

        // test get and update
        String updateDescription = "updateDescription";
        Apartment[] apartments = restTemplate.getForObject("http://localhost:" + port + "/apartments/coordinates/5_5/radius/10/pages/1/size/10", Apartment[].class);
        assertNotNull(apartments);
        assertTrue(apartments.length > 0);
        apartment = apartments[0];
        apartment.setDescription(updateDescription);

        mvc.perform(put("/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(apartment))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(updateDescription, restTemplate.getForObject("http://localhost:" + port + "/apartments/" + apartment.getId(), Apartment.class).getDescription());
        apartments = restTemplate.getForObject("http://localhost:" + port + "/apartments/pages/1/size/10", Apartment[].class);

        assertNotNull(apartments);
        assertTrue(apartments.length > 0);
        apartment = apartments[0];
        assertEquals(updateDescription, apartment.getDescription());

        //test delete
        mvc.perform(delete("/apartments/{id}", apartment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        apartments = restTemplate.getForObject("http://localhost:" + port + "/apartments/pages/1/size/10", Apartment[].class);
        assertEquals(0, apartments.length);
    }
}
