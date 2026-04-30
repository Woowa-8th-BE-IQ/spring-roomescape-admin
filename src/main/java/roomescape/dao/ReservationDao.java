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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationDao {

    private static final String SELECT_ALL = """
            SELECT
                r.id       AS reservation_id,
                r.name,
                r.date,
                t.id       AS time_id,
                t.start_at AS time_value
            FROM reservation AS r
            INNER JOIN reservation_time AS t ON r.time_id = t.id
            """;

    private static final String INSERT =
            "INSERT INTO reservation (name, date, time_id) VALUES (?, ?, ?)";

    private static final String DELETE_BY_ID =
            "DELETE FROM reservation WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Reservation> rowMapper = (rs, rowNum) -> {
        ReservationTime time = new ReservationTime(
                rs.getLong("time_id"),
                LocalTime.parse(rs.getString("time_value"))
        );
        return new Reservation(
                rs.getLong("reservation_id"),
                rs.getString("name"),
                LocalDate.parse(rs.getString("date")),
                time
        );
    };

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Reservation> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    public Reservation save(ReservationRequest request, ReservationTime time) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, request.name());
            ps.setString(2, request.date().toString());
            ps.setLong(3, request.timeId());
            return ps;
        }, keyHolder);
        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Reservation(id, request.name(), request.date(), time);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
