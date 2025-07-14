import { defineStore } from 'pinia'
import axios from 'axios'
import type { Tag, UpdateNoteTags } from 'stores/tagStore'

export interface Note {
  id: number
  title: string
  content: string
  noteType: NoteType
  references: NoteReference[]
  referenced: NoteReference[]
  tags: Tag[]
}

export interface NoteReference {
  id: number
  title: string
  noteType: NoteType
}

export interface AddEditNote {
  title: string
  content: string
  noteType: NoteType
}

export interface NoteDocument {
  id: number
  title: string
  content: string
  noteType: NoteType
  highlightedContent: string[]
}

export enum NoteType {
  MAIN = 'MAIN',
  DAILY = 'DAILY',
  BRIEF_THOUGHT = 'BRIEF_THOUGHT',
  BIBLIOGRAPHICAL = 'BIBLIOGRAPHICAL',
}

export const useNoteStore = defineStore('noteStore', {
  state: () => ({
    notes: [] as Note[],
    note: null as Note | null,
  }),
  actions: {
    async fetchNotes() {
      try {
        const response = await axios.get<Note[]>('http://localhost:8080/notes')
        this.notes = response.data.filter((note) => Object.values(NoteType).includes(note.noteType))
        return this.notes
      } catch (error) {
        console.error('Unable to fetch notes %s', error)
      }
    },
    async searchNotes(searchQuery: string): Promise<NoteDocument[]> {
      try {
        const response = await axios.get<NoteDocument[]>(
          `http://localhost:8080/notes/search?query=${encodeURIComponent(searchQuery)}`,
        )
        return response.data
      } catch (error) {
        console.error('Unable to search notes', error)
        return []
      }
    },
    async fetchNoteById(id: number): Promise<Note | null> {
      try {
        const response = await axios.get<Note>('http://localhost:8080/notes/' + id)
        console.log(response.data)
        return response.data || null
      } catch (error) {
        console.error('Unable to fetch note:', error)
        return null
      }
    },
    async addNote(newNote: AddEditNote) {
      try {
        const response = await axios.post<Note>('http://localhost:8080/notes', newNote)
        this.notes.push(response.data)
      } catch (error) {
        console.error('Unable to add note %s', error)
      }
    },
    async editNote(id: number, updateNote: AddEditNote) {
      try {
        const response = await axios.put<Note>('http://localhost:8080/notes/' + id, updateNote)
        const index = this.notes.findIndex((note) => note.id === id)
        if (index !== -1) {
          console.log(response.data)
          this.notes[index] = response.data
          this.note = {
            ...this.note,
            ...response.data,
          }
        }
      } catch (error) {
        console.error('Unable to add note %s', error)
      }
    },
    async deleteNote(id: number) {
      try {
        await axios.delete('http://localhost:8080/notes/' + id)
        const index = this.notes.findIndex((note) => note.id === id)
        if (index !== -1) {
          this.notes.splice(index, 1)
        }
      } catch (error) {
        console.error('Unable to delete note', error)
      }
    },
    async addReferences(id: number, noteIds: number[]) {
      try {
        const response = await axios.put<Note>(
          'http://localhost:8080/notes/' + id + '/references',
          noteIds,
        )

        const index = this.notes.findIndex((note) => note.id === id)
        if (index !== -1) {
          this.notes[index] = response.data
          this.note = response.data
        }
      } catch (error) {
        console.error('Unable to add references', error)
        throw error
      }
    },
    async updateNoteTags(id: number, tagUpdate: UpdateNoteTags) {
      try {
        const response = await axios.put<Note>(`http://localhost:8080/notes/${id}/tags`, tagUpdate)
        const index = this.notes.findIndex((note) => note.id === id)
        if (index !== -1) {
          this.notes[index] = response.data
          this.note = response.data
        }
      } catch (error) {
        console.error(`Unable to ${tagUpdate.add ? 'add' : 'remove'} tag for note ${id}:`, error)
      }
    },
  },
})
