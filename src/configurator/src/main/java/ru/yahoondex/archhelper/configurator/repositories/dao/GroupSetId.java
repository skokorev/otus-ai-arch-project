package ru.yahoondex.archhelper.configurator.repositories.dao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
public class GroupSetId implements Serializable {
    private String groupId;
    private String setId;
}
