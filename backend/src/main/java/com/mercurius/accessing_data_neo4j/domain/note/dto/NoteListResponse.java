package com.mercurius.accessing_data_neo4j.domain.note.dto;

import com.mercurius.accessing_data_neo4j.domain.note.NoteType;
import com.mercurius.accessing_data_neo4j.domain.tag.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteListResponse {
    private Long id;

    private String title;

    private NoteType noteType;
}
