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

    /**
     * Depersonalise table.
     *
     * @return response entity with list of depersonalised people.
     */
    @GetMapping({"/admin/updated", "/root/updated"})
    public ResponseEntity<List<DepersonalisedPerson>> depersonalisePeople() {
        return ResponseEntity.ok(depersonalisedPersonService.depersonalisePeople(personService.getInitialData()));
    }

    /**
     * Get all rows of the table.
     *
     * @return response entity with list of people info.
     */
    @GetMapping({"/admin/users", "/root/users"})
    public ResponseEntity<List<InitialPerson>> getUsers() {
        return ResponseEntity.ok(personService.getPeople());
    }

    /**
     * Sort table by parameter - one of columns name.
     *
     * @param param column name.
     * @param sortingType ascending or descending.
     * @return response entity with sorted list of people.
     */
    @GetMapping({"/admin/sort", "root/sort"})
    public ResponseEntity<List<InitialPerson>> sortTable(@RequestParam String param,
                                                         @RequestParam SortingType sortingType) {
        return ResponseEntity.ok(personService.sortTable(param, sortingType));
    }

    /**
     * Find person in table.
     *
     * @param param what must be in found row.
     * @return response entity with person object or bad request message.
     */
    @GetMapping({"/root/find", "/admin/find"})
    public ResponseEntity<?> findPerson(@RequestParam String param) {
        try {
            LocalDate localDate = LocalDate.parse(param);
            List<InitialPerson> person = personService.findPerson(localDate);
            if (person != null) {
                return ResponseEntity.ok(person);
            } else {
                return ResponseEntity.badRequest().body("Cannot find anything for that request");
            }
        } catch (DateTimeParseException e1) {
            try {
                Integer integer = Integer.parseInt(param);
                List<InitialPerson> person = personService.findPerson(integer);
                if (person != null) {
                    return ResponseEntity.ok(person);
                } else {
                    return ResponseEntity.badRequest().body("Cannot find anything for that request");
                }
            } catch (NumberFormatException e2) {
                List<InitialPerson> person = personService.findPerson(param);
                if (person != null) {
                    return ResponseEntity.ok(person);
                } else {
                    return ResponseEntity.badRequest().body("Cannot find anything for that request");
                }
            }
        }
    }

    /**
     * Add new person to table.
     *
     * @param name name of new person.
     * @return response entity with result - saved or not.
     */
    @PostMapping(value = "/root/addName", consumes = "application/json")
    public ResponseEntity<String> addNewPersonByName(@RequestBody String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Empty string");
        }
        personService.addNewPerson(name);
        return ResponseEntity.ok().body("Saved");
    }

    /**
     * Add new person to table.
     *
     * @param person new person.
     * @return response entity with result - saved or not.
     */
    @PostMapping(value = "/root/add", consumes = "application/json")
    public ResponseEntity<String> addNewPerson(@RequestBody InitialPerson person) {
        if (personService.addNewPerson(person)) {
            return ResponseEntity.ok().body("Person has been added");
        } else {
            return ResponseEntity.badRequest().body("Person already exists");
        }
    }

    /**
     * Change persons info to param.
     *
     * @param personId personId
     * @param name name.
     * @param surname surname.
     * @param fatherName fatherName.
     * @param age age.
     * @param sex sex.
     * @param dateOfBirth dateOfBirth.
     * @param passportSeries passportSeries.
     * @param passportNumber passportNumber.
     * @param wherePassportWasIssued wherePassportWasIssued.
     * @param whenPassportWasIssued whenPassportWasIssued.
     * @param registration registration.
     * @param work work.
     * @param taxpayerIdentificationNumber taxpayerIdentificationNumber.
     * @param snils snils.
     * @return response entity with result - updated or not.
     */
    @PutMapping("/root/update/{personId}")
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

    /**
     * Change persons info to param.
     *
     * @param personId personId.
     * @param person new info about person with personId.
     * @return response entity with result - updated or not.
     */
    @PutMapping("/root/updatePerson/{personId}")
    public ResponseEntity<String> updatePerson(@PathVariable Integer personId,
                                               @RequestBody InitialPerson person) {
        personService.updateInfo(personId, person);
        return ResponseEntity.ok().body("Info has been updated");
    }

    /**
     * Delete person with personId.
     *
     * @param personId personId.
     * @return response entity with result - deleted or not.
     */
    @DeleteMapping("/root/{personId}")
    public ResponseEntity<String> deletePerson(@PathVariable("personId") Integer personId) {
        if (personService.deletePerson(personId)) {
            return ResponseEntity.ok("Person has been deleted");
        } else {
            return ResponseEntity.badRequest().body("Cannot find such person to delete it");
        }
    }
}
