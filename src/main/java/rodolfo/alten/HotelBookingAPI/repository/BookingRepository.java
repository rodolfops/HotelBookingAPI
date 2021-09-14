package rodolfo.alten.HotelBookingAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rodolfo.alten.HotelBookingAPI.domain.Booking;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select b from Booking b " +
            "where (b.from >= :startDate and b.from <= :endDate) " +
            "or (b.to >= :startDate and b.to <= :endDate) ")
    List<Booking> findBookingByPeriod(LocalDate startDate, LocalDate endDate);
}
