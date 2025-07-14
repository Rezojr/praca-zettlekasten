<template>
  <q-list class="custom-scroll" style="max-height: 300px; overflow-y: auto">
    <q-item-label header>
      <div class="row items-center justify-between">
        <span class="text-h6">Tags</span>
        <div>
          <q-btn
            round
            color="purple-8"
            dense
            icon="search"
            class="q-mr-sm"
            @click="showTagsListDialog = true"
          />
          <q-btn round color="purple-8" dense icon="add" @click="showAddDialog = true" />
        </div>
      </div>
    </q-item-label>
  </q-list>
  <q-dialog v-model="showTagsListDialog" maximized>
    <TagSearchDialog />
  </q-dialog>
  <q-dialog v-model="showAddDialog" maximized>
    <AddTagComponent @tag-added="onTagAdded" />
  </q-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import TagSearchDialog from 'components/tags/TagSearchDialog.vue'
import AddTagComponent from 'components/tags/AddTagComponent.vue'

const showAddDialog = ref(false)
const showTagsListDialog = ref(false)

const onTagAdded = () => {
  showAddDialog.value = false
}
</script>

<style scoped>
.custom-scroll {
  scrollbar-width: thin;
  scrollbar-color: var(--q-color-grey-4) var(--q-color-grey-2);
}

.custom-scroll::-webkit-scrollbar {
  width: 6px;
}

.custom-scroll::-webkit-scrollbar-thumb {
  background-color: var(--q-color-grey-4);
  border-radius: 3px;
}

.custom-scroll::-webkit-scrollbar-track {
  background-color: var(--q-color-grey-2);
}

.custom-input {
  background-color: var(--q-color-grey-1);
  border-radius: 8px;
  padding: 8px;
}

.custom-input .q-input__control {
  border-radius: 8px !important;
}

.custom-input .q-input__clear {
  color: var(--q-color-purple-8) !important;
}

.custom-input .q-input__control .q-placeholder {
  color: var(--q-color-grey-6);
}
</style>
