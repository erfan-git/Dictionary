package com.example.dictionary.dataBase;

import android.content.Context;

import com.example.dictionary.model.Word;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface IRepository<E> {
    List<E> getList();
    E get(UUID uuid);
    void update(E e);
    void delete(E e);
    void insert(E e);
    void clear();
//    void insertList(List<E> list);
    int getPosition(UUID uuid);
    File getPhotoFile(Context context, Word word);
}
