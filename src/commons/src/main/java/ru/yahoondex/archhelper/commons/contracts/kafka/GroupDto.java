package ru.yahoondex.archhelper.commons.contracts.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupDto {
    String id;
    String name;
    String[] sets;
    Operation operation;
}
