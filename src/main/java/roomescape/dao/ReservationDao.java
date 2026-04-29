package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class ReservationDao {

    private final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                rs.getString("time_value")
        );
        return new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                rs.getString("date"),
                time
        );
    };

    private static final String SELECT_WITH_TIME = """
            SELECT
                r.id       AS reservation_id,
                r.name,
                r.date,
                t.id       AS time_id,
                t.start_at AS time_value
            FROM reservation AS r
            INNER JOIN reservation_time AS t ON r.time_id = t.id
            """;

    public List<Reservation> findAll() {
        return jdbcTemplate.query(SELECT_WITH_TIME, rowMapper);
    }

    public Reservation findById(Long id) {
        return jdbcTemplate.queryForObject(
                SELECT_WITH_TIME + "WHERE r.id = ?",
                rowMapper,
                id
        );
    }

    public Long save(ReservationRequest request) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, request.getName());
            ps.setString(2, request.getDate());
            ps.setLong(3, request.getTimeId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM reservation WHERE id = ?", id);
    }
}
