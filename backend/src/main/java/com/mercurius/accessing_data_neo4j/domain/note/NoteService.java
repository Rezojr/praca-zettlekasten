package com.mercurius.accessing_data_neo4j.domain.note;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.*;
import co.elastic.clients.elasticsearch.transform.Source;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteListResponse;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteRequest;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteResponse;
import com.mercurius.accessing_data_neo4j.domain.note.elasticsearch.NoteDocument;
import com.mercurius.accessing_data_neo4j.domain.note.elasticsearch.NoteSearchRepository;
import com.mercurius.accessing_data_neo4j.domain.tag.Tag;
import com.mercurius.accessing_data_neo4j.domain.tag.TagRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class NoteService {

    private final NoteRepository noteRepository;
    private final TagRepository tagRepository;
    private final NoteMapper noteMapper;
    private final ElasticsearchClient elasticsearchClient;
    private final NoteSearchRepository noteSearchRepository;

    public List<NoteListResponse> getNotes() {
        List<NoteListResponse> noteListResponses = noteMapper.mapAllToListResponse(noteRepository.findAll());
        log.info(noteListResponses.toString());
        return noteListResponses;
    }

    public NoteResponse getNote(Long id) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note with id does not exists"));
        return noteMapper.toResponse(note);
    }

    public List<NoteDocument> searchNotesByContent(String query) throws IOException {
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("notes")
                .query(q -> q
                        .match(m -> m
                                .field("content")
                                .query(query)
                                .fuzziness("AUTO")
                        )
                )
                .highlight(h -> h
                        .fields("content", f -> f
                                .preTags("<b>")
                                .postTags("</b>")
                                .fragmentSize(100)
                        )
                        .numberOfFragments(3)
                        .boundaryScanner(BoundaryScanner.Sentence)
                        .type(HighlighterType.Unified)
                )
                .build();

        SearchResponse<NoteDocument> searchResponse = elasticsearchClient.search(searchRequest, NoteDocument.class);
        List<NoteDocument> result = new ArrayList<>();

        for (Hit<NoteDocument> hit : searchResponse.hits().hits()) {
            NoteDocument noteDocument = hit.source();
            if (noteDocument != null) {
                noteDocument.setHighlightedContent(hit.highlight() != null
                        ? hit.highlight().getOrDefault("content", new ArrayList<>())
                        : new ArrayList<>());
                result.add(noteDocument);
            }
        }

        return result;
    }

    public NoteResponse getNoteWithRelations(Long noteId, int depth) {
        Note note = noteRepository.findNoteWithReferencesAndTags(noteId, depth);
        return noteMapper.toResponse(note);
    }

    @Transactional
    public NoteResponse createNote(NoteRequest noteRequest) {
        Note entity = noteRepository.save(noteMapper.toEntity(noteRequest));
        Note savedNote = noteRepository.save(entity);

        noteSearchRepository.save(savedNote.toDocument());

        return noteMapper.toResponse(entity);
    }

    @Transactional
    public NoteResponse updateNote(Long noteId, NoteRequest updateRequest) {
        Note note = noteRepository.findById(noteId).orElseThrow(() -> new RuntimeException("Note with id does not exist"));
        note.update(updateRequest);
        Note updatedNote = noteRepository.save(note);

        noteSearchRepository.save(updatedNote.toDocument());

        return noteMapper.toResponse(updatedNote);
    }

    @Transactional
    public void delete(Long noteId) {
        noteRepository.deleteById(noteId);
        noteSearchRepository.deleteById(noteId);
    }

    @Transactional
    public NoteResponse addReferences(Long noteId, List<Long> noteIds) {
        noteIds.forEach(referenceId -> noteRepository.addReferences(noteId, referenceId));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note with id does not exist"));

        noteIds.forEach(refId -> noteRepository.findById(refId)
                .ifPresent(refNote -> noteSearchRepository.save(refNote.toDocument())));

        noteSearchRepository.save(note.toDocument());

        return noteMapper.toResponse(note);
    }


    @Transactional
    public void removeReferences(Long noteId, List<Long> noteIds) {
        noteIds.forEach(referenceId -> noteRepository.removeReference(noteId, referenceId));
    }

    @Transactional
    public NoteResponse manageTags(Long noteId, List<Long> tagIds, boolean add) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        List<Tag> tags = tagRepository.findAllById(tagIds);

        if (add) {
            note.getTags().addAll(tags);
        } else {
            tags.forEach(note.getTags()::remove);
        }

        noteRepository.save(note);
        noteSearchRepository.save(note.toDocument());

        return noteMapper.toResponse(note);
    }
}
