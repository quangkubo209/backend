package org.testGoogleAds.services;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v15.services.*;
import org.springframework.stereotype.Service;
import org.testGoogleAds.Validate.ValidateFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserListService {

    public List<String> validateExcelFile(File excelFile) throws IOException {
        //return array errors.
        return ValidateFile.validateExcelFile(excelFile);
    }

    public void mutateExcelFile(File excelFile) throws IOException {

    }

    public static List<String> mutateUserLists(GoogleAdsClient googleAdsClient,Long customerId, List<UserListOperation> userlistoperation) {
        List<String> resourceNames = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        try(UserListServiceClient userserviceclient = googleAdsClient.getVersion15().createUserListServiceClient()){
            MutateUserListsResponse response = userserviceclient.mutateUserLists(String.valueOf(customerId), userlistoperation);
            for(MutateUserListResult result : response.getResultsList()){
                resourceNames.add(String.valueOf(result));
            }
            if(response.getPartialFailureError().equals(true)){
                errors.add(response.getPartialFailureError().getMessage());
            }
        }
        if(!errors.isEmpty()){
            return errors;
        }
        return resourceNames;
    }





}
