package dev.adebayoolan.budgetpilot.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DBConnectionController {

    private final JdbcTemplate jdbcTemplate;

    public DBConnectionController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/db-test")
    public String dbTest() {
        return jdbcTemplate.queryForObject(
                "SELECT current_database()",
                String.class);
    }
}