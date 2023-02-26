package ru.nsu.backend.person.initial;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ru.nsu.backend.person.depersonalised.DepersonalisedPerson;

class DepersonalisationTest {

  @Test
  public void matrixTest() {
    List<InitialPerson> people = new ArrayList<>();
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));

    Depersonalisation depersonalisation = new Depersonalisation(people);
    var matrix = depersonalisation.createMatrix();
    assertEquals(matrix.nofSubsets().size(), InitialPerson.PARAMS_COUNT);

    for (int i = 0; i < matrix.nofElements().length; i++) {
      assertEquals(matrix.nofElements()[i].size(), matrix.nofSubsets().get(i));
      int sum = 0;
      for (var k : matrix.nofElements()[i]) {
        sum += k;
      }
      assertEquals(sum, people.size());
    }
  }

  @Test
  public void mapTest() {
    @SuppressWarnings("unchecked") Depersonalisation.MatrixOfParameters matrix = new Depersonalisation.MatrixOfParameters(
        Arrays.asList(3, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
        new List[]{
            Arrays.asList(3, 2, 3),

            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
            List.of(8),
        },
        Arrays.asList(
            new Depersonalisation.SubsetPermutationParameters(2, new int[]{2, 1, 1}),

            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4}),
            new Depersonalisation.SubsetPermutationParameters(1, new int[]{4})
        )
    );
    List<InitialPerson> people = new ArrayList<>();
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));

    Depersonalisation depersonalisation = new Depersonalisation(people);
    var map = depersonalisation.createMapList(matrix);
    var check = map[0];
    int[] expected = new int[]{4, 3, 7, 5, 6, 1, 2, 0};
    int[] actual = new int[8];
    for (int i = 0; i < check.length; i++) {
      actual[i] = check[i].oldPos();
    }
    assertEquals(Arrays.toString(expected), Arrays.toString(actual));
  }

  @Test
  public void permuteTest() {
    List<InitialPerson> people = new ArrayList<>();
    people.add(new InitialPerson(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new InitialPerson(1, "sur1", "first1", "pat1", 1, '1', LocalDate.now(), "ser1", "num1", "where1", LocalDate.now(), "reg1", "work1", "tin1", "snils1"));
    people.add(new InitialPerson(2, "sur2", "first2", "pat2", 2, '2', LocalDate.now(), "ser2", "num2", "where2", LocalDate.now(), "reg2", "work2", "tin2", "snils2"));
    people.add(new InitialPerson(3, "sur3", "first3", "pat3", 3, '3', LocalDate.now(), "ser3", "num3", "where3", LocalDate.now(), "reg3", "work3", "tin3", "snils3"));
    people.add(new InitialPerson(4, "sur4", "first4", "pat4", 4, '4', LocalDate.now(), "ser4", "num4", "where4", LocalDate.now(), "reg4", "work4", "tin4", "snils4"));

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
    Depersonalisation.MapToNewPosition[][] map = new Depersonalisation.MapToNewPosition[InitialPerson.PARAMS_COUNT][5];
    for (int i = 0; i < InitialPerson.PARAMS_COUNT; i++) {
      for (int j = 0; j < 5; j++) {
        map[i][j] = new Depersonalisation.MapToNewPosition(j, arr[i][j]);
      }
    }

    List<DepersonalisedPerson> result = depersonalisation.permute(map);

    // asserts
    List<InitialPerson> expected = new ArrayList<>();
    expected.add(new InitialPerson(2, "sur1", "first0", "pat3", 2, '2', LocalDate.now(), "ser2", "num2", "where2", LocalDate.now(), "reg4", "work2", "tin4", "snils0"));
    expected.add(new InitialPerson(1, "sur4", "first1", "pat1", 0, '1', LocalDate.now(), "ser1", "num1", "where3", LocalDate.now(), "reg3", "work1", "tin0", "snils1"));
    expected.add(new InitialPerson(0, "sur0", "first3", "pat0", 1, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg2", "work0", "tin1", "snils2"));
    expected.add(new InitialPerson(3, "sur2", "first4", "pat2", 3, '3', LocalDate.now(), "ser3", "num3", "where4", LocalDate.now(), "reg1", "work3", "tin2", "snils4"));
    expected.add(new InitialPerson(4, "sur3", "first2", "pat4", 4, '4', LocalDate.now(), "ser4", "num4", "where1", LocalDate.now(), "reg0", "work4", "tin3", "snils3"));

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