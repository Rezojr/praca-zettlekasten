package com.mercurius.accessing_data_neo4j.domain.tag;

import com.mercurius.accessing_data_neo4j.config.GlobalMapperConfig;
import com.mercurius.accessing_data_neo4j.domain.common.AbstractMapper;
import com.mercurius.accessing_data_neo4j.domain.note.NoteMapper;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagRequest;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface TagMapper extends AbstractMapper<Tag, TagResponse> {
    Tag toEntity(TagRequest tagRequest);

}
