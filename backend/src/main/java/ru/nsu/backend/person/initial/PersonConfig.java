package ru.nsu.backend.person.initial;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class PersonConfig {

    @Bean
    CommandLineRunner commandLineRunner(PersonRepository personRepository) {
        return args -> {
            List<InitialPerson> people = new ArrayList<>();
            for (int i = 1; i < 20; i++){
                people.add(
                        new InitialPerson(
                                i, Integer.toString(i), Integer.toString(i), Integer.toString(i), i, 'M',
                                LocalDate.of(i, 12, 30), Integer.toString(i), Integer.toString(i),
                                "Kutateladze " + i, LocalDate.of(i, 12, 30),
                                "Kutateladze " + i, Integer.toString(i),
                                Integer.toString(i), Integer.toString(i)
                                )
                );
            }
            personRepository.saveAll(people);
        };
    }
}
