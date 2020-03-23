package com.hunter.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;
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
public class OperatorControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @Before
    public void setupMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    @Test
    public void user_insert() throws Exception {
        ObjectMapper mapper = new ObjectMapper();


        Map<String, Object> params = new HashMap<>();
        params.put("userName", "测试用户" + System.currentTimeMillis());
        params.put("password", UUID.randomUUID().toString().replace("-", ""));
        params.put("email", RandomStringUtils.randomNumeric(11) + "@qq.com");
        params.put("insertTime", System.currentTimeMillis());
        params.put("updateTime", System.currentTimeMillis());


        String json = mapper.writeValueAsString(params);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/mb/opr/junit/tbl_user.operator/insert").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(json))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }

    @Test
    public void user_select() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/mb/opr/junit/tbl_user.operator/selectById?id=70"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
}