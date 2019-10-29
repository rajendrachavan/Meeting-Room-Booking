package neo.spring5.MeetingRoomBooking.repositories;

import neo.spring5.MeetingRoomBooking.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByRole(String role);
}
