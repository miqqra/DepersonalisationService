package ru.nsu.backend.person.initial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<InitialPerson, Integer> {
    Optional<InitialPerson> findBySur(String sur);

    Optional<InitialPerson> findByFirst(String first);

    Optional<InitialPerson> findByPatronymic(String patronymic);

    Optional<InitialPerson> findByAge(int age);

    Optional<InitialPerson> findByDob(LocalDate dob);

    Optional<InitialPerson> findBySeries(String series);

    Optional<InitialPerson> findByNumber(String number);

    Optional<InitialPerson> findByWhereIssued(String whereIssued);

    Optional<InitialPerson> findByWhenIssued(LocalDate whenIssued);

    Optional<InitialPerson> findByRegistration(String registration);

    Optional<InitialPerson> findByWork(String work);

    Optional<InitialPerson> findByTin(String tin);

    Optional<InitialPerson> findBySnils(String snils);

    List<InitialPerson> findAllBySur(String sur);

    List<InitialPerson> findAllByFirst(String first);

    List<InitialPerson> findAllByPatronymic(String patronymic);

    List<InitialPerson> findAllByAge(int age);

    List<InitialPerson> findAllByDob(LocalDate dob);

    List<InitialPerson> findAllBySeries(String series);

    List<InitialPerson> findAllByNumber(String number);

    List<InitialPerson> findAllByWhereIssued(String whereIssued);

    List<InitialPerson> findAllByWhenIssued(LocalDate whenIssued);

    List<InitialPerson> findAllByRegistration(String registration);

    List<InitialPerson> findAllByWork(String work);

    List<InitialPerson> findAllByTin(String tin);

    List<InitialPerson> findAllBySnils(String snils);

    Optional<InitialPerson>
    findBySurAndFirstAndPatronymicAndAgeAndSexAndDobAndSeriesAndNumberAndWhenIssuedAndWhereIssuedAndRegistrationAndWorkAndTinAndSnils(
            String sur, String first, String patronymic, int age,
            char sex, LocalDate dob, String series, String number,
            LocalDate whenIssued, String whereIssued,
            String registration, String work, String tin, String snils);
}
