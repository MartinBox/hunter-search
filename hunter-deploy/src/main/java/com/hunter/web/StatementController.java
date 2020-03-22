package com.hunter.web;

import com.hunter.persistence.mybatis.config.ConfigService;
import com.hunter.persistence.mybatis.config.MybatisEntity;
import org.apache.ibatis.mapping.MappedStatement;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value = "/statement")
public class StatementController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @PostMapping
    @ResponseBody
    public ResponseEntity add(@RequestBody MybatisEntity mybatisEntity) {
        Map<String, String> result = new HashMap<>();
        try {
            configService.add(mybatisEntity);
            result.put("code", "200");
        } catch (IllegalArgumentException e) {
            result.put("code", "400");
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("id") String id) {
        Map<String, String> result = new HashMap<>();
        try {
            Collection<MappedStatement> collection = sqlSessionTemplate.getConfiguration().getMappedStatements();
            Iterator<MappedStatement> iterator = collection.iterator();
            while (iterator.hasNext()) {
                MappedStatement mappedStatement = iterator.next();
                if (mappedStatement.getId().equals(id)) {
                    collection.remove(mappedStatement);
                    break;
                }
            }
            result.put("code", "200");
        } catch (IllegalArgumentException e) {
            result.put("code", "400");
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity update(@RequestBody MybatisEntity mybatisEntity) {
        Map<String, String> result = new HashMap<>();
        try {
            configService.update(mybatisEntity);
            result.put("code", "200");
        } catch (IllegalArgumentException e) {
            result.put("code", "400");
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
