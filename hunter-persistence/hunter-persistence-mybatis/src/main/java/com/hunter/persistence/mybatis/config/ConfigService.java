package com.hunter.persistence.mybatis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hunter.persistence.mybatis.ext.JsonMapperBuilder;
import com.hunter.persistence.mybatis.ext.SqlModel;
import com.hunter.persistence.mybatis.mapper.MybatisConfigMapper;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Service
public class ConfigService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String NAME_SPACE_PREFIX = "MybatisMapper.";
    @Autowired
    private MybatisConfigMapper mybatisConfigMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private String LOG_FORMAT = "load mybatis sql config,sys -> %-10s namespace -> %-20s sqlId -> %-20s";

    @PostConstruct
    public void setUp() {
        List<MybatisEntity> list = mybatisConfigMapper.selectAll();
        if (null == list || list.isEmpty()) {
            // throw new IllegalArgumentException("mybatis config data not find");
            return;
        }
        list.forEach(mybatisEntity -> {
            logger.info(String.format(LOG_FORMAT, mybatisEntity.getSys(), mybatisEntity.getNamespace(), mybatisEntity.getSqlId()));
            config(mybatisEntity);
        });
    }

    public void add(MybatisEntity mybatisEntity) {
        mybatisConfigMapper.insert(mybatisEntity);
        config(mybatisEntity);
    }

    public void config(MybatisEntity mybatisEntity) {
        SqlModel sqlModel = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            sqlModel = mapper.readValue(mybatisEntity.getProperties(), SqlModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sqlModel.setNamespace(mybatisEntity.getSys() + "." + mybatisEntity.getNamespace());
        sqlModel.setId(mybatisEntity.getSqlId());
        sqlModel.setSql(mybatisEntity.getSql());
        config(sqlModel);
    }


    private void config(SqlModel sqlModel) {
        Assert.notNull(sqlModel.getNamespace(), "namespace must be not null");
        Assert.notNull(sqlModel.getSql(), "sql must be not null");
        Assert.notNull(sqlModel.getId(), "id must be not null");
        if (!StringUtils.isEmpty(sqlModel.getResultType())) {
            try {
                Class.forName(sqlModel.getResultType());
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("resultType is illegal argument");
            }
        }
        Configuration configuration = sqlSessionTemplate.getConfiguration();
        JsonMapperBuilder jsonMapperBuilder = new JsonMapperBuilder(configuration, "", Lists.newArrayList(sqlModel));
        jsonMapperBuilder.parse();
    }
}
