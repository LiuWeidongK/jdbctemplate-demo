package com.example.jdbctemplatedemo.jdbc;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author lwd
 * Created on 2023/10/17
 */
public class UserRepository extends JDBCTemplateRepository<UserEntity> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected NamedParameterJdbcTemplate jdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    protected TypeReference<UserEntity> typeReference() {
        return new TypeReference<>() {
        };
    }

    @Override
    public String table() {
        return "user";
    }
}
