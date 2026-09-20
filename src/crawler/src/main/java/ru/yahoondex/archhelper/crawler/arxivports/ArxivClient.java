package ru.yahoondex.archhelper.crawler.arxivports;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.util.retry.Retry;

@Slf4j
public class ArxivClient {
    private final WebClient client;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final int TIMEOUT = 5000;
    public ArxivClient(String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(connection -> {
                    log.debug("Arxiv client connected");
                    connection.addHandlerLast(new ReadTimeoutHandler(TIMEOUT / 1000));
                    connection.addHandlerLast(new WriteTimeoutHandler(TIMEOUT / 1000));
                });
        this.client = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    public Mono<String> getAllSets() {
        return client.get()
                .attribute("verb", "ListSets")
                .exchangeToMono(clientResponse -> {
            if (clientResponse.statusCode().equals(HttpStatus.OK))
                return clientResponse.bodyToMono(String.class);
            else
                return clientResponse.createException().flatMap(Mono::error);
        }).retryWhen(Retry.backoff(3, Duration.ofSeconds(3)).jitter(0.5));
    }

    public Mono<String> getArticlesForPeriod(String set, LocalDate from, LocalDate to) {
        return client.get()
                .attributes(params -> {
                    params.put("verb", "ListRecords");
                    params.put("metadataPrefix", "arXiv");
                    params.put("set", set);
                    params.put("from", from.format(dateFormatter));
                    params.put("until", to.format(dateFormatter));
                }).exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().equals(HttpStatus.OK))
                        return clientResponse.bodyToMono(String.class);
                    else
                        return clientResponse.createException().flatMap(Mono::error);
                }).retryWhen(Retry.backoff(3, Duration.ofSeconds(3)).jitter(0.5));
    }
}
