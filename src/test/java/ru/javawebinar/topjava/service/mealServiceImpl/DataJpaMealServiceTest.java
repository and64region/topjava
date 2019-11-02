package ru.javawebinar.topjava.service.mealServiceImpl;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.abstractService.AbstractMealServiceTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
}
