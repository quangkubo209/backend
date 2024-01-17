package org.testGoogleAds.controllers;

import com.google.ads.googleads.lib.GoogleAdsClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.testGoogleAds.model.Action;
import org.testGoogleAds.services.ActionService;
import org.testGoogleAds.services.UserListService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import static org.testGoogleAds.Validate.ValidateFile.getFields;

@RestController
@RequestMapping("/api/userlist")
public class UserListController {

    @Autowired
    private UserListService userListService;
    private ActionService actionService;


    //handle validate data from file excel
    @PostMapping("/validate/excel")
    public ResponseEntity<Object> validateExcel(@RequestParam("file") MultipartFile file) {
        try {
            // return list errors from validate check file
            List<String> errors = userListService.validateExcelFile(convertMultiPartToFile(file));
            if (!errors.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("statusError", true);
                errorResponse.put("errors", errors);
                return ResponseEntity.status(HttpStatus.OK).body(errorResponse);
            }
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("statusError", false);
            successResponse.put("successMessage","File has been validated successfully. Mutation processing is handling!!!");
            return ResponseEntity.status(HttpStatus.OK).body(successResponse);
//            return ResponseEntity.ok("File has been validated successfully. Mutation processing is handling!!!!");
        } catch (IOException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusError", true);
            errorResponse.put("errors", ("Error processing the file"));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
//    handle mutate media
    @PostMapping("/mutate/excel")
    public ResponseEntity<Object> mutateExcel(@RequestParam("file") MultipartFile file) {
        try {
            Map<String,Object> response = userListService.mutateExcelFile( convertMultiPartToFile(file));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    @PostMapping("/mutate/getTest")
    public ResponseEntity<Object> getFiledTest(@RequestParam("file") MultipartFile file) {
        try {
            List<String> response = getFields( convertMultiPartToFile(file));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing the file");
        }
    }

    @PostMapping("/actions")
    public void createAction(@RequestBody Action action) {
        actionService.insertAction(action);
    }


    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }


}
