package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.api.ApiService
import com.example.myapplication.api.RetrofitClient
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.TransactionDao
import com.example.myapplication.data.models.Transaction
import com.example.myapplication.data.models.TransactionEntity
import com.example.myapplication.data.models.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * El Repositorio es la "Fuente Única de Verdad".
 * La UI le pide datos a él, y él decide si los saca
 * de la base de datos (Room) o de la API (Retrofit).
 */
class TransactionRepository(context: Context) {

    // 1. Conexión a nuestras fuentes de datos
    private val apiService: ApiService = RetrofitClient.instance
    private val db: AppDatabase = AppDatabase.getDatabase(context)
    private val transactionDao: TransactionDao = db.transactionDao()

    // La API Key
    private val apiKey = "123456789"

    // --- 2. FUNCIÓN PARA EL HEADER (Balance, Gasto) ---
    // (Esta no usa Room, solo llama a la API)
    suspend fun getUserData(userId: Int): Response<UserResponse> {
        // Usamos withContext(Dispatchers.IO) para hacer la llamada de red
        // en un hilo secundario (buena práctica).
        return withContext(Dispatchers.IO) {
            apiService.getUserData(apiKey, userId)
        }
    }

    // --- 3. FUNCIÓN PARA LA LISTA DE TRANSACCIONES (La importante) ---

    /**
     * Esta función le da a la UI un "Flow" (flujo) de datos desde Room.
     * La UI se suscribe a esto y se actualiza automáticamente
     * cada vez que Room cambia.
     */
    fun getTransactionsFromDb(): Flow<List<TransactionEntity>> {
        return transactionDao.getAll()
    }

    /**
     * Esta función trae datos frescos de la API y los guarda en Room.
     * La UI (que está escuchando a "getTransactionsFromDb()")
     * se actualizará sola cuando esto termine.
     */
    suspend fun refreshTransactionsFromApi() {
        withContext(Dispatchers.IO) {
            try {
                // 1. Llama a la API (Retrofit)
                val response = apiService.getTransactions(apiKey)

                if (response.isSuccessful && response.body() != null) {
                    val transactionsFromApi = response.body()!!

                    // 2. Mapea/Convierte los datos de la API a la Entidad de Room
                    val entities = mapApiToEntity(transactionsFromApi.bankAccountTransactions + transactionsFromApi.creditCardTransactions)

                    // 3. Borra los datos viejos de Room
                    transactionDao.deleteAll()

                    // 4. Inserta los datos nuevos en Room
                    transactionDao.insertAll(entities)
                } else {
                    // Manejar el error de API (ej. loguearlo)
                    throw Exception("API Error: ${response.message()}")
                }
            } catch (e: Exception) {
                // Manejar error de red
                // (No borramos la caché de Room si la API falla)
                e.printStackTrace()
            }
        }
    }

    // --- 4. FUNCIÓN "AYUDANTE" DE MAPEO ---
    /**
     * Convierte una lista de "Transaction" (de la API)
     * a una lista de "TransactionEntity" (para Room).
     */
    private fun mapApiToEntity(apiTransactions: List<Transaction>): List<TransactionEntity> {
        return apiTransactions.map { apiTransaction ->
            TransactionEntity(
                id = apiTransaction.id,
                title = apiTransaction.getTitle(),
                category = apiTransaction.getCategory(),
                amount = apiTransaction.amount,
                date = apiTransaction.getFormattedDate(),
                icon = apiTransaction.getIconResource(),
                type = apiTransaction.type
            )
        }
    }
}