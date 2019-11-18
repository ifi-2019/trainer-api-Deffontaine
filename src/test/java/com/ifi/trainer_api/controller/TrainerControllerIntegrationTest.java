package com.ifi.trainer_api.controller;

import com.ifi.trainer_api.bo.Trainer;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrainerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TrainerController controller;

    @Test
    void trainerController_shouldBeInstanciated(){
        assertNotNull(controller);
    }

    @Test
    void getTrainer_withNameAsh_shouldReturnAsh() {
        var ash = this.restTemplate.getForObject("http://localhost:" + port + "/trainers/Ash", Trainer.class);
        assertNotNull(ash);
        assertEquals("Ash", ash.getName());
        assertEquals(1, ash.getTeam().size());

        assertEquals(25, ash.getTeam().get(0).getPokemonType());
        assertEquals(18, ash.getTeam().get(0).getLevel());
    }

    @Test
    void getAllTrainers_shouldReturnAshAndMisty() {
        var trainers = this.restTemplate.getForObject("http://localhost:" + port + "/trainers/", Trainer[].class);
        assertNotNull(trainers);
        assertEquals(2, trainers.length);

        assertEquals("Ash", trainers[0].getName());
        assertEquals("Misty", trainers[1].getName());
    }

//    @Test
//    void addTrainer() {
//        String json = "{\"name\": \"Bug Catcher\",\"team\": [{\"pokemonType\": 13, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
//
//        var bugCatcher = this.restTemplate.postForObject("http://localhost:" + port + "/trainers/", entity, Trainer.class);
//
//        assertNotNull(bugCatcher);
//        assertEquals("Bug Catcher", bugCatcher.getName());
//        assertEquals(2, bugCatcher.getTeam().size());
//
//        assertEquals(13, bugCatcher.getTeam().get(0).getPokemonType());
//        assertEquals(10, bugCatcher.getTeam().get(1).getPokemonType());
//        assertEquals(6, bugCatcher.getTeam().get(0).getLevel());
//        assertEquals(6, bugCatcher.getTeam().get(1).getLevel());
//    }
//
//    @Test
//    void updateTrainer() {
//        String json = "{\"name\": \"to_update\",\"team\": [{\"pokemonType\": 13, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";
//        String json2 = "{\"name\": \"to_update\",\"team\": [{\"pokemonType\": 14, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
//        HttpEntity<String> entity2 = new HttpEntity<String>(json2, headers);
//
//        var toUpdate = this.restTemplate.postForObject("http://localhost:" + port + "/trainers/", entity, Trainer.class);
//
//        assertNotNull(toUpdate);
//        assertEquals(13, toUpdate.getTeam().get(0).getPokemonType());
//
//        this.restTemplate.put("http://localhost:" + port + "/trainers/", entity2, Trainer.class);
//
//        var toUpdateAfter = this.restTemplate.getForObject("http://localhost:" + port + "/trainers/to_update", Trainer.class);
//        assertNotNull(toUpdateAfter);
//        assertEquals(14, toUpdateAfter.getTeam().get(0).getPokemonType());
//    }
//
//    @Test
//    void deleteTrainer() {
//        String json = "{\"name\": \"to_delete\",\"team\": [{\"pokemonType\": 13, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
//
//        var toDelete = this.restTemplate.postForObject("http://localhost:" + port + "/trainers/", entity, Trainer.class);
//
//        assertNotNull(toDelete);
//        assertEquals("to_delete", toDelete.getName());
//
//        var toDeleteBefore = this.restTemplate.getForObject("http://localhost:" + port + "/trainers/to_delete", Trainer.class);
//        assertNotNull(toDeleteBefore);
//
//        this.restTemplate.delete("http://localhost:" + port + "/trainers/to_delete");
//
//        var toDeleteAfter = this.restTemplate.getForObject("http://localhost:" + port + "/trainers/to_delete", Trainer.class);
//        assertNull(toDeleteAfter);
//    }


}