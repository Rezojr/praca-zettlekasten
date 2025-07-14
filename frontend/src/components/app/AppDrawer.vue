<template>
  <q-drawer bordered>
    <q-list>
      <NotesList />
      <q-separator inset />
      <TagsList />
    </q-list>
  </q-drawer>
</template>

<script setup lang="ts">
import NotesList from 'components/notes/NotesList.vue'
import TagsList from 'components/tags/TagsList.vue'
</script>
