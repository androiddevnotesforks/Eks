package ir.fallahpoor.eks.data.database

import androidx.room.*
import ir.fallahpoor.eks.data.entity.Library

@Dao
interface LibraryDao {

    @Query("SELECT COUNT(*) FROM ${DatabaseContract.TABLE_NAME}")
    suspend fun getLibrariesCount(): Int

    @Query("SELECT * FROM ${DatabaseContract.TABLE_NAME} WHERE ${DatabaseContract.FIELD_NAME} LIKE '%' || :searchQuery || '%' ORDER BY ${DatabaseContract.FIELD_NAME} ASC")
    suspend fun getLibraries(searchQuery: String = ""): List<Library>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLibraries(libraries: List<Library>)

    @Query("DELETE FROM ${DatabaseContract.TABLE_NAME}")
    suspend fun deleteLibraries()

    @Update
    suspend fun updateLibrary(library: Library)

}