package ru.nsu.backend.person.depersonalised;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DepersonalisedPersonRepository extends JpaRepository<DepersonalisedPerson, Integer> {
    Optional<DepersonalisedPerson> findBySur(String sur);

    Optional<DepersonalisedPerson> findByFirst(String first);

    Optional<DepersonalisedPerson> findByPatronymic(String patronymic);

    Optional<DepersonalisedPerson> findByAge(int age);

    Optional<DepersonalisedPerson> findByDob(LocalDate dob);

    Optional<DepersonalisedPerson> findBySeries(String series);

    Optional<DepersonalisedPerson> findByNumber(String number);

    Optional<DepersonalisedPerson> findByWhereIssued(String whereIssued);

    Optional<DepersonalisedPerson> findByWhenIssued(LocalDate whenIssued);

    Optional<DepersonalisedPerson> findByRegistration(String registration);

    Optional<DepersonalisedPerson> findByWork(String work);

    Optional<DepersonalisedPerson> findByTin(String tin);

    Optional<DepersonalisedPerson> findBySnils(String snils);

    List<DepersonalisedPerson> findAllBySur(String sur);

    List<DepersonalisedPerson> findAllByFirst(String first);

    List<DepersonalisedPerson> findAllByPatronymic(String patronymic);

    List<DepersonalisedPerson> findAllByAge(int age);

    List<DepersonalisedPerson> findAllByDob(LocalDate dob);

    List<DepersonalisedPerson> findAllBySeries(String series);

    List<DepersonalisedPerson> findAllByNumber(String number);

    List<DepersonalisedPerson> findAllByWhereIssued(String whereIssued);

    List<DepersonalisedPerson> findAllByWhenIssued(LocalDate whenIssued);

    List<DepersonalisedPerson> findAllByRegistration(String registration);

    List<DepersonalisedPerson> findAllByWork(String work);

    List<DepersonalisedPerson> findAllByTin(String tin);

    List<DepersonalisedPerson> findAllBySnils(String snils);

    Optional<DepersonalisedPerson>
    findBySurAndFirstAndPatronymicAndAgeAndSexAndDobAndSeriesAndNumberAndWhenIssuedAndWhereIssuedAndRegistrationAndWorkAndTinAndSnils(
            String sur, String first, String patronymic, int age,
            char sex, LocalDate dob, String series, String number,
            LocalDate whenIssued, String whereIssued,
            String registration, String work, String tin, String snils);
}
