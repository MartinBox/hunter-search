package com.hunter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest 
{
    @Autowired
    private ApplicationContext context;

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
}
