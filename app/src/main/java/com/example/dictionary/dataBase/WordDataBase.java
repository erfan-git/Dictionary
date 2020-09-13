package com.example.dictionary.dataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.dictionary.model.Word;
import com.example.dictionary.utils.UUIDConverter;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
@TypeConverters({UUIDConverter.class})
public abstract class WordDataBase extends RoomDatabase {
    public abstract WordDataBaseDAO WordDAO();
}
