package neo.spring5.MeetingRoomBooking.repositories;

import neo.spring5.MeetingRoomBooking.models.Facilities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilitiesRepository extends JpaRepository<Facilities, Long> {
    Facilities findFacilityById(Long id);
}
