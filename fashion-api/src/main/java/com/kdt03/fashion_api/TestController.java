package com.kdt03.fashion_api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test-thread")
    public String test() {

        return Thread.currentThread().toString();
    }

}