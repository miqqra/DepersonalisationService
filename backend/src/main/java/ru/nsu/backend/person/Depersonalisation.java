package ru.nsu.backend.person;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Depersonalisation {

  private final List<Person> sourceData;

  public Depersonalisation(List<Person> sourceData) {
    this.sourceData = List.copyOf(sourceData);
  }

  public List<Person> depersonalise() {
    MatrixOfParameters matrix = createMatrix();
    System.out.println(matrix);
    MapToNewPosition[][] map = createMapList(matrix);
    System.out.println(Arrays.deepToString(map));
    return permute(map);
  }

  /**
   * A convenient form for cycle shift for the column.
   *
   * @param r0 cycle shift for subsets
   * @param rv a vector of cycle shifts inside the subsets
   */
  record SubsetPermutationParameters(int r0, int[] rv) {
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
      return "SubsetPermutationParameters{" +
          "r0=" + r0 +
          ", rs=" + Arrays.toString(rv) +
          "}\n";
    }
  }

  /**
   * The matrix sets a division of the data into several subsets for each column,
   * where `nofSubsets` contains a quantity of subsets for each column and
   * `nofElements` contains a number of elements for each corresponding subset.
   *
   * The number r0 and the vector rv are associated with each of column.
   *
   * @param nofSubsets a list of subsets number for each of column
   * @param nofElements a list of subset sizes
   * @param permutations for each column there is a SubsetPermutationParameters record
   */
  record MatrixOfParameters(List<Integer> nofSubsets,
                            List<Integer>[] nofElements,
                            List<SubsetPermutationParameters> permutations) {
    @Override
    public @NotNull String toString() {
      StringBuilder nofElementsString = new StringBuilder();
      for (var v : nofElements) {
        nofElementsString.append(Arrays.toString(v.toArray()));
        nofElementsString.append("\n");
      }
      return "MatrixOfParameters{" +
          "nofSubsets=" + Arrays.toString(nofSubsets.toArray()) +
          ", nofElements=" + nofElementsString +
          ", permutations=" + Arrays.toString(permutations.toArray()) +
          '}';
    }
  }

  record MapToNewPosition(int oldPos, int newPos) {
  }

  /**
   * Creates a new MatrixOfParameters variable
   *
   * @return matrix of permutations
   */
  MatrixOfParameters createMatrix() {
    int N = Person.PARAMS_COUNT;
    int M = sourceData.size();

    List<Integer> numberOfSubsets = new ArrayList<>();

    @SuppressWarnings("unchecked")
    List<Integer>[] numberOfElements = new ArrayList[N];

    Random random = new Random();

    // generation of a subsets' quantity and a quantity of their elements
    for (int i = 0; i < N; i++) {

      numberOfElements[i] = new ArrayList<>();

      numberOfElements[i].add(random.nextInt(2, (M + 1) / 2));

      int remains = M - numberOfElements[i].get(0);
      while (remains != 0) {

        int nofElements = random.nextInt(2, remains + 1);

        if (remains - nofElements == 1) {
          nofElements++;
        }
        numberOfElements[i].add(nofElements);
        remains -= nofElements;
      }
      numberOfSubsets.add(numberOfElements[i].size());
    }

    // generation numbers of cycle shift
    // shift(2, [1,2,3,4,5]) = [4, 5, 1, 2, 3]
    List<SubsetPermutationParameters> permutations = new ArrayList<>();

    for (int i = 0; i < N; i++) {
      int[] shiftVector = new int[numberOfSubsets.get(i)];
      for (int j = 0; j < shiftVector.length; j++) {
        shiftVector[j] = random.nextInt(1, numberOfElements[i].get(j));
      }
      permutations.add(new SubsetPermutationParameters(
          random.nextInt(1, numberOfSubsets.get(i)),
          shiftVector
      ));
    }

    return new MatrixOfParameters(numberOfSubsets, numberOfElements, permutations);
  }

  MapToNewPosition[][] createMapList(MatrixOfParameters matrix) {
    int N = Person.PARAMS_COUNT;
    int M = sourceData.size();

    // Для каждой колонки имеем отображение из чисел 0, 1,.. , M - 2, M - 1
    // в новые позиции a1, a2, .. , aM-1, где aj <- [0; M-1]
    //
    // В каждой i-й колонке, где 0 <= i < N, содержится массив из M элементов MapToNewPosition
    // таких, что result[i][j].oldPos = j, а result[i][j].newPos = aj,
    // oldPos - старая позиция, newPos - новая позиция после перестановки.
    MapToNewPosition[][] result = new MapToNewPosition[N][M];

    for(int column = 0; column < N; column++) {

      int subsetCount = matrix.nofSubsets.get(column);
      int r0 = matrix.permutations.get(column).r0;
      int[] rv = matrix.permutations.get(column).rv;

      int[] subsetPositions = new int[subsetCount];
      for(int pos = 0, i = 0; i < subsetCount; pos += matrix.nofElements[column].get(i), i++) {
        subsetPositions[i] = pos;
      }

      for(
              int row = 0, subset = subsetCount - r0;
              row == 0 || subset != subsetCount - r0;
              subset = (subset + 1) % subsetCount
      ) {
        int numCount = matrix.nofElements[column].get(subset);
        for(int k = 0; k < numCount; k++, row++) {
          int oldPos = subsetPositions[subset] + k;
          int newPos = ( k + rv[subset] ) % numCount + row - k;
          result[column][newPos] = new MapToNewPosition(oldPos, newPos);
        }
      }

    }

    return result;
  }

  List<Person> permute(MapToNewPosition[][] map) {
    int N = Person.PARAMS_COUNT;
    int M = sourceData.size();

    List<Person> result = new ArrayList<>(M);
    for (int i = 0; i < M; i++) {
      result.add(new Person());
    }

    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        switch (i) {
          case 0 -> result.get(map[i][j].newPos).setId(
              sourceData.get(map[i][j].oldPos).getId()
          );
          case 1 -> result.get(map[i][j].newPos).setSur(
              sourceData.get(map[i][j].oldPos).getSur()
          );
          case 2 -> result.get(map[i][j].newPos).setFirst(
              sourceData.get(map[i][j].oldPos).getFirst()
          );
          case 3 -> result.get(map[i][j].newPos).setPatronymic(
              sourceData.get(map[i][j].oldPos).getPatronymic()
          );
          case 4 -> result.get(map[i][j].newPos).setAge(
              sourceData.get(map[i][j].oldPos).getAge()
          );
          case 5 -> result.get(map[i][j].newPos).setSex(
              sourceData.get(map[i][j].oldPos).getSex()
          );
          case 6 -> result.get(map[i][j].newPos).setDob(
              sourceData.get(map[i][j].oldPos).getDob()
          );
          case 7 -> result.get(map[i][j].newPos).setSeries(
              sourceData.get(map[i][j].oldPos).getSeries()
          );
          case 8 -> result.get(map[i][j].newPos).setNumber(
              sourceData.get(map[i][j].oldPos).getNumber()
          );
          case 9 -> result.get(map[i][j].newPos).setWhereIssued(
              sourceData.get(map[i][j].oldPos).getWhereIssued()
          );
          case 10 -> result.get(map[i][j].newPos).setWhenIssued(
              sourceData.get(map[i][j].oldPos).getWhenIssued()
          );
          case 11 -> result.get(map[i][j].newPos).setRegistration(
              sourceData.get(map[i][j].oldPos).getRegistration()
          );
          case 12 -> result.get(map[i][j].newPos).setWork(
              sourceData.get(map[i][j].oldPos).getWork()
          );
          case 13 -> result.get(map[i][j].newPos).setTin(
              sourceData.get(map[i][j].oldPos).getTin()
          );
          case 14 -> result.get(map[i][j].newPos).setSnils(
              sourceData.get(map[i][j].oldPos).getSnils()
          );
        }
      }
    }

    return result;
  }

  public static void main(String[] args) {

    // permute
    List<Person> people = new ArrayList<>();
    people.add(new Person(0, "sur0", "first0", "pat0", 0, '0', LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0", "work0", "tin0", "snils0"));
    people.add(new Person(1, "sur1", "first1", "pat1", 1, '1', LocalDate.now(), "ser1", "num1", "where1", LocalDate.now(), "reg1", "work1", "tin1", "snils1"));
    people.add(new Person(2, "sur2", "first2", "pat2", 2, '2', LocalDate.now(), "ser2", "num2", "where2", LocalDate.now(), "reg2", "work2", "tin2", "snils2"));
    people.add(new Person(3, "sur3", "first3", "pat3", 3, '3', LocalDate.now(), "ser3", "num3", "where3", LocalDate.now(), "reg3", "work3", "tin3", "snils3"));
    people.add(new Person(4, "sur4", "first4", "pat4", 4, '4', LocalDate.now(), "ser4", "num4", "where4", LocalDate.now(), "reg4", "work4", "tin4", "snils4"));
    people.add(new Person(5, "sur5", "first5", "pat5", 5, '5', LocalDate.now(), "ser5", "num5", "where5", LocalDate.now(), "reg5", "work5", "tin5", "snils5"));
    people.add(new Person(6, "sur6", "first6", "pat6", 6, '6', LocalDate.now(), "ser6", "num6", "where6", LocalDate.now(), "reg6", "work6", "tin6", "snils6"));
    people.add(new Person(7, "sur7", "first7", "pat7", 7, '7', LocalDate.now(), "ser7", "num7", "where7", LocalDate.now(), "reg7", "work7", "tin7", "snils7"));

    for (var i : people) {
      System.out.println(i);
    }
    System.out.println("---------------------------------------");
    for (var i : new Depersonalisation(people).depersonalise()) {
      System.out.println(i);
    }
  }
}
