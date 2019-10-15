package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    MealRestController(MealService service) {
        this.service = service;
    }

    public Meal create(Meal meal) throws NotFoundException {
        log.info("create {}", meal);

        if (meal.getUserId() == null)
            meal = new Meal(meal.getId(), SecurityUtil.authUserId(), meal.getDateTime(), meal.getDescription(),meal.getCalories());

        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");

        return service.create(meal, SecurityUtil.authUserId());

    }

    public void update(Meal meal){
        log.info("update {}", meal);
        if (meal.getUserId() == null)
            meal = new Meal(meal.getId(), SecurityUtil.authUserId(), meal.getDateTime(), meal.getDescription(),meal.getCalories());

        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");
//          Под ВОПРОСОМ
//        assureIdConsistent(meal, userId);

        service.update(meal, SecurityUtil.authUserId());
    }

    public Meal get(int id) throws  NotFoundException{
        Meal meal = service.get(id, SecurityUtil.authUserId());
        log.info("get {}", meal);
        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");

        return meal;
    }

    public void delete(int id) throws NotFoundException {
        Meal meal = service.get(id, SecurityUtil.authUserId());
        log.info("delete {}", meal);
        if (mealUserIdEqualsCurrentUserId(meal))
            throw new NotFoundException("Еда не принадлежит пользователю");

        service.delete(id, SecurityUtil.authUserId());
    }

    public List<MealTo> getDateTime(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        log.info("getDateTime {}");

        Collection<Meal> mealCollection = service.getBetweenDateTime(LocalDateTime.of(startDate, LocalTime.MIN), LocalDateTime.of(endDate, LocalTime.MAX), SecurityUtil.authUserId());

        return getFilteredTos(mealCollection, SecurityUtil.authUserCaloriesPerDay(),startTime, endTime);


    }

    public List<MealTo> getAll() {
        return getTos(service.getAll(SecurityUtil.authUserId()), SecurityUtil.authUserCaloriesPerDay());
    }

    private boolean mealUserIdEqualsCurrentUserId(Meal meal) {
        return meal.getUserId() != SecurityUtil.authUserId();
    }

}