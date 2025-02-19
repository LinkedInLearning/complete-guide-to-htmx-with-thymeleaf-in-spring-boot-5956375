package com.mdrsolutions.records_management.service;

import com.mdrsolutions.records_management.PersonDoesNotExistException;
import com.mdrsolutions.records_management.controller.dto.EmailDto;
import com.mdrsolutions.records_management.controller.dto.PersonDto;
import com.mdrsolutions.records_management.entity.Email;
import com.mdrsolutions.records_management.entity.Person;
import com.mdrsolutions.records_management.repository.PersonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getPersonById(final Long personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            return person.get();
        } else {
            throw new PersonDoesNotExistException("Person with ID " + personId + " does not exist");
        }
    }

    public void savePerson(Person existingPerson) {
        personRepository.save(existingPerson);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    // Added for lesson 06-05
    public List<PersonDto> findAllPersonDtoList() {
        //grabs all records - these were loaded from DataLoader.java - fake persons
        List<Person> all = findAll();
        List<PersonDto> dtoList = new ArrayList<>();

        for (Person person : all) {
            // Safely handle userId
            Long userId = (person.getUser() != null)
                    ? person.getUser().getUserId()
                    : null; // or 0L, if you prefer

            // If you only want one email, pick the first:
            dtoList.add(mapToDto(person));
        }

        return dtoList;
    }

    // Added for lesson 06-05
    public Page<PersonDto> findPaginated(Pageable pageable) {
        //grabs from findAlPersonDtoList
        List<PersonDto> all = findAllPersonDtoList();
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<PersonDto> list;
        if (all.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, all.size());
            list = all.subList(startItem, toIndex);
        }

        // Use 'list' for the content, not 'all'
        return new PageImpl<>(
                list,
                PageRequest.of(currentPage, pageSize),
                all.size()
        );
    }

    //added 06-06
    public List<PersonDto> searchByNameOrEmail(String searchTerm) {
        List<Person> persons = personRepository.searchByNameOrEmail(searchTerm);

        return persons.stream()
                .map(this::mapToDto)
                .toList();
    }

    //added 06-06
    private PersonDto mapToDto(Person person) {
        EmailDto emailDto =
                person.getEmails().isEmpty()
                        ? new EmailDto(null, "", null, person.getPersonId())
                        : getFirstEmail(person.getEmails());

        Long userId = null;
        if (person.getUser() != null) {
            userId = person.getUser().getUserId();
        }
        PersonDto dto = new PersonDto(
                person.getPersonId(),
                person.getEmploymentStatus(),
                person.getPrefix(),
                person.getFirstName(),
                person.getMiddleName(),
                person.getLastName(),
                person.getSuffix(),
                person.getPersonType(),
                person.getLegalGuardianType(),
                userId,
                emailDto
        );
        return dto;
    }

    //moved in 06-06
    private EmailDto getFirstEmail(Set<Email> emails) {
        for (Email email : emails) {
            //returning first email regardless...I know this is a hack
            return new EmailDto(
                    email.getEmailId(),
                    email.getEmailAddress(),
                    email.getType(),
                    email.getPerson().getPersonId()
            );
        }
        return new EmailDto(null, "", null, null);
    }

}
