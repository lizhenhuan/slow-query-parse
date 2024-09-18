package com.pingcap.slowqueryparse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgumentExtractor {
    public static void main(String[] args) {
        // The full input string
        String input = "select           id, user_id as userId, product_id as productId, melon_type as melonType         , due_proc_mode as dueProcMode, buy_flag as buyFlag, buy_latest_time as buyLatestTime         , is_attention as isAttention, attention_time as attentionTime, custom_sort as customSort         , read_status as readStatus, read_time as readTime         , remark, latest_unit_nav as latestUnitNav, attention_unit_nav as attentionUnitNav         , create_time as createTime, modify_time as modifyTime               from tcs_user_fund_info         where user_id = ?                and product_id = ? [arguments: (\"517702024073017122373002\", 615032)];";

        // Define regex pattern to match the arguments part
        Pattern pattern = Pattern.compile("\\[arguments: \\((.*?)\\)\\]");
        Matcher matcher = pattern.matcher(input);
        String sql = input.replaceAll("\\[arguments: \\((.*?)\\)\\]","");
        System.out.println(sql);

        // Extract and display the arguments
        if (matcher.find()) {
            String argumentsString = matcher.group(1);
            System.out.println("Extracted arguments: " + argumentsString);

            // Parse the extracted arguments
            String[] arguments = parseArguments(argumentsString);
            System.out.println("Parsed arguments:");
            for (String argument : arguments) {
                System.out.println(argument);
            }
        } else {
            System.out.println("No arguments found in the input string.");
        }
    }

    private static String[] parseArguments(String argumentsString) {
        // Split by comma but ignore commas inside quotes
        return argumentsString.split(", (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
    }
}
