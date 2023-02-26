package ru.nsu.backend.person.depersonalised;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.nsu.backend.person.Person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DepersonalisedPerson extends Person {
    public static final int PARAMS_COUNT = 15;

    @Id
    @SequenceGenerator(
            name = "person_sequence",
            sequenceName = "person_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "person_sequence"
    )
    private int id;
    private String sur; //фамилия
    private String first; //имя
    private String patronymic; //отчество
    private int age; //возраст
    private char sex; //пол
    private LocalDate dob; //дата рождения
    private String series; //серия паспорта
    private String number; //номер паспорта
    private String whereIssued; //где был выдан паспорт
    private LocalDate whenIssued; //когда был выдан паспорт
    private String registration; //регистрация
    private String work; //работа
    private String tin; // taxpayer identification number ИНН
    private String snils; //снилс
}
