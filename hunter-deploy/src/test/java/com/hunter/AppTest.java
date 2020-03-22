package com.hunter;

import com.google.common.collect.Lists;
import com.hunter.persistence.mybatis.ext.JsonMapperBuilder;
import com.hunter.persistence.mybatis.ext.SqlModel;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple HunterSearchApplication.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Value("${foo:spam}")
    private String foo = "bar";

    @Test
    public void testContextInitialized() {
        assertThat(this.foo).isEqualTo("bucket");
    }

    @Before
    public void setup() {
        SqlModel sqlModel = new SqlModel();
        sqlModel.setNamespace("external");
        sqlModel.setId("selectByUsername");
        sqlModel.setSql("select * from tbl_user where userName=#{userName}");
        sqlModel.setResultType("java.util.Map");
        sqlModel.setCommandType(SqlCommandType.SELECT.name());

        Configuration configuration = sqlSessionTemplate.getConfiguration();
        JsonMapperBuilder jsonMapperBuilder = new JsonMapperBuilder(configuration, "", Lists.newArrayList(sqlModel));
        jsonMapperBuilder.parse();
    }

    @Test
    public void external() {
        Map<String, Object> args = new HashMap<>();
        args.put("userName", "2019031902924240");
        Map<String, Object> map = sqlSessionTemplate.selectOne("external.selectByUsername", args);
        System.out.println(map);
    }

    @Test
    public void sql() {
        Map<String, Object> args = new HashMap<>();
        args.put("userName", "2019031902924240");
        Map<String, Object> map = sqlSessionTemplate.selectOne("UserMapper.selectByUsername", args);
        System.out.println(map);
    }
}
