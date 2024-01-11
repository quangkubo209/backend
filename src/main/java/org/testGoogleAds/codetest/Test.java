package org.testGoogleAds.codetest;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v15.common.UserListRuleInfo;
import com.google.ads.googleads.v15.common.UserListRuleItemGroupInfo;
import com.google.ads.googleads.v15.services.UserListOperation;
import com.google.ads.googleads.v15.enums.UserListLogicalRuleOperatorEnum.UserListLogicalRuleOperator;
import com.google.ads.googleads.v15.enums.UserListMembershipStatusEnum.UserListMembershipStatus;
import com.google.ads.googleads.v15.enums.UserListPrepopulationStatusEnum.UserListPrepopulationStatus;
import com.google.ads.googleads.v15.enums.UserListTypeEnum.UserListType;
import com.google.ads.googleads.v15.resources.*;
import com.google.ads.googleads.v15.services.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Test {

    private static final String DEVELOPER_TOKEN = "YOUR_DEVELOPER_TOKEN";
    private static final String CLIENT_ID = "YOUR_CLIENT_ID";
    private static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
    private static final String REFRESH_TOKEN = "YOUR_REFRESH_TOKEN";
    private static final String CUSTOMER_ID = "YOUR_CUSTOMER_ID";

    public static void main(String[] args) throws IOException {
        GoogleAdsClient googleAdsClient = GoogleAdsClient.newBuilder()
                .setDeveloperToken(DEVELOPER_TOKEN)
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRefreshToken(REFRESH_TOKEN)
                .setImpersonatedCustomerId(CUSTOMER_ID)
                .build();

        String filePath = "path/to/your/file.xlsx";
        List<UserListOperation> operations = readUserListInfoFromXlsx(filePath);

        if (!operations.isEmpty()) {
            mutateUserLists(googleAdsClient, operations);
        }
    }

    private static List<UserListOperation> readUserListInfoFromXlsx(String filePath) throws IOException {
        List<UserListOperation> operations = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath));
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("Userlist Info");
            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String type = row.getCell(0).getStringCellValue();
                String userListName = row.getCell(1).getStringCellValue();
                String description = row.getCell(2).getStringCellValue();
                String precondition = row.getCell(3).getStringCellValue();
                String listOperator = row.getCell(4).getStringCellValue();
                String url = row.getCell(5).getStringCellValue();
                String ruleType = row.getCell(6).getStringCellValue();
                String ruleOperator = row.getCell(7).getStringCellValue();

                UserListOperation operation = buildUserListOperation(
                        type, userListName, description, precondition, listOperator, url, ruleType, ruleOperator);

                operations.add(operation);
            }
        }

        return operations;
    }

    private static UserListOperation buildUserListOperation(
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

    private static UserList buildUserList(
            String userListName, String description, String precondition,
            String listOperator, String url, String ruleType, String ruleOperator) {

        UserList.Builder userListBuilder = UserList.newBuilder()
                .setName(userListName)
                .setDescription(description)
                .setMembershipStatus(UserListMembershipStatus.OPEN)
                .setLogicalUserList(
                        buildLogicalUserList(precondition, listOperator, url, ruleType, ruleOperator))
                .setType(UserListType.LOGICAL);

        return userListBuilder.build();
    }

    private static LogicalUserListInfo buildLogicalUserList(
            String precondition, String listOperator, String url, String ruleType, String ruleOperator) {

        RuleInfo ruleInfo = RuleInfo.newBuilder()
                .setRuleItemGroupsList(buildRuleItemGroups(precondition, listOperator, url, ruleType, ruleOperator))
                .build();

        return LogicalUserListInfo.newBuilder()
                .setPrepopulationStatus(UserListPrepopulationStatus.valueOf(precondition))
                .setRules(ruleInfo)
                .build();
    }

    private static List<UserListRuleItemGroupInfo> buildRuleItemGroups(
            String precondition, String listOperator, String url, String ruleType, String ruleOperator) {

        UserListRuleInfo ruleItemInfo = RuleItemInfo.newBuilder()
                .setName("url_rule")
                .setStringRuleItem(StringRuleItemInfo.newBuilder()
                        .setOperator(UserListLogicalRuleOperator.valueOf(ruleOperator))
                        .setValue(url)
                        .build())
                .build();

        RuleItemGroupInfo ruleItemGroupInfo = RuleItemGroupInfo.newBuilder()
                .setRuleItemsList(List.of(ruleItemInfo))
                .setOperator(UserListLogicalRuleOperator.valueOf(listOperator))
                .build();

        return List.of(ruleItemGroupInfo);
    }

    private static void mutateUserLists(GoogleAdsClient googleAdsClient, List<UserListOperation> operations) {
        try (UserListServiceClient userListServiceClient = googleAdsClient.getUserListServiceClient()) {
            MutateUserListsResponse response = userListServiceClient.mutateUserLists(operations);
            for (MutateUserListResult result : response.getResultsList()) {
                System.out.println("UserList with resource name '" + result.getResourceName() +
                        "' has been added/updated.");
            }
        }
    }
}
