package com.example.hexsoftwares_todolist
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.hexsoftwares_todolist.TaskDataClass

class TaskDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "tasks.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "tasks"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_COMPLETED = "isCompleted"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_COMPLETED INTEGER DEFAULT 0
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // Create operation
    fun addTask(task: TaskDataClass): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, task.date)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        return db.insert(TABLE_NAME, null, values).also {
            db.close()
        }
    }

    // Read operation
    fun getTasksByDate(date: String): List<TaskDataClass> {
        val taskList = mutableListOf<TaskDataClass>()
        val db = this.readableDatabase
        val cursor: Cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_DATE = ?",
            arrayOf(date),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val task = TaskDataClass(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1
                )
                taskList.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return taskList
    }

    // Update operation
    fun updateTask(task: TaskDataClass): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_DATE, task.date)
            put(COLUMN_DESCRIPTION, task.description)
            put(COLUMN_COMPLETED, if (task.isCompleted) 1 else 0)
        }
        val result = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(task.id.toString())
        )
        db.close()
        return result
    }

    // Delete operation
    fun deleteTask(taskId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete("tasks", "id = ?", arrayOf(taskId.toString()))
        db.close()
        return result > 0 // Return true if at least one row was deleted
    }

}


