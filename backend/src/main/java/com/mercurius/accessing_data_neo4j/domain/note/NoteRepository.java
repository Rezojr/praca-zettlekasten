package com.mercurius.accessing_data_neo4j.domain.note;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public interface NoteRepository extends Neo4jRepository<Note, Long> {
    @Query("MATCH (t:Tag)-[:TAGGED_WITH]->(n:Note) WHERE ID(t) = $tagId RETURN n")
    List<Note> findNotesByTagId(@Param("tagId") Long tagId);

    @Query("MATCH (n1:Note), (n2:Note) " +
            "WHERE ID(n1) = $noteId AND ID(n2) = $referenceId " +
            "MERGE (n1)-[:REFERENCES]->(n2) " +
            "MERGE (n2)-[:REFERENCED]->(n1)")
    void addReferences(@Param("noteId") Long noteId, @Param("referenceId") Long referenceId);

    @Query("MATCH (n1:Note)-[r1:REFERENCES]->(n2:Note) WHERE ID(n1) = $noteId AND ID(n2) = $referenceId DELETE r1 " +
            "MATCH (n2:Note)-[r2:REFERENCED]->(n1:Note) DELETE r2")
    void removeReference(@Param("noteId") Long noteId, @Param("referenceId") Long referenceId);

    @Query("MATCH (n:Note)-[:REFERENCES*1..$depth]->(r:Note), (n)-[:TAGGED_WITH]->(t:Tag) WHERE n.id = $noteId RETURN n, r, t")
    Note findNoteWithReferencesAndTags(@Param("noteId") Long noteId, @Param("depth") int depth);
}
