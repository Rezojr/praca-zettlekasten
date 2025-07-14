import { defineStore } from 'pinia'
import axios from 'axios'
import type { Note } from 'stores/noteStore'

export interface Tag {
  id: number
  label: string
  notes: Note[]
}

export interface AddEditTag {
  label: string
}

export interface UpdateNoteTags {
  tagIds: number[],
  add: boolean
}

export const useTagStore = defineStore('tagStore', {
  state: () => ({
    tags: [] as Tag[],
  }),
  actions: {
    async fetchTags() {
      try {
        const response = await axios.get<Tag[]>('http://localhost:8080/tags')
        this.tags = response.data
        return this.tags
      } catch (error) {
        console.error('Unable to fetch data %s', error)
      }
    },
    async fetchTagById(id: number) {
      try {
        const response = await axios.get<Tag>('http://localhost:8080/tags/' + id);
        return response.data || null;
      } catch (error) {
        console.error('Unable to fetch data %s', error);
        return null;
      }
    },
    async addTag(tag: AddEditTag) {
      try {
        const response = await axios.post<Tag>('http://localhost:8080/tags', tag)
        this.tags.push(response.data)
        return response.data
      } catch (error) {
        console.error('Unable to add tag %s', error)
      }
    }
  },
})
