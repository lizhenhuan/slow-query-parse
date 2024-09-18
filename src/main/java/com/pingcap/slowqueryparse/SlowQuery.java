package com.pingcap.slowqueryparse;

import java.util.Set;

public class SlowQuery {
    private String time;
    private String user;
    private String queryTime;
    private String isInternal;
    private String prepared;
    private String planFromCache;
    private String BackoffTotal;
    private String sql;
    private Set<String> tableNames;
    private String tableNamesStr;

    public String getTableNamesStr() {
        return tableNamesStr;
    }

    public void setTableNamesStr(String tableNamesStr) {
        this.tableNamesStr = tableNamesStr;
    }

    private String db;
    private String showCreateTable;
    private String sourceSql;

    public String getSourceSql() {
        return sourceSql;
    }

    public void setSourceSql(String sourceSql) {
        this.sourceSql = sourceSql;
    }

    public String getShowCreateTable() {
        return showCreateTable;
    }

    public void setShowCreateTable(String showCreateTable) {
        this.showCreateTable = showCreateTable;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public Set<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(Set<String> tableNames) {
        this.tableNames = tableNames;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(String queryTime) {
        this.queryTime = queryTime;
    }

    public String getIsInternal() {
        return isInternal;
    }

    public void setIsInternal(String isInternal) {
        this.isInternal = isInternal;
    }

    public String getPrepared() {
        return prepared;
    }

    public void setPrepared(String prepared) {
        this.prepared = prepared;
    }

    public String getPlanFromCache() {
        return planFromCache;
    }

    public void setPlanFromCache(String planFromCache) {
        this.planFromCache = planFromCache;
    }

    public String getBackoffTotal() {
        return BackoffTotal;
    }

    public void setBackoffTotal(String backoffTotal) {
        BackoffTotal = backoffTotal;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}