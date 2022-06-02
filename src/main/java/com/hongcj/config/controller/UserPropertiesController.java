package com.hongcj.config.controller;

import com.hongcj.config.common.RefreshScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Hongcj
 * @Date: 2022/05/30 5:04 下午
 */
@RestController
@ResponseBody
@RefreshScope
public class UserPropertiesController {

    @Value("${name}")
    private String name;

    @Value("${age}")
    private String age;

    @GetMapping("print")
    public String print(){
        return name+" "+age;
    }
}
