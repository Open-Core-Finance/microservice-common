package tech.corefinance.common.jpa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
@ConditionalOnProperty(prefix = "tech.corefinance.database", name = "mode", havingValue = "postgresql", matchIfMissing = false)
public class PostgreSqlSequenceHandling implements DbSequenceHandling {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PostgreSqlSequenceHandling(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long getCurrentSequenceValue(String sequenceName) {
        String sql = "select last_value FROM " + sequenceName.replace("--", "||||||");
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    @Override
    public Long getNextSequenceValue(String sequenceName) {
        var sql = "SELECT nextval(?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{sequenceName}, new int[]{Types.VARCHAR}, Long.class);
    }

    @Override
    public void restartSequence(long newVal, String sequenceName) {
        var sql = "ALTER SEQUENCE " + sequenceName + " RESTART WITH " + newVal + ";";
        jdbcTemplate.update(sql);
    }
}
