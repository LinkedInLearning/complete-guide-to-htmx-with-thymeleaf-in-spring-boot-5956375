package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.entity.Email;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.repository.EmailRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmailService {
    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public Optional<Email> getEmailById(Long emailId) {
        return emailRepository.findById(emailId);
    }

    public void saveOrUpdateEmail(Person person, Email email) {
        email.setPerson(person);
        emailRepository.save(email);
    }

    public void deleteEmail(Long emailId) {
        emailRepository.deleteById(emailId);
    }

    public boolean emailExistsForPerson(String emailAddress, Person person) {
        Optional<Email> optionalEmail = emailRepository.findByEmailAddress(emailAddress);
        if(optionalEmail.isPresent()){
            Email email = optionalEmail.get();
            return email.getPerson().getPersonId().equals(person.getPersonId());
        }
        return false;
    }
}
