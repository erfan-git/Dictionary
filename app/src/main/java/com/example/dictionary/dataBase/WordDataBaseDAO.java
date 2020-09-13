package com.example.dictionary.dataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.dictionary.model.Word;

import java.util.List;

@Dao
public interface WordDataBaseDAO {
    @Insert
    void insertWord(Word word);

    @Update
    void updateWord(Word word);

    @Delete
    void deleteWord(Word word);

    @Query("SELECT * FROM wordTable")
    List<Word> getWords();

    @Query("SELECT * FROM wordTable WHERE uuid=:uuid")
    Word getWord(String uuid);

    @Query("Delete FROM wordTable")
    void clear();
}
