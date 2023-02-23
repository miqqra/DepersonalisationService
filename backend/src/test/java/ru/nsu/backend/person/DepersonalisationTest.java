package ru.nsu.backend.person;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DepersonalisationTest {

  @Test
  public void permuteTest() {
    List<Person> people = new ArrayList<>();
    people.add(new Person(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new Person(1, "sur1", "first1", "pat1", 1, '1', LocalDate.now(), "ser1", "num1", "where1", LocalDate.now(), "reg1", "work1", "tin1", "snils1"));
    people.add(new Person(2, "sur2", "first2", "pat2", 2, '2', LocalDate.now(), "ser2", "num2", "where2", LocalDate.now(), "reg2", "work2", "tin2", "snils2"));
    people.add(new Person(3, "sur3", "first3", "pat3", 3, '3', LocalDate.now(), "ser3", "num3", "where3", LocalDate.now(), "reg3", "work3", "tin3", "snils3"));
    people.add(new Person(4, "sur4", "first4", "pat4", 4, '4', LocalDate.now(), "ser4", "num4", "where4", LocalDate.now(), "reg4", "work4", "tin4", "snils4"));

    Depersonalisation depersonalisation = new Depersonalisation(people);
    int[][] arr = new int[][]{
        {2, 1, 0, 3, 4},
        {2, 0, 3, 4, 1},
        {0, 1, 4, 2, 3},
        {2, 1, 3, 0, 4},
        {1, 2, 0, 3, 4},
        {2, 1, 0, 3, 4},
        {2, 1, 0, 3, 4},
        {2, 1, 0, 3, 4},
        {2, 1, 0, 3, 4},
        {2, 4, 0, 1, 3},
        {2, 1, 0, 3, 4},
        {4, 3, 2, 1, 0},
        {2, 1, 0, 3, 4},
        {1, 2, 3, 4, 0},
        {0, 1, 2, 4, 3}
    };
    Depersonalisation.MapToNewPosition[][] map = new Depersonalisation.MapToNewPosition[Person.PARAMS_COUNT][5];
    for (int i = 0; i < Person.PARAMS_COUNT; i++) {
      for (int j = 0; j < 5; j++) {
        map[i][j] = new Depersonalisation.MapToNewPosition(j, arr[i][j]);
      }
    }

    List<Person> result = depersonalisation.permute(map);

    // asserts
    List<Person> expected = new ArrayList<>();
    expected.add(new Person(2, "sur1", "first0", "pat3", 2, '2', LocalDate.now(), "ser2", "num2", "where2", LocalDate.now(), "reg4", "work2", "tin4", "snils0"));
    expected.add(new Person(1, "sur4", "first1", "pat1", 0, '1', LocalDate.now(), "ser1", "num1", "where3", LocalDate.now(), "reg3", "work1", "tin0", "snils1"));
    expected.add(new Person(0, "sur0", "first3", "pat0", 1, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg2", "work0", "tin1", "snils2"));
    expected.add(new Person(3, "sur2", "first4", "pat2", 3, '3', LocalDate.now(), "ser3", "num3", "where4", LocalDate.now(), "reg1", "work3", "tin2", "snils4"));
    expected.add(new Person(4, "sur3", "first2", "pat4", 4, '4', LocalDate.now(), "ser4", "num4", "where1", LocalDate.now(), "reg0", "work4", "tin3", "snils3"));

    for (int i = 0; i < 5; i++) {
      var q = expected.get(i);
      var p = result.get(i);
      assertEquals(q, p);
    }

    // log to console
    for (var i : people) {
      System.out.println(i);
    }
    System.out.println("---------------------------------------");
    for (var i : result) {
      System.out.println(i);
    }
  }
}