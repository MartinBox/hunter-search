package com.hunter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hunter.persistence.mybatis.ext.SqlModel;
import org.apache.ibatis.mapping.SqlCommandType;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.sql.SQLWarning;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SearchControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void add() throws Exception {
        SqlModel sqlModel = new SqlModel();
        sqlModel.setNamespace("junit");
        sqlModel.setId("selectByName");
        sqlModel.setSql("select * from tbl_user where userName=#{userName}");
        sqlModel.setResultType("java.util.Map");
        sqlModel.setCommandType(SqlCommandType.SELECT.name());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(sqlModel);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/statement").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void search() throws Exception {
        String result =  mockMvc.perform(MockMvcRequestBuilders.get("/search/junit/selectByName?userName=1"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
}