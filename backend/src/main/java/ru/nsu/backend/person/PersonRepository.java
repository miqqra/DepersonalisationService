package ru.nsu.backend.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findBySur(String sur);

    Optional<Person> findByFirst(String first);

    Optional<Person> findByPatronymic(String patronymic);

    Optional<Person> findByAge(int age);

    Optional<Person> findByDob(LocalDate dob);

    Optional<Person> findBySeries(String series);

    Optional<Person> findByNumber(String number);

    Optional<Person> findByWhereIssued(String whereIssued);

    Optional<Person> findByWhenIssued(LocalDate whenIssued);

    Optional<Person> findByRegistration(String registration);

    Optional<Person> findByWork(String work);

    Optional<Person> findByTin(String tin);

    Optional<Person> findBySnils(String snils);

    Optional<Person>
    findBySurAndFirstAndPatronymicAndAgeAndSexAndDobAndSeriesAndNumberAndWhenIssuedAndWhereIssuedAndRegistrationAndWorkAndTinAndSnils(
            String sur, String first, String patronymic, int age,
            char sex, LocalDate dob, String series, String number,
            LocalDate whenIssued, String whereIssued,
            String registration, String work, String tin, String snils);
}
