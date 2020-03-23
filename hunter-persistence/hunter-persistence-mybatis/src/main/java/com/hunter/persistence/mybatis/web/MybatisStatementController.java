package com.hunter.persistence.mybatis.web;

import com.hunter.persistence.mybatis.config.ConfigService;
import com.hunter.persistence.mybatis.config.MybatisEntity;
import java.util.HashMap;
import java.util.Map;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/mb/statement")
public class MybatisStatementController {
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

    @DeleteMapping(value = "/{sys}/{namespace}/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable("sys") String sys, @PathVariable("namespace") String namespace, @PathVariable("id") String id) {
        Map<String, String> result = new HashMap<>(2);
        try {
            configService.delete(sys + "." + namespace + "." + id);
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
