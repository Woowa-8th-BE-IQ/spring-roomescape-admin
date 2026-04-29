package roomescape.controller;

import org.springframework.web.bind.annotation.*;
import roomescape.domain.ReservationTime;
import roomescape.service.ReservationTimeService;

import java.util.List;

@RestController
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @GetMapping("/times")
    public List<ReservationTime> getTimes() {
        return reservationTimeService.findAll();
    }

    @PostMapping("/times")
    public ReservationTime addTime(@RequestBody ReservationTime reservationTime) {
        return reservationTimeService.add(reservationTime);
    }

    @DeleteMapping("/times/{id}")
    public void deleteTime(@PathVariable Long id) {
        reservationTimeService.delete(id);
    }
}
