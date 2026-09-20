package ru.yahoondex.archhelper.commons.contracts.kafka;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDto {
    private String id;
    private LocalDate published;
    private String abstr;
    private String title;
    private String[] sets;
}
