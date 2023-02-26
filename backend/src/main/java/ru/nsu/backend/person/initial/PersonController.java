package ru.nsu.backend.person.initial;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.backend.person.depersonalised.DepersonalisedPerson;
import ru.nsu.backend.person.depersonalised.DepersonalisedPersonService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class PersonController {
    private final PersonService personService;
    private final DepersonalisedPersonService depersonalisedPersonService;

    @GetMapping({"/admin/updated", "/root/updated"})
    public ResponseEntity<List<DepersonalisedPerson>> depersonalisePeople() {
        return ResponseEntity.ok(depersonalisedPersonService.depersonalisePeople(personService.getInitialData()));
    }

    @GetMapping({"/user/users", "/admin/users", "/root/users"})
    public ResponseEntity<List<InitialPerson>> getUsers() {
        return ResponseEntity.ok(personService.getPeople());
    }

    @GetMapping({"/user/sort", "/admin/sort", "root/sort"})
    public ResponseEntity<List<InitialPerson>> sortTable(@RequestParam String param,
                                                         @RequestParam SortingType sortingType) {
        return ResponseEntity.ok(personService.sortTable(param, sortingType));
    }

    @GetMapping({"/user/find", "/root/find", "/admin/find"})
    public ResponseEntity<?> findPerson(@RequestParam String param) {
        try {
            LocalDate localDate = LocalDate.parse(param);
            InitialPerson person = personService.findPerson(localDate);
            if (person != null) {
                return ResponseEntity.ok(person);
            } else {
                return ResponseEntity.badRequest().body("Cannot find anything for that request");
            }
        } catch (DateTimeParseException e1) {
            try {
                Integer integer = Integer.parseInt(param);
                InitialPerson person = personService.findPerson(integer);
                if (person != null) {
                    return ResponseEntity.ok(person);
                } else {
                    return ResponseEntity.badRequest().body("Cannot find anything for that request");
                }
            } catch (NumberFormatException e2) {
                InitialPerson person = personService.findPerson(param);
                if (person != null) {
                    return ResponseEntity.ok(person);
                } else {
                    return ResponseEntity.badRequest().body("Cannot find anything for that request");
                }
            }
        }
    }

    @PostMapping({"/admin/addName/{name}", "/root/addName/{name}"})
    public ResponseEntity<String> addNewPersonByName(@RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Empty string");
        }
        personService.addNewPerson(name);
        return ResponseEntity.ok().body("Saved");
    }

    @PostMapping({"/admin/add", "/root/add"})
    public ResponseEntity<String> addNewPerson(@RequestBody InitialPerson person) {
        if (personService.addNewPerson(person)) {
            return ResponseEntity.ok().body("Person has been added");
        } else {
            return ResponseEntity.badRequest().body("Person already exists");
        }
    }

    @PutMapping({"/user/update/{personId}", "/root/update/{personId}", "/admin/update/{personId}"})
    public ResponseEntity<String> updatePerson(@PathVariable Integer personId,
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
        if (personService.updateInfo(personId,
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
                snils)) {
            return ResponseEntity.ok("Info has been updated");
        } else {
            return ResponseEntity.badRequest().body("Cannot find such person");
        }
    }

    @PutMapping({"/user/updatePerson/{personId}",
            "/root/updatePerson/{personId}",
            "/admin/updatePerson/{personId}"})
    public ResponseEntity<String> updatePerson(@PathVariable Integer personId,
                                               @RequestBody InitialPerson person) {
        personService.updateInfo(personId, person);
        return ResponseEntity.ok().body("Info has been updated");
    }

    @DeleteMapping({"/admin/{personId}", "/root/{personId}", "/user/{personId}"})
    public ResponseEntity<String> deletePerson(@PathVariable("personId") Integer personId) {
        if (personService.deletePerson(personId)) {
            return ResponseEntity.ok("Person has been deleted");
        } else {
            return ResponseEntity.badRequest().body("Cannot find such person to delete it");
        }
    }
}
