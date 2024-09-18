package com.pingcap.slowqueryparse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SaveSlowQuery {
    private String url;
    private String user;
    private String password;
    private String tableName;
    private PreparedStatement insertStatement;

    private static final String driver = "com.mysql.jdbc.Driver";
    public SaveSlowQuery(String ipPort, String user, String password, String dbName, String tableName) {
        this.url = String.format("jdbc:mysql://%s/%s?useUnicode=true&useSSL=false&useServerPrepStmts=true&prepStmtCacheSqlLimit=655360&prepStmtCacheSize=2000&allowMultiQueries=true&cachePrepStmts=true&rewriteBatchedStatements=true&useConfigs=maxPerformance", ipPort, dbName);
        this.user = user;
        this.password = password;
        this.tableName = tableName;

    }
    public void init() throws Exception {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(url, user, password);
        String insertSQL = String.format("insert into %s(time,user,query_time,is_internal,prepared,plan_from_cache,backoff_total,`sql`,table_names,db,show_create_table,source_sql) values(?,?,?,?,?,?,?,?,?,?,?,?)", tableName);
        insertStatement = connection.prepareStatement(insertSQL);

    }

    public void addSlowQuery(SlowQuery slowQuery) throws Exception {
        insertStatement.setString(1,slowQuery.getTime());
        insertStatement.setString(2,slowQuery.getUser());
        insertStatement.setString(3,slowQuery.getQueryTime());
        insertStatement.setString(4,slowQuery.getIsInternal());
        insertStatement.setString(5,slowQuery.getPrepared());
        insertStatement.setString(6,slowQuery.getPlanFromCache());
        insertStatement.setString(7,slowQuery.getBackoffTotal());
        insertStatement.setString(8,slowQuery.getSql());
        insertStatement.setString(9,slowQuery.getTableNamesStr());
        insertStatement.setString(10,slowQuery.getDb());
        insertStatement.setString(11,slowQuery.getShowCreateTable());
        insertStatement.setString(12,slowQuery.getSourceSql());
        insertStatement.addBatch();
    }

    public void commitSlowQuery() throws Exception {
        insertStatement.executeBatch();
    }




}
