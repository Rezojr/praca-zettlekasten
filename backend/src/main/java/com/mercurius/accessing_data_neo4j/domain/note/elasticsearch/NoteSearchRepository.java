package com.mercurius.accessing_data_neo4j.domain.note.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteSearchRepository extends ElasticsearchRepository<NoteDocument, Long> {
    List<NoteDocument> findByContentContaining(String query);
}
