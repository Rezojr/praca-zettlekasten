package com.mercurius.accessing_data_neo4j.domain.tag;

import com.mercurius.accessing_data_neo4j.domain.note.Note;
import com.mercurius.accessing_data_neo4j.domain.note.NoteMapper;
import com.mercurius.accessing_data_neo4j.domain.note.NoteRepository;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteResponse;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagRequest;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagResponse;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagWithNotesResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;
    private final NoteMapper noteMapper;

    public List<TagResponse> getAll() {
        List<Tag> all = tagRepository.findAll();
        return tagMapper.mapAllToResponse(all);
    }

    public TagWithNotesResponse getById(Long id) {
        Tag entity = tagRepository.findById(id).orElse(null);
        TagWithNotesResponse response = new TagWithNotesResponse();
        if (entity != null) {
            response.setId(entity.getId());
            response.setLabel(entity.getLabel());
            response.setNotes(entity.getNotes().stream().map(noteMapper::toResponse).collect(Collectors.toList()));
        }
        return response;
    }

    @Transactional
    public TagResponse create(TagRequest tagRequest) {
        Tag entity = tagMapper.toEntity(tagRequest);
        return tagMapper.toResponse(tagRepository.save(entity));
    }

    @Transactional
    public TagResponse update(Long tagId, String label) {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag != null) {
            tag.setLabel(label);
        }
        return tagMapper.toResponse(tag);
    }

    public void delete(Long tagId) {
        tagRepository.deleteById(tagId);
    }

    public List<NoteResponse> getNotesByTag(Long tagId) {
        List<Note> notes = tagRepository.findNotesByTagId(tagId);
        return notes.stream()
                .map(noteMapper::toResponse)
                .collect(Collectors.toList());
    }
}
