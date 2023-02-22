package ru.nsu.backend.person;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PersonService {
    public List<Person> getPeople(){
        return List.of(new Person(
                1,
                "surname",
                "firstName",
                "patronymic",
                10,
                'M',
                LocalDate.of(2000, 10, 10),
                "5050",
                "777777",
                "Kutateladze 16",
                LocalDate.of(2014, 10, 10),
                "Kutateladze 16",
                "slave",
                "1234567890",
                "0987654321"
        ));
    }
}
