package ru.nsu.backend.person;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
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
@JacksonXmlRootElement(localName = "person")
public class Person {
    public static final int MAX_PEOPLE_COUNT = 50;
    public static final int PARAMS_COUNT = 15;

    @JsonProperty
    private int id;
    @JsonProperty
    private String sur; //фамилия
    @JsonProperty
    private String first; //имя
    @JsonProperty
    private String patronymic; //отчество
    @JsonProperty
    private int age; //возраст
    @JsonProperty
    private char sex; //пол
    @JsonProperty
    private LocalDate dob; //дата рождения
    @JsonProperty
    private String series; //серия паспорта
    @JsonProperty
    private String number; //номер паспорта
    @JsonProperty
    private String whereIssued; //где был выдан паспорт
    @JsonProperty
    private LocalDate whenIssued; //когда был выдан паспорт
    @JsonProperty
    private String registration; //регистрация
    @JsonProperty
    private String work; //работа
    @JsonProperty
    private String tin; // taxpayer identification number ИНН
    @JsonProperty
    private String snils; //снилс
}
