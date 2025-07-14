package com.mercurius.accessing_data_neo4j.domain.note.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mercurius.accessing_data_neo4j.domain.note.NoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.List;

@Document(indexName = "notes")
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoteDocument {
    @Id
    private Long id;
    private String title;
    private String content;
    private NoteType noteType;
    private List<String> highlightedContent = new ArrayList<>();

    public NoteDocument(Long id, String title, String content, NoteType noteType) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.noteType = noteType;
    }
}
