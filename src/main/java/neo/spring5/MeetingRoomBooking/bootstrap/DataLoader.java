package neo.spring5.MeetingRoomBooking.bootstrap;

import neo.spring5.MeetingRoomBooking.models.Role;
import neo.spring5.MeetingRoomBooking.models.User;
import neo.spring5.MeetingRoomBooking.repositories.RoleRepository;
import neo.spring5.MeetingRoomBooking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

//@Component
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
        role.setId(1L);
        role.setRole("ADMIN");

        User user = new User();
        user.setId(1L);
        user.setFirstName("test1");
        user.setLastName("test");
        user.setEmail("test@test.com");
        user.setPassword(new BCryptPasswordEncoder().encode("correct"));
        user.setGender("Others");
        user.setMobileNo("987456321");
        user.setDepartment("IOS");
        user.setActive(1);
        user.setRole(role);

        userRepository.save(user);
        System.out.println("-----------------initial-Users-Added-----------------");

        Role role1 = new Role();
        role1.setId(6L);
        role1.setRole("USER");
        roleRepository.save(role1);
        System.out.println("-----------------initial-Roles-Added-----------------");
    }
}
