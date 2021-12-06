package com.mvlprem.apod.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the [DatabasePictures], [FavoritePictures] class with Room.
 */
@Dao
interface PicturesDao {

    /**
     * Returns all values from the table [DatabasePictures]
     */
    @Query("SELECT * FROM databasepictures")
    fun getPictures(): LiveData<List<DatabasePictures>>

    /**
     * Insert new data into [DatabasePictures],
     * @param pictures new value to write
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg pictures: DatabasePictures)

    /**
     * Deletes all values from the table [DatabasePictures]
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM databasepictures")
    fun deleteCache()

    /**
     * Returns all values from the table [FavoritePictures]
     */
    @Query("SELECT * FROM favoritepictures")
    fun getFavorites(): LiveData<List<FavoritePictures>>

    /**
     * Insert new data into [FavoritePictures],
     * @param pictures new value to write
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(vararg pictures: FavoritePictures)

    /**
     * Deletes all values from the table [FavoritePictures]
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM favoritepictures")
    fun deleteFavorites()
}

/**
 * A database with two tables that stores pictures information.
 * And a global method to get access to the database.
 */
@Database(entities = [DatabasePictures::class, FavoritePictures::class], version = 1)
abstract class PicturesDatabase : RoomDatabase() {

    /**
     * Connects the database to the DAO.
     */
    abstract val picturesDao: PicturesDao

    /**
     * Companion object allows us to add functions on the [PicturesDatabase] class.
     * We can call `PicturesDatabase.getDatabase(context)` to instantiate
     * a new PicturesDatabase.
     */
    companion object {
        /**
         * INSTANCE will keep a reference to any database returned via getDatabase.
         * This will help us avoid repeatedly initializing the database, which is expensive.
         */
        @Volatile
        private var INSTANCE: PicturesDatabase? = null

        /**
         * Function to get the database.
         * If a database has already been created, the previous database will be returned.
         * Otherwise, create a new database.
         * @param context The application context used to build database.
         */
        fun getDatabase(context: Context): PicturesDatabase {
            /**
             * synchronized. Only one thread may enter a synchronized block at a time.
             */
            synchronized(this) {
                var instance = INSTANCE
                /**
                 * If instance is `null` make a new database instance.
                 */
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PicturesDatabase::class.java,
                        "Pictures"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

