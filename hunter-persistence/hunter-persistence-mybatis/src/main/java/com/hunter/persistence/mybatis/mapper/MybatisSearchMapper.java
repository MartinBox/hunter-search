package com.hunter.persistence.mybatis.mapper;

/**
 * The class MybatisSearchMapper.
 *
 * Description:
 *
 * @author: liuheng
 * @since: 2020/03/21 16:35
 * @version: 1.0
 */
public interface MybatisSearchMapper {
    Object search(String selectId, Object... args);
}
