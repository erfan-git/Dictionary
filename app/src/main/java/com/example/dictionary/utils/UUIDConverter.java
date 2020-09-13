package com.example.dictionary.utils;

import androidx.room.TypeConverter;

import java.util.UUID;

public class UUIDConverter {
    @TypeConverter
    public String UUIDtoString(UUID value){
        return value.toString();
    }
    @TypeConverter
    public UUID fromString(String value){
        return UUID.fromString(value);
    }
}
