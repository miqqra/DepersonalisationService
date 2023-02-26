package ru.nsu.backend.person.depersonalised;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.backend.person.initial.Depersonalisation;
import ru.nsu.backend.person.initial.InitialPerson;
import ru.nsu.backend.person.initial.SortingType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

@Service
public class DepersonalisedPersonService {
    private final DepersonalisedPersonRepository depersonalisedPersonRepository;

    @Autowired
    public DepersonalisedPersonService(DepersonalisedPersonRepository depersonalisedPersonRepository) {
        this.depersonalisedPersonRepository = depersonalisedPersonRepository;
    }

    public List<DepersonalisedPerson> getPeople() {
        return depersonalisedPersonRepository.findAll();
    }

    @Transactional
    public void addNewPerson(String name) {
        DepersonalisedPerson depersonalisedPerson = new DepersonalisedPerson();
        depersonalisedPerson.setFirst(name);
        depersonalisedPersonRepository.save(depersonalisedPerson);
    }

    @Transactional
    public boolean addNewPerson(DepersonalisedPerson depersonalisedPerson) {
        if (depersonalisedPersonRepository.existsById(depersonalisedPerson.getId()) || depersonalisedPersonRepository
                .findBySurAndFirstAndPatronymicAndAgeAndSexAndDobAndSeriesAndNumberAndWhenIssuedAndWhereIssuedAndRegistrationAndWorkAndTinAndSnils(
                        depersonalisedPerson.getSur(), depersonalisedPerson.getFirst(),
                        depersonalisedPerson.getPatronymic(), depersonalisedPerson.getAge(),
                        depersonalisedPerson.getSex(), depersonalisedPerson.getDob(),
                        depersonalisedPerson.getSeries(), depersonalisedPerson.getNumber(),
                        depersonalisedPerson.getWhenIssued(), depersonalisedPerson.getWhereIssued(),
                        depersonalisedPerson.getRegistration(), depersonalisedPerson.getWork(),
                        depersonalisedPerson.getTin(), depersonalisedPerson.getSnils()
                ).isPresent()) {
            return false;
        }
        depersonalisedPersonRepository.save(depersonalisedPerson);
        return true;
    }

    @Transactional
    public boolean deletePerson(Integer personId) {
        if (depersonalisedPersonRepository.existsById(personId)) {
            depersonalisedPersonRepository.deleteById(personId);
            return true;
        } else {
            return false;
        }
    }

    public List<DepersonalisedPerson> depersonalisePeople(List<InitialPerson> people) {
        List<DepersonalisedPerson> depersonalisedPeople = new Depersonalisation(people).depersonalise();
        depersonalisedPersonRepository.saveAll(depersonalisedPeople);
        return depersonalisedPeople;
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
        DepersonalisedPerson depersonalisedPerson = depersonalisedPersonRepository.findById(personId).orElse(null);
        if (depersonalisedPerson != null) {
            if (name != null && !name.isEmpty() && !name.isBlank()) {
                depersonalisedPerson.setFirst(name);
            }
            if (surname != null && !surname.isEmpty() && !surname.isBlank()) {
                depersonalisedPerson.setSur(surname);
            }
            if (fatherName != null && !fatherName.isEmpty() && !fatherName.isBlank()) {
                depersonalisedPerson.setPatronymic(fatherName);
            }
            if (age != null && age > 0) {
                depersonalisedPerson.setAge(age);
            }
            if (sex != null && (Character.toLowerCase(sex) == 'M' || Character.toLowerCase(sex) == 'F')) {
                depersonalisedPerson.setSex(sex);
            }
            if (dateOfBirth != null) {
                try {
                    LocalDate newDob = LocalDate.parse(dateOfBirth);
                    depersonalisedPerson.setDob(newDob);
                } catch (DateTimeParseException ignored) {
                }
            }
            if (passportSeries != null && !passportSeries.isEmpty() && !passportSeries.isBlank()) {
                depersonalisedPerson.setSeries(passportSeries);
            }
            if (passportNumber != null && !passportNumber.isEmpty() && !passportNumber.isBlank()) {
                depersonalisedPerson.setNumber(passportNumber);
            }
            if (wherePassportWasIssued != null &&
                    !wherePassportWasIssued.isEmpty() &&
                    !wherePassportWasIssued.isBlank()) {
                depersonalisedPerson.setWhereIssued(wherePassportWasIssued);
            }
            if (whenPassportWasIssued != null) {
                try {
                    LocalDate newDate = LocalDate.parse(whenPassportWasIssued);
                    depersonalisedPerson.setWhenIssued(newDate);
                } catch (DateTimeParseException ignored) {
                }
            }
            if (registration != null && !registration.isEmpty() && !registration.isBlank()) {
                depersonalisedPerson.setRegistration(registration);
            }
            if (work != null && work.length() > 0) {
                depersonalisedPerson.setWork(work);
            }
            if (taxpayerIdentificationNumber != null &&
                    !taxpayerIdentificationNumber.isEmpty() &&
                    !taxpayerIdentificationNumber.isBlank()) {
                depersonalisedPerson.setTin(taxpayerIdentificationNumber);
            }
            if (snils != null && !snils.isEmpty() && !snils.isBlank()) {
                depersonalisedPerson.setSnils(snils);
            }
            depersonalisedPersonRepository.save(depersonalisedPerson);
            return true;
        }
        return false;
    }

