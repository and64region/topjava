package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Objects.*;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFound;

@Service
public class MealService {

    private final MealRepository repository;

    MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        Meal mealPlusUserId = new Meal(meal.getId(), userId, meal.getDateTime(), meal.getDescription(), meal.getCalories());
        return repository.save(mealPlusUserId, userId);
    }

    public void delete(int id, int userId) throws NotFoundException {
        Meal meal = repository.get(id, userId);

        checkNotFound(meal, "в базе нет еды по этому id");

        repository.delete(id, userId);
    }

    public Meal get(int id, int userId) throws NotFoundException {
        Meal meal = repository.get(id, userId);

        checkNotFound(meal, "в базе нет еды по этому id");

        return repository.get(id, userId);

    }

    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Meal meal, int userId) throws NotFoundException {
        checkNotFound(meal, "в базе нет еды по этому id");

        repository.save(meal, userId);
    }

    public Collection<Meal> getBetweenDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {

        requireNonNull(startDateTime);
        requireNonNull(endDateTime);

        return getAll(userId).stream()
                .filter(meal -> meal.getDateTime().isAfter(startDateTime) && meal.getDateTime().isBefore(endDateTime))
                .collect(Collectors.toList());
    }


}