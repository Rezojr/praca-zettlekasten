<template>
  <q-page>
    <q-card v-if="tag" class="q-pa-md">
      <q-card-section>
        <div class="text-h6 text-bold">{{ tag.label }}</div>
        <div class="text-subtitle2 text-grey-7">Type: {{ tag.label }}</div>
      </q-card-section>
      <q-card-section v-if="tag.notes && tag.notes.length">
        <div class="text-subtitle1 text-bold">Notes:</div>
        <q-list bordered separator>
          <q-item v-for="(note, index) in tag.notes" :key="index" clickable @click="selectNote(note)">
            <q-item-section>
              <div class="text-h6 text-weight-bold">{{ note.title }}</div>
              <div class="text-caption text-grey-7">{{ note.noteType }}</div>
            </q-item-section>
          </q-item>
        </q-list>
      </q-card-section>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { Tag } from 'stores/tagStore'
import { useTagStore } from 'stores/tagStore'
import type { Note } from 'stores/noteStore'

const route = useRoute()
const router = useRouter()
const tagStore = useTagStore()

const tag = ref<Tag | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)

watch(
  () => route.params.id,
  async (newId) => {
    if (newId) {
      await fetchData(Number(newId))
    }
  },
  { immediate: true },
)

async function fetchData(tagId: number) {
  error.value = null
  tag.value = null
  loading.value = true

  try {
    tag.value = await tagStore.fetchTagById(tagId)
  } catch (err) {
    error.value = err instanceof Error ? err.message : String(err)
  } finally {
    loading.value = false
  }
}

const selectNote = (note: Note) => {
  router.push({ name: 'NoteDetails', params: { id: note.id } })
}
</script>
