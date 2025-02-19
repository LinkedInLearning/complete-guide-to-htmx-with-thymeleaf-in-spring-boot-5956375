package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.entity.Role;
import com.mdrsolutions.records_management.entity.User;
import com.mdrsolutions.records_management.repository.PersonRepository;
import com.mdrsolutions.records_management.repository.RoleRepository;
import com.mdrsolutions.records_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PersonRepository personRepository, RoleRepository roleRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(String email, String rawPassword, Person person, Set<Role> roles) {
        String encodedPassword = passwordEncoder.encode(rawPassword);
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(roles);
        user.setEnabled(true); // Enable the user by default

        User newUser = userRepository.save(user);
        person.setUser(newUser);
        personRepository.save(person);

        return newUser;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user){
        return userRepository.save(user);
    }

    public Role createRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return roleRepository.save(role);
    }

    public void disableUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(false);
        userRepository.save(user);
    }

    public void enableUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }
}
