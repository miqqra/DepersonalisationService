package ru.nsu.backend.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
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

    @Transactional
    public void addNewPerson(String name) {
        Person person = new Person();
        person.setFirst(name);
        personRepository.save(person);
    }

    @Transactional
    public boolean addNewPerson(Person person) {
        if (personRepository.existsById(person.getId()) || personRepository
                .findBySurAndFirstAndPatronymicAndAgeAndSexAndDobAndSeriesAndNumberAndWhenIssuedAndWhereIssuedAndRegistrationAndWorkAndTinAndSnils(
                        person.getSur(), person.getFirst(), person.getPatronymic(), person.getAge(),
                        person.getSex(), person.getDob(), person.getSeries(), person.getNumber(),
                        person.getWhenIssued(), person.getWhereIssued(), person.getRegistration(),
                        person.getWork(), person.getTin(), person.getSnils()
                ).isPresent()) {
            return false;
        }
        personRepository.save(person);
        return true;
    }

    @Transactional
    public boolean deletePerson(Integer personId) {
        if (personRepository.existsById(personId)) {
            personRepository.deleteById(personId);
            return true;
        } else {
            return false;
        }
    }

    public List<Person> depersonalisePeople() {
        return new Depersonalisation(personRepository.findAll()).depersonalise();
    }

    @Transactional
    public boolean updateInfo(Integer personId,
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
            personRepository.save(person);
            return true;
        }
        return false;
    }

    @Transactional
    public void updateInfo(Integer personId, Person person) {
        person.setId(personId);
        Person newPerson = personRepository.findById(personId).orElse(person);
        personRepository.delete(newPerson);
        personRepository.save(person);
    }

    public List<Person> sortTable(String param, SortingType sortingType) {
        List<Person> people = personRepository.findAll();
        Comparator<Person> comp = switch (param) {
            case ("name") -> Comparator.comparing(Person::getFirst);
            case ("surname") -> Comparator.comparing(Person::getSur);
            case ("fatherName") -> Comparator.comparing(Person::getPatronymic);
            case ("age") -> Comparator.comparing(Person::getAge);
            case ("sex") -> Comparator.comparing(Person::getSex);
            case ("dateOfBirth") -> Comparator.comparing(Person::getDob);
            case ("passportSeries") -> Comparator.comparing(Person::getSeries);
            case ("passportNumber") -> Comparator.comparing(Person::getNumber);
            case ("wherePassportWasIssued") -> Comparator.comparing(Person::getWhereIssued);
            case ("whenPassportWasIssued") -> Comparator.comparing(Person::getWhenIssued);
            case ("registration") -> Comparator.comparing(Person::getRegistration);
            case ("work") -> Comparator.comparing(Person::getWork);
            case ("taxpayerIdentificationNumber") -> Comparator.comparing(Person::getTin);
            case ("snils") -> Comparator.comparing(Person::getSnils);
            default -> Comparator.comparing(Person::getId);
        };
        if (sortingType == SortingType.ASC) {
            people.sort(comp);
        } else {
            people.sort(comp.reversed());
        }
        return people;
    }

    public Person findPerson(String param) {
        return personRepository.findBySur(param).orElse(
                personRepository.findByFirst(param).orElse(
                        personRepository.findByPatronymic(param).orElse(
                                personRepository.findByWhereIssued(param).orElse(
                                        personRepository.findByRegistration(param).orElse(
                                                personRepository.findByWork(param).orElse(null))))));
    }

    public Person findPerson(Integer param) {
        return personRepository.findByAge(param).orElse(
                personRepository.findByNumber(param.toString()).orElse(
                        personRepository.findBySeries(param.toString()).orElse(
                                personRepository.findByTin(param.toString()).orElse(
                                        personRepository.findBySnils(param.toString()).orElse(null)
                                )
                        )
                )
        );
    }

    public Person findPerson(LocalDate param) {
        return personRepository.findByWhenIssued(param).orElse(
                personRepository.findByDob(param).orElse(null)
        );
    }
}
