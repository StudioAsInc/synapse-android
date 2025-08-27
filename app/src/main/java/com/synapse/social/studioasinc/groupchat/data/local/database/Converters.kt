package com.synapse.social.studioasinc.groupchat.data.local.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.synapse.social.studioasinc.groupchat.data.model.GroupSettings
import com.synapse.social.studioasinc.groupchat.data.model.MessageAttachment

class Converters {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromAttachmentList(attachments: List<MessageAttachment>): String {
        return gson.toJson(attachments)
    }
    
    @TypeConverter
    fun toAttachmentList(attachmentsString: String): List<MessageAttachment> {
        val listType = object : TypeToken<List<MessageAttachment>>() {}.type
        return gson.fromJson(attachmentsString, listType) ?: emptyList()
    }
    
    @TypeConverter
    fun fromStringLongMap(map: Map<String, Long>): String {
        return gson.toJson(map)
    }
    
    @TypeConverter
    fun toStringLongMap(mapString: String): Map<String, Long> {
        val mapType = object : TypeToken<Map<String, Long>>() {}.type
        return gson.fromJson(mapString, mapType) ?: emptyMap()
    }
    
    @TypeConverter
    fun fromStringStringListMap(map: Map<String, List<String>>): String {
        return gson.toJson(map)
    }
    
    @TypeConverter
    fun toStringStringListMap(mapString: String): Map<String, List<String>> {
        val mapType = object : TypeToken<Map<String, List<String>>>() {}.type
        return gson.fromJson(mapString, mapType) ?: emptyMap()
    }
    
    @TypeConverter
    fun fromGroupSettings(settings: GroupSettings): String {
        return gson.toJson(settings)
    }
    
    @TypeConverter
    fun toGroupSettings(settingsString: String): GroupSettings {
        return gson.fromJson(settingsString, GroupSettings::class.java) ?: GroupSettings()
    }
}