package com.lovfreshuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lovfreshuser.database.models.CartLocalDbModel


@Database(
    entities = [CartLocalDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class OfflineDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var instance: OfflineDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            OfflineDatabase::class.java, "lovefresh-client-app-database.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
