package com.mercurius.accessing_data_neo4j.domain.tags_index;

import com.mercurius.accessing_data_neo4j.domain.note.Note;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagsIndexRepository extends Neo4jRepository<TagsIndex, Long> {

}
