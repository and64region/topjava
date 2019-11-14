package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping("/create")
public class JspMealCreateController {

    private MealService mealService;

    public JspMealCreateController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public String create(HttpServletRequest request, Model model) {
        Meal newMeal = new Meal(null,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        mealService.create(newMeal, authUserId());

        model.addAttribute("meals", getTos(mealService.getAll(authUserId()), authUserCaloriesPerDay()));
        return "redirect:/meals";
    }

    @GetMapping
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";

    }


}
