package ru.nsu.backend.person;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Person {
    public static final int PARAMS_COUNT = 15;

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
