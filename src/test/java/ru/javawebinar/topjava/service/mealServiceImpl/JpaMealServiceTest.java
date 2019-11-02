package ru.javawebinar.topjava.service.mealServiceImpl;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.abstractService.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.JPA;

@ActiveProfiles(JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest {
}
