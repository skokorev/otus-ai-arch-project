package ru.yahoondex.archhelper.commons.contracts.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationDto {
    private String articleId;
    private String articleTitle;
}
