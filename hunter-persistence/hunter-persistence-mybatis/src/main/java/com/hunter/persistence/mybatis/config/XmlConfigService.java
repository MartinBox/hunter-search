package com.hunter.persistence.mybatis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.hunter.persistence.mybatis.config.model.Insert;
import com.hunter.persistence.mybatis.config.model.Mapper;
import com.hunter.persistence.mybatis.config.model.Select;
import com.hunter.persistence.mybatis.ext.JsonMapperBuilder;
import com.hunter.persistence.mybatis.ext.SqlModel;
import com.hunter.persistence.mybatis.mapper.MybatisConfigMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
public class XmlConfigService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private String NAME_SPACE_PREFIX = "MybatisMapper.";
    @Autowired
    private MybatisConfigMapper mybatisConfigMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    private String LOG_FORMAT = "load mybatis sql config,sys -> %-10s namespace -> %-20s sqlId -> %-20s";

    /*@PostConstruct
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
    }*/

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

        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(new ByteArrayInputStream(beanToxmlValue().getBytes()), configuration, "", configuration.getSqlFragments());
        xmlMapperBuilder.parse();
    }


    public static void main(String[] args) throws JAXBException {
        String xml = beanToxmlValue();
        System.out.println(xml);
        String result = StringEscapeUtils.unescapeXml(xml);
        System.out.println(result);

    }

    public static String beanToxmlValue() {
        Mapper mapper = new Mapper();
        mapper.setNamespace("test");

        Insert insert = new Insert();
        insert.setId("insertById");
        insert.setParameterType("java.lang.String");
        insert.setText("insert into tbl_user (userName,password,email,insertTime,updateTime) values(#{userName},#{password},#{email},#{insertTime},#{updateTime})");

        mapper.setInsert(Lists.newArrayList(insert));


        Select select = new Select();
        select.setId("insertById2");
        select.setResultType("java.util.Map");
        select.setText("SELECT * FROM tbl_mybatis where 1=1  <if test=\"sys != null\">and sys=#{sys}</if>");
        mapper.setSelect(Lists.newArrayList(select));

        String str = null;
        try {
            str = beanToXml(mapper, Mapper.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        System.out.println(str);
        return StringEscapeUtils.unescapeXml(str);
    }
    public static Object xmlToBean(String xmlValue, Class<?> load) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(load);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object object = unmarshaller.unmarshal(new ByteArrayInputStream(xmlValue.getBytes()));
        return object;
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
