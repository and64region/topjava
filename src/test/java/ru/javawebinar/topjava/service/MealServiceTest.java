package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private MealService mealService;

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "ЗавтракаюЯЯ", 200);
        Meal mealCreated = mealService.create(newMeal, USER_ID);
        newMeal.setId(mealCreated.getId());
        assertMatch(mealService.getAll(USER_ID),
                mealOnDbID_1,
                mealOnDbID_2,
                mealOnDbID_3,
                mealOnDbID_4,
                mealCreated);

    }

    @Test(expected = DataAccessException.class)
    public void duplicateMealDate() throws Exception {
        mealService.create(new Meal(null,
                LocalDateTime.of(2019, 10, 20, 9, 0),
                "breakfast", 500),USER_ID);
    }

    @Test
    public void get() {
        Meal meal = mealService.get(1, 100_000);
        assertMatch(meal, mealOnDbID_1);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() throws Exception {
        mealService.get(150, USER_ID);
    }

    @Test
    public void delete() {
        mealService.delete(mealOnDbID_1.getId(), USER_ID);
        assertThat(mealService.getAll(USER_ID)).doesNotContain(mealOnDbID_1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        mealService.delete(150, USER_ID);
    }

    @Test
    public void getBetweenDates() {
        assertMatch(mealService.getBetweenDates(LocalDate.of(2019,10, 20),
                LocalDate.of(2019,10, 20), USER_ID),
                mealOnDbID_1,mealOnDbID_2,mealOnDbID_3);
    }

    @Test
    public void getAll() {
        assertMatch(mealService.getAll(USER_ID), fakeDB);
    }

    @Test
    public void update() {
        mealService.update(mealGetForUpdate, USER_ID);
        assertMatch(mealService.get(1, USER_ID), mealGetForUpdate);

    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() throws Exception {
        mealService.update(notExistsMeal, USER_ID);
    }


}
