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
        return reservationDao.save(request, time);
    }

    public void delete(Long id) {
        reservationDao.deleteById(id);
    }
}
