package ru.nsu.backend.person.initial;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.nsu.backend.person.Person;
import ru.nsu.backend.person.depersonalised.DepersonalisedPerson;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The Depersonalization class ensures that the data or its order is changed
 * so that the original personal data cannot be restored.
 * <p>
 * The data shuffling algorithm makes a new table from the source data,
 * and for each row the elements are taken from different rows of the table.
 * It ensures the reversibility of the mixing operation.
 * It should be noted that the algorithm will work the better, the more data the table contains.
 */
public class Depersonalisation {

    private final List<Person> sourceData;

    public Depersonalisation(List<InitialPerson> sourceData) {
        this.sourceData = List.copyOf(sourceData);
    }

    /**
     * The function processes a standard depersonalisation and changes all the dates adding a random number
     * in range [-dataRange; dataRange].
     * Passport number and series are changed to equivalent data from 0000 000000 to 9999 999999.
     *
     * @param dateRange a random number is selected in the segment, which is added to the original date
     * @return a depersonalised list with some anonymous data
     */
    public List<DepersonalisedPerson> depersonaliseWithRandom(int dateRange) {
        var personListDepersonalised = depersonalise();

        int passportNum = 0;
        int passportSeries = 0;
        long snils = 0;
        long tin = 0;

        Random random = new Random();

        for (var person : personListDepersonalised) {
            // dates
            person.setDob(
                person.getDob().plusDays(random.nextInt(-dateRange, dateRange + 1))
            );
            person.setWhenIssued(
                person.getWhenIssued().plusDays(random.nextInt(-dateRange, dateRange + 1))
            );

            // passport
            if (passportNum == 1000000) {
                passportNum = 0;
            }
            if (passportSeries == 10000) {
                passportSeries = 0;
            }

            String numberFormatted = String.format("%06d", passportNum++);
            String seriesFormatted = String.format("%04d", passportSeries++);

            person.setNumber(numberFormatted);
            person.setSeries(seriesFormatted);

            // snils
            if (snils == 100000000000L) {
                snils = 0;
            }

            String snilsFormatted = String.format("%011d", snils++);
            person.setSnils(snilsFormatted);

            // tin (ИНН)
            if (tin == 1000000000000L) {
                tin = 0;
            }

            String tinFormatted = String.format("%012d", tin++);
            person.setTin(tinFormatted);
        }
        return personListDepersonalised;
    }

    /**
     * The function takes an original table `sourceData` and
     * permutes its elements in random order.
     *
     * @return A new shuffled table
     */
    public List<DepersonalisedPerson> depersonalise() {
        if (sourceData.size() < 5) {
            List<DepersonalisedPerson> result = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                result.add(new DepersonalisedPerson(0, "sur0", "first0", "pat0", 0, '0',
                        LocalDate.now(), "ser0", "num0", "where0", LocalDate.now(), "reg0",
                        "work0", "tin0", "snils0"));
            }
            return result;
        }

        MatrixOfParameters matrix = createMatrix();

        MapToNewPosition[][] map = createMapList(matrix);

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
     * <p>
     * The number r0 and the vector rv are associated with each of column.
     *
     * @param nofSubsets   a list of subsets number for each of column
     * @param nofElements  a list of subset sizes
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
     * Creates a new MatrixOfParameters variable.
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

    /**
     * Based on the matrix that the permutation is given,
     * this function builds a mapping from old positions to new ones.
     * <p>
     * The output is a two-dimensional array, where A[i][j] stores
     * the old and new values in the i-th column and j-th row.
     *
     * @param matrix matrix of parameters
     * @return Array of N mappings from old positions to new ones
     */
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

        for (int column = 0; column < N; column++) {

            int subsetCount = matrix.nofSubsets.get(column);
            int r0 = matrix.permutations.get(column).r0;
            int[] rv = matrix.permutations.get(column).rv;

            int[] subsetPositions = new int[subsetCount];
            for (int pos = 0, i = 0; i < subsetCount; pos += matrix.nofElements[column].get(i), i++) {
                subsetPositions[i] = pos;
            }

            for (
                    int row = 0, subset = subsetCount - r0;
                    row == 0 || subset != subsetCount - r0;
                    subset = (subset + 1) % subsetCount
            ) {
                int numCount = matrix.nofElements[column].get(subset);
                for (int k = 0; k < numCount; k++, row++) {
                    int oldPos = subsetPositions[subset] + k;
                    int newPos = (k + rv[subset]) % numCount + row - k;
                    result[column][newPos] = new MapToNewPosition(oldPos, newPos);
                }
            }

        }

        return result;
    }

    /**
     * The function goes through all the elements of the old table
     * and puts them in new positions in accordance with the mapping.
     *
     * @param map An array of mappings
     * @return A new shuffled table
     */
    List<DepersonalisedPerson> permute(MapToNewPosition[][] map) {
        int N = Person.PARAMS_COUNT;
        int M = sourceData.size();

        List<DepersonalisedPerson> result = new ArrayList<>(M);
        for (int i = 0; i < M; i++) {
            result.add(new DepersonalisedPerson());
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
}
