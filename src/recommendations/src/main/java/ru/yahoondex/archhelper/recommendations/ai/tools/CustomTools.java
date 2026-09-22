package ru.yahoondex.archhelper.recommendations.ai.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yahoondex.archhelper.recommendations.repositories.ArticleRepository;
import ru.yahoondex.archhelper.recommendations.repositories.SetRepository;
import ru.yahoondex.archhelper.recommendations.repositories.dao.Article;
import ru.yahoondex.archhelper.recommendations.repositories.dao.NameSet;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CustomTools {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private SetRepository setRepository;

    @Tool(description="Получить наборы для заданной группы")
    public List<NameSet> getCollectedSets(String groupId) {
        log.info("Get collected sets for group {}", groupId);
        return setRepository.findAllByGroupId(groupId);
    }

    @Tool(description = "Получить статьи для заданного набора за последнюю неделю")
    public List<Article> getArticles(String setId) {
        if (setId == null)
            return List.of();
        final LocalDate correctDateFrom = LocalDate.now().minusDays(8);
        final LocalDate correctDateTo = LocalDate.now().minusDays(1);
        log.info("Get articles for {} between {dateFrom} and {dateTo}", setId, correctDateFrom, correctDateTo);
        return articleRepository.findAllBySet(setId, correctDateFrom, correctDateTo);
    }
}