package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LiffController {

    @GetMapping("/liff/{viewType:compact|tall|full}")
    public String index(@PathVariable String viewType) {
        log.info("{}", viewType);
        return "index";
    }
}
