package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.util.MealsUtil.getTos;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping("/update")
public class JspMealUpdateController {

    private MealService mealService;

    public JspMealUpdateController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id, HttpServletRequest request, Model model) {
        Meal newMeal = new Meal(id,
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        mealService.update(newMeal, authUserId());

        model.addAttribute("meals", getTos(mealService.getAll(authUserId()), authUserCaloriesPerDay()));
        return "redirect:/meals";
    }

    @GetMapping("/{id}")
    public String update(@PathVariable(required = false) Integer id, Model model) {
        model.addAttribute("meal", mealService.get(id, authUserId()));
        return "mealForm";

    }


}


