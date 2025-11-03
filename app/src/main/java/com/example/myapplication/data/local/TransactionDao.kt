package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.models.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    /**
     * Trae TODAS las transacciones de la tabla.
     * Devuelve un "Flow", lo que significa que la UI se
     * actualizará automáticamente cada vez que los datos cambien.
     */
    @Query("SELECT * FROM transactions")
    fun getAll(): Flow<List<TransactionEntity>>

    /**
     * Inserta una lista de transacciones.
     * Si una transacción ya existe (mismo ID), la reemplaza.
     */
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    /**
     * Borra todas las transacciones.
     * (Lo usaremos para limpiar la tabla antes de insertar datos nuevos).
     */
    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}
