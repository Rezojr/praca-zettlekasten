<template>
  <q-item-label header>
    <div class="row items-center justify-between">
      <span>Notes List</span>
      <div>
        <q-btn
          round
          color="purple-8"
          dense
          icon="search"
          class="q-mr-sm"
          @click="showSearchDialog = true"
        />
        <q-btn round color="purple-8" dense icon="add" @click="showAddDialog = true" />
      </div>
    </div>
  </q-item-label>
  <q-list dense padding>
    <q-item-label v-for="(group, key) in groupedNotes" :key="key">
      <q-expansion-item
        :label="String(key)"
        expand-separator
        dense
        dense-toggle
        header-class="text-purple"
      >
        <q-list>
          <q-item v-for="note in group" :key="note.id" clickable @click="selectNote(note)" dense>
            <q-item-section>
              <q-item-label>{{ note.title }}</q-item-label>
            </q-item-section>
          </q-item>
        </q-list>
      </q-expansion-item>
    </q-item-label>
  </q-list>

  <q-dialog v-model="showAddDialog" maximized>
    <AddNoteComponent @note-added="onNoteAdded" />
  </q-dialog>

  <q-dialog v-model="showSearchDialog">
    <q-card
      class="q-pa-md"
      style="max-width: 75%; max-height: 80%; display: flex; flex-direction: column"
    >
      <q-card-section>
        <q-input
          v-model="searchQuery"
          label="Search notes"
          @update:model-value="searchNotes"
          filled
          clearable
        />
      </q-card-section>
      <q-card-section>
        <div v-if="searchResults.length" class="search-results-container">
          <q-item-label class="text-h6 q-mb-md">Search Results</q-item-label>
          <q-list>
            <q-item
              v-for="result in searchResults"
              :key="result.id"
              clickable
              @click="scrollToFragment(result)"
            >
              <q-item-section>
                <q-item-label class="text-h6 text-primary q-mb-xs">{{ result.title }}</q-item-label>
                <q-item-label v-for="(fragment, index) in result.highlightedContent" :key="index">
                  <span v-html="highlightText(fragment)"></span>
                </q-item-label>
              </q-item-section>
            </q-item>
          </q-list>
        </div>
        <div v-else>
          <p>No results found</p>
        </div>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { Note, NoteDocument } from 'stores/noteStore'
import { useNoteStore } from 'stores/noteStore'
import { useRouter } from 'vue-router'
import AddNoteComponent from 'components/notes/AddNoteComponent.vue'

const noteStore = useNoteStore()
const router = useRouter()
const showAddDialog = ref(false)
const showSearchDialog = ref(false)
const searchQuery = ref('')
const searchResults = ref<NoteDocument[]>([])

onMounted(() => {
  noteStore.fetchNotes()
})

const searchNotes = async () => {
  if (!searchQuery.value) {
    searchResults.value = []
    return
  }
  searchResults.value = await noteStore.searchNotes(searchQuery.value)
}

const groupedNotes = computed(() => {
  const grouped: { [key: string]: Note[] } = {}
  noteStore.notes.forEach((note) => {
    const noteType = note.noteType
    if (!grouped[noteType]) {
      grouped[noteType] = []
    }
    grouped[noteType].push(note)
  })
  return grouped
})

const scrollToFragment = (result: NoteDocument) => {
  showSearchDialog.value = false
  router.push({ name: 'NoteDetails', params: { id: result.id } }).then(() => {
    setTimeout(() => {
      const element = document.getElementById(`note-${result.id}`)
      if (element) {
        element.scrollIntoView({ behavior: 'smooth', block: 'center' })
        element.style.backgroundColor = 'yellow'
        setTimeout(() => {
          element.style.backgroundColor = ''
        }, 2000)
      }
    }, 500)
  })
}

const highlightText = (text: string) => {
  return text.replace(/<b>(.*?)<\/b>/gi, '<span class="bg-yellow-2 text-black">$1</span>');
}


const onNoteAdded = () => {
  showAddDialog.value = false
}

const selectNote = (note: Note) => {
  router.push({ name: 'NoteDetails', params: { id: note.id } })
}
</script>

<style scoped>
.search-results-container {
  padding: 10px;
}

.q-item-label span {
  display: inline-block;
  padding: 5px;
}

.q-item-label .highlight {
  background-color: yellow;
  font-weight: bold;
}

/* Styl q-dialog: pozycjonowanie wyżej i szersza szerokość */
.custom-dialog {
  margin-top: 20px; /* Wyższa pozycja */
}
</style>
