package ru.yahoondex.archhelper.communicator.repositories.dao;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@AllArgsConstructor
@EqualsAndHashCode
public class UserGroupId implements Serializable {
    private String email;
    private String groupId;
}
