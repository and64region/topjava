package ru.javawebinar.topjava.service.mealServiceImpl;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.abstractService.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {
}
