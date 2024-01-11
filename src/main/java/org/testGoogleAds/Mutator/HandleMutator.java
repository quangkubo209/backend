package org.testGoogleAds.Mutator;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v15.common.*;
import com.google.ads.googleads.v15.enums.UserListLogicalRuleOperatorEnum;
import com.google.ads.googleads.v15.enums.UserListMembershipStatusEnum;
import com.google.ads.googleads.v15.enums.UserListPrepopulationStatusEnum;
import com.google.ads.googleads.v15.enums.UserListTypeEnum;
import com.google.ads.googleads.v15.resources.UserList;
import com.google.ads.googleads.v15.services.*;

import java.util.Arrays;
import java.util.List;

public class HandleMutator {

    public  static UserListOperation buildUserListOperation(
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
                .setMembershipStatus(UserListMembershipStatusEnum.UserListMembershipStatus.OPEN)
                .setLogicalUserList(
                        buildLogicalUserList(precondition, listOperator, url, ruleType, ruleOperator)
                )
                .setType(UserListTypeEnum.UserListType.LOGICAL);

        return userListBuilder.build();
    }

    private static LogicalUserListInfo buildLogicalUserList(
            String precondition, String listOperator, String url, String ruleType, String ruleOperator) {

        UserListRuleInfo.Builder ruleInfoBuilder = UserListRuleInfo.newBuilder()
                .addAllRuleItemGroups(buildRuleItemGroups(url, ruleOperator));

        return LogicalUserListInfo.newBuilder()
                .setPrepopulationStatus(UserListPrepopulationStatusEnum.UserListPrepopulationStatus.valueOf(precondition))
                .setRuleBasedUserList(ruleInfoBuilder)
                .build();
    }

    private static List<UserListRuleItemGroupInfo> buildRuleItemGroups(String url, String ruleOperator) {

        UserListRuleItemInfo ruleItemInfo = UserListRuleItemInfo.newBuilder()
                .setName("url_rule")
                .setStringRuleItem(UserListStringRuleItemInfo.newBuilder()
                        .setOperator(UserListLogicalRuleOperatorEnum.UserListLogicalRuleOperator.valueOf(ruleOperator))
                        .setValue(url)
                        .build())
                .build();

        UserListRuleItemGroupInfo ruleItemGroupInfo = UserListRuleItemGroupInfo.newBuilder()
                .addAllRuleItems(Arrays.asList(ruleItemInfo))
//                .setOperator(UserListLogicalRuleOperatorEnum.UserListLogicalRuleOperator.valueOf(ruleOperator))
                .build();


        return Arrays.asList(ruleItemGroupInfo);
    }

    public static void mutateUserLists(GoogleAdsClient googleAdsClient, List<UserListOperation> operations) {
        try (UserListServiceClient userListServiceClient = googleAdsClient.getVersion15().createUserListServiceClient()) {
            MutateUserListsResponse response = userListServiceClient.mutateUserLists(operations);
            for (MutateUserListResult result : response.getResultsList()) {
                System.out.println("UserList with resource name '" + result.getResourceName() +
                        "' has been added/updated.");
            }
        }
    }
}
