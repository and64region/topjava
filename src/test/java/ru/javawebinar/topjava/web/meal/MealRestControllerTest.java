package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.web.SecurityUtil.*;
import static ru.javawebinar.topjava.web.json.JsonUtil.*;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_MEALS_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(getTos(MEALS, authUserCaloriesPerDay())));
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();

        ResultActions action = mockMvc.perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = readFromJson(action, Meal.class);
        Integer newId = created.getId();
        newMeal.setId(created.getId());
        assertMatch(created, newMeal);
        assertMatch(mealService.get(newId, authUserId()), newMeal);

    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        mockMvc.perform(MockMvcRequestBuilders.put(REST_URL + MEAL1_ID)
        .contentType(MediaType.APPLICATION_JSON)
        .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        assertMatch(mealService.get(MEAL1_ID, authUserId()), updated);
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, authUserId()));
    }

    @Test
    void getBetween() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(REST_URL + "between?startDateTime=2015-05-30T07:00:00&endDateTime=2015-05-31T11:00:00"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(contentJson(createTo(MEAL5, true), createTo(MEAL1, false)));
    }
}