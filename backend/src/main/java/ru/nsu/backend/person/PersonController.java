package ru.nsu.backend.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hello")
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public List<Person> getUsers() {
        return personService.getPeople();
    }

    @PutMapping
    public void addNewPerson(@RequestBody Person person) {
        personService.addNewPerson(person);
    }

    @DeleteMapping(path = "{personId}")
    public void deletePerson(@PathVariable("personId") Integer personId) {
        personService.deletePerson(personId);
    }

    @PutMapping(path = "{personId")
    public void updatePerson(@PathVariable("personId") Integer personId,
                             @RequestParam(required = false) String name,
                             @RequestParam(required = false) String surname,
                             @RequestParam(required = false) String fatherName,
                             @RequestParam(required = false) Integer age,
                             @RequestParam(required = false) Character sex,
                             @RequestParam(required = false) String dateOfBirth,
                             @RequestParam(required = false) String passportSeries,
                             @RequestParam(required = false) String passportNumber,
                             @RequestParam(required = false) String wherePassportWasIssued,
                             @RequestParam(required = false) String whenPassportWasIssued,
                             @RequestParam(required = false) String registration,
                             @RequestParam(required = false) String work,
                             @RequestParam(required = false) String taxpayerIdentificationNumber,
                             @RequestParam(required = false) String snils) {
        personService.updateInfo(personId,
                name,
                surname,
                fatherName,
                age,
                sex,
                dateOfBirth,
                passportSeries,
                passportNumber,
                wherePassportWasIssued,
                whenPassportWasIssued,
                registration,
                work,
                taxpayerIdentificationNumber,
                snils);
    }
}
