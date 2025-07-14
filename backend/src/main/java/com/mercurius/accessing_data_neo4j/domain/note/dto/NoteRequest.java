package com.mercurius.accessing_data_neo4j.domain.note.dto;

import com.mercurius.accessing_data_neo4j.domain.note.Note;
import com.mercurius.accessing_data_neo4j.domain.note.NoteType;
import com.mercurius.accessing_data_neo4j.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {
    private String title;
    private String content;
    private NoteType noteType;
}
