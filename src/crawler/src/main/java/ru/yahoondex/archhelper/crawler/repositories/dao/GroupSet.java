package ru.yahoondex.archhelper.crawler.repositories.dao;

import jakarta.persistence.*;
import lombok.*;

@Entity
@IdClass(GroupSetId.class)
@Table(name = "group_sets")
@EqualsAndHashCode
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupSet {
    @Id
    @Column(name = "group_id")
    @Getter @Setter
    @NonNull
    private String groupId;
    @Id
    @Column(name = "set_id")
    @Getter @Setter
    @NonNull
    private String setId;
}
