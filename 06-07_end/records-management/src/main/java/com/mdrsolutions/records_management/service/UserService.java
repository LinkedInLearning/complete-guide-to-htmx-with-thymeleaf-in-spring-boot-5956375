package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.PersonDoesNotExistException;
import com.mdrsolutions.records_management.controller.dto.ErrorUtil;
import com.mdrsolutions.records_management.controller.dto.RegistrationResponse;
import com.mdrsolutions.records_management.controller.dto.UserDto;
import com.mdrsolutions.records_management.entity.*;
import com.mdrsolutions.records_management.repository.EmailRepository;
import com.mdrsolutions.records_management.repository.PersonRepository;
import com.mdrsolutions.records_management.repository.RoleRepository;
import com.mdrsolutions.records_management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       PersonRepository personRepository,
                       RoleRepository roleRepository,
                       @Lazy PasswordEncoder passwordEncoder, EmailRepository emailRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailRepository = emailRepository;
    }

    public User createUser(String email, String rawPassword, Person person, Set<Role> roles) {
        LOGGER.info("createUser(...)");
        LOGGER.info("raw password: " + rawPassword);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        LOGGER.info("encoded password: " + encodedPassword);
        User user = new User();
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRoles(roles);
        user.setEnabled(true); // Enable the user by default

        User newUser = userRepository.save(user);
        person.setUser(newUser);
        personRepository.save(person);

        LOGGER.info("new user and person persisted");
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

    public RegistrationResponse registerUser(final UserDto userDto) {
        Map<String, String> errors = new HashMap<>();

        Optional<User> byEmail = findByEmail(userDto.email());
        if(byEmail.isPresent()){
            errors.put("error", "Email already Exists");
            return new RegistrationResponse(false, "Registration Failed", ErrorUtil.mapToObjectErrorList(errors));
        }


        try {
            LOGGER.info("registerUser(...)");
            Person person = new Person();
            person.setFirstName(userDto.firstName());
            person.setLastName(userDto.lastName());
            person.setMiddleName(userDto.middleName());
            person.setPersonType(PersonType.valueOf(userDto.personType()));

            Email email = new Email();
            email.setEmailAddress(userDto.email());
            email.setType(EmailType.PERSONAL);

            LOGGER.info(person.getPersonType().name());
            //persist new person first.
            Person savedPerson = personRepository.save(person);
            email.setPerson(savedPerson);
            emailRepository.save(email);


            Set<Role> roles = new HashSet<>();
            roles.add(createRole("USER"));

            createUser(userDto.email(), userDto.password(), person, roles);

            return new RegistrationResponse(true,"User Account successfully created");
        } catch (IllegalArgumentException e) {
            errors.put("system", "An unexpected error occurred during registration");
            return new RegistrationResponse(false,"Registration Failed", ErrorUtil.mapToObjectErrorList(errors));
        }
    }

    public Person findPersonByEmailUser(final String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if(byEmail.isPresent()){
            return byEmail.get().getPerson();
        } else {
            throw new PersonDoesNotExistException();
        }
    }
}
