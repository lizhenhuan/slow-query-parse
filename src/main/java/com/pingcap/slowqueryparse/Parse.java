package com.pingcap.slowqueryparse;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.util.JdbcConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseSlowQuery.class);

    private static String SLOW_QUERY_DIR;
    private static String IP_PORT;
    private static String USER;
    private static String PASSWORD;
    private static String DB_NAME;
    private static String TABLE_NAME;
    private static void parseArgs(String [] args) {
        if (args.length == 6) {
            SLOW_QUERY_DIR = args[0];
            IP_PORT = args[1];
            USER = args[2];
            PASSWORD = args[3];
            DB_NAME = args[4];
            TABLE_NAME = args[5];
        } else {
            LOGGER.error("please input 6 args: SLOW_QUERY_DIR,IP_PORT,USER,PASSWORD,DB_NAME,TABLE_NAME");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {
        parseArgs(args);
        SaveSlowQuery saveSlowQuery = new SaveSlowQuery(IP_PORT, USER, PASSWORD, DB_NAME, TABLE_NAME);
        saveSlowQuery.init();

        String sql = "select           id, user_id as userId, product_id as productId, melon_type as melonType         , due_proc_mode as dueProcMode, buy_flag as buyFlag, buy_latest_time as buyLatestTime         , is_attention as isAttention, attention_time as attentionTime, custom_sort as customSort         , read_status as readStatus, read_time as readTime         , remark, latest_unit_nav as latestUnitNav, attention_unit_nav as attentionUnitNav         , create_time as createTime, modify_time as modifyTime               from tcs_user_fund_info         where user_id = ?                and product_id = ? [arguments: (\"517702024073017122373002\", 615032)];";
        String input = sql;
        //    where user_id = ?                and product_id = ? [arguments: (\"517702024073017122373002\", 615032)];";
        // String sql = "select                                                                 case t.fund_status when '0' THEN '0' ELSE '1' END as vFundStatus,         case when curr_year_rate is null then total_rate else curr_year_rate end as vCurrYearRate,         t.ID as id, t.MERCHANT_ID as merchantId, PRODUCT_TYPE as productType         , t.FUND_ID as fundId, t.FUND_NAME as fundName, t.asset_type AS assetType         , t.fund_master_code as fundMainCode         , TANO, t.CURRENCY_CODE as currencyCode, FUND_TYPE as fundType, FEE_TYPE as feeType         , regularly as regularly, regularly_cycle as regularlyCycle, cycle_type as cycleType, cycle as cycle,series_cycle_num as seriesCycleNum         , conversion as conversion, sale_out as saleOut,automatically_redeem as isAutomaticallyRedeem , breaker as breaker         , NAV_DATE as navDate, NAV, TOTAL_NAV as totalNav         , CLASS as fundClass, RISK as risk,FUND_PUBLISH_DATE as fundPublishDate ,FUND_PUBLISH as fundPublish,expire_date as expireDate         , FUND_SALE_TIME as fundSaleTime, FUND_RAISE_DATE as fundRaiseDate         , FUND_STATUS as fundStatus,t.STATUS as status, DISPLAY_ORDER as displayOrder,user_sub_group as userSubGroup ,dividend_carryOver_date as dividendCarryOverDate         , rating_date as ratingDate         , support_early_redem as supportEarlyRedem         , YIELD,PROFIT_UNIT as profitUnit, t.CREATE_TIME as createTime, t.MODIFY_TIME as modifyTime         , fund_raise_start_date as fundRaiseStartDate         , support_melon_type as  supportMelonType         , def_melon_type as  defMelonType         , tax_incentives as taxIncentives         , qgg_tax_incentives as qggTaxIncentives         , elderly_type as elderlyType         , fund_full_ch_name as fundFullChName, fund_full_en_name as fundFullEnName, total_people as totalPeople, curr_people as currPeople         , total_amount as totalAmount, current_amount as currentAmount,reservation_amount as reservationAmount,used_reservation_amount as usedReservationAmount,nomark_amount as nomarkAmount,used_nomark_amount as usedNomarkAmount         , min_append_pur_amount as minAppendPurAmount, min_pur_amount as minPurAmount         , min_res_pur_amount as minResPurAmount, max_pur_amount as maxPurAmount         , min_append_sub_amount as minAppendSubAmount         , min_sub_amount as minSubAmount, max_sub_amount as maxSubAmount         , min_red_quty as minRedQuty, min_conv_quty as minConvQuty         , min_hold_quty as minHoldQuty, MONEY_INCREASE as moneyIncrease         , quty_scale as qutyScale, sub_ack_days as subAckDays         , red_ack_days as redAckDays, deliver_days as deliverDays         , melon_days as melonDays, sub_days as subDays         , red_days as redDays, days_can__redeem as daysCanRedeem,daily_rate as dailyRate         , subscription_ack_days as subscriptionAckDays         , latest_month_rate as latestMonthRate, curr_month_rate as currMonthRate         , latest_year_rate as latestYearRate, curr_year_rate as currYearRate         , quarter_rate as quarterRate, half_year_rate as halfYearRate, total_rate as totalRate         , latest_week_rate as latestWeekRate, curr_week_rate as currWeekRate, latest_two_year_rate as latestTwoYearRate,latest_three_year_rate as latestThreeYearRate, latest_five_year_rate as latestFiveYearRate         , bench_date as benchDate, daily_bench_rate as dailyBenchRate, weekly_bench_rate as weeklyBenchRate,monthly_bench_rate as monthlyBenchRate, three_month_bench_rate as threeMonthBenchRate         , six_month_bench_rate as sixMonthBenchRate, cur_year_bench_rate as curYearBenchRate, year_bench_rate as yearBenchRate,two_year_bench_rate as twoYearBenchRate, three_year_bench_rate as threeYearBenchRate         , five_year_bench_rate as fiveYearBenchRate, total_bench_rate as totalBenchRate         , manager_name as managerName         , trustee_name as trusteeName, invest_broker as investBroker         , invest_type as investType, invest_area as investArea         , issue_price as issuePrice, face_value as faceValue         , nav_scale_num as navScaleNum, nav_scale_mode as navScaleMode         , manage_ratio as manageRatio, sale_service_ratio as saleServiceRatio         , standard_ratio as standardRatio, current_ratio as currentRatio         , trustee_ratio as trusteeRatio, renege_ratio as renegeRatio         , cust_service_ratio as custServiceRatio         , setup_date as setupDate         , DETAILS_PAGE as detailsPage, DETAILS_PAGE_H as detailsPageH         , RECOMMEND as recommend, APP_RECOMMEND as appRecommend, APP_CHOICE as appChoice         , choice_desc as choiceDesc         , manager_ids as managerIds         , show_rate as showRate         , product_feature as productFeature          , publisher_name as publisherName          ,revenue_type as revenueType ,sub_revenue_type as subRevenueType          , min_yield as minYield, max_yield as maxYield,customer_service_ex as customerServiceEx,operation_ex as operationEx          , group_name as groupName ,aip_one_rate as aipOneRate,aip_two_rate as aipTwoRate,aip_three_rate as aipThreeRate,aip_five_rate as aipFiveRate          ,intell_aip_one_rate as  intellAipOneRate,intell_aip_two_rate as intellAipTwoRate          ,intell_aip_three_rate as intellAipThreeRate,intell_aip_five_rate as intellAipFiveRate          ,his_ave_annual_aip_rate as hisAveAnnualAipRate,his_intell_ave_annual_aip_rate as hisIntellAveAnnualAipRate          ,total_aip_rate as totalAipRate , intell_total_aip_rate as intellTotalAipRate          , presentation_image as presentationImage, presentation_audio_url as presentationAudioUrl, presentation_audio_desc as presentationAudioDesc          , intell_aip_support as intellAipSupport          ,scale_type as  scaleType,scale as scale, portfolio_exist as portfolioExist          ,retreat_ratio as retreatRatio          , retreat_date as retreatDate          , placing_type as placingType          , holding_year as holdingYear          , qgg_platform_sale as qggPlatformSale          , grading_share_type as gradingShareType          ,regular_operation_period as regularOperationPeriod, risk_disclosure_url as riskDisclosureUrl          ,detail_asset_type as  detailAssetType          ,manage_ratio_Type as manageRatioType          ,total_yield_back as totalYieldBack         , detail_fund_type as detailFundType         ,min_hold_amount as minHoldAmount         ,performance_cal_rate as performanceCalRate         ,fund_huge_redem as fundHugeRedem                                                                                       from PCS_PRODUCT t join PCS_PRODUCT_EX tx         on t.id = tx.product_id                             where 1 = 1                                                                                                                                                                   and fund_id ='001211'                                                                                                                                                                                                                                                                                                                                                                                                                                                limit 0, 10;";
        // String sql = "group by product_id,bus_scen_code [arguments: (\"517702024073017122373002\", \"01\", \"02\")];";
        // String sql = "SELECT @@session.transaction_read_only;";
        // String sql = "group /* 主题进是否需要默认排序，老版本app需要，新版本都会传入isNew，老版本app不传入*/                                         limit 0, 10;";
        sql = sql.replaceAll("\\[arguments:.+;",";");
        sql = sql.replaceAll("\\/\\*.+\\*\\/","");
        Pattern pattern = Pattern.compile("\\[arguments: \\((.*?)\\)\\]");
        // Pattern pattern = Pattern.compile("\\.+");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            System.out.println("++++++");
            System.out.println(matcher.group());
        }
        Pattern p=Pattern.compile("\\d+");

        System.out.println("=========");
        System.out.println(sql);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, "mysql");
        SlowQuery slowQuery = new SlowQuery();
        slowQuery.setSql(sql);
        slowQuery.setTableNames(new HashSet<String>());
        saveSlowQuery.addSlowQuery(slowQuery);

        saveSlowQuery.commitSlowQuery();

        ExportTableAliasVisitor visitor = new ExportTableAliasVisitor();
        for (SQLStatement stmt : stmtList) {
            stmt.accept(visitor);
        }


        for (Map.Entry<String,SQLTableSource> entry : visitor.getAliasMap().entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());

        }

    }
}
