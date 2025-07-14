<template>
  <q-page>
    <q-card v-if="note" class="q-pa-md">
      <q-card-section>
        <div class="text-h6 text-bold">{{ note.title }}</div>
        <div class="text-subtitle2 text-grey-7">Type: {{ note.noteType }}</div>
        <q-card-section>
          <div class="text-h6">Tags</div>
          <q-chip
            v-for="tag in tags"
            :key="tag.id"
            size="md"
            removable
            clickable
            @click="selectTag(tag)"
            @remove="removeTag(tag.id)"
            class="bg-purple-6 text-white"
          >
            {{ tag.label }}
          </q-chip>

          <q-chip
            clickable
            icon="add"
            @click="addingTag = true"
            color="grey-5"
            text-color="black"
            v-if="!addingTag"
          >
            Add tag
          </q-chip>

          <q-chip
            clickable
            icon="search"
            @click="showTagSearch = true"
            color="grey-5"
            text-color="black"
            v-if="!addingTag"
          >
            Search tags
          </q-chip>

          <q-input
            v-else
            v-model="newTag"
            autofocus
            dense
            filled
            placeholder="Nowy tag..."
            @keyup.enter="addTag"
            @blur="addingTag = false"
            class="q-mt-sm"
          />
        </q-card-section>
      </q-card-section>

      <q-separator />

      <q-card-section class="scrollable-content">
        <div class="text-body1" v-html="renderMarkdown(note.content)"></div>
      </q-card-section>

      <q-card-section>
        <q-select
          v-model="selectedReferences"
          :options="availableNotes"
          multiple
          use-chips
          label="Add reference to different notes"
          emit-value
          map-options
        />
        <q-btn
          icon="save"
          label="Add References"
          color="primary"
          flat
          @click="addReferences(selectedReferences)"
        />
      </q-card-section>

      <q-expansion-item
        icon="expand_more"
        expand-separator
        v-if="note?.references && note.references.length > 0"
        class="q-pa-md"
      >
        <template v-slot:header>
          <div class="text-h6 text-bold">References</div>
        </template>
        <q-list>
          <q-item
            v-for="(reference, index) in note.references"
            :key="index"
            dense
            @click="selectNote(reference.id)"
            clickable
          >
            <q-item-section>
              <div class="text-h6 text-weight-bold">{{ reference.title }}</div>
              <div class="text-caption text-grey-7">{{ reference.noteType }}</div>
            </q-item-section>
          </q-item>
        </q-list>
      </q-expansion-item>

      <q-expansion-item
        icon="expand_more"
        expand-separator
        v-if="note?.referenced && note.referenced.length > 0"
        class="q-pa-md"
      >
        <template v-slot:header>
          <div class="text-h6 text-bold">Referenced By</div>
        </template>
        <q-list>
          <q-item
            v-for="(referencedBy, index) in note.referenced"
            :key="index"
            dense
            @click="selectNote(referencedBy.id)"
            clickable
          >
            <q-item-section>
              <div class="text-h6 text-weight-bold">{{ referencedBy.title }}</div>
              <div class="text-caption text-grey-7">{{ referencedBy.noteType }}</div>
            </q-item-section>
          </q-item>
        </q-list>
      </q-expansion-item>

      <q-separator />

      <q-card-actions align="right">
        <q-btn icon="edit" label="Edit" color="primary" flat @click="showEditDialog = true" />
        <q-btn icon="delete" label="Delete" color="negative" flat @click="confirm = true" />
        <q-btn
          icon="account_tree"
          label="View Graph"
          color="secondary"
          flat
          @click="showGraphDialog = true"
        />
      </q-card-actions>
    </q-card>

    <div v-else-if="loading" class="text-center q-mt-md">
      <q-spinner color="primary" />
      <p>Loading note...</p>
    </div>

    <div v-else-if="error" class="text-center text-negative q-mt-md">
      <q-icon name="error" size="2em" />
      <p>{{ error }}</p>
    </div>

    <q-dialog v-model="showEditDialog" maximized>
      <EditNoteComponent :note="note" />
    </q-dialog>

    <q-dialog v-model="confirm">
      <q-card>
        <q-card-section class="row items-center">
          <q-avatar icon="delete" color="primary" text-color="white" />
          <span class="q-ml-sm">Are you sure you want to delete this note?</span>
        </q-card-section>

        <q-card-actions align="right">
          <q-btn flat label="Cancel" color="primary" v-close-popup />
          <q-btn flat label="Remove" color="negative" v-close-popup @click="deleteNote" />
        </q-card-actions>
      </q-card>
    </q-dialog>
    <q-dialog v-model="showTagSearch" maximized>
      <AddTagSearchDialog @tag-selected="addTagFromSearch" />
    </q-dialog>
    <q-dialog v-model="showGraphDialog" maximized>
      <q-card>
        <q-card-section>
          <q-btn flat label="Close" color="primary" @click="showGraphDialog = false" />
          <GraphView :note="note" />
        </q-card-section>
      </q-card>
    </q-dialog>

  </q-page>
