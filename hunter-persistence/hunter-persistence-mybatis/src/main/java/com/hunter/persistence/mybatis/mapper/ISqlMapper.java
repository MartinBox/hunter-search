/**
 * Copyright (C) 2011-2020 ShenZhen iBOXCHAIN Information Technology Co.,Ltd.
 * <p>
 * All right reserved.
 * <p>
 * This software is the confidential and proprietary information of iBOXCHAIN Company of China. ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall use it only in accordance with the
 * terms of the contract agreement you entered into with iBOXCHAIN inc.
 */

package com.hunter.persistence.mybatis.mapper;

import java.util.List;
import java.util.Map;

/**
 * The class ISqlMapper.
 *
 * Description:
 *
 * @author: liuheng
 * @since: 2020/03/21 18:41
 * @version: 1.0
 */
public interface ISqlMapper {
    Integer insert(String statement);

    Integer delete(String statement);

    Integer update(String statement);

    List<Map<String, Object>> selectList(String statement);

    Map selectOne(String statement);

    Map selectOne(String statement, Object... args);
}
