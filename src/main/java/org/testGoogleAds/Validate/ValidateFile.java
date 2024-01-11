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
        List<String> errors = new ArrayList<>();

        try (FileInputStream fi = new FileInputStream(excelFile);
             Workbook workbook = WorkbookFactory.create(fi)) {

            Sheet sheet = workbook.getSheet("Userlist Info");
            if (sheet == null) {
                errors.add("Sheet UserlistInfo not found ");
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                List<String> rowErrors = validateRow(row);
                if(!rowErrors.isEmpty()){
                    errors.addAll(rowErrors);
                }
            }
        }
        return errors;
    }
    public static List<String>  GetFieldsRow(Row row){
        List<String> rowErrors = new ArrayList<>();

        String type = getStringCellValue(row, 0);
        String userListName = getStringCellValue(row, 1);
        String description = getStringCellValue(row, 2);
        String precondition = getStringCellValue(row, 3);
        String listOperator = getStringCellValue(row, 4);
        String url = getStringCellValue(row, 5);
        String ruleType = getStringCellValue(row, 6);
        String ruleOperator = getStringCellValue(row,7 );


        validateEnum("Type", type.toUpperCase(), VALID_TYPE, rowErrors);
        validateStringLength("Userlistname", userListName, 100, rowErrors);
        validateStringLength("Description", description, 200, rowErrors);
        validateEnum("Precondition", precondition.toUpperCase(), VALID_PRECONDITION, rowErrors);
        validateEnum("List Operator", listOperator.toUpperCase(), VALID_LIST_OPERATOR, rowErrors);
        validateEnum("Rule Type", ruleType.toUpperCase(), VALID_RULE_TYPES, rowErrors);
        validateEnum("Rule Operator", ruleOperator.toUpperCase(), VALID_RULE_OPERATORS, rowErrors);
        


        return rowErrors;
    }


//handle check columns of file
    public static List<String>  validateRow(Row row){
        List<String> rowErrors = new ArrayList<>();

        String type = getStringCellValue(row, 0);
        String userListName = getStringCellValue(row, 1);
        String description = getStringCellValue(row, 2);
        String precondition = getStringCellValue(row, 3);
        String listOperator = getStringCellValue(row, 4);
        String url = getStringCellValue(row, 5);
        String ruleType = getStringCellValue(row, 6);
        String ruleOperator = getStringCellValue(row,7 );


        validateEnum("Type", type.toUpperCase(), VALID_TYPE, rowErrors);
        validateStringLength("Userlistname", userListName, 100, rowErrors);
        validateStringLength("Description", description, 200, rowErrors);
        validateEnum("Precondition", precondition.toUpperCase(), VALID_PRECONDITION, rowErrors);
        validateEnum("List Operator", listOperator.toUpperCase(), VALID_LIST_OPERATOR, rowErrors);
        validateEnum("Rule Type", ruleType.toUpperCase(), VALID_RULE_TYPES, rowErrors);
        validateEnum("Rule Operator", ruleOperator.toUpperCase(), VALID_RULE_OPERATORS, rowErrors);


        return rowErrors;
    }


//Handle check enum
    public static void validateEnum(String fieldName, String value, List<String> validValues, List<String> errors) {
            if(validValues.contains(value)) {
                return ;
            }
            else{
                errors.add(fieldName + " must be one of : " + String.join(",", validValues));
            }
        }

//    handle check length
    public static void validateStringLength(String fieldName, String value, int maxLength, List<String> errors) {
        if (value.length() > maxLength){
        errors.add(fieldName+" must not exceed "+maxLength+"characters ");
        }
    }

}
