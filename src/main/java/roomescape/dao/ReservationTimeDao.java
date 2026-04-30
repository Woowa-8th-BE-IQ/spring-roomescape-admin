package roomescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.domain.ReservationTime;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Repository
public class ReservationTimeDao {

    private static final String SELECT_ALL =
            "SELECT id, start_at FROM reservation_time";

    private static final String INSERT =
            "INSERT INTO reservation_time (start_at) VALUES (?)";

    private static final String DELETE_BY_ID =
            "DELETE FROM reservation_time WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<ReservationTime> rowMapper = (rs, rowNum) ->
            new ReservationTime(rs.getLong("id"), rs.getString("start_at"));

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query(SELECT_ALL, rowMapper);
    }

    public Long save(ReservationTime reservationTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(INSERT, new String[]{"id"});
            ps.setString(1, reservationTime.startAt());   // ← getStartAt() → startAt()
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}
