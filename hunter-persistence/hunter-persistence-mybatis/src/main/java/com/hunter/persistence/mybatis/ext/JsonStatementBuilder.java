package com.hunter.persistence.mybatis.ext;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.builder.xml.XMLIncludeTransformer;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.ResultSetType;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.scripting.defaults.RawSqlSource;
import org.apache.ibatis.session.Configuration;

import java.util.Locale;
import java.util.Optional;

public class JsonStatementBuilder extends BaseBuilder {

    private final MapperBuilderAssistant builderAssistant;
    private final SqlModel context;
    private final String requiredDatabaseId;

    public JsonStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, SqlModel context) {
        this(configuration, builderAssistant, context, null);
    }

    public JsonStatementBuilder(Configuration configuration, MapperBuilderAssistant builderAssistant, SqlModel context, String databaseId) {
        super(configuration);
        this.builderAssistant = builderAssistant;
        this.context = context;
        this.requiredDatabaseId = databaseId;
    }

    public void parseStatementNode() {

        String id = context.getId();
        String databaseId = context.getDatabaseId();

        String nodeName = context.getCommandType();
        SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ENGLISH));
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        boolean flushCache = context.isFlushCache();
        boolean useCache = context.isUseCache();
        boolean resultOrdered = context.isResultOrdered();

        // Include Fragments before parsing
        XMLIncludeTransformer includeParser = new XMLIncludeTransformer(configuration, builderAssistant);

        String parameterType = context.getParameterType();
        Class<?> parameterTypeClass = resolveClass(parameterType);

        String lang = context.getLang();
        LanguageDriver langDriver = getLanguageDriver(lang);

        // Parse the SQL (pre: <selectKey> and <include> were parsed and removed)
        KeyGenerator keyGenerator;
        String keyStatementId = id + SelectKeyGenerator.SELECT_KEY_SUFFIX;
        keyStatementId = builderAssistant.applyCurrentNamespace(keyStatementId, true);

        keyGenerator = NoKeyGenerator.INSTANCE;
        /*if (configuration.hasKeyGenerator(keyStatementId)) {
            keyGenerator = configuration.getKeyGenerator(keyStatementId);
        } else {
            keyGenerator = context.getBooleanAttribute("useGeneratedKeys",
                    configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType))
                    ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
        }*/

        SqlSource sqlSource = new RawSqlSource(configuration, context.getSql(), parameterTypeClass);//langDriver.createSqlSource(configuration, context, parameterTypeClass);
        StatementType statementType = StatementType.valueOf(Optional.ofNullable(context.getStatementType()).orElse(StatementType.PREPARED.toString()));
        Integer fetchSize = context.getFetchSize();
        Integer timeout = context.getTimeout();
        String parameterMap = context.getParameterMap();
        String resultType = context.getResultType();
        Class<?> resultTypeClass = resolveClass(resultType);
        String resultMap = context.getResultMap();
        String resultSetType = context.getResultSetType();
        ResultSetType resultSetTypeEnum = resolveResultSetType(resultSetType);
        if (resultSetTypeEnum == null) {
            resultSetTypeEnum = configuration.getDefaultResultSetType();
        }
        String keyProperty = context.getKeyProperty();
        String keyColumn = context.getKeyColumn();
        String resultSets = context.getResultSets();

        builderAssistant.addMappedStatement(id, sqlSource, statementType, sqlCommandType,
                fetchSize, timeout, parameterMap, parameterTypeClass, resultMap, resultTypeClass,
                resultSetTypeEnum, flushCache, useCache, resultOrdered,
                keyGenerator, keyProperty, keyColumn, databaseId, langDriver, resultSets);

    }


    private LanguageDriver getLanguageDriver(String lang) {
        Class<? extends LanguageDriver> langClass = null;
        if (lang != null) {
            langClass = resolveClass(lang);
        }
        return configuration.getLanguageDriver(langClass);
    }

}
