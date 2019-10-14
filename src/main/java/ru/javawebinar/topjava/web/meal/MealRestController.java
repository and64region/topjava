package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class MealRestController {
    private final MealService service;

    MealRestController(MealService service){
        this.service = service;
    }

    public Meal create(Meal meal) throws NotFoundException  {
        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");

        return service.create(meal, SecurityUtil.authUserId());

    }

    public void update(Meal meal, int userId){
        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");
//          Под ВОПРОСОМ
//        assureIdConsistent(meal, userId);

        service.update(meal, SecurityUtil.authUserId());
    }

    public Meal get(int id) throws  NotFoundException{
        Meal meal = service.get(id, SecurityUtil.authUserId());

        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");

        return meal;
    }

    public void delete(int id) throws NotFoundException {
        Meal meal = service.get(id, SecurityUtil.authUserId());

        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");

        service.delete(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getDateTime(LocalTime startTime, LocalTime endTime){
        return getFilteredTos(service.getAll(SecurityUtil.authUserId()),
                SecurityUtil.authUserCaloriesPerDay(),
                startTime,
                endTime);


    }

    public List<MealTo> getAll() {
        return getTos(service.getAll(SecurityUtil.authUserId()),SecurityUtil.authUserCaloriesPerDay());
    }

    private boolean mealUserIdEqualsCurrentUserId(Meal meal){
        return meal.getUserId() != SecurityUtil.authUserId();
    }

}