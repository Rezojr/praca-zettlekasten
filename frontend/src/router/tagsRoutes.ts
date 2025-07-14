export const tagsRoutes = [
  {
    path: 'tags/:id',
    name: 'TagDetails',
    component: () => import('components/tags/TagDetails.vue'),
  },
]
