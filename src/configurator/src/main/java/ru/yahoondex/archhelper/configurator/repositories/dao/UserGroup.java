package ru.yahoondex.archhelper.configurator.repositories.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_group_mapping")
@IdClass(UserGroupId.class)
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup {
    @Id
    @Column(name = "group_id")
    @Getter @Setter
    private String groupId;
    @Id
    @Column(name = "email")
    @Getter @Setter
    private String email;
}
