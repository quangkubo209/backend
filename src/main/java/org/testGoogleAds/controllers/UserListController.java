package org.testGoogleAds.controllers;

import com.google.ads.googleads.lib.GoogleAdsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.testGoogleAds.services.UserListService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/userlist")
public class UserListController {

    @Autowired
    private UserListService userListService;



    // ... your other methods

    //handle validate data from file excel
    @PostMapping("/validate/excel")
    public ResponseEntity<Object> validateExcel(@RequestParam("file") MultipartFile file) {
        try {
            // return list errors from validate check file
            List<String> errors = userListService.validateExcelFile(convertMultiPartToFile(file));
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }
            return ResponseEntity.ok("File has been validated successfully. Mutation processing is handling!!!!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    //handle mutate media
    @PostMapping("/mutate/excel")
    public ResponseEntity<Object> mutateExcel(@RequestParam("file") MultipartFile file) {
        try {
            userListService.mutateExcelFile(convertMultiPartToFile(file));
            return ResponseEntity.ok("Mutations applied successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    @GetMapping("/getTest")
    public ResponseEntity<Object> Hello(){
        return ResponseEntity.ok().body("hello from port 8080");
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}
