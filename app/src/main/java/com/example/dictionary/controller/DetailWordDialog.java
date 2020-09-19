package com.example.dictionary.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.dictionary.R;
import com.example.dictionary.dataBase.WordRepository;
import com.example.dictionary.model.Word;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.GregorianCalendar;

public class DetailWordDialog extends DialogFragment {

    public static final String EXTRA_USER_SELECTED_DATE = "com.example.dictionary.userSelectedWord";
    public static final String ARG_WORD = "currentWord";
    private WordRepository mRepository;
    private Word mCurrentWord;
    private TextInputEditText mEditTextWord;
    private TextInputEditText mEditTextTranslation;
    private ImageView mDelete;
    private ImageView mShare;
    private boolean mIsNewWord;


    public DetailWordDialog() {
        // Required empty public constructor
    }

    public DetailWordDialog(boolean isNewWord) {
        mIsNewWord = isNewWord;
    }


    public static DetailWordDialog newInstance() {
        DetailWordDialog fragment = new DetailWordDialog(true);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailWordDialog newInstance(Word word) {
        DetailWordDialog fragment = new DetailWordDialog(false);
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORD, word);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = WordRepository.getInstance(getContext());
        if (getArguments() != null) {
            mCurrentWord = (Word) getArguments().getSerializable(ARG_WORD);
        }
        onConfiguration(savedInstanceState);
    }

    private void onConfiguration(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
//            mUnSavedDate = (GregorianCalendar) savedInstanceState.getSerializable(BUNDLE_UNSAVED_DATE_EDIT_DIALOG);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.detail_word_dialog, null);

        findViews(view);
        initialViews();
        setListeners();

        if (mIsNewWord) {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_word)
                    .setIcon(R.drawable.dictionary1)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mEditTextWord == null || mEditTextTranslation == null
                                    || mEditTextWord.getText().toString().length() == 0
                                    || mEditTextTranslation.getText().toString().length() == 0) {
                                mEditTextWord.setError("Word Can not be empty.");
                                mEditTextTranslation.setError("Translation Can not be empty.");
                            } else {
                                mRepository.insert(new Word(mEditTextWord.getText().toString(), mEditTextTranslation.getText().toString()));
                                dismiss();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        } else {
            return new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_word)
                    .setIcon(R.drawable.dictionary1)
                    .setView(view)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mEditTextWord == null || mEditTextTranslation == null
                                    || mEditTextWord.getText().toString().length() == 0
                                    || mEditTextTranslation.getText().toString().length() == 0) {
                                mEditTextWord.setError("Word Can not be empty.");
                                mEditTextTranslation.setError("Translation Can not be empty.");
                            } else {
                                mCurrentWord.setBaseWord(mEditTextWord.getText().toString());
                                mCurrentWord.setTranslation(mEditTextTranslation.getText().toString());
                                mRepository.update(mCurrentWord);
                                dismiss();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null)
                    .create();
        }

    }

    private void initialViews() {
        if (mCurrentWord != null) {
            mEditTextWord.setText(mCurrentWord.getBaseWord());
            mEditTextTranslation.setText(mCurrentWord.getTranslation());
        }
    }

    private void findViews(View view) {
        mEditTextWord = view.findViewById(R.id.editText_word);
        mEditTextTranslation = view.findViewById(R.id.editText_translation);
        mDelete = view.findViewById(R.id.imageView_delete);
        mShare = view.findViewById(R.id.imageView_share);
    }

    private void setListeners() {
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRepository.delete(mCurrentWord);
                dismiss();
            }
        });

        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

//    private void setResult(W userSelectedDate) {
//        Fragment fragment = getTargetFragment();
//        Intent intent = new Intent();
//        intent.putExtra(EXTRA_USER_SELECTED_DATE, userSelectedDate);
//        fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
//    }
}