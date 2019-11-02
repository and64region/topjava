package ru.javawebinar.topjava.service.userServiceImpl;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.abstractService.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
}
