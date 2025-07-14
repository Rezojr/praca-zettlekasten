package com.mercurius.accessing_data_neo4j.domain.tag;

import com.mercurius.accessing_data_neo4j.domain.note.Note;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends Neo4jRepository<Tag, Long> {

    @Query("MATCH (t:Tag) WHERE ID(t) IN $tagIds RETURN t")
    List<Tag> findAllByIds(@Param("tagIds") List<Long> tagIds);

    @Query("MATCH (t:Tag)-[:TAGGED_WITH]->(n:Note) WHERE ID(t) = $tagId RETURN n.noteType AS noteType, n.title AS title, n.content AS content")
    List<Note> findNotesByTagId(Long tagId);
}
