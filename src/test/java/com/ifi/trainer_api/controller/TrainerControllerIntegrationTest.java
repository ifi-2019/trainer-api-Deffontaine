package com.ifi.trainer_api.controller;

import com.ifi.trainer_api.bo.Pokemon;
import com.ifi.trainer_api.bo.Trainer;

import com.ifi.trainer_api.repository.TrainerRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TrainerControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TrainerController controller;

    @Value("${spring.security.user.name}")
    private String username;

    @Value("${spring.security.user.password}")
    private String password;

    @Autowired
    private TrainerRepository repository;

    @BeforeEach
    void before(){
        var ash = new Trainer("Ash");
        var pikachu = new Pokemon(25, 18);
        ash.setTeam(List.of(pikachu));

        var misty = new Trainer("Misty");
        var staryu = new Pokemon(120, 18);
        var starmie = new Pokemon(121, 21);
        misty.setTeam(List.of(staryu, starmie));

        // save a couple of trainers
        repository.save(ash);
        repository.save(misty);
    }

    @AfterEach
    void after(){
        repository.deleteAll();
    }

    @Test
    void getTrainers_shouldThrowAnUnauthorized(){
        var responseEntity = this.restTemplate
                .getForEntity("http://localhost:" + port + "/trainers/Ash", Trainer.class);
        assertNotNull(responseEntity);
        assertEquals(401, responseEntity.getStatusCodeValue());
    }

    @Test
    void trainerController_shouldBeInstanciated(){
        assertNotNull(controller);
    }

    @Test
    void getTrainer_withNameAsh_shouldReturnAsh() {
        var ash = this.restTemplate
                .withBasicAuth(username, password)
                .getForObject("http://localhost:" + port + "/trainers/Ash", Trainer.class);
        assertNotNull(ash);
        assertEquals("Ash", ash.getName());
        assertEquals(1, ash.getTeam().size());

        assertEquals(25, ash.getTeam().get(0).getPokemonType());
        assertEquals(18, ash.getTeam().get(0).getLevel());
    }

    @Test
    void getAllTrainers_shouldReturnAshAndMisty() {
        var trainerNotExist = "trainerNotExist";

        var trainers = this.restTemplate
                .withBasicAuth(username, password)
                .getForObject("http://localhost:" + port + "/trainers/all/" + trainerNotExist, Trainer[].class);
        assertNotNull(trainers);
        assertEquals(2, trainers.length);

        assertEquals("Ash", trainers[0].getName());
        assertEquals("Misty", trainers[1].getName());
    }

    @Test
    void addTrainer() {
        String json = "{\"name\": \"Bug Catcher\",\"team\": [{\"pokemonType\": 13, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        var bugCatcher = this.restTemplate
                .withBasicAuth(username, password)
                .postForObject("http://localhost:" + port + "/trainers/", entity, Trainer.class);

        assertNotNull(bugCatcher);
        assertEquals("Bug Catcher", bugCatcher.getName());
        assertEquals(2, bugCatcher.getTeam().size());

        assertEquals(13, bugCatcher.getTeam().get(0).getPokemonType());
        assertEquals(10, bugCatcher.getTeam().get(1).getPokemonType());
        assertEquals(6, bugCatcher.getTeam().get(0).getLevel());
        assertEquals(6, bugCatcher.getTeam().get(1).getLevel());
    }

    @Test
    void updateTrainer() {
        String json = "{\"name\": \"to_update\",\"team\": [{\"pokemonType\": 13, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";
        String json2 = "{\"name\": \"to_update\",\"team\": [{\"pokemonType\": 14, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);
        HttpEntity<String> entity2 = new HttpEntity<String>(json2, headers);

        var toUpdate = this.restTemplate
                .withBasicAuth(username, password)
                .postForObject("http://localhost:" + port + "/trainers/", entity, Trainer.class);

        assertNotNull(toUpdate);
        assertEquals(13, toUpdate.getTeam().get(0).getPokemonType());

        this.restTemplate
                .withBasicAuth(username, password)
                .put("http://localhost:" + port + "/trainers/", entity2, Trainer.class);

        var toUpdateAfter = this.restTemplate
                .withBasicAuth(username, password)
                .getForObject("http://localhost:" + port + "/trainers/to_update", Trainer.class);
        assertNotNull(toUpdateAfter);
        assertEquals(14, toUpdateAfter.getTeam().get(0).getPokemonType());
    }

    @Test
    void deleteTrainer() {
        String json = "{\"name\": \"to_delete\",\"team\": [{\"pokemonType\": 13, \"level\": 6},{\"pokemonType\": 10, \"level\": 6}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json, headers);

        var toDelete = this.restTemplate
                .withBasicAuth(username, password)
                .postForObject("http://localhost:" + port + "/trainers/", entity, Trainer.class);

        assertNotNull(toDelete);
        assertEquals("to_delete", toDelete.getName());

        var toDeleteBefore = this.restTemplate
                .withBasicAuth(username, password)
                .getForObject("http://localhost:" + port + "/trainers/to_delete", Trainer.class);
        assertNotNull(toDeleteBefore);

        this.restTemplate
                .withBasicAuth(username, password)
                .delete("http://localhost:" + port + "/trainers/to_delete");

        var toDeleteAfter = this.restTemplate
                .withBasicAuth(username, password)
                .getForObject("http://localhost:" + port + "/trainers/to_delete", Trainer.class);
        assertNull(toDeleteAfter);
    }


}