package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({"classpath:spring/spring-app.xml",
    "classpath:spring/spring-db.xml"})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "ЗавтракаюЯЯ", 200);
        Meal mealCreated = mealService.create(newMeal, 100_000);
        newMeal.setId(mealCreated.getId());

    }

    @Autowired
    private MealService mealService;

    @Test
    public void get() {
        Meal meal = mealService.get(1, 100_000);
        assertMatch(meal, MEALUSER);
    }

    @Test
    public void delete() {
    }

    @Test
    public void getBetweenDates() {
    }

    @Test
    public void getAll() {
    }

    @Test
    public void update() {
    }



}
