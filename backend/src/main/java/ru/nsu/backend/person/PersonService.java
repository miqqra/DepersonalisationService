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

    public List<Person> depersonalisePeople(){
        List<Person> newTable = new Depersonalisation(personRepository.findAll()).depersonalise();
        personRepository.deleteAll();
        personRepository.saveAll(newTable);
        return newTable;
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
            if (name != null && !name.isEmpty() && !name.isBlank()) {
                person.setFirst(name);
            }
            if (surname != null && !surname.isEmpty() && !surname.isBlank()) {
                person.setSur(surname);
            }
            if (fatherName != null && !fatherName.isEmpty() && !fatherName.isBlank()) {
                person.setPatronymic(fatherName);
            }
            if (age != null && age > 0) {
                person.setAge(age);
            }
            if (sex != null && (Character.toLowerCase(sex) == 'M' || Character.toLowerCase(sex) == 'F')) {
                person.setSex(sex);
            }
            if (dateOfBirth != null) {
                try {
                    LocalDate newDob = LocalDate.parse(dateOfBirth);
                    person.setDob(newDob);
                } catch (DateTimeParseException ignored) {
                }
            }
            if (passportSeries != null && !passportSeries.isEmpty() && !passportSeries.isBlank()) {
                person.setSeries(passportSeries);
            }
            if (passportNumber != null && !passportNumber.isEmpty() && !passportNumber.isBlank()) {
                person.setNumber(passportNumber);
            }
            if (wherePassportWasIssued != null &&
                    !wherePassportWasIssued.isEmpty() &&
                    !wherePassportWasIssued.isBlank()) {
                person.setWhereIssued(wherePassportWasIssued);
            }
            if (whenPassportWasIssued != null) {
                try {
                    LocalDate newDate = LocalDate.parse(whenPassportWasIssued);
                    person.setWhenIssued(newDate);
                } catch (DateTimeParseException ignored) {
                }
            }
            if (registration != null && !registration.isEmpty() && !registration.isBlank()) {
                person.setRegistration(registration);
            }
            if (work != null && work.length() > 0) {
                person.setWork(work);
            }
            if (taxpayerIdentificationNumber != null &&
                    !taxpayerIdentificationNumber.isEmpty() &&
                    !taxpayerIdentificationNumber.isBlank()) {
                person.setTin(taxpayerIdentificationNumber);
            }
            if (snils != null && !snils.isEmpty() && !snils.isBlank()) {
                person.setSnils(snils);
            }
        }
    }
}
