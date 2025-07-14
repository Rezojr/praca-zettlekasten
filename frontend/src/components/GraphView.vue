<template>
  <q-page>
    <q-card class="q-pa-md">
      <q-card-section>
        <div class="text-h6 text-bold">Graph View</div>
      </q-card-section>

      <!-- Miejsce na kontener grafu -->
      <div ref="graphContainer" class="q-pa-md" style="height: 500px;"></div>

      <!-- Legenda -->
      <div class="q-pa-md" style="position: absolute; bottom: 10px; right: 10px;">
        <div class="q-mb-xs">
          <span class="text-h6">Legend</span>
        </div>
        <div class="q-mb-xs">
          <q-chip color="blue" label="Node (Main Note)" class="q-ma-xs" />
        </div>
        <div class="q-mb-xs">
          <q-chip color="green" label="Reference" class="q-ma-xs" />
        </div>
        <div class="q-mb-xs">
          <q-chip color="orange" label="Tags" class="q-ma-xs" />
        </div>
        <div class="q-mb-xs">
          <q-chip color="lightblue" label="References Relation" class="q-ma-xs" />
        </div>
        <div class="q-mb-xs">
          <q-chip color="lightcoral" label="Referenced Relation" class="q-ma-xs" />
        </div>
      </div>
    </q-card>
  </q-page>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { Network } from 'vis-network/standalone/esm/vis-network';
import { useNoteStore } from 'stores/noteStore'; // Importujemy store
import type { Note } from 'stores/noteStore';

// Definiujemy props, przyjmujemy notatkę jako prop
const props = defineProps<{
  note: Note | null; // Przekazujemy pojedynczą notatkę
}>();

// Referencja do kontenera
const graphContainer = ref<HTMLElement | null>(null);

// Pobieramy instancję store'a
const noteStore = useNoteStore();

// Funkcja do rysowania grafu
onMounted(() => {
  if (props.note && graphContainer.value) {
    const note = props.note;
    const notes = noteStore.notes; // Pobieramy wszystkie notatki

    // Zbieramy węzły (wszystkie notatki)
    const nodes = notes.map(note => ({
      id: note.id,
      label: note.title,
      color: note.id === props.note?.id ? '#1E90FF' : '#32CD32', // Kolor dla głównej notatki (niebieski) i referencji (zielony)
      title: note.id === props.note?.id ? 'Main Note' : 'Reference',
    }));

    // Zbieramy krawędzie (relacje references i referenced)
    const edges = [
      ...note.references?.map((ref) => ({
        from: note.id,
        to: ref.id,
        color: { color: '#ADD8E6' }, // Jasny niebieski dla relacji "references"
        type: 'references',
        arrows: 'to',
      })) || [],
      ...note.referenced?.map((ref) => ({
        from: ref.id,
        to: note.id,
        color: { color: '#F08080' }, // Jasny czerwony dla relacji "referenced"
        type: 'referenced',
        arrows: 'to',
      })) || [],
    ];

    // Zbieramy tagi (tags) tylko z props.note i dodajemy je do węzłów oraz relacji z nimi
    // Zbieramy tagi (tags) tylko z props.note i dodajemy je do węzłów oraz relacji z nimi
    const tagNodes = note.tags?.map((tag) => ({
      id: `tag-${tag.id}`, // Unikalne id dla każdego tagu
      label: tag.label, // Gwarantujemy, że label jest typu string
      color: '#FFA500', // Kolor tagu (pomarańczowy)
      title: 'Tag',
    })) || [];

    console.log(tagNodes)

    const tagEdges = note.tags?.map((tag) => ({
      from: note.id,
      to: `tag-${tag.id}`,
      color: { color: '#FFA07A' }, // Relacja tagu (jasny pomarańczowy)
      type: 'tag',
      arrows: 'to',
    })) || [];

    // Przygotowanie danych dla grafu
    const graphData = {
      nodes: [...nodes, ...tagNodes],
      edges: [...edges, ...tagEdges],
    };

    // Opcje grafu
    // Opcje grafu
// Opcje grafu
    const options = {
      nodes: {
        shape: 'dot',
        size: 15,
        font: {
          size: 16, // Zwiększamy rozmiar czcionki
          color: '#D3D3D3', // Ustawiamy jaśniejszy kolor czcionki (jasnoszary)
          face: 'arial', // Można zmienić na inną czcionkę, np. "verdana"
          align: 'center', // Wyrównanie tekstu w środku węzła
        },
        // Zwiększamy odstępy między węzłami
        margin: {
          top: 40,
          right: 40,
          bottom: 40,
          left: 40
        },
      },
      edges: {
        width: 2,
        smooth: {
          enabled: true,
          type: 'continuous',
          roundness: 0.5,
        },
        color: { color: '#000000' }, // Domyślny kolor krawędzi (czarny)
        arrows: { to: { enabled: true, scaleFactor: 0.5 } },
      },
      physics: {
        enabled: true,
      },
      layout: {
        randomSeed: 2,
        improvedLayout: true,
        hierarchical: {
          enabled: false, // Można włączyć, jeśli chce się mieć układ hierarchiczny
        },
      },
    };



    // Inicjalizacja grafu
    new Network(graphContainer.value, graphData, options);
  }
});
</script>

<style scoped>
/* Stylowanie grafu */
.q-page {
  position: relative;
}

.q-chip {
  font-weight: bold;
  font-size: 14px;
}

.q-card {
  position: relative;
}
</style>
