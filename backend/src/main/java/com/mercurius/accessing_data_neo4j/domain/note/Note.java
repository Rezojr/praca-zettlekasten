package com.mercurius.accessing_data_neo4j.domain.note;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteRequest;
import com.mercurius.accessing_data_neo4j.domain.note.elasticsearch.NoteDocument;
import com.mercurius.accessing_data_neo4j.domain.tag.Tag;
import com.mercurius.accessing_data_neo4j.domain.tags_index.TagsIndex;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;
import java.util.*;

@Node
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    private LocalDateTime creationDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    private String title;
    @Property("content")
    private String content;
    private NoteType noteType;

    @EqualsAndHashCode.Exclude
    @Relationship(type = "TAGGED_WITH", direction = Relationship.Direction.OUTGOING)
    private Set<Tag> tags = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @Relationship(type = "REFERENCES", direction = Relationship.Direction.OUTGOING)
    private Set<Note> references = new HashSet<>();

    @EqualsAndHashCode.Exclude
    @Relationship(type = "REFERENCED")
    private Set<Note> referenced = new HashSet<>();


    public void update(NoteRequest note) {
        this.title = Objects.requireNonNullElse(note.getTitle(), this.title);
        this.content = Objects.requireNonNullElse(note.getContent(), this.content);
        this.noteType = Objects.requireNonNullElse(note.getNoteType(), this.noteType);
    }

    public void removeReferences(Collection<Note> notes) {
        this.references.removeAll(notes);
        notes.forEach(note -> note.getReferenced().remove(this));
    }

    public void addTagsToNote(Collection<Tag> tags) {
        this.tags.addAll(tags);
    }

    public NoteDocument toDocument() {
        return new NoteDocument(this.id, this.title, this.content, this.noteType);
    }
}
