package org.testGoogleAds.Mutator;

import com.google.ads.googleads.lib.GoogleAdsClient;
import com.google.ads.googleads.v15.common.*;
import com.google.ads.googleads.v15.enums.*;
import com.google.ads.googleads.v15.resources.UserList;
import com.google.ads.googleads.v15.services.*;

import java.util.Arrays;
import java.util.List;

public class HandleMutator {

    public  static UserListOperation buildUserListOperation(
            String type, String userListName, String description, String precondition
            , String url, String ruleType, String ruleOperator, String ruleItem) {

        UserListOperation.Builder operationBuilder = UserListOperation.newBuilder();

        if ("ADD".equalsIgnoreCase(type)) {
            operationBuilder.setCreate(buildUserList(
                    userListName, description, precondition, url, ruleType, ruleOperator, ruleItem));
        } else if ("UPDATE".equalsIgnoreCase(type)) {
            operationBuilder.setUpdate(buildUserList(
                    userListName, description, precondition,  url, ruleType, ruleOperator, ruleItem));
        }

        return operationBuilder.build();
    }

    private static UserList buildUserList(
            String userListName, String description, String precondition,
            String url, String ruleType, String ruleOperator, String ruleItem) {
        UserList.Builder userListBuilder = UserList.newBuilder()
                .setName(userListName)
                .setDescription(description)
                .setMembershipStatus(UserListMembershipStatusEnum.UserListMembershipStatus.OPEN)
                .setRuleBasedUserList(buildRuleBaseUserList(precondition,  url, ruleType, ruleOperator, ruleItem))
                .setType(UserListTypeEnum.UserListType.RULE_BASED);

        return userListBuilder.build();
    }

    private static RuleBasedUserListInfo buildRuleBaseUserList(
            String precondition,  String url, String ruleType, String ruleOperator, String ruleItem) {
        if(precondition.equalsIgnoreCase("enable")){
            return RuleBasedUserListInfo.newBuilder()
                    .setPrepopulationStatus(UserListPrepopulationStatusEnum.UserListPrepopulationStatus.REQUESTED)
                    .setFlexibleRuleUserList(buildFlexibleRuleOperator(url, ruleType, ruleOperator, ruleItem))
                    .build();
        }
        return RuleBasedUserListInfo.newBuilder()
                .setFlexibleRuleUserList(buildFlexibleRuleOperator(url, ruleType, ruleOperator, ruleItem))
                .build();
    }

    private static FlexibleRuleUserListInfo buildFlexibleRuleOperator(
            String url, String ruleType, String ruleOperator, String ruleItem) {
       return    FlexibleRuleUserListInfo.newBuilder()
                .setInclusiveRuleOperator(UserListFlexibleRuleOperatorEnum.UserListFlexibleRuleOperator.valueOf(ruleOperator))
                .addExclusiveOperands(buildRuleOperand(url, ruleType,ruleItem))
                .addInclusiveOperands(buildRuleOperand(url, ruleType, ruleItem))
               .build();


    }

    private static FlexibleRuleOperandInfo buildRuleOperand(String url, String ruleType, String ruleItem) {
        return FlexibleRuleOperandInfo.newBuilder()
                .setRule(UserListRuleInfo.newBuilder()
//                        .setRuleType(UserListRuleTypeEnum.UserListRuleType.OR_OF_ANDS)
                        .addRuleItemGroups(UserListRuleItemGroupInfo.newBuilder()
                                .addRuleItems(UserListRuleItemInfo.newBuilder()
                                        .setName(ruleType)
                                        .setStringRuleItem(UserListStringRuleItemInfo.newBuilder()
                                                .setValue(url)
                                                .setOperator(UserListStringRuleItemOperatorEnum.UserListStringRuleItemOperator.valueOf(ruleItem))
                                                .build())
                                        .build())
                                .build())
                        .build())
                .build();
    }




}
