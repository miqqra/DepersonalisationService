package ru.nsu.backend.person.depersonalised;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.nsu.backend.converter.TypesConverter;
import ru.nsu.backend.person.initial.SortingType;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/depersonalised")
@AllArgsConstructor
public class DepersonalisedPersonController {
    private final DepersonalisedPersonService depersonalisedPersonService;

//    @Value("${download.path}")
//    private final String downloadPath;
//    @Value("${download.filename}")
//    private final String downloadFilename;

    @PostMapping(value = {"/admin/uploadFile", "/root/uploadFile"})
    public ResponseEntity<String> uploadFile(@RequestBody MultipartFile file, @RequestBody String format) {
        try {
            if (format.equals("json") && depersonalisedPersonService.downloadJSONFile(file.getInputStream())) {
                return ResponseEntity.ok("Downloaded");
            } else if (format.equals("xml") && depersonalisedPersonService.downloadXMLFile(file.getInputStream())) {
                return ResponseEntity.ok("Downloaded");
            } else {
                return ResponseEntity.badRequest().body("Can not download file");
            }
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Can not download file");
        }
    }

//    @GetMapping(value = {"root/download", "admin/download"})
//    public ResponseEntity<Resource> downloadFile(@RequestParam String format) { // format: .xml .json
//        File file = new File(downloadPath + downloadFilename + format);
//        if (file.exists() && !file.isDirectory()) {
//            try {
//                if (format.equals("json") && depersonalisedPersonService.uploadJSONFile(file)) {
//                    return ResponseEntity.ok(new InputStreamResource(new FileInputStream(file)));
//                } else if (format.equals("json") && depersonalisedPersonService.uploadXMLFile(file)) {
//                    return ResponseEntity.ok(new InputStreamResource(new FileInputStream(file)));
//                } else return ResponseEntity.badRequest().body(null);
//            } catch (IOException e) {
//                return ResponseEntity.internalServerError().body(null);
//            }
//        }
//        return ResponseEntity.internalServerError().body(null);
//    }


    @GetMapping({"root/downloadFile", "admin/downloadFile", "user/downloadFile"})
    public ResponseEntity<byte[]> downloadFile(@RequestParam String fileType) {
        switch (fileType) {
            case "json" -> {
                return ResponseEntity.ok(TypesConverter.toJson(depersonalisedPersonService.getPeople()));
            }
            case "csv" -> {
                return ResponseEntity.ok(TypesConverter.toCsv(depersonalisedPersonService.getPeople()));
            }
            case "xlsx" -> {
                return ResponseEntity.ok(TypesConverter.toXLSX(depersonalisedPersonService.getPeople()));
            }
        }
        return ResponseEntity.badRequest().body("Can't download file".getBytes());
    }

    /**
     * Get all rows of the depersonalised table.
     *
     * @return response entity with list of depersonalised people info.
     */
    @GetMapping({"/user/users", "/admin/users", "/root/users"})
    public ResponseEntity<List<DepersonalisedPerson>> getUsers() {
        return ResponseEntity.ok(depersonalisedPersonService.getPeople());
    }

    /**
     * Sort table by parameter - one of columns name.
     *
     * @param param       column name.
     * @param sortingType ascending or descending.
     * @return response entity with sorted list of depersonalised people.
     */
    @GetMapping({"/user/sort", "/admin/sort", "root/sort"})
    public ResponseEntity<List<DepersonalisedPerson>> sortTable(@RequestParam String param,
                                                                @RequestParam SortingType sortingType) {
        return ResponseEntity.ok(depersonalisedPersonService.sortTable(param, sortingType));
    }

