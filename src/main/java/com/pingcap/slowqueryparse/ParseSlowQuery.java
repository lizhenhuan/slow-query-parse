package com.pingcap.slowqueryparse;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ParseSlowQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseSlowQuery.class);

    private static String SLOW_QUERY_DIR;
    private static String IP_PORT;
    private static String USER;
    private static String PASSWORD;
    private static String DB_NAME;
    private static String TABLE_NAME;

    public static void main(String[] args) throws Exception {
        parseArgs(args);
        SaveSlowQuery saveSlowQuery = new SaveSlowQuery(IP_PORT, USER, PASSWORD, DB_NAME, TABLE_NAME);
        saveSlowQuery.init();
        File[] files = new File(SLOW_QUERY_DIR).listFiles();
        int count = 0;
        List<SlowQuery> slowQueryList = new ArrayList<>();
        for (File file : files) {
            if (file.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line = reader.readLine();
                    SlowQuery slowQuery = new SlowQuery();
                    while (line != null) {

                        if (line.startsWith("# Time: ")) {
                            slowQuery = new SlowQuery();
                            slowQuery.setTime(line.replace("# Time: ", ""));
                        } else if (line.startsWith("# Is_internal: ")) {
                            slowQuery.setIsInternal(line.replace("# Is_internal: ", ""));
                        } else if (line.startsWith("# User@Host: ")) {
                            slowQuery.setUser(line.replace("# User@Host: ", ""));
                        } else if (line.startsWith("# Query_time: ")) {
                            slowQuery.setQueryTime(line.replace("# Query_time: ", ""));
                        } else if (line.startsWith("# DB: ")) {
                            slowQuery.setDb(line.replace("# DB: ", ""));
                        } else if (line.startsWith("# Prepared: ")) {
                            slowQuery.setPrepared(line.replace("# Prepared: ", ""));
                        } else if (line.startsWith("# Backoff_total: ")) {
                            slowQuery.setBackoffTotal(line.replace("# Backoff_total: ", ""));
                        } else if (line.startsWith("# Plan_from_cache: ")) {
                            slowQuery.setPlanFromCache(line.replace("# Plan_from_cache: ", ""));
                        } else if (line.toLowerCase(Locale.ROOT).startsWith("select") || line.toLowerCase(Locale.ROOT).startsWith("insert") || line.toLowerCase(Locale.ROOT).startsWith("update") || line.toLowerCase(Locale.ROOT).startsWith("delete") || line.toLowerCase(Locale.ROOT).startsWith("set") || line.toLowerCase(Locale.ROOT).startsWith("drop")) {
                            slowQuery.setSourceSql(line);
                            line =  line.replaceAll("\\[arguments:.+;",";");
                            // line = line.replaceAll("\\/\\*.+\\*\\/","");
                            slowQuery.setSql(line);
                            System.out.println(line);

                            List<SQLStatement> stmtList = SQLUtils.parseStatements(line, "mysql");
                            ExportTableAliasVisitor visitor = new ExportTableAliasVisitor();
                            for (SQLStatement stmt : stmtList) {
                                stmt.accept(visitor);
                            }
                            slowQuery.setTableNames(visitor.getAliasMap().keySet());

                            slowQueryList.add(slowQuery);
                        }
                        line = reader.readLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            StringBuilder stringBuilderTableNames = new StringBuilder();

            for (SlowQuery slowQuery : slowQueryList) {
                for (String tableName: slowQuery.getTableNames()) {
                    if (tableName.startsWith("null") && slowQuery.getDb() != null) {
                        tableName = tableName.replace("null",slowQuery.getDb());
                    }
                    stringBuilder.append("show create table ").append(tableName).append(";");
                    stringBuilderTableNames.append(tableName).append(",");
                }
                slowQuery.setShowCreateTable(stringBuilder.toString());
                slowQuery.setTableNamesStr(stringBuilderTableNames.toString());
                stringBuilder.setLength(0);
                stringBuilderTableNames.setLength(0);
                saveSlowQuery.addSlowQuery(slowQuery);
                if (count++ % 500 ==0) {
                    saveSlowQuery.commitSlowQuery();
                }
            }
            saveSlowQuery.commitSlowQuery();
            System.out.println(slowQueryList.size());
            slowQueryList.clear();
        }


    }
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

}
