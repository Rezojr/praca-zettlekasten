package com.mercurius.accessing_data_neo4j.domain.note;

import com.mercurius.accessing_data_neo4j.config.GlobalMapperConfig;
import com.mercurius.accessing_data_neo4j.domain.common.AbstractMapper;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteListResponse;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteReferenceResponse;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteRequest;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteResponse;
import com.mercurius.accessing_data_neo4j.domain.tag.TagMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class, uses = {NoteReferenceMapper.class, TagMapper.class})
public interface NoteMapper extends AbstractMapper<Note, NoteResponse> {

    List<NoteListResponse> mapAllToListResponse(List<Note> notes);
    Note toEntity(NoteRequest noteRequest);

}
