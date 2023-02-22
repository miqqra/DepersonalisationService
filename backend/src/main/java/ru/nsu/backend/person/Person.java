package ru.nsu.backend.person;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table
public class Person {
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

    public Person() {

    }

    public Person(int id,
                  String sur,
                  String first,
                  String patronymic,
                  int age,
                  char sex,
                  LocalDate dob,
                  String series,
                  String number,
                  String where,
                  LocalDate when,
                  String registration,
                  String work,
                  String tin,
                  String snils) {
        this.id = id;
        this.sur = sur;
        this.first = first;
        this.patronymic = patronymic;
        this.age = age;
        this.sex = sex;
        this.dob = dob;
        this.series = series;
        this.number = number;
        this.whereIssued = where;
        this.whenIssued = when;
        this.registration = registration;
        this.work = work;
        this.tin = tin;
        this.snils = snils;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSur() {
        return sur;
    }

    public void setSur(String sur) {
        this.sur = sur;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String passportNumber) {
        this.number = passportNumber;
    }

    public String getWhereIssued() {
        return whereIssued;
    }

    public void setWhereIssued(String whereIssued) {
        this.whereIssued = whereIssued;
    }

    public LocalDate getWhenIssued() {
        return whenIssued;
    }

    public void setWhenIssued(LocalDate whenIssued) {
        this.whenIssued = whenIssued;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "\nid=" + id +
                ", \nsurname='" + sur + '\'' +
                ", \nfirstName='" + first + '\'' +
                ", \npatronymic='" + patronymic + '\'' +
                ", \nage=" + age +
                ", \nsex=" + sex +
                ", \ndateOfBirth=" + dob +
                ", \npassportSeries='" + series + '\'' +
                ", \npassportNumber='" + number + '\'' +
                ", \npassportNumber='" + whereIssued + '\'' +
                ", \npassportNumber='" + whenIssued + '\'' +
                ", \nregistration='" + registration + '\'' +
                ", \nwork='" + work + '\'' +
                ", \ntin='" + tin + '\'' +
                ", \nsnils='" + snils + '\'' +
                "\n}";
    }
}

