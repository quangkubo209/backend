package org.testGoogleAds.services;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v15.services.*;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testGoogleAds.Mutator.HandleMutator;
import org.testGoogleAds.Validate.ValidateFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserListService {
    private final long customerId = 3434;
    private final GoogleAdsClient googleAdsClient = GoogleAdsClient.newBuilder()
            .fromEnvironment()
            .build();

    public List<String> validateExcelFile(File excelFile) throws IOException {
        return ValidateFile.validateExcelFile(excelFile);
    }

    public Map<String, Object> mutateExcelFile(File excelFile) throws IOException {
        Map<String, Object> response = new HashMap<>();
        List<String> errors = ValidateFile.validateExcelFile(excelFile);

        if (!errors.isEmpty()) {
            response.put("errorStatus", true);
            response.put("errors", errors);
            return response;
        }

        List<String> fieldList = ValidateFile.getFields(excelFile);
        String type = fieldList.get(0);
        String userListName = fieldList.get(1);
        String description = fieldList.get(2);
        String precondition = fieldList.get(3);
        String url = fieldList.get(4);
        String ruleType = fieldList.get(5);
        String ruleOperator = fieldList.get(6);
        String ruleItem = fieldList.get(7);

        UserListOperation operation = HandleMutator.buildUserListOperation(type, userListName, description, precondition, url, ruleType, ruleOperator, ruleItem);

        response = mutateUserLists(customerId, operation);
        return response;
    }

    private Map<String, Object> mutateUserLists(long customerId, UserListOperation operation) {
        Map<String, Object> response = new HashMap<>();
        List<String> resourceNames = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (UserListServiceClient userServiceClient = googleAdsClient.getVersion15().createUserListServiceClient()) {
            MutateUserListsResponse mutateResponse = userServiceClient.mutateUserLists(
                    Long.toString(customerId), ImmutableList.of(operation));

            for (MutateUserListResult result : mutateResponse.getResultsList()) {
                resourceNames.add(result.getResourceName());
            }

            if (mutateResponse.hasPartialFailureError()) {
                errors.add(mutateResponse.getPartialFailureError().getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            errors.add(e.getMessage());
        }

        if (!errors.isEmpty()) {
            response.put("errorStatus", true);
            response.put("errors", errors);
            return response;
        }

        response.put("errorStatus", false);
        response.put("successMessage", "The mutator processing has been successful!!!");
        response.put("resourceNames", resourceNames);

        return response;
    }
}
