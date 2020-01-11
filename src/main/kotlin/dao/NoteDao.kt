package dao

import Note

object NoteDao: BaseDao<Note>(Note::class.java)