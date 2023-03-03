package ru.nsu.backend.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import ru.nsu.backend.person.initial.InitialPerson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class File2TableConverter {

    public static List<InitialPerson> convert(InputStream file, @NotNull String format) {

        return switch (format) {
            case "csv" -> convertCsv(file);
            case "xlsx" -> convertXlsx(file);
            case "json" -> convertJson(file);
            default -> null;
        };

    }

    private static @NotNull List<InitialPerson> convertCsv(InputStream file) {
        try {
            List<String[]> people = new CSVReader(new InputStreamReader(file)).readAll();
            return matrix2Table(people);
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull List<InitialPerson> convertXlsx(InputStream file) {

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

    private static List<InitialPerson> convertJson(InputStream file) {

        //TODO: doesn't work. Need to be fixed

        ObjectMapper mapper = new ObjectMapper();

        try {
            //noinspection unchecked
            return (List<InitialPerson>) mapper.readValue(file, List.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static @NotNull List<InitialPerson> matrix2Table(@NotNull List<String[]> matrix) {

        List<InitialPerson> people = new ArrayList<>();

        for (int i = 1; i < matrix.size(); i++) {
            String[] row = matrix.get(i);
            people.add(new InitialPerson(
                    Integer.parseInt(row[0]), row[1], row[2], row[3], Integer.parseInt(row[4]), row[5].charAt(0),
                    LocalDate.parse(row[6]), row[7], row[8], row[9], LocalDate.parse(row[10]), row[11], row[12],
                    row[13], row[14]
            ));
        }

        return people;

    }

}
