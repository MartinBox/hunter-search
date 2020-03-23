package com.hunter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatementControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void user_add() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> properties = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("sys", "junit");
        params.put("namespace", "tbl_user.operator");
        params.put("sql", "insert into tbl_user(userName,password,email,insertTime,updateTime) values(#{userName},#{password},#{email},#{insertTime},#{updateTime})");
        params.put("sqlId", "insert");
        params.put("commandType", "INSERT");
        params.put("remark", "新增用户");
        params.put("properties", mapper.writeValueAsString(properties));


        String json = mapper.writeValueAsString(params);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/mb/statement").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void user_select_add() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> properties = new HashMap<>();
        properties.put("resultType", "java.util.Map");

        Map<String, Object> params = new HashMap<>();
        params.put("sys", "junit");
        params.put("namespace", "tbl_user.operator");
        params.put("sql", "select * from tbl_user where id=#{id}");
        params.put("sqlId", "selectById");
        params.put("commandType", "SELECT");
        params.put("remark", "根据用户id查询详情");
        params.put("properties", mapper.writeValueAsString(properties));


        String json = mapper.writeValueAsString(params);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/mb/statement").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

}
