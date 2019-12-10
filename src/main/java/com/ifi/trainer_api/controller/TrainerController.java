package com.ifi.trainer_api.controller;

import com.ifi.trainer_api.bo.Trainer;
import com.ifi.trainer_api.service.TrainerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainers")
public class TrainerController {

    private final TrainerService trainerService;

    TrainerController(TrainerService trainerService){
        this.trainerService = trainerService;
    }

    @GetMapping("/all/{name}")
    Iterable<Trainer> getAllTrainers(@PathVariable String name){
        return this.trainerService.getAllTrainers(name);
    }

    @GetMapping("/{name}")
    Trainer getTrainer(@PathVariable String name){
        return this.trainerService.getTrainer(name);
    }

    @PostMapping("/")
    Trainer addTrainer(@RequestBody Trainer trainer){
        return this.trainerService.createTrainer(trainer);
    }

    @PutMapping("/")
    Trainer updateTrainer(@RequestBody Trainer trainer){
        return this.trainerService.createTrainer(trainer);
    }

    @DeleteMapping("/{name}")
    void deleteTrainer(@PathVariable String name){
        this.trainerService.deleteTrainer(name);
    }

}
