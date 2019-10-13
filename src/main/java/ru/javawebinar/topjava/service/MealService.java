package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.repository.MealRepository;

@Service
public class MealService {

    private final MealRepository repository;

    MealService(MealRepository repository){
        this.repository = repository;
    }



}