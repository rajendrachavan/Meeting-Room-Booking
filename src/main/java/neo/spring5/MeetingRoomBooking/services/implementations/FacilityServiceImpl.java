package neo.spring5.MeetingRoomBooking.services.implementations;

import neo.spring5.MeetingRoomBooking.models.Facility;
import neo.spring5.MeetingRoomBooking.repositories.FacilityRepository;
import neo.spring5.MeetingRoomBooking.services.FacilityService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;

    public FacilityServiceImpl(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    @Override
    public void save(Facility facility) {
        facilityRepository.save(facility);
    }

    @Override
    public List<Facility> findAll() {
        List<Facility> facilityList = facilityRepository.findAll();
        return facilityList;
    }

    @Override
    public Optional<Facility> findById(Long id) {
        Optional<Facility> facility = facilityRepository.findById(id);
        return facility;
    }

    @Override
    public void deleteById(Long id) {
        facilityRepository.deleteById(id);
    }

    @Override
    public Facility findByFacility(String facility_name) {
        Facility facility = facilityRepository.findByFacility(facility_name);
        return facility;
    }
}
