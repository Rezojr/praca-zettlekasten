package com.mercurius.accessing_data_neo4j.domain.note.dto;

import com.mercurius.accessing_data_neo4j.domain.note.NoteType;
import com.mercurius.accessing_data_neo4j.domain.tag.Tag;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private Long id;

    private String title;

    private String content;

    private NoteType noteType;

    private Set<NoteReferenceResponse> references = new HashSet<>();
    private Set<NoteReferenceResponse> referenced = new HashSet<>();

    private Set<TagResponse> tags = new HashSet<>();
}
