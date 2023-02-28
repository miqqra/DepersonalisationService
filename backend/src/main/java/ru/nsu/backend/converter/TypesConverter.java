package ru.nsu.backend.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.nsu.backend.person.Person;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class TypesConverter {

    public static String toCsv(List<Person> people) {

        String[][] matrix = listToMatrix(people);

        StringJoiner rows = new StringJoiner("\n");

        for (String[] row : matrix) {
            StringJoiner items = new StringJoiner(", ");
            Arrays.stream(row).forEach(items::add);
            rows.add(items.toString());
        }

        return rows.toString();

    }

    public static @Nullable String toJson(List<Person> people) {

        ObjectMapper mapper = new ObjectMapper();
        mapper
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        try {
            return mapper.writeValueAsString(people);
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
