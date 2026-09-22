package ru.yahoondex.archhelper.crawler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yahoondex.archhelper.crawler.services.ArxivService;

@RestController
@RequestMapping("/")
public class TestController {

    @Autowired
    private ArxivService arxivService;

    @PostMapping("/sets")
    public void crawlSets() {
        arxivService.updateSets();
    }

    @PostMapping("articles")
    public void crawlArticles() {
        arxivService.updateArticlesDaily();
    }

}
