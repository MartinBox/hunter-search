package com.hunter.web;

import com.hunter.persistence.mybatis.config.ConfigService;
import com.hunter.persistence.mybatis.config.MybatisEntity;
import com.hunter.persistence.mybatis.ext.SqlModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/statement")
public class StatementController {
    @Autowired
    private ConfigService configService;

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
}