</template>

<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { Note } from 'stores/noteStore'
import { NoteType, useNoteStore } from 'stores/noteStore'
import type { Tag } from 'stores/tagStore'
import { useTagStore } from 'stores/tagStore'
import EditNoteComponent from 'components/notes/EditNoteComponent.vue'
import { marked } from 'marked'
import AddTagSearchDialog from 'components/tags/AddTagSearchDialog.vue'
import GraphView from 'components/GraphView.vue'

const showEditDialog = ref(false)
const confirm = ref(false)
const route = useRoute()
const router = useRouter()
const noteStore = useNoteStore()
const tagStore = useTagStore()
const note = ref<Note | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)
const selectedReferences = ref<number[]>([])
const availableNotes = computed(() =>
  noteStore.notes
    .filter((n) =>
      n.id !== note.value?.id &&
      !note.value?.references.some(ref => ref.id === n.id) &&
      !note.value?.referenced.some(ref => ref.id === n.id)
    )
    .map((n) => ({
      label: n.title,
      value: n.id,
      noteType: n.noteType as NoteType,
    }))
)

const newTag = ref('')
const addingTag = ref(false)
const showTagSearch = ref(false)
const showGraphDialog = ref(false);

const tags = computed(() => note.value?.tags || [])

onMounted(() => {
  noteStore.fetchNotes()
  tagStore.fetchTags()
})

watch(
  () => route.params.id,
  async (newId) => {
    if (newId) {
      await fetchData(Number(newId))
    }
  },
  { immediate: true },
)

async function fetchData(noteId: number) {
  error.value = null
  note.value = null
  loading.value = true

  try {
    note.value = await noteStore.fetchNoteById(noteId)
  } catch (err) {
    error.value = err instanceof Error ? err.message : String(err)
  } finally {
    loading.value = false
  }
}

const deleteNote = async () => {
  if (note.value) {
    try {
      await noteStore.deleteNote(note.value.id)
      router.push({ name: 'Home' })
    } catch (err) {
      error.value = err instanceof Error ? err.message : String(err)
    }
  }
}

const addReferences = async (references: number[]) => {
  if (note.value) {
    try {
      await noteStore.addReferences(note.value.id, references)
      note.value.references = [
        ...note.value.references,
        ...references.map((id) => {
          const foundNote = availableNotes.value.find((note) => note.value === id)
          return {
            id,
            title: foundNote?.label || 'Unknown',
            noteType: foundNote?.noteType ?? NoteType.BRIEF_THOUGHT,
          }
        }),
      ]
      selectedReferences.value = []
    } catch (err) {
      error.value = err instanceof Error ? err.message : String(err)
    }
  }
}

const renderMarkdown = (markdown: string) => {
  return marked(markdown)
}

const selectNote = (id: number) => {
  router.push({ name: 'NoteDetails', params: { id: id } })
}

const addTag = async () => {
  if (!newTag.value.trim() || !note.value) return;

  try {
    const tagLabel = newTag.value.trim().toLowerCase();
    let tag = tagStore.tags.find(t => t.label.toLowerCase() === tagLabel);
    if (!tag) {
      tag = await tagStore.addTag({ label: tagLabel });
      if (!tag) {
        console.error("Failed to create tag.");
        return;
      }
    }

    await noteStore.updateNoteTags(note.value.id, { tagIds: [tag.id], add: true });

    if (!note.value.tags.some(t => t.id === tag!.id)) {
      note.value.tags.push(tag);
    }

    newTag.value = '';
    addingTag.value = false;
  } catch (err) {
    console.error("Error adding tag:", err);
  }
};


const removeTag = async (tagId: number) => {
  if (!note.value) return;

  try {
    await noteStore.updateNoteTags(note.value.id, { tagIds: [tagId], add: false });
    note.value.tags = note.value.tags.filter(tag => tag.id !== tagId);
  } catch (err) {
    console.error("Error removing tag:", err);
  }
};

const addTagFromSearch = async (tag: Tag) => {
  if (!note.value) return;

  try {
    await noteStore.updateNoteTags(note.value.id, { tagIds: [tag.id], add: true });
    if (!note.value.tags.some(t => t.id === tag.id)) {
      note.value.tags.push(tag);
    }
    showTagSearch.value = false;
  } catch (err) {
    console.error("Error adding tag from search:", err);
  }
};

const selectTag = (tag: Tag) => {
  router.push({ name: 'TagDetails', params: { id: tag.id } })
}
</script>
