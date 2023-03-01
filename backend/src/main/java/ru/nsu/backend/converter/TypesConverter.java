package ru.nsu.backend.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.backend.person.Person;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

public class TypesConverter {

    public static byte @NotNull [] toCsv(List<Person> people) {

        var matrix = listToMatrix(people);
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            try (
                    Writer writer = new OutputStreamWriter(baos);
                    CSVWriter csvWriter = new CSVWriter(writer)
            ) {
                csvWriter.writeAll(List.of(matrix));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte @NotNull [] toXLSX(List<Person> people) {

        String[][] matrix = listToMatrix(people);

        Workbook workBook = new XSSFWorkbook();

        Sheet sheet = workBook.createSheet("Sheet");

        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            Row row = sheet.createRow(rowIndex);
            for (int cell = 0; cell < matrix[rowIndex].length; cell++) {
                row.createCell(cell).setCellValue(matrix[rowIndex][cell]);
            }
        }
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        try {
            workBook.write(fos);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        byte[] bytes = fos.toByteArray();

        try {
            workBook.close();
            fos.close();

        } catch (IOException e) {
            System.out.println("Exception While Closing I/O Objects");
            e.printStackTrace();
        }

        return bytes;

    }

    public static byte @Nullable [] toJson(List<Person> people) {

        ObjectMapper mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            return mapper.writeValueAsBytes(people);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;

    }

    private static String @NotNull [] @NotNull [] listToMatrix(@NotNull List<Person> people) {

        String[][] matrix = new String[people.size() + 1][Person.PARAMS_COUNT];

        matrix[0] = new String[]{
                "ID", "Surname", "Name", "Patronymic", "Age", "Sex", "Birthday", "Passport series", "Passport number",
                "Where issued", "When issued", "Registration", "Work", "Taxpayer Identification Number", "SNILS"
        };

        for (int i = 0; i < people.size(); i++) {

            Person person = people.get(i);

            matrix[i + 1] = new String[]{
                    String.valueOf(person.getId()), person.getSur(), person.getFirst(), person.getPatronymic(),
                    String.valueOf(person.getAge()), String.valueOf(person.getSex()), String.valueOf(person.getDob()),
                    person.getSeries(), person.getNumber(), person.getWhereIssued(),
                    String.valueOf(person.getWhenIssued()), person.getRegistration(), person.getWork(), person.getTin(),
                    person.getSnils()
            };

        }

        return matrix;

    }

}
