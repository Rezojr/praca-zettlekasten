<template>
  <q-card
    class="q-pa-md"
    style="max-width: 75%; max-height: 80%; display: flex; flex-direction: column"
  >
    <q-item-label class="text-center text-h6 q-mb-lg">Edit Note</q-item-label>
    <q-form @submit="onSubmit" @reset="onReset" class="q-gutter-md">
      <q-input filled v-model="editedNote.title" label="Note title" class="w-full" />
      <q-select
        filled
        v-model="editedNote.noteType"
        :options="noteTypeOptions"
        label="Note Type"
        class="w-full"
      />
      <q-input
        filled
        v-model="editedNote.content"
        label="Note content"
        type="textarea"
        class="q-mt-md w-full"
        input-style="min-height: 200px;"
      />
      <q-btn label="Submit" type="submit" color="primary" />
      <q-btn label="Reset" type="reset" color="primary" flat />
    </q-form>
  </q-card>
  <q-dialog v-model="dialogVisible">
    <q-card>
      <q-card-section class="text-center">
        <div class="text-h6">Note Submitted Successfully!</div>
      </q-card-section>
      <q-card-actions>
        <q-btn label="Close" color="primary" @click="dialogVisible = false" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { defineProps, ref } from 'vue'
import type { Note} from 'stores/noteStore';
import { NoteType, useNoteStore } from 'stores/noteStore'

const props = defineProps<{
  note: Note | null | undefined
}>()

const editedNote = ref({
  title: '',
  content: '',
  noteType: NoteType.BRIEF_THOUGHT,
})

const noteStore = useNoteStore()
const dialogVisible = ref(false)
const noteTypeOptions = Object.values(NoteType)

if (props.note) {
  editedNote.value = {
    title: props.note.title,
    content: props.note.content,
    noteType: props.note.noteType,
  }
}

const onSubmit = async () => {
  try {
    if (props.note && props.note.id !== null) {
      await noteStore.editNote(props.note.id, editedNote.value)
    } else {
      console.warn('Note ID is missing or invalid')
    }
    dialogVisible.value = true
  } catch (error) {
    console.error('Failed to add note:', error)
  }
}

const onReset = () => {
  editedNote.value = {
    title: '',
    content: '',
    noteType: NoteType.BRIEF_THOUGHT,
  }
}
</script>
