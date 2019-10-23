package neo.spring5.MeetingRoomBooking.bootstrap;

import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Role role = new Role();
        role.setRoleType("ADMIN");
        roleRepository.save(role);

        Role role1 = new Role();
        role1.setRoleType("USER");
        roleRepository.save(role1);

        User user = new User();
        user.setFirstName("test");
        user.setLastName("testtest");
        user.setGender("Male");
        user.setEmail("test@gmail.com");
        user.setMobileno("0987654321");
        user.setDepartment("Java");
        user.setPasswd(new BCryptPasswordEncoder().encode("incorrect"));
        user.setRole(role);


        userRepository.save(user);
        System.out.println("-----------------initial-User-Added-----------------");

    }
}
