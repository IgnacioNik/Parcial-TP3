package com.example.myapplication.data.models

import com.google.gson.*
import java.lang.reflect.Type

class NameAdapter : JsonDeserializer<String> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String {
        // Si es un objeto (ej: { "first": "Juan", "last": "Pérez" })
        return if (json != null && json.isJsonObject) {
            val obj = json.asJsonObject
            val first = obj["first"]?.asString ?: ""
            val last = obj["last"]?.asString ?: ""
            "$first $last".trim()
        }
        // Si ya es un string ("Juan Pérez")
        else {
            json?.asString ?: ""
        }
    }
}