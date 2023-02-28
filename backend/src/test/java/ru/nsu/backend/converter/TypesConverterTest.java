package ru.nsu.backend.converter;

import org.junit.jupiter.api.Test;

class TypesConverterTest {

    @Test
    void test() {
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

        System.out.println(TypesConverter.jsonToCsv(jsonData));

    }

}