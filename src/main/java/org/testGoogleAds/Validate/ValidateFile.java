package org.testGoogleAds.Validate;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import com.google.ads.googleads.v15.services.UserListOperation;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.*;

public class ValidateFile {

    private static final List<String> VALID_RULE_TYPES = Arrays.asList("URL", "REF_URL");
    private static final List<String> VALID_RULE_OPERATORS = Arrays.asList("CONTAINS", "EQUALS", "STARTS_WITH", "ENDS_WITH", "NOT_CONTAINS", "NOT_EQUALS", "NOT_ENDS_WITH", "NOT_STARTS_WITH");
    private static final List<String> VALID_PRECONDITION = Arrays.asList("ENABLE", "DISABLE");
    private static final List<String> VALID_LIST_OPERATOR = Arrays.asList("INCLUSIVE", "EXCLUSIVE");
    private static final List<String> VALID_TYPE = Arrays.asList("ADD", "UPDATE");
    private static String getStringCellValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }

    public static List<String> validateExcelFile(File excelFile) throws IOException {
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

                return validateRow(row);
            }
        }
    }
//handle check columns of file
    public static List<String>  validateRow(Row row){
        String type = getStringCellValue(row, 0);
        String userListName = getStringCellValue(row, 1);
        String description = getStringCellValue(row, 2);
        String precondition = getStringCellValue(row, 3);
        String listOperator = getStringCellValue(row, 4);
        String ruleType = getStringCellValue(row, 5);
        String ruleOperator = getStringCellValue(row, 6);


        validateEnum("Type", type.toUpperCase(), VALID_TYPE);
        validateStringLength("Userlistname", userListName, 100);
        validateStringLength("Description", description, 200);
        validateEnum("Precondition", precondition.toUpperCase(), VALID_PRECONDITION);
        validateEnum("List Operator", listOperator.toUpperCase(), VALID_LIST_OPERATOR);
        validateEnum("Rule Type", ruleType.toUpperCase(), VALID_RULE_TYPES);
        validateEnum("Rule Operator", ruleOperator.toUpperCase(), VALID_RULE_OPERATORS);

        return Arrays.asList(type, userListName, description, precondition, listOperator, ruleType, ruleOperator);
    }

//Handle check enum
    public static void validateEnum(String fieldName, String value, List<String> validValues) {
        for (String validValue : validValues) {
            if (validValue.equalsIgnoreCase(value)) {
                return;
            }
        }
        throw new IllegalArgumentException(fieldName + " must be one of: " + String.join(", ", validValues));
    }

//    handle check length
    public static void validateStringLength(String fieldName, String value, int maxLength) {
        if (value.length() > maxLength) {
            throw new IllegalArgumentException(fieldName + " must not exceed " + maxLength + " characters.");
        }
    }
}
