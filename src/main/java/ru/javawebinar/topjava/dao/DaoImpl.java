package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class DaoImpl implements Dao{
    private List<Meal> meals;

    public DaoImpl() {
        meals = new ArrayList<>();
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));

    }


    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    public void deleteMeal(int id) {
        Meal removeMeal = null;
        for(Meal meal : meals){
            if (meal.getId() == id)
                removeMeal = meal;
        }

        meals.remove(removeMeal);
    }

    public void updateMeal(Meal meal) {
        meals.forEach(thisMeal -> {
            if (thisMeal.getId() == meal.getId()) {
                thisMeal.setCalories(meal.getCalories());
                thisMeal.setDateTime(meal.getDateTime());
                thisMeal.setDescription(meal.getDescription());
            }
        });
    }

    public List<Meal> getAllMeal() {
        return meals;
    }

    public Meal getMealById(int id) {
        Meal res = null;
        for (Meal meal : meals) {
            if (meal.getId() == id)
                res = meal;
        }

        return res;
    }

}
