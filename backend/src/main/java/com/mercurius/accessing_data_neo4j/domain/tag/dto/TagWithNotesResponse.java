package com.mercurius.accessing_data_neo4j.domain.tag.dto;

import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagWithNotesResponse {
    private Long id;
    private String label;
    private List<NoteResponse> notes;
}
