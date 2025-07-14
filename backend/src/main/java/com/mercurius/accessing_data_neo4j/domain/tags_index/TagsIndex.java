package com.mercurius.accessing_data_neo4j.domain.tags_index;

import com.mercurius.accessing_data_neo4j.domain.tag.Tag;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Node
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagsIndex {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @Relationship(type = "CONTAINS")
    private List<Tag> tags;

    public TagsIndex(String name) {
        this.name = name;
    }

    public void addToIndex(List<Tag> tags) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.removeIf(tags::contains);
        this.tags.addAll(tags);
    }

    public String toString() {
        return this.name + "'s tags => " +
                Optional.ofNullable(this.tags).orElse(new ArrayList<>()).stream()
                        .map(Tag::getLabel)
                        .toList();
    }

}
