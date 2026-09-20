package ru.yahoondex.archhelper.crawler.services.helper;

import org.junit.jupiter.api.Test;
import ru.yahoondex.archhelper.commons.contracts.kafka.ArticleDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ArxivArticleHelperTest {
    @Test
    public void shouldParseArticles() throws IOException {
        String articlesXml = Files.readString(Path.of("src", "test", "resources", "oai-articles.xml"), StandardCharsets.UTF_8);
        List<ArticleDto> articles = ArxivArticleHelper.getArticles(articlesXml);
        assertThat(articles).hasSize(1271);
        ArticleDto firstArticle = articles.get(0);
        assertThat(firstArticle.getId()).isEqualTo("1805.08841");
        assertThat(firstArticle.getTitle()).isEqualTo("Distribution Matching Losses Can Hallucinate Features in Medical Image Translation");
        assertThat(firstArticle.getSets()).containsExactlyInAnyOrder("cs:cs:CV", "cs:cs:LG");
        assertThat(firstArticle.getPublished()).hasDayOfMonth(18).hasMonth(Month.SEPTEMBER).hasYear(2026);
    }
}
