package org.testGoogleAds.Validate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;

public class ValidateFile {
    private static String getStringCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    public static void validateExcelFile(File excelFile) throws IOException {
        try (FileInputStream fi = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(fi)) {

            Sheet sheet = workbook.getSheet("Userlist Info");
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet 'Userlist Info' not found ");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                validateRow(row);
            }
        }
    }

    public static void validateRow(Row row){
        String type = getStringCellValue(row, 0);
        String userlistName = getStringCellValue(row, 1);
        String description = getStringCellValue(row, 2);
        String precondition = getStringCellValue(row, 3);
        String listOperator = getStringCellValue(row, 4);
        String url = getStringCellValue(row, 5);
        String ruleType = getStringCellValue(row, 6);
        String ruleOperator = getStringCellValue(row, 7);


        validateEnum("Type", type, "ADD", "UPDATE");
        validateStringLength("Userlist Name", userlistName, 100);
        validateStringLength("Description", description, 200);
        validateEnum("Precondition", precondition, "ENABLE", "DISABLE");
        validateEnum("List Operator", listOperator, "Include", "Exclude");
        // Add more validations for other fields as needed
        validateEnum("Rule Type", ruleType, "URL", "REF_URL");
        validateEnum("Rule Operator", ruleOperator, "CONTAINS", "EQUALS",
                "STARTS_WITH", "ENDS_WITH", "NOT_CONTAINS", "NOT_EQUALS",
                "NOT_ENDS_WITH", "NOT_STARTS_WITH");
    }

    public static void validateEnum(String fieldName, String value, String... validValues) {
        for (String validValue : validValues) {
            if (validValue.equalsIgnoreCase(value)) {
                return;
            }
        }
        throw new IllegalArgumentException(fieldName + " must be one of: " + String.join(", ", validValues));
    }

    public static void validateStringLength(String fieldName, String value, int maxLength) {
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " must not exceed " + maxLength + " characters.");
        }
    }
}
