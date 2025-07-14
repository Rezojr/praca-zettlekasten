import type { RouteRecordRaw } from 'vue-router'
import MainLayout from 'layouts/MainLayout.vue'
import { notesRoutes } from './notesRoutes'
import { tagsRoutes } from './tagsRoutes'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Home',
    component: MainLayout,
    children: [
      ...notesRoutes,
      ...tagsRoutes,
    ],
  },
  {
    path: '/:catchAll(.*)*',
    component: () => import('pages/ErrorNotFound.vue'),
  },
]

export default routes
