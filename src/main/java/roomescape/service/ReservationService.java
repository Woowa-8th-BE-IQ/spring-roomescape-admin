package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.domain.Reservation;
import roomescape.dto.ReservationRequest;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation add(ReservationRequest request) {
        Long id = reservationDao.save(request);
        return reservationDao.findById(id);
    }

    public void delete(Long id) {
        reservationDao.deleteById(id);
    }
}
