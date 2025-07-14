<template>
  <q-card
    class="q-pa-md"
    style="max-width: 75%; max-height: 80%; display: flex; flex-direction: column"
  >
    <q-item-label class="text-center text-h6 q-mb-lg">Add a Note</q-item-label>
    <q-form
      @submit="onSubmit"
      @reset="onReset"
      class="q-gutter-md"
      style="display: flex; flex-direction: column; flex-grow: 1"
    >
      <q-input
        filled
        v-model="note.title"
        label="Note title"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Title field cannot be null']"
        class="w-full"
      />
      <q-select
        filled
        v-model="note.noteType"
        :options="noteTypeOptions"
        label="Note Type"
        class="w-full"
      />
      <q-input
        filled
        v-model="note.content"
        label="Note content"
        hint="Enter the content of the note"
        class="q-mt-md w-full"
        type="textarea"
        input-style="min-height: 200px;"
        autogrow
      />

      <div style="margin-top: auto; margin-left: auto; margin-right: auto">
        <q-btn label="Submit" type="submit" color="primary" />
        <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
      </div>
    </q-form>
  </q-card>
</template>

<script setup lang="ts">
import { ref, defineEmits } from 'vue';
import type { AddEditNote} from 'stores/noteStore';
import { NoteType, useNoteStore } from 'stores/noteStore'

const emit = defineEmits(['note-added']);
const noteStore = useNoteStore();

const note = ref({
  title: '',
  content: '',
  noteType: NoteType.BRIEF_THOUGHT,
  linkedNotes: [],
  tags: [],
});

const noteTypeOptions = Object.values(NoteType);

const onSubmit = async () => {
  try {
    const newNote: AddEditNote = {
      title: note.value.title,
      content: note.value.content,
      noteType: note.value.noteType,
    };

    await noteStore.addNote(newNote);
    emit('note-added');
  } catch (error) {
    console.error('Failed to add note:', error);
  }
};

const onReset = () => {
  note.value = {
    title: '',
    content: '',
    noteType: NoteType.BRIEF_THOUGHT,
    linkedNotes: [],
    tags: [],
  };
};
</script>
