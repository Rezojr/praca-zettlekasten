package com.mercurius.accessing_data_neo4j.domain.note;

import com.mercurius.accessing_data_neo4j.domain.note.dto.AddTagRequest;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteListResponse;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteRequest;
import com.mercurius.accessing_data_neo4j.domain.note.dto.NoteResponse;
import com.mercurius.accessing_data_neo4j.domain.note.elasticsearch.NoteDocument;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public ResponseEntity<List<NoteListResponse>> getAllNotes() {
        List<NoteListResponse> notes = noteService.getNotes();
        if (notes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(notes);
    }

    @GetMapping("/{noteId}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable Long noteId) {
        try {
            NoteResponse noteResponse = noteService.getNote(noteId);
            return ResponseEntity.ok(noteResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/search")
    public List<NoteDocument> searchNotes(@RequestParam String query) throws IOException {
        return noteService.searchNotesByContent(query);
    }

    @GetMapping("/{noteId}/relations/{depth}")
    public ResponseEntity<NoteResponse> getNoteRelations(@PathVariable Long noteId, @PathVariable Integer depth) {
        try {
            NoteResponse noteResponse = noteService.getNoteWithRelations(noteId, depth);
            return ResponseEntity.ok(noteResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<NoteResponse> addNote(@RequestBody NoteRequest noteRequest) {
        try {
            NoteResponse noteResponse = noteService.createNote(noteRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(noteResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{noteId}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable Long noteId, @RequestBody NoteRequest noteRequest) {
        try {
            NoteResponse noteResponse = noteService.updateNote(noteId, noteRequest);
            return ResponseEntity.ok(noteResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long noteId) {
        try {
            noteService.delete(noteId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{noteId}/references")
    public ResponseEntity<NoteResponse> addReferences(@PathVariable Long noteId, @RequestBody List<Long> noteIds) {
        try {
            NoteResponse updatedNote = noteService.addReferences(noteId, noteIds);
            return ResponseEntity.ok(updatedNote);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{noteId}/references")
    public ResponseEntity<Void> deleteReference(@PathVariable Long noteId, @RequestBody List<Long> noteIds) {
        try {
            noteService.removeReferences(noteId, noteIds);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{noteId}/tags")
    public ResponseEntity<NoteResponse> manageTags(@PathVariable Long noteId, @RequestBody AddTagRequest addTagRequest) {
        try {
            NoteResponse updatedNote = noteService.manageTags(noteId, addTagRequest.getTagIds(), addTagRequest.isAdd());
            return ResponseEntity.ok(updatedNote);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
