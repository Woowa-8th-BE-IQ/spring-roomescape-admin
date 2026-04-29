package roomescape;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();
    private final AtomicLong index = new AtomicLong(1);

    // GET /reservations — 전체 조회
    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return reservations;
    }

    // POST /reservations — 예약 추가
    @PostMapping("/reservations")
    public Reservation addReservation(@RequestBody Reservation reservation) {
        reservation.setId(index.getAndIncrement());
        reservations.add(reservation);
        return reservation;
    }

    // DELETE /reservations/{id} — 예약 삭제
    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservations.removeIf(r -> r.getId().equals(id));
    }
}
