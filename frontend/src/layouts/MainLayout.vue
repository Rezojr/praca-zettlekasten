<template>
  <q-layout view="lHh Lpr lFf">
    <AppHeader v-model:left-drawer-open="leftDrawerOpen"/>
    <AppDrawer v-model="leftDrawerOpen"/>
    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
import AppHeader from 'components/app/AppHeader.vue';
import AppDrawer from 'components/app/AppDrawer.vue';
import { ref } from 'vue'

const leftDrawerOpen = ref<boolean>(true)

</script>
