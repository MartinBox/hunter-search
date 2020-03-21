package com.hunter;

import com.hunter.persistence.mybatis.mapper.ISqlMapper;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple HunterSearchApplication.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {
    @Autowired
    private ApplicationContext context;

    @Autowired
    private DataSource dataSource;
    @Autowired
    private SqlSession sqlSession;
    @Value("${foo:spam}")
    private String foo = "bar";

    @Test
    public void testContextCreated() {
        assertThat(this.context).isNotNull();
    }

    @Test
    public void testContextInitialized() {
        assertThat(this.foo).isEqualTo("bucket");
    }

    @Test
    public void sql() {
        Map<String, Object> args = new HashMap<>();
        args.put("userName", "2019031902924240");
        Map<String, Object> map = sqlSession.selectOne("UserMapper.selectByUsername", args);
        System.out.println(map);

        ISqlMapper sqlMapper = sqlSession.getMapper(ISqlMapper.class);

        String sql = "select * from tbl_user where userName=#{userName}";
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            sql = sql.replaceAll("#\\{" + entry.getKey() + "}", entry.getValue() + "");
        }

        System.out.println(sql);
        Map map1 = sqlMapper.selectOne(sql);
        System.out.println(map1);
    }
}
