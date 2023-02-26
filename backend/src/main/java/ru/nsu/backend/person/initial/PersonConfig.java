package ru.nsu.backend.person.initial;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class PersonConfig {

    @Bean
    CommandLineRunner commandLineRunner(PersonRepository personRepository) {
        return args -> {
            InitialPerson p1 = new InitialPerson(
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
            );
            InitialPerson p2 = new InitialPerson(
                    2,
                    "surname2",
                    "firstName2",
                    "patronymic",
                    12,
                    'M',
                    LocalDate.of(2004, 10, 10),
                    "5050",
                    "777777",
                    "Kutateladze 16",
                    LocalDate.of(2025, 10, 10),
                    "Kutateladze 16",
                    "master",
                    "1234567890",
                    "0987654321"
            );
            personRepository.saveAll(List.of(p1, p2));
        };
    }
}
