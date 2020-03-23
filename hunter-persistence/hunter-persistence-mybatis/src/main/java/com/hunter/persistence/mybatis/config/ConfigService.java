package com.hunter.persistence.mybatis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hunter.persistence.mybatis.config.model.Delete;
import com.hunter.persistence.mybatis.config.model.Insert;
import com.hunter.persistence.mybatis.config.model.Mapper;
import com.hunter.persistence.mybatis.config.model.Select;
import com.hunter.persistence.mybatis.config.model.Update;
import com.hunter.persistence.mybatis.ext.SqlModel;
import com.hunter.persistence.mybatis.mapper.MybatisConfigMapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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

    public void update(MybatisEntity mybatisEntity) {
        String mapperId = mybatisEntity.getSys() + "." + mybatisEntity.getNamespace() + "." + mybatisEntity.getSqlId();
        delete(mapperId);
        config(mybatisEntity);
    }

    public void delete(String mapperId) {
        Collection<MappedStatement> collection = sqlSessionTemplate.getConfiguration().getMappedStatements();
        Iterator<MappedStatement> iterator = collection.iterator();
        while (iterator.hasNext()) {
            MappedStatement mappedStatement = iterator.next();
            if (mappedStatement.getId().equals(mapperId)) {
                iterator.remove();
            }
        }
    }

    public void config(MybatisEntity mybatisEntity) {
        SqlModel sqlModel = null;
        if (!StringUtils.isEmpty(mybatisEntity.getProperties())) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                sqlModel = mapper.readValue(mybatisEntity.getProperties(), SqlModel.class);
            } catch (IOException e) {
                throw new IllegalArgumentException("[properties] is illegal argument", e);
            }
        } else {
            sqlModel = new SqlModel();
        }
        sqlModel.setNamespace(mybatisEntity.getSys() + "." + mybatisEntity.getNamespace());
        sqlModel.setId(mybatisEntity.getSqlId());
        sqlModel.setSql(mybatisEntity.getSql());
        sqlModel.setCommandType(mybatisEntity.getCommandType());
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
        /*JsonMapperBuilder jsonMapperBuilder = new JsonMapperBuilder(configuration, "", Lists.newArrayList(sqlModel));
        jsonMapperBuilder.parse();*/
        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(new ByteArrayInputStream(beanToxmlValue(sqlModel).getBytes()), configuration, UUID.randomUUID().toString(), configuration.getSqlFragments());
        xmlMapperBuilder.parse();
    }

    public static String beanToxmlValue(SqlModel sqlModel) {
        Mapper mapper = new Mapper();
        mapper.setNamespace(sqlModel.getNamespace());

        switch (sqlModel.getCommandType()) {
            case "SELECT":
                Select select = new Select();
                select.setId(sqlModel.getId());
                select.setResultType(sqlModel.getResultType());
                select.setParameterType(sqlModel.getParameterType());
                select.setText(sqlModel.getSql());
                mapper.setSelect(Lists.newArrayList(select));
                break;
            case "DELETE":
                Delete delete = new Delete();
                delete.setId(sqlModel.getId());
                delete.setResultType(sqlModel.getResultType());
                delete.setParameterType(sqlModel.getParameterType());
                delete.setText(sqlModel.getSql());
                mapper.setDelete(Lists.newArrayList(delete));
                break;
            case "UPDATE":
                Update update = new Update();
                update.setId(sqlModel.getId());
                update.setResultType(sqlModel.getResultType());
                update.setParameterType(sqlModel.getParameterType());
                update.setText(sqlModel.getSql());
                mapper.setUpdate(Lists.newArrayList(update));
                break;
            case "INSERT":
                Insert insert = new Insert();
                insert.setId(sqlModel.getId());
                insert.setResultType(sqlModel.getResultType());
                insert.setParameterType(sqlModel.getParameterType());
                insert.setText(sqlModel.getSql());
                mapper.setInsert(Lists.newArrayList(insert));
                break;
            default:
                throw new IllegalArgumentException("commandType must be [SELECT/DELETE/UPDATE/INSERT]");
        }
        String str = null;
        try {
            str = beanToXml(mapper, Mapper.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return StringEscapeUtils.unescapeXml(str);
    }

    public static String beanToXml(Object obj, Class<?> load) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(load);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        marshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders",
                "\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n");
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }
}
