package ir.fallahpoor.eks.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.fallahpoor.eks.data.entity.LibraryEntity

@Database(entities = [LibraryEntity::class], version = 1)
abstract class LibraryDatabase : RoomDatabase() {
    abstract fun libraryDao(): LibraryDao
}