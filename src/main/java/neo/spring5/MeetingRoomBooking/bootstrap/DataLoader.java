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
        user.setPassword(new BCryptPasswordEncoder().encode("incorrect"));
        user.setRole(role);

        userRepository.save(user);

        User user1 = new User();
        user1.setFirstName("test1");
        user1.setLastName("testtest1");
        user1.setGender("Female");
        user1.setEmail("test1@gmail.com");
        user1.setMobileno("0987654321");
        user1.setDepartment("Java");
        user1.setPassword(new BCryptPasswordEncoder().encode("correct"));
        user1.setRole(role1);

        userRepository.save(user1);
        System.out.println("-----------------initial-User-Added-----------------");

    }
}
