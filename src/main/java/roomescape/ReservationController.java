package roomescape;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

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
    public Reservation addReservation(@RequestBody Map<String, Object> params) {
        String name   = (String) params.get("name");
        String date   = (String) params.get("date");
        Long timeId   = Long.valueOf(params.get("timeId").toString());

        String sql = "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, date);
            ps.setLong(3, timeId);
            return ps;
        }, keyHolder);

        Long newId = keyHolder.getKey().longValue();

        // 방금 INSERT한 예약을 JOIN해서 다시 조회 (time 객체 포함 응답)
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
