package neo.spring5.MeetingRoomBooking.services.implementations;

import neo.spring5.MeetingRoomBooking.models.ChangeRequest;
import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.ChangeRequestRepository;
import neo.spring5.MeetingRoomBooking.services.ChangeRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChangeRequestServiceImpl implements ChangeRequestService {

    private final ChangeRequestRepository changeRequestRepository;

    public ChangeRequestServiceImpl(ChangeRequestRepository changeRequestRepository) {
        this.changeRequestRepository = changeRequestRepository;
    }

    @Override
    public <Optional> ChangeRequest findByUser(User user) {
        return changeRequestRepository.findByUser(user);
    }

    @Override
    public List<ChangeRequest> findAllByUserRole(Role role) {
        return changeRequestRepository.findAllByUserRole(role);
    }

    @Override
    public ChangeRequest save(ChangeRequest changeRequest) {
        return changeRequestRepository.save(changeRequest);
    }

    @Override
    public List<ChangeRequest> findAll() {
        return changeRequestRepository.findAll();
    }

    @Override
    public <Optional> ChangeRequest findById(Long id) {
        return changeRequestRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        changeRequestRepository.deleteById(id);
    }
}
