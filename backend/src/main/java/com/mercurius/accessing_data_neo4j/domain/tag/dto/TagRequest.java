package com.mercurius.accessing_data_neo4j.domain.tag.dto;

import com.mercurius.accessing_data_neo4j.domain.note.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    private String label;
    private List<Long> noteIds = new ArrayList<>();
}
