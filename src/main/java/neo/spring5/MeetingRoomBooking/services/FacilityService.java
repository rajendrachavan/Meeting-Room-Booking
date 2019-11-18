package neo.spring5.MeetingRoomBooking.services;

import neo.spring5.MeetingRoomBooking.models.Facility;
import java.util.List;
import java.util.Optional;

public interface FacilityService {

    void save(Facility facility);
    List<Facility> findAll();
    Optional<Facility> findById(Long id);
    void deleteById(Long id);
    Facility findByFacility(String facility_name);
}
