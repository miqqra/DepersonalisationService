package ru.nsu.backend.person.initial;

import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.backend.person.Person;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<InitialPerson> getPeople() {
        Comparator<InitialPerson> comp = Comparator.comparing(InitialPerson::getId);
        return personRepository.findAll().stream().sorted(comp).limit(Person.MAX_PEOPLE_COUNT).toList();
    }

    @Transactional
    public void addNewPerson(String name) {
        InitialPerson person = new InitialPerson(0, name, "", "", 0, 'M',
                LocalDate.of(0, 1, 1), "", "", "",
                LocalDate.of(0, 1, 1), "", "", "", "");
        personRepository.save(person);
    }

    @Transactional
    public boolean addNewPerson(InitialPerson person) {
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
        InitialPerson person = personRepository.findById(personId).orElse(null);
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
    public void updateInfo(Integer personId, InitialPerson person) {
        InitialPerson oldPerson = personRepository.findById(personId).orElseThrow(() -> {
            throw new IllegalIdentifierException("Wrong id");
        });
        oldPerson.setSur(person.getSur());
        oldPerson.setFirst(person.getFirst());
        oldPerson.setPatronymic(person.getPatronymic());
        oldPerson.setAge(person.getAge());
        oldPerson.setSex(person.getSex());
        oldPerson.setDob(person.getDob());
        oldPerson.setSeries(person.getSeries());
        oldPerson.setNumber(person.getNumber());
        oldPerson.setWhereIssued(person.getWhereIssued());
        oldPerson.setWhenIssued(person.getWhenIssued());
        oldPerson.setRegistration(person.getRegistration());
        oldPerson.setWork(person.getWork());
        oldPerson.setTin(person.getTin());
        oldPerson.setSnils(person.getSnils());
    }

    public List<InitialPerson> sortTable(String param, SortingType sortingType) {
        List<InitialPerson> people = personRepository.findAll();
        Comparator<InitialPerson> comp = switch (param) {
            case ("name") -> Comparator.comparing(InitialPerson::getFirst);
            case ("surname") -> Comparator.comparing(InitialPerson::getSur);
            case ("fatherName") -> Comparator.comparing(InitialPerson::getPatronymic);
            case ("age") -> Comparator.comparing(InitialPerson::getAge);
            case ("sex") -> Comparator.comparing(InitialPerson::getSex);
            case ("dateOfBirth") -> Comparator.comparing(InitialPerson::getDob);
            case ("passportSeries") -> Comparator.comparing(InitialPerson::getSeries);
            case ("passportNumber") -> Comparator.comparing(InitialPerson::getNumber);
            case ("wherePassportWasIssued") -> Comparator.comparing(InitialPerson::getWhereIssued);
            case ("whenPassportWasIssued") -> Comparator.comparing(InitialPerson::getWhenIssued);
            case ("registration") -> Comparator.comparing(InitialPerson::getRegistration);
            case ("work") -> Comparator.comparing(InitialPerson::getWork);
            case ("taxpayerIdentificationNumber") -> Comparator.comparing(InitialPerson::getTin);
            case ("snils") -> Comparator.comparing(InitialPerson::getSnils);
            default -> Comparator.comparing(InitialPerson::getId);
        };
        if (sortingType == SortingType.ASC) {
            return people.stream().limit(Person.MAX_PEOPLE_COUNT).sorted(comp).toList();
        } else {
            return people.stream().limit(Person.MAX_PEOPLE_COUNT).sorted(comp.reversed()).toList();
        }
    }

    public List<InitialPerson> findPerson(String param){
        List<InitialPerson> people = personRepository.findAll();
        HashSet<InitialPerson> result = new HashSet<>();
        result.addAll(people.stream().filter(person -> person.getSur().toLowerCase().contains(param)).toList());
        result.addAll(people.stream().filter(person -> person.getFirst().toLowerCase().contains(param)).toList());
        result.addAll(people.stream().filter(person -> person.getPatronymic().toLowerCase().contains(param)).toList());
        result.addAll(people.stream().filter(person -> person.getWhereIssued().toLowerCase().contains(param)).toList());
        result.addAll(people.stream().filter(person -> person.getRegistration().toLowerCase().contains(param)).toList());
        result.addAll(people.stream().filter(person -> person.getWork().toLowerCase().contains(param)).toList());
        return result.stream().limit(Person.MAX_PEOPLE_COUNT).toList();
    }

    public List<InitialPerson> findPerson(Integer param){
        HashSet<InitialPerson> people = new HashSet<>(personRepository.findAllByAge(param));
        people.addAll(personRepository.findAllByNumber(param.toString()));
        people.addAll(personRepository.findAllBySeries(param.toString()));
        people.addAll(personRepository.findAllByTin(param.toString()));
        people.addAll(personRepository.findAllBySnils(param.toString()));
        return people.stream().limit(Person.MAX_PEOPLE_COUNT).toList();
    }

    public List<InitialPerson> findPerson(LocalDate param){
        HashSet<InitialPerson> people = new HashSet<>(personRepository.findAllByWhenIssued(param));
        people.addAll(personRepository.findAllByDob(param));
        return people.stream().limit(Person.MAX_PEOPLE_COUNT).toList();
    }

    public List<InitialPerson> getInitialData() {
        return personRepository.findAll().stream().limit(Person.MAX_PEOPLE_COUNT).toList();
    }
}
