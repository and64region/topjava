package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;

    public static final Meal mealOnDbID_1 = new Meal(1,
            LocalDateTime.of(2019, 10, 20, 9, 0),
            "breakfast", 500);

    public static final Meal mealOnDbID_2 = new Meal(2,
            LocalDateTime.of(2019, 10, 20, 13, 0),
            "lunch", 1000);

    public static final Meal mealOnDbID_3 = new Meal(3,
            LocalDateTime.of(2019, 10, 20, 18, 0),
            "dinner", 500);

    public static final Meal mealOnDbID_4 = new Meal(4,
            LocalDateTime.of(2019, 9, 21,13,0),
            "lunch", 1000);

    public static final Meal forCreateMeal = new Meal(
            LocalDateTime.of(2019, 10, 21, 9, 0),
            "breakfast",
            500);


    public static final List<Meal> fakeDB = Arrays.asList( mealOnDbID_3, mealOnDbID_2, mealOnDbID_1, mealOnDbID_4);

    public static final Meal notExistsMeal = new Meal(98765454,
            LocalDateTime.now(),
            "notFound", 500);

    public static final Meal mealGetForUpdate = new Meal(1,
            LocalDateTime.of(2019, 10, 20, 9, 0),
            "breakfastGOOD",
            1000);


    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingOnlyGivenFields(
                expected,
                "id",
                "dateTime",
                "description",
                "calories"
        );
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorOnFields("id",
                "dateTime",
                "description",
                "calories").isEqualTo(expected);
    }
}
