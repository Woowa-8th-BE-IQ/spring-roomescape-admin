package roomescape.controller;

import org.springframework.web.bind.annotation.*;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<Reservation> getReservations() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public Reservation addReservation(@RequestBody ReservationRequest request) {
        return reservationService.add(request);
    }

    @DeleteMapping("/reservations/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.delete(id);
    }
}
