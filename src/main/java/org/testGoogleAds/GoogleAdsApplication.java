package org.testGoogleAds;

import com.google.ads.googleads.lib.GoogleAdsClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.testGoogleAds.Validate.ValidateFile;
import org.testGoogleAds.Mutator.HandleMutator;

import com.google.ads.googleads.v15.services.UserListOperation;

import java.io.File;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class GoogleAdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoogleAdsApplication.class, args);

        // Replace this with your actual file path
        String excelFilePath = "path/to/your/excel/file.xlsx";

        try {
            // Validate the Excel file
            List<String> listOpe = ValidateFile.validateExcelFile(new File(excelFilePath));


            // If validation succeeds, build UserListOperations
            List<UserListOperation> operations = buildUserListOperation(listOpe);

            // Mutate user lists using Google Ads API
            HandleMutator.mutateUserLists(GoogleAdsClient.newBuilder().build(), operations);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static List<UserListOperation> buildUserListOperationsFromExcel(String excelFilePath) {
//        return null;
//    }

    private static UserListOperation buildUserListOperationsFromExcel(
            String type, String userListName, String description, String precondition,
            String listOperator, String url, String ruleType, String ruleOperator) {

        UserListOperation.Builder operationBuilder = UserListOperation.newBuilder();

        if ("ADD".equalsIgnoreCase(type)) {
            operationBuilder.setCreate(buildUserList(
                    userListName, description, precondition, listOperator, url, ruleType, ruleOperator));
        } else if ("UPDATE".equalsIgnoreCase(type)) {
            operationBuilder.setUpdate(buildUserList(
                    userListName, description, precondition, listOperator, url, ruleType, ruleOperator));
        }

        return operationBuilder.build();
    }


    private static void mutateUserLists(List<UserListOperation> operations) {
        // Implement this method to handle the mutation of user lists
        // Use the GoogleAdsClient bean to interact with the Google Ads API
        // Example: GoogleAdsClient googleAdsClient = applicationContext.getBean(GoogleAdsClient.class);
        // Use the client to perform operations on Google Ads API
        // Example: userListServiceClient.mutateUserLists(googleAdsClient, operations);
    }
}
