package com.school.campusexpress.controller;

import com.school.campusexpress.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public R<String> hello() {
        return R.success("Hello Campus Express");
    }
}
