package ru.nsu.backend.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    public void addNewPerson(Person person) {
        personRepository.save(person);
    }

    public void deletePerson(Integer personId) {
        if (personRepository.existsById(personId)) {
            personRepository.deleteById(personId);
        }
    }

    @Transactional
    public void updateInfo(Integer personId,
                           String name,
                           String surname,
                           String fatherName,
                           Integer age,
                           Character sex,
                           String dateOfBirth,
                           String passportSeries,
                           String passportNumber,
                           String wherePassportWasIssued,
                           String whenPassportWasIssued,
                           String registration,
                           String work,
                           String taxpayerIdentificationNumber,
                           String snils) {
        Person person = personRepository.findById(personId).orElse(null);
        if (person != null) {
            if (name != null && name.length() > 0) {
                person.setFirst(name);
            }
            if (surname != null && surname.length() > 0) {
                person.setSur(surname);
            }
            if (fatherName != null && fatherName.length() > 0) {
                person.setPatronymic(fatherName);
            }
            if (age != null && age > 0) {
                person.setAge(age);
            }
            if (sex != null && (sex == 'M' || sex == 'F')) {
                person.setSex(sex);
            }
            if (dateOfBirth != null) {
                try {
                    LocalDate newDob = LocalDate.parse(dateOfBirth);
                    person.setDob(newDob);
                } catch (DateTimeParseException ignored) {
                }
            }
            if (passportSeries != null && passportSeries.length() > 0) {
                person.setSeries(passportSeries);
            }
            if (passportNumber != null && passportNumber.length() > 0) {
                person.setNumber(passportNumber);
            }
            if (wherePassportWasIssued != null && wherePassportWasIssued.length() > 0) {
                person.setWhereIssued(wherePassportWasIssued);
            }
            if (whenPassportWasIssued != null) {
                try {
                    LocalDate newDate = LocalDate.parse(whenPassportWasIssued);
                    person.setWhenIssued(newDate);
                } catch (DateTimeParseException ignored) {
                }
            }
            if (registration != null && registration.length() > 0) {
                person.setRegistration(registration);
            }
            if (work != null && work.length() > 0) {
                person.setWork(work);
            }
            if (taxpayerIdentificationNumber != null && taxpayerIdentificationNumber.length() > 0) {
                person.setTin(taxpayerIdentificationNumber);
            }
            if (snils != null && snils.length() > 0) {
                person.setSnils(snils);
            }
        }
    }
}
