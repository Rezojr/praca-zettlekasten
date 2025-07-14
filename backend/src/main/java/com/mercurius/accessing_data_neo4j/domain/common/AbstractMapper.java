package com.mercurius.accessing_data_neo4j.domain.common;

import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface AbstractMapper<ENTITY, DTO> {

    DTO toResponse(ENTITY entity);

    default Page<DTO> mapAllToResponse(Page<ENTITY> entities) {
        return entities.map(this::toResponse);
    }

    default List<DTO> mapAllToResponse(List<ENTITY> entities) {
        return Optional.ofNullable(entities)
                .map(list -> list.stream().map(this::toResponse).toList())
                .orElse(Collections.emptyList());
    }
}
