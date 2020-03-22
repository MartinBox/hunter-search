package com.hunter.persistence.mybatis.ext;

public class SqlModel {
    private String namespace;
    private String id;
    private String sql;
    private String databaseId;
    private boolean flushCache;
    private boolean useCache;
    private boolean resultOrdered;
    private String parameterType;
    private String lang;
    private String commandType;
    private String statementType;
    private Integer fetchSize;
    private Integer timeout;
    private String parameterMap;
    private String resultType;
    private String resultMap;
    private String resultSetType;
    private String keyProperty;
    private String keyColumn;
    private String resultSets;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Integer getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(Integer fetchSize) {
        this.fetchSize = fetchSize;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(String parameterMap) {
        this.parameterMap = parameterMap;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public String getResultSetType() {
        return resultSetType;
    }

    public void setResultSetType(String resultSetType) {
        this.resultSetType = resultSetType;
    }

    public String getKeyProperty() {
        return keyProperty;
    }

    public void setKeyProperty(String keyProperty) {
        this.keyProperty = keyProperty;
    }

    public String getKeyColumn() {
        return keyColumn;
    }

    public void setKeyColumn(String keyColumn) {
        this.keyColumn = keyColumn;
    }

    public String getResultSets() {
        return resultSets;
    }

    public void setResultSets(String resultSets) {
        this.resultSets = resultSets;
    }

    public String getStatementType() {
        return statementType;
    }

    public void setStatementType(String statementType) {
        this.statementType = statementType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public boolean isFlushCache() {
        return flushCache;
    }

    public void setFlushCache(boolean flushCache) {
        this.flushCache = flushCache;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public boolean isResultOrdered() {
        return resultOrdered;
    }

    public void setResultOrdered(boolean resultOrdered) {
        this.resultOrdered = resultOrdered;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }
}
