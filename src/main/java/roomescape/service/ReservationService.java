package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationRequest;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation add(ReservationRequest request) {
        ReservationTime time = reservationTimeDao.findById(request.timeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "존재하지 않는 시간입니다: timeId=" + request.timeId()
                ));
        Reservation reservation = Reservation.ofNew(request.name(), request.date(), time);
        return reservationDao.save(reservation);
    }

    public void delete(Long id) {
        reservationDao.deleteById(id);
    }
}
