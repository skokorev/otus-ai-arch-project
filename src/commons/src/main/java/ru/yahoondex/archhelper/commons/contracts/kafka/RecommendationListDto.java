package ru.yahoondex.archhelper.commons.contracts.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendationListDto {
    private String groupId;
    //Key - SetId, Value - recommendations
    private Map<String, List<RecommendationDto>> recommendations;
}
