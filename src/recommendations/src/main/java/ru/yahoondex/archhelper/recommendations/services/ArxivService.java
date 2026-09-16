package ru.yahoondex.archhelper.recommendations.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Service
public class ArxivService {
    private HttpClient httpClient;
    private String arxivUrl;

    public ArxivService(@Value("${arxiv.api.url}") String arxivUrl) {
        this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(30))
            .build();
        this.arxivUrl = arxivUrl;
    }

    //TODO RETURN ArxivResponse
    //Пример https://export.arxiv.org/api/query?search_query=ti:"computer vision"&sortBy=lastUpdatedDate&sortOrder=ascending&start=0&max_results=1
    @Retryable(retryFor = {IOException.class, InterruptedException.class })
    public void search(String query, int start, int maxResults) throws IOException, InterruptedException {
        final String q = URLEncoder.encode(query, StandardCharsets.UTF_8);
        final String queryUrl = String.format("%s?search_query=%s&start=%d&max_results=%d", this.arxivUrl, q, start, maxResults);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(queryUrl))
            .timeout(Duration.ofSeconds(30))
            .header("User-Agent", "Yahoondex Arch Helper (arch-helper@yahoondex.ru)")
            .GET()
            .build();


            HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            //TODO Process response
            if (response.statusCode() == 429) {
                throw new IOException("ArXiv rate limiter");
            }


    }

    @Recover
    public void recoverSearch(IOException e, String query, int start, int maxResults) {
        //TODO Сделать что-то правильное в этом случае
    }
}