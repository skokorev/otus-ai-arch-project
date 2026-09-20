package ru.yahoondex.archhelper.commons.contracts.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NameSetDto {
    private String id;
    private String name;
    private Operation operation;
}
