package ir.fallahpoor.eks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.fallahpoor.eks.data.entity.Library

@Database(entities = [Library::class], version = 1)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
}