package ru.nsu.backend.person;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping
    public List<Person> getUsers() {
        return personService.getPeople();
    }

    @GetMapping("/updated")
    public List<Person> depersonalisePeople(){
        return personService.depersonalisePeople();
    }

    @PostMapping("/add")
    public ResponseEntity<String> addNewPerson(@RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Empty string");
        }
        Person person = new Person();
        person.setSur(name);
        personService.addNewPerson(person);
        return ResponseEntity.ok().body("Saved");
    }

    @PostMapping
    public void addNewPerson(@RequestBody Person person) {
        personService.addNewPerson(person);
    }

    @DeleteMapping(path = "/{personId}")
    public void deletePerson(@PathVariable("personId") Integer personId) {
        personService.deletePerson(personId);
    }

    @PutMapping( "/{personId}")
    public void updatePerson(@PathVariable Integer personId,
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
