/**
 * Copyright (C) 2011-2020 ShenZhen iBOXCHAIN Information Technology Co.,Ltd.
 * <p>
 * All right reserved.
 * <p>
 * This software is the confidential and proprietary information of iBOXCHAIN Company of China. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the
 * terms of the contract agreement you entered into with iBOXCHAIN inc.
 */

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
