package com.hunter.persistence.mybatis.mapper;

import com.hunter.persistence.mybatis.config.MybatisEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * The class MybatisSearchMapper.
 * <p>
 * Description:
 *
 * @author: liuheng
 * @since: 2020/03/21 16:35
 * @version: 1.0
 */
@Mapper
public interface MybatisConfigMapper {
    List<MybatisEntity> selectAll();

    int insert(MybatisEntity mybatisEntity);
}
