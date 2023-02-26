package ru.nsu.backend.person.depersonalised;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.nsu.backend.person.initial.SortingType;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/depersonalised")
@AllArgsConstructor
public class DepersonalisedPersonController {
    private final DepersonalisedPersonService depersonalisedPersonService;

    @GetMapping({"/user/users", "/admin/users", "/root/users"})
    public ResponseEntity<List<DepersonalisedPerson>> getUsers() {
        return ResponseEntity.ok(depersonalisedPersonService.getPeople());
    }

    @GetMapping({"/user/sort", "/admin/sort", "root/sort"})
    public ResponseEntity<List<DepersonalisedPerson>> sortTable(@RequestParam String param,
                                                  @RequestParam SortingType sortingType) {
        return ResponseEntity.ok(depersonalisedPersonService.sortTable(param, sortingType));
    }

    @GetMapping({"/user/find", "/root/find", "/admin/find"})
    public ResponseEntity<?> findPerson(@RequestParam String param) {
        try {
            LocalDate localDate = LocalDate.parse(param);
            DepersonalisedPerson depersonalisedPerson = depersonalisedPersonService.findPerson(localDate);
            if (depersonalisedPerson != null) {
                return ResponseEntity.ok(depersonalisedPerson);
            } else {
                return ResponseEntity.badRequest().body("Cannot find anything for that request");
            }
        } catch (DateTimeParseException e1) {
            try {
                Integer integer = Integer.parseInt(param);
                DepersonalisedPerson depersonalisedPerson = depersonalisedPersonService.findPerson(integer);
                if (depersonalisedPerson != null) {
                    return ResponseEntity.ok(depersonalisedPerson);
                } else {
                    return ResponseEntity.badRequest().body("Cannot find anything for that request");
                }
            } catch (NumberFormatException e2) {
                DepersonalisedPerson depersonalisedPerson = depersonalisedPersonService.findPerson(param);
                if (depersonalisedPerson != null) {
                    return ResponseEntity.ok(depersonalisedPerson);
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
        depersonalisedPersonService.addNewPerson(name);
        return ResponseEntity.ok().body("Saved");
    }

    @PostMapping({"/admin/add", "/root/add"})
    public ResponseEntity<String> addNewPerson(@RequestBody DepersonalisedPerson depersonalisedPerson) {
        if (depersonalisedPersonService.addNewPerson(depersonalisedPerson)) {
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
        if (depersonalisedPersonService.updateInfo(personId,
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
                                               @RequestBody DepersonalisedPerson depersonalisedPerson) {
        depersonalisedPersonService.updateInfo(personId, depersonalisedPerson);
        return ResponseEntity.ok().body("Info has been updated");
    }

    @DeleteMapping({"/admin/{personId}", "/root/{personId}", "/user/{personId}"})
    public ResponseEntity<String> deletePerson(@PathVariable("personId") Integer personId) {
        if (depersonalisedPersonService.deletePerson(personId)) {
            return ResponseEntity.ok("Person has been deleted");
        } else {
            return ResponseEntity.badRequest().body("Cannot find such person to delete it");
        }
    }
}
