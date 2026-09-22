package ru.yahoondex.archhelper.recommendations.controllers.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yahoondex.archhelper.recommendations.services.RecommendationService;

@RestController
@RequestMapping("/")
public class TestController {
    private final RecommendationService recommendationService;
    @Autowired
    public TestController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping
    public void generate() {
        recommendationService.generateRecommendations();
    }
}