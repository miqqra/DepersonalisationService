package ru.nsu.backend.converter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.nsu.backend.person.Person;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class TypesConverterTest {

    @Test
    void testCsv() {
        String jsonData = """
                [
                  {
                    "id": 0,
                    "surname": "Соловьёв",
                    "name": "Артемий",
                    "patronymic": "Налимович",
                    "age": 37,
                    "sex": "Мужской",
                    "birthday": "06.09.1987",
                    "passportSeries": "50 16",
                    "passportNumber": "162837",
                    "whereIssued": "ОУФМС Ленинского района гор. Мухосранска в Центральном районе",
                    "whenIssued": "05.06.2016",
                    "registration": "гор. Зион, ул. Гагарина, д. 56, кв. 15",
                    "work": "Кеша-банк",
                    "TIN": "517635246978",
                    "SNILS": "517635246978"
                  }
                ]""";

        List<Person> people = new ArrayList<>();
        people.add(new Person(
                0,
                "Соловьёв",
                "Артемий",
                "Налимович",
                37,
                'M',
                LocalDate.of(1987, 9, 6),
                "50 16",
                "1622837",
                "ОУФМС Ленинского района гор. Мухосранска в Центральном районе",
                LocalDate.of(2016, 6, 5),
                "гор. Зион, ул. Гагарина, д. 56, кв. 15",
                "Кеша-банк",
                "517635246978",
                "517635246978"
        ));

        String expected = """
                ID, Surname, Name, Patronymic, Age, Sex, Birthday, Passport series, Passport number, Where issued, When issued, Registration, Work, Taxpayer Identification Number, SNILS
                0, Соловьёв, Артемий, Налимович, 37, M, 1987-09-06, 50 16, 1622837, ОУФМС Ленинского района гор. Мухосранска в Центральном районе, 2016-06-05, гор. Зион, ул. Гагарина, д. 56, кв. 15, Кеша-банк, 517635246978, 517635246978""";

        Assertions.assertEquals(expected, TypesConverter.toCsv(people));

    }

}