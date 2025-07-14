<template>
  <q-card
    class="q-pa-md"
    style="max-width: 75%; max-height: 80%; display: flex; flex-direction: column"
  >
    <q-item-label class="text-center text-h6 q-mb-lg">Create tag</q-item-label>
    <q-form
      @submit="onSubmit"
      @reset="onReset"
      class="q-gutter-md"
      style="display: flex; flex-direction: column; flex-grow: 1"
    >
      <q-input
        filled
        v-model="tag.label"
        label="Tag label"
        lazy-rules
        :rules="[(val) => (val && val.length > 0) || 'Title field cannot be null']"
        class="w-full"
      />

      <div style="margin-top: auto; margin-left: auto; margin-right: auto">
        <q-btn label="Submit" type="submit" color="primary" />
        <q-btn label="Reset" type="reset" color="primary" flat class="q-ml-sm" />
      </div>
    </q-form>
  </q-card>
</template>

<script setup lang="ts">
import { defineEmits, ref } from 'vue'
import { useTagStore } from 'stores/tagStore'

const emit = defineEmits(['tag-added'])
const tagStore = useTagStore()

const tag = ref({
  label: '',
})

const onSubmit = async () => {
  try {
    await tagStore.addTag(tag.value)
    emit('tag-added')
  } catch (error) {
    console.error('Failed to add note:', error)
  }
}

const onReset = () => {
  tag.value = {
    label: '',
  }
}
</script>
