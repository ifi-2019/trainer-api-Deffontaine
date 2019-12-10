package com.ifi.trainer_api.controller;

import com.ifi.trainer_api.bo.Trainer;
import com.ifi.trainer_api.repository.TrainerRepository;
import com.ifi.trainer_api.service.TrainerService;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.web.bind.annotation.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;

    @InjectMocks
    private TrainerController trainerController;

    @BeforeEach
    void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAllTrainers_shouldCallTheService() {
        trainerController.getAllTrainers("trainerNotExist");

        verify(trainerService).getAllTrainers("trainerNotExist");
    }

    @Test
    void getTrainer_shouldCallTheService() {
        trainerController.getTrainer("Ash");

        verify(trainerService).getTrainer("Ash");
    }

    @Test
    void trainerController_shouldBeAnnotated(){
        var controllerAnnotation =
                TrainerController.class.getAnnotation(RestController.class);
        assertNotNull(controllerAnnotation);

        var requestMappingAnnotation =
                TrainerController.class.getAnnotation(RequestMapping.class);
        assertArrayEquals(new String[]{"/trainers"}, requestMappingAnnotation.value());
    }

    @Test
    void getAllTrainers_shouldBeAnnotated() throws NoSuchMethodException {
        var getAllTrainers =
                TrainerController.class.getDeclaredMethod("getAllTrainers", String.class);
        var getMapping = getAllTrainers.getAnnotation(GetMapping.class);

        assertNotNull(getMapping);
        assertArrayEquals(new String[]{"/all/{name}"}, getMapping.value());
    }

    @Test
    void getTrainer_shouldBeAnnotated() throws NoSuchMethodException {
        var getTrainer =
                TrainerController.class.getDeclaredMethod("getTrainer", String.class);
        var getMapping = getTrainer.getAnnotation(GetMapping.class);

        var pathVariableAnnotation = getTrainer.getParameters()[0].getAnnotation(PathVariable.class);

        assertNotNull(getMapping);
        assertArrayEquals(new String[]{"/{name}"}, getMapping.value());

        assertNotNull(pathVariableAnnotation);
    }

    @Test
    void addTrainer_shouldBeAnnotated() throws NoSuchMethodException {
        var addTrainer =
                TrainerController.class.getDeclaredMethod("addTrainer", Trainer.class);
        var addMapping = addTrainer.getAnnotation(PostMapping.class);

        var requestBodyAnnotation = addTrainer.getParameters()[0].getAnnotation(RequestBody.class);

        assertNotNull(addMapping);
        assertArrayEquals(new String[]{"/"}, addMapping.value());

        assertNotNull(requestBodyAnnotation);
    }

    @Test
    void updateTrainer_shouldBeAnnotated() throws NoSuchMethodException {
        var updateTrainer =
                TrainerController.class.getDeclaredMethod("updateTrainer", Trainer.class);
        var updateMapping = updateTrainer.getAnnotation(PutMapping.class);

        var requestBodyAnnotation = updateTrainer.getParameters()[0].getAnnotation(RequestBody.class);

        assertNotNull(updateMapping);
        assertArrayEquals(new String[]{"/"}, updateMapping.value());

        assertNotNull(requestBodyAnnotation);
    }

    @Test
    void deleteTrainer_shouldBeAnnotated() throws NoSuchMethodException {
        var deleteTrainer =
                TrainerController.class.getDeclaredMethod("deleteTrainer", String.class);
        var deleteMapping = deleteTrainer.getAnnotation(DeleteMapping.class);

        var pathVariableAnnotation = deleteTrainer.getParameters()[0].getAnnotation(PathVariable.class);

        assertNotNull(deleteMapping);
        assertArrayEquals(new String[]{"/{name}"}, deleteMapping.value());

        assertNotNull(pathVariableAnnotation);
    }
}