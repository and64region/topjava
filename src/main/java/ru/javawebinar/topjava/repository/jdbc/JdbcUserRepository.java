package ru.javawebinar.topjava.repository.jdbc;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRolesForSave(new ArrayList<>(user.getRoles()), user.getId());
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            // Update Roles
            deleteRoles(user);
            setRoles(user);
        }

        return user;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        return getRoles(user);
    }

    private User getRoles(User user) {
        if (user != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", Role.class, user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        return getRoles(user);
    }

    @Override
    public List<User> getAll() {
        Map<Integer, Set<Role>> roles = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM user_roles",
                new RowCallbackHandler() {
                    @Override
                    public void processRow(ResultSet rs) throws SQLException {
                        roles.computeIfAbsent(rs.getInt("user_id"),
                                userId -> EnumSet.noneOf(Role.class))
                                .add(Role.valueOf(rs.getString("role")));
                    }
                });
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        users.forEach(u -> u.setRoles(roles.get(u.getId())));
        return users;
    }

    private void insertRolesForSave(List<Role> roles, Integer userId) {
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (role, user_id) values(?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, roles.get(i).name());
                        ps.setInt(2, userId);
                    }

                    @Override
                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    private User setRoles(User user) {
        if (user != null) {
            List<Role> roles = jdbcTemplate.queryForList("SELECT role FROM user_roles  WHERE user_id=?", Role.class, user.getId());
            user.setRoles(roles);

        }
        return user;
    }

    private void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }
}
