package com.mercurius.accessing_data_neo4j.domain.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercurius.accessing_data_neo4j.domain.note.Note;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id @GeneratedValue
    private Long id;

    private String label;

    @EqualsAndHashCode.Exclude
    @Relationship(type = "TAGGED_WITH", direction = Relationship.Direction.INCOMING)
    private Set<Note> notes = new HashSet<>();

    public Tag(String label) {
        this.label = label;
    }

}
