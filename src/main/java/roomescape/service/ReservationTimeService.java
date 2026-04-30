package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;

import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime add(ReservationTime reservationTime) {
        Long id = reservationTimeDao.save(reservationTime);
        return new ReservationTime(id, reservationTime.startAt());  // 새 객체 반환
    }

    public void delete(Long id) {
        reservationTimeDao.deleteById(id);
    }
}
