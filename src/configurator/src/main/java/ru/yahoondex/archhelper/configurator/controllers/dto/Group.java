package ru.yahoondex.archhelper.configurator.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group {
    private String id;
    private String name;
    private List<String> setIds;
}
