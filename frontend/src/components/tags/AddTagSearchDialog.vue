<template>
  <q-card class="q-pa-md" style="max-width: 75%; max-height: 80%; display: flex; flex-direction: column">
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
import { computed, defineEmits, ref } from 'vue'
import type { Tag} from 'stores/tagStore';
import { useTagStore } from 'stores/tagStore'

const tagStore = useTagStore();
const searchQuery = ref('');
const filteredTags = computed(() => {
  return tagStore.tags.filter(tag =>
    tag.label.toLowerCase().includes(searchQuery.value.toLowerCase())
  );
});

const emit = defineEmits(['tag-selected']);


const selectTag = (tag: Tag) => {
  emit('tag-selected', tag);
};
</script>
