package com.hpe.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogsController {

    @RequestMapping("/")
    public String index() {
        return "It works!";
    }
}