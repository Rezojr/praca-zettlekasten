package com.mercurius.accessing_data_neo4j.domain.note;

import org.springframework.data.neo4j.core.mapping.callback.BeforeBindCallback;
import org.springframework.stereotype.Component;

@Component
public class NoteCallback implements BeforeBindCallback<Note> {

    @Override
    public Note onBeforeBind(Note note) {
        note.getTags().forEach(tag -> tag.getNotes().add(note));
        return note;
    }
}
