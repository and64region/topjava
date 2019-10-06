package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.Dao;
import ru.javawebinar.topjava.dao.DaoImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFiltered;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private static String INSERT_OR_EDIT = "/mealEdit.jsp";
    private static String LIST_USER = "/meals.jsp";

    private Dao dao = new DaoImpl();
    private List<MealTo> mealListWithExcees;

    private List<MealTo> getMealListWithExcees() {
        return getFiltered(dao.getAllMeal(), LocalTime.MIN, LocalTime.MAX, MealsUtil.getDefaultCaloriesPerDay());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String forward = "";
        String action = request.getParameter("action");



        if (action.equalsIgnoreCase("delete")) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            dao.deleteMeal(userId);
            forward = LIST_USER;
            request.setAttribute("meals", getMealListWithExcees());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            int userId = Integer.parseInt(request.getParameter("userId"));
            Meal meal = dao.getMealById(userId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("mealsList")) {
            forward = LIST_USER;
            request.setAttribute("mealsListWithExcees", getMealListWithExcees());
        } else {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);

    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LocalDateTime dateTime = LocalDateTime.parse(request.getParameter("dateTime"), DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"));
        String description = request.getParameter("description");
        int calories = Integer.parseInt(request.getParameter("calories"));
        String userId = request.getParameter("userId");
        if (userId == null || userId.isEmpty()) {
            dao.addMeal(new Meal(dateTime, description, calories));
        } else {

            dao.updateMeal(new Meal(Integer.parseInt(userId), dateTime, description, calories));
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_USER);
        request.setAttribute("meals", dao.getAllMeal());
        view.forward(request, response);
    }
}
