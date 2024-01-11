package org.testGoogleAds.services;

import org.springframework.stereotype.Service;
import org.testGoogleAds.Validate.ValidateFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class UserListService {

    public List<String> validateExcelFile(File excelFile) throws IOException {
        return ValidateFile.validateExcelFile(excelFile);
    }

    public void mutateExcelFile(File excelFile) throws IOException {
        // Implement your mutation logic here
        // You can modify ValidateFile class or create a new MutateFile class
    }
}
