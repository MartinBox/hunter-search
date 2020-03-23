package com.hunter.persistence.mybatis.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/mb/opr")
public class MybatisOperatorController {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @GetMapping(value = "/{sys}/{namespace}/{id}")
    public ResponseEntity search(@PathVariable("sys") String sys, @PathVariable("namespace") String namespace, @PathVariable("id") String id, @RequestParam Map<String, Object> args) {
        Map<String, Object> result = sqlSessionTemplate.selectOne(sys + "." + namespace + "." + id, args);

        return ResponseEntity.ok(Optional.ofNullable(result));
    }

    @PostMapping(value = "/{sys}/{namespace}/{id}")
    @ResponseBody
    public ResponseEntity insert(@PathVariable("sys") String sys, @PathVariable("namespace") String namespace, @PathVariable("id") String id, @RequestBody Map<String, Object> args) {
        Map<String, String> result = new HashMap<>();
        try {
            int count = sqlSessionTemplate.insert(sys + "." + namespace + "." + id, args);
            result.put("code", count >= 1 ? "200" : "500");
        } catch (Exception e) {
            result.put("code", "500");
            result.put("message", e.getMessage());
        }
        return ResponseEntity.ok(Optional.ofNullable(result));
    }
}
