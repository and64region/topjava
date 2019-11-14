package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredTos;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping("/meals")
public class JspMealController {

    private final MealService mealService;

    public JspMealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public String getAllWithDateMeals(@RequestParam(value = "startDate", required = false) String startDateStr,
                                      @RequestParam(value = "endDate", required = false) String endDateStr,
                                      @RequestParam(value = "startTime", required = false) String startTimeStr,
                                      @RequestParam(value = "endTime", required = false) String endTimeStr,
                                      Model model) {
        LocalDate startDate = parseLocalDate(startDateStr);
        LocalDate endDate = parseLocalDate(endDateStr);
        LocalTime startTime = parseLocalTime(startTimeStr);
        LocalTime endTime = parseLocalTime(endTimeStr);

        List<Meal> mealsDateFiltered = mealService.getBetweenDates(startDate, endDate, authUserId());
        List<MealTo> filteredMealsTos = getFilteredTos(mealsDateFiltered, authUserCaloriesPerDay(), startTime, endTime);
        model.addAttribute("meals", filteredMealsTos);

        return "meals";
    }

    @GetMapping
    public String getAllMeals(Model model) {
        List<MealTo> mealsTo = getTos(mealService.getAll(authUserId()), authUserCaloriesPerDay());
        model.addAttribute("meals", mealsTo);
        return "meals";
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        mealService.delete(id, authUserId());
        model.addAttribute("meals", getTos(mealService.getAll(authUserId()), authUserCaloriesPerDay()));
        return "redirect:/meals";
    }


}
