package com.example.dictionary.dataBase;

import android.content.Context;

import androidx.room.Room;

import com.example.dictionary.model.Word;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class WordRepository implements IRepository<Word> {
    private static WordRepository sCrimeRepository;

    //future referenced: memory leaks
    private static Context mContext;

    private WordDataBase mDatabase;

    public static WordRepository getInstance(Context context) {
        mContext = context.getApplicationContext();
        if (sCrimeRepository == null)
            sCrimeRepository = new WordRepository();

        return sCrimeRepository;
    }

    private WordRepository() {
        mDatabase = Room.databaseBuilder(mContext, WordDataBase.class, "WordDB.db")
                .allowMainThreadQueries()
                .build();
    }


    @Override
    public List<Word> getList() {
        return mDatabase.WordDAO().getWords();
    }

    @Override
    public Word get(UUID uuid) {
        return mDatabase.WordDAO().getWord(uuid.toString());
    }

    @Override
    public void update(Word word) {
        mDatabase.WordDAO().updateWord(word);
    }

    @Override
    public void delete(Word word) {
        mDatabase.WordDAO().deleteWord(word);
    }

    @Override
    public void insert(Word word) {
        mDatabase.WordDAO().insertWord(word);
    }

    @Override
    public void clear() {
        mDatabase.WordDAO().clear();
    }

    @Override
    public int getPosition(UUID uuid) {
        List<Word> words = getList();
        for (int i = 0; i < words.size(); i++) {
            if (words.get(i).getUUID().equals(uuid))
                return i;
        }

        return -1;
    }

    @Override
    public File getPhotoFile(Context context, Word word) {
        return null;
    }
}
