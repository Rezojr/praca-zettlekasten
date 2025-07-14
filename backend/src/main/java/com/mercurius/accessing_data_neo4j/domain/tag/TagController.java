package com.mercurius.accessing_data_neo4j.domain.tag;

import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteResponse;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagRequest;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagResponse;
import com.mercurius.accessing_data_neo4j.domain.tag.dto.TagWithNotesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        List<TagResponse> tags = tagService.getAll();
        if (tags.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(tags);
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagWithNotesResponse> getById(@PathVariable Long tagId) {
        try {
            return ResponseEntity.ok(tagService.getById(tagId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<TagResponse> create(@RequestBody TagRequest tagRequest) {
        try {
            TagResponse tagResponse = tagService.create(tagRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(tagResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagResponse> update(@PathVariable Long tagId, @RequestBody String label) {
        try {
            TagResponse tagResponse = tagService.update(tagId, label);
            return ResponseEntity.ok(tagResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> delete(@PathVariable Long tagId) {
        try {
            tagService.delete(tagId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{tagId}/notes")
    public List<NoteResponse> getNotesByTag(@PathVariable Long tagId) {
        return tagService.getNotesByTag(tagId);
    }
}
