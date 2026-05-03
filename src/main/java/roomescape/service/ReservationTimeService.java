package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequest;

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

    public ReservationTime add(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(null, request.startAt());
        return reservationTimeDao.save(reservationTime);
    }

    public void delete(Long id) {
        reservationTimeDao.deleteById(id);
    }
}
