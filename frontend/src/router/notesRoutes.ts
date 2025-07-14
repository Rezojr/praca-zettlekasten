import NoteDetails from 'components/notes/NoteDetails.vue'

export const notesRoutes = [
  {
    path: 'notes/:id',
    name: 'NoteDetails',
    component: NoteDetails,
  },
  {
    path: '/graph/:noteId',
    name: 'GraphView',
    component: () => import('components/GraphView.vue')
  }
]
