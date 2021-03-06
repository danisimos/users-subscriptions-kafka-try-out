package com.orioninc.repositories.impl;

import com.orioninc.models.Subscription;
import com.orioninc.models.SubscriptionType;
import com.orioninc.models.User;
import com.orioninc.repositories.SubscriptionsRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SubscriptionsRepositoryJdbcImpl implements SubscriptionsRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SQL_INSERT_INTO = "insert into subscriptions(user_id, type, week_count, received_timestamp)" +
            "values(:userId, :type, :weekCount, :receivedTimestamp)";
    private static final String SQL_SELECT_ALL = "select p.type as \"type\", p.received_timestamp as \"received_timestamp\"," +
            "u.id as \"user_id\", u.first_name as \"first_name\", u.last_name as \"last_name\"," +
            "u.age as \"age\", p.week_count as \"week_count\" " +
            "from subscriptions p left join users u on u.id = p.user_id ";

    private final RowMapper<Subscription> SubscriptionRowMapper = (row, rowNumber) -> Subscription.builder()
            .user(User.builder()
                    .id(row.getInt("user_id"))
                    .firstName(row.getString("first_name"))
                    .lastName(row.getString("last_name"))
                    .age(row.getInt("age"))
                    .build())
            .subscriptionType(SubscriptionType.valueOf(row.getString("type")))
            .weekNumber(row.getInt("week_count"))
            .timestamp(row.getTimestamp("received_timestamp").getTime())
            .build();

    public SubscriptionsRepositoryJdbcImpl(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Subscription> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, SubscriptionRowMapper);
    }

    @Override
    public Subscription save(Subscription subscription) {
        Map<String, Object> values = new HashMap<>();

        values.put("userId", subscription.getUser().getId());
        values.put("type", subscription.getSubscriptionType().toString());
        values.put("weekCount", subscription.getWeekNumber());
        values.put("receivedTimestamp", new Timestamp(subscription.getTimestamp()));

        SqlParameterSource parameterSource = new MapSqlParameterSource(values);

        jdbcTemplate.update(SQL_INSERT_INTO, parameterSource);

        return subscription;
    }

    @Override
    public Optional<Subscription> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void update(Integer id, Subscription entity) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Subscription> getSubscriptionsByType(SubscriptionType subscriptionType) {
        return null;
    }
}