    @Transactional
    public void updateInfo(Integer personId, DepersonalisedPerson person) {
        person.setId(personId);
        DepersonalisedPerson newPerson = depersonalisedPersonRepository.findById(personId).orElse(person);
        depersonalisedPersonRepository.delete(newPerson);
        depersonalisedPersonRepository.save(person);
    }

    public List<DepersonalisedPerson> sortTable(String param, SortingType sortingType) {
        List<DepersonalisedPerson> people = depersonalisedPersonRepository.findAll();
        Comparator<DepersonalisedPerson> comp = switch (param) {
            case ("name") -> Comparator.comparing(DepersonalisedPerson::getFirst);
            case ("surname") -> Comparator.comparing(DepersonalisedPerson::getSur);
            case ("fatherName") -> Comparator.comparing(DepersonalisedPerson::getPatronymic);
            case ("age") -> Comparator.comparing(DepersonalisedPerson::getAge);
            case ("sex") -> Comparator.comparing(DepersonalisedPerson::getSex);
            case ("dateOfBirth") -> Comparator.comparing(DepersonalisedPerson::getDob);
            case ("passportSeries") -> Comparator.comparing(DepersonalisedPerson::getSeries);
            case ("passportNumber") -> Comparator.comparing(DepersonalisedPerson::getNumber);
            case ("wherePassportWasIssued") -> Comparator.comparing(DepersonalisedPerson::getWhereIssued);
            case ("whenPassportWasIssued") -> Comparator.comparing(DepersonalisedPerson::getWhenIssued);
            case ("registration") -> Comparator.comparing(DepersonalisedPerson::getRegistration);
            case ("work") -> Comparator.comparing(DepersonalisedPerson::getWork);
            case ("taxpayerIdentificationNumber") -> Comparator.comparing(DepersonalisedPerson::getTin);
            case ("snils") -> Comparator.comparing(DepersonalisedPerson::getSnils);
            default -> Comparator.comparing(DepersonalisedPerson::getId);
        };
        if (sortingType == SortingType.ASC) {
            people.sort(comp);
        } else {
            people.sort(comp.reversed());
        }
        return people;
    }

    public DepersonalisedPerson findPerson(String param) {
        return depersonalisedPersonRepository.findBySur(param).orElse(
                depersonalisedPersonRepository.findByFirst(param).orElse(
                        depersonalisedPersonRepository.findByPatronymic(param).orElse(
                                depersonalisedPersonRepository.findByWhereIssued(param).orElse(
                                        depersonalisedPersonRepository.findByRegistration(param).orElse(
                                                depersonalisedPersonRepository.findByWork(param).orElse(null))))));
    }

    public DepersonalisedPerson findPerson(Integer param) {
        return depersonalisedPersonRepository.findByAge(param).orElse(
                depersonalisedPersonRepository.findByNumber(param.toString()).orElse(
                        depersonalisedPersonRepository.findBySeries(param.toString()).orElse(
                                depersonalisedPersonRepository.findByTin(param.toString()).orElse(
                                        depersonalisedPersonRepository.findBySnils(param.toString()).orElse(null)
                                )
                        )
                )
        );
    }

    public DepersonalisedPerson findPerson(LocalDate param) {
        return depersonalisedPersonRepository.findByWhenIssued(param).orElse(
                depersonalisedPersonRepository.findByDob(param).orElse(null)
        );
    }
}
