package com.example.dictionary.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "wordTable")
public class Word {

    @PrimaryKey(autoGenerate = true)
    private int mId;
    @ColumnInfo(name = "UUID")
    private UUID mUUID;
    @ColumnInfo(name = "baseWord")
    private String mBaseWord;
    @ColumnInfo(name = "translation")
    private String mTranslation;

    public Word( String baseWord, String translation) {
        mUUID = UUID.randomUUID();
        mBaseWord = baseWord;
        mTranslation = translation;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public UUID getUUID() {
        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }

    public String getBaseWord() {
        return mBaseWord;
    }

    public void setBaseWord(String baseWord) {
        mBaseWord = baseWord;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public void setTranslation(String translation) {
        mTranslation = translation;
    }
}
