<template>
  <q-card
    class="q-pa-md"
    style="max-width: 75%; max-height: 80%; display: flex; flex-direction: column"
  >
    <q-item-label class="text-center text-h6 q-mb-lg">Tag List</q-item-label>
    <q-input
      dense
      outlined
      v-model="searchQuery"
      placeholder="Search tags..."
      class="q-mb-sm custom-input"
      clearable
    />

    <div>
      <q-chip
        v-for="tag in filteredTags"
        size="md"
        :key="tag.id"
        clickable
        @click="selectTag(tag)"
        class="bg-purple-6 text-white"
      >
        #{{ tag.label }}
      </q-chip>
    </div>
  </q-card>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { Tag} from 'stores/tagStore';
import { useTagStore } from 'stores/tagStore'
import { useRouter } from 'vue-router'

const tagStore = useTagStore();
const router = useRouter();
const searchQuery = ref('');

onMounted(() => {
  tagStore.fetchTags();
});

const tags = computed(() => tagStore.tags);

const filteredTags = computed(() => {
  if (!searchQuery.value) {
    return tags.value;
  }
  return tags.value.filter(tag =>
    tag.label.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const selectTag = (tag: Tag) => {
  router.push({ name: 'TagDetails', params: { id: tag.id }})
};
</script>
