package com.example.ksa.classes

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.*
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.*

@Serializable(with = CurrentMemberDeserializer::class)
@Entity(tableName = "currentAccount")
data class CurrentMember(
    @PrimaryKey
    val id: Int,
)



@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = CurrentMember::class)
object CurrentMemberDeserializer : DeserializationStrategy<CurrentMember> {
    override fun deserialize(decoder: Decoder): CurrentMember {
        val json = decoder.decodeSerializableValue(JsonElement.serializer())
        val id  = json
            .jsonObject["data"]
            ?.jsonObject
            ?.get("id")
            ?.jsonPrimitive
            ?.intOrNull
            ?: throw Exception("Expected 'id' field in 'data' object")
        return CurrentMember(id)
    }
}