    /**
     * Find person in depersonalised table.
     *
     * @param param what must be in found row.
     * @return response entity with depersonalised person object or bad request message.
     */
    @GetMapping({"/user/find", "/root/find", "/admin/find"})
    public ResponseEntity<?> findPerson(@RequestParam String param) {
        try {
            LocalDate localDate = LocalDate.parse(param);
            List<DepersonalisedPerson> depersonalisedPerson = depersonalisedPersonService.findPerson(localDate);
            if (depersonalisedPerson != null) {
                return ResponseEntity.ok(depersonalisedPerson);
            } else {
                return ResponseEntity.badRequest().body("Cannot find anything for that request");
            }
        } catch (DateTimeParseException e1) {
            try {
                Integer integer = Integer.parseInt(param);
                List<DepersonalisedPerson> depersonalisedPerson = depersonalisedPersonService.findPerson(integer);
                if (depersonalisedPerson != null) {
                    return ResponseEntity.ok(depersonalisedPerson);
                } else {
                    return ResponseEntity.badRequest().body("Cannot find anything for that request");
                }
            } catch (NumberFormatException e2) {
                List<DepersonalisedPerson> depersonalisedPerson = depersonalisedPersonService
                        .findPerson(param.toLowerCase());
                if (depersonalisedPerson != null) {
                    return ResponseEntity.ok(depersonalisedPerson);
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
    @PostMapping({"/admin/addName/{name}", "/root/addName/{name}"})
    public ResponseEntity<String> addNewPersonByName(@RequestParam String name) {
        if (name == null || name.isBlank()) {
            return ResponseEntity.badRequest().body("Empty string");
        }
        depersonalisedPersonService.addNewPerson(name);
        return ResponseEntity.ok().body("Saved");
    }

    /**
     * Add new person to table.
     *
     * @param depersonalisedPerson new person.
     * @return response entity with result - saved or not.
     */
    @PostMapping({"/admin/add", "/root/add"})
    public ResponseEntity<String> addNewPerson(@RequestBody DepersonalisedPerson depersonalisedPerson) {
        if (depersonalisedPersonService.addNewPerson(depersonalisedPerson)) {
            return ResponseEntity.ok().body("Person has been added");
        } else {
            return ResponseEntity.badRequest().body("Person already exists");
        }
    }

    /**
     * Change persons info to param.
     *
     * @param personId                     personId
     * @param name                         name.
     * @param surname                      surname.
     * @param fatherName                   fatherName.
     * @param age                          age.
     * @param sex                          sex.
     * @param dateOfBirth                  dateOfBirth.
     * @param passportSeries               passportSeries.
     * @param passportNumber               passportNumber.
     * @param wherePassportWasIssued       wherePassportWasIssued.
     * @param whenPassportWasIssued        whenPassportWasIssued.
     * @param registration                 registration.
     * @param work                         work.
     * @param taxpayerIdentificationNumber taxpayerIdentificationNumber.
     * @param snils                        snils.
     * @return response entity with result - updated or not.
     */
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

    /**
     * Change persons info to param.
     *
     * @param personId             personId.
     * @param depersonalisedPerson new info about person with personId.
     * @return response entity with result - updated or not.
     */
    @PutMapping({"/user/updatePerson/{personId}",
            "/root/updatePerson/{personId}",
            "/admin/updatePerson/{personId}"})
    public ResponseEntity<String> updatePerson(@PathVariable Integer personId,
                                               @RequestBody DepersonalisedPerson depersonalisedPerson) {
        depersonalisedPersonService.updateInfo(personId, depersonalisedPerson);
        return ResponseEntity.ok().body("Info has been updated");
    }

    /**
     * Delete person with personId.
     *
     * @param personId personId.
     * @return response entity with result - deleted or not.
     */
    @DeleteMapping({"/admin/{personId}", "/root/{personId}", "/user/{personId}"})
    public ResponseEntity<String> deletePerson(@PathVariable("personId") Integer personId) {
        if (depersonalisedPersonService.deletePerson(personId)) {
            return ResponseEntity.ok("Person has been deleted");
        } else {
            return ResponseEntity.badRequest().body("Cannot find such person to delete it");
        }
    }
}
