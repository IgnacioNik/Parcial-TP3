package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.models.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    // Le dice a Room cuál es nuestro DAO
    abstract fun transactionDao(): TransactionDao

    companion object {
        // Volatile asegura que la instancia sea siempre la más actualizada
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // "synchronized" evita que dos hilos creen la BD al mismo tiempo
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_app_database" // Nombre del archivo de la BD
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}