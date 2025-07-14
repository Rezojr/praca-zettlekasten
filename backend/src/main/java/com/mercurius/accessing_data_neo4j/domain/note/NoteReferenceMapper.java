package com.mercurius.accessing_data_neo4j.domain.note;

import com.mercurius.accessing_data_neo4j.config.GlobalMapperConfig;
import com.mercurius.accessing_data_neo4j.domain.common.AbstractMapper;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteListResponse;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteReferenceResponse;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteRequest;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = GlobalMapperConfig.class)
public interface NoteReferenceMapper extends AbstractMapper<Note, NoteReferenceResponse> {

    List<NoteReferenceResponse> mapAllToListResponse(List<Note> notes);

}
