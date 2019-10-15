package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());


    private Map<Integer, Map<Integer, Meal>> userAndMeals = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));

        save(new Meal(1,2,LocalDateTime.of(2019, Month.OCTOBER, 12, 9, 0), "Завтрак Админа", 400), 2);
        save(new Meal(2,2,LocalDateTime.of(2019, Month.OCTOBER, 12, 14, 0), "Обед Админа", 800), 2);

    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);

        Map<Integer, Meal> mealMap = userAndMeals.computeIfAbsent(userId, ConcurrentHashMap::new);

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        mealMap.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);

        Map<Integer, Meal> mealMap = userAndMeals.get(userId);

        return mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);

        Map<Integer, Meal> mealMap = userAndMeals.get(userId);

        return mealMap.get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        log.info("getAll {}");

        Map<Integer, Meal> mealMap = userAndMeals.get(userId);
        return mealMap == null ? Collections.emptyList() : mealMap.values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDate).reversed())
                .collect(Collectors.toList());
    }


}

