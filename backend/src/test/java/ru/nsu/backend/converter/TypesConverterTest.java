package ru.nsu.backend.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.backend.person.Person;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class TypesConverterTest {

    List<Person> people;

    public TypesConverterTest() {

        people = new ArrayList<>();

        people.add(new Person(0, "Соловьёв", "Артемий", "Налимович", 37, 'M', LocalDate.of(1987, 9, 6), "50 16", "162837", "ОУФМС Ленинского района гор. Мухосранска в Центральном районе", LocalDate.of(2016, 6, 5), "гор. Зион, ул. Гагарина, д. 56, кв. 15", "Кеша-банк", "517635246978", "517635246978"));

    }

    @Test
    void toCsvTest() {

        String expected = """
                "ID","Surname","Name","Patronymic","Age","Sex","Birthday","Passport series","Passport number","Where issued","When issued","Registration","Work","Taxpayer Identification Number","SNILS"
                "0","Соловьёв","Артемий","Налимович","37","M","1987-09-06","50 16","162837","ОУФМС Ленинского района гор. Мухосранска в Центральном районе","2016-06-05","гор. Зион, ул. Гагарина, д. 56, кв. 15","Кеша-банк","517635246978","517635246978"
                """;

        Assertions.assertArrayEquals(expected.getBytes(), TypesConverter.toCsv(people));

    }

    @Test
    void toJsonTest() {

        String expected = "[{\"id\":0,\"sur\":\"Соловьёв\",\"first\":\"Артемий\",\"patronymic\":\"Налимович\"," +
                "\"age\":37,\"sex\":\"M\",\"dob\":\"1987-09-06\",\"series\":\"50 16\",\"number\":\"162837\"," +
                "\"whereIssued\":\"ОУФМС Ленинского района гор. Мухосранска в Центральном районе\"," +
                "\"whenIssued\":\"2016-06-05\",\"registration\":\"гор. Зион, ул. Гагарина, д. 56, кв. 15\"," +
                "\"work\":\"Кеша-банк\",\"tin\":\"517635246978\",\"snils\":\"517635246978\"}]";

        Assertions.assertArrayEquals(expected.getBytes(), TypesConverter.toJson(people));

    }

    @Test
    void toXLSX() {

        File file = new File("test.xlsx");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(TypesConverter.toXLSX(people));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}