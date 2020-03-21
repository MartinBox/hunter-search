package com.hunter.persistence.mybatis.configuration;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * The class DatabaseConfiguration.
 *
 * Description: 
 *
 * @author: liuheng
 * @since: 2020/03/21 16:41
 * @version: 1.0
 *
 */
@Configuration
public class DatabaseConfiguration {

    @Autowired
    private DataSource dataSource;


}
