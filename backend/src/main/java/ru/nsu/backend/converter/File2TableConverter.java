package ru.nsu.backend.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import ru.nsu.backend.person.Person;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

public class File2TableConverter {

    public static List<Person> convert(InputStream file, @NotNull String format) {

        return switch (format) {
            case "csv" -> convertCsv(file);
            case "xlsx" -> convertXlsx(file);
            case "json" -> convertJson(file);
            default -> null;
        };

    }

    private static @NotNull List<Person> convertCsv(InputStream file) {
        try {
            List<String[]> people = new CSVReader(new InputStreamReader(file)).readAll();
            return matrix2Table(people);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull List<Person> convertXlsx(InputStream file) {

        // Finds the workbook instance for XLSX file
        XSSFWorkbook myWorkBook;
        try {
            myWorkBook = new XSSFWorkbook(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Return first sheet from the XLSX workbook
        XSSFSheet mySheet = myWorkBook.getSheetAt(0);

        List<String[]> matrix = new ArrayList<>();

        // Traversing over each row of XLSX file
        for (Row row : mySheet) {

            Queue<String> matrixRow = new ArrayDeque<>();

            // For each row, iterate through each columns
            for (Cell cell : row) {
                switch (cell.getCellType()) {
                    case STRING -> matrixRow.add(cell.getStringCellValue());
                    case NUMERIC -> matrixRow.add(String.valueOf(cell.getNumericCellValue()));
                    default -> {
                    }
                }
            }
            matrix.add(matrixRow.toArray(new String[0]));
        }

        return matrix2Table(matrix);
    }

    private static List<Person> convertJson(InputStream file) {

        ObjectMapper mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            return Arrays.stream(mapper.readValue(file, Person[].class)).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static @NotNull List<Person> matrix2Table(@NotNull List<String[]> matrix) {

        List<Person> people = new ArrayList<>();

        for (int i = 1; i < matrix.size(); i++) {
            String[] row = matrix.get(i);
            people.add(new Person(
                    Integer.parseInt(row[0]), row[1], row[2], row[3], Integer.parseInt(row[4]), row[5].charAt(0),
                    LocalDate.parse(row[6]), row[7], row[8], row[9], LocalDate.parse(row[10]), row[11], row[12],
                    row[13], row[14]
            ));
        }

        return people;

    }

}
