package com.mercurius.accessing_data_neo4j.domain.note.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddTagRequest {
    private List<Long> tagIds;
    private boolean add;
}
