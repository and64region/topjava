package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 3 Реализовать через Spring JDBC Template JdbcMealRepository
 * 3.1. сделать каждый метод за один SQL запрос
 * 3.2. userId в класс Meal вставлять НЕ надо (для UI и REST это лишние данные, userId это id залогиненного пользователя)
 * 3.3. JbdcTemplate работает через сеттеры. Вместе с конструктором по умолчанию их нужно добавить в Meal
 * 3.4. Cписок еды должен быть отсортирован (тогда мы его сможем сравнивать с тестовыми данными). Кроме того это требуется для UI и API: последняя еда наверху.
 */

@Repository
public class JdbcMealRepository implements MealRepository {

    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertMeal;

    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertMeal = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }




    @Override
    public Meal save(Meal meal, int userId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("datetime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("user_id", userId);

        if (meal.isNew()) {
            Number newKey = insertMeal.executeAndReturnKey(mapSqlParameterSource);
            meal.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE meals SET datetime=:datetime, description=:description, " +
                        "calories=:calories WHERE id=:id AND user_id=:user_id", mapSqlParameterSource) == 0) {
            return null;
        }

        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return jdbcTemplate.queryForObject("SELECT id, datetime, description, calories FROM meals WHERE id=? AND user_id=?",
                ROW_MAPPER, id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query("SELECT id, datetime, description, calories FROM meals ", ROW_MAPPER);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query("SELECT id, datetime, description, calories FROM meals " +
                "WHERE datetime > ? AND datetime < ? AND user_id=?", ROW_MAPPER, startDate, endDate, userId);
    }
}
