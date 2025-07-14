<template>
  <q-header elevated :class="{ 'bg-blue-5': !darkMode, 'bg-purple-10': darkMode }">
    <q-toolbar>
      <q-btn
        flat
        dense
        round
        icon="menu"
        aria-label="Menu"
        @click="check"
      />
      <q-toolbar-title> Zettlekasten App </q-toolbar-title>
      <div>v 0.0.1</div>
      <q-toggle
        v-model="darkMode"
        label="Dark Mode"
        color="primary"
        class="q-ml-md"
        @update:model-value="toggleDarkMode"
      />
    </q-toolbar>
  </q-header>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useQuasar } from 'quasar'

const $q = useQuasar()
const darkMode = ref($q.dark.isActive)
const model = defineModel('leftDrawerOpen')

onMounted(() => {
  $q.dark.set(true);
  darkMode.value = true
});

const check = () => {
  model.value = !model.value
}

const toggleDarkMode = (value: boolean) => {
  $q.dark.set(value)
  darkMode.value = value
}
</script>
