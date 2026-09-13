package ru.yahoondex.archhelper.recommendations.controllers.rest;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class TestController {

    @GetMapping
    public String dummyResponse() {
        return "!!!";
    }
}