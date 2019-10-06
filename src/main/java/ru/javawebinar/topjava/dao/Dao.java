package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Dao  {
    public void addMeal(Meal meal);

    public void deleteMeal(int id);

    public void updateMeal(Meal meal);

    public List<Meal> getAllMeal();

    public Meal getMealById(int id);
}
