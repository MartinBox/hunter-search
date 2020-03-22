package com.hunter.persistence.mybatis.ext;

import org.apache.ibatis.builder.BaseBuilder;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;

import java.util.List;

public class JsonMapperBuilder extends BaseBuilder {
    private final MapperBuilderAssistant builderAssistant;
    private final String resource;
    private final List<SqlModel> sqlModels;

    public JsonMapperBuilder(Configuration configuration, String resource, List<SqlModel> sqlModels) {
        super(configuration);
        this.builderAssistant = new MapperBuilderAssistant(configuration, resource);
        this.resource = resource;
        this.sqlModels = sqlModels;
    }

    public void parse() {
        if (!configuration.isResourceLoaded(resource)) {
            buildStatementFromContext(sqlModels, configuration.getDatabaseId());
        }
    }

    private void buildStatementFromContext(List<SqlModel> list, String requiredDatabaseId) {
        for (SqlModel context : list) {
            builderAssistant.setCurrentNamespace(context.getNamespace());
            final JsonStatementBuilder statementParser = new JsonStatementBuilder(configuration, builderAssistant, context, requiredDatabaseId);
            statementParser.parseStatementNode();
        }
    }
}
