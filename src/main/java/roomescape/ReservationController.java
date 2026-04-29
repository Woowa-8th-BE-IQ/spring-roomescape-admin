package roomescape;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservationController {

    private final JdbcTemplate jdbcTemplate;

    public ReservationController(JdbcTemplate jdbcTemplate) {
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

    // GET /reservations
    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        String sql = """
                SELECT
                    r.id   AS reservation_id,
                    r.name,
                    r.date,
                    t.id   AS time_id,
                    t.start_at AS time_value
                FROM reservation AS r
                INNER JOIN reservation_time AS t ON r.time_id = t.id
                """;
        return jdbcTemplate.query(sql, rowMapper);
    }

    // POST /reservations  — 요청: { name, date, timeId }
    @PostMapping("/reservations")
    public Reservation addReservation(@RequestBody ReservationRequest request) {
        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, request.getName());
            ps.setString(2, request.getDate());
            ps.setLong(3, request.getTimeId());
            return ps;
        }, keyHolder);

        Long newId = keyHolder.getKey().longValue();

        String selectSql = """
            SELECT
                r.id   AS reservation_id,
                r.name,
                r.date,
                t.id   AS time_id,
                t.start_at AS time_value
            FROM reservation AS r
            INNER JOIN reservation_time AS t ON r.time_id = t.id
            WHERE r.id = ?
            """;
        return jdbcTemplate.queryForObject(selectSql, rowMapper, newId);
    }

    // DELETE /reservations/{id}
    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable Long id) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
