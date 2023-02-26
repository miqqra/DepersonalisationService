package ru.nsu.backend.person.initial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    Optional<InitialPerson>
    findBySurAndFirstAndPatronymicAndAgeAndSexAndDobAndSeriesAndNumberAndWhenIssuedAndWhereIssuedAndRegistrationAndWorkAndTinAndSnils(
            String sur, String first, String patronymic, int age,
            char sex, LocalDate dob, String series, String number,
            LocalDate whenIssued, String whereIssued,
            String registration, String work, String tin, String snils);
}
