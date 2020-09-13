package com.example.dictionary.controller;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dictionary.R;
import com.example.dictionary.dataBase.IRepository;
import com.example.dictionary.dataBase.WordRepository;
import com.example.dictionary.model.Word;

import java.util.List;


public class DictionaryFragment extends Fragment {
    public static final int SPAN_COUNT = 1;
    private RecyclerView mRecyclerView;
    private IRepository mRepository;
//    private WordAdapter mAdapter;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    public static DictionaryFragment newInstance() {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        mRepository = WordRepository.getInstance(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        mRepository.insert(new Word("Hello","سلام"));
        findViews(view);
        initViews();

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
    }

    private void initViews() {

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            // In portrait
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        }

        initUI();
    }

    private void initUI() {
        List<Word> sounds = mRepository.getList();
        WordAdapter adapter = new WordAdapter(sounds);
        mRecyclerView.setAdapter(adapter);
    }

    private class WordHolder extends RecyclerView.ViewHolder {

        private TextView mBaseWord;
        private TextView mTranslation;
        private Word mWord;

        public WordHolder(@NonNull View itemView) {
            super(itemView);

            mBaseWord = itemView.findViewById(R.id.textView_baseWord);
            mTranslation = itemView.findViewById(R.id.textView_translation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        public void bindWord(Word word) {
            mWord = word;
            mBaseWord.setText(mWord.getBaseWord());
            mTranslation.setText(mWord.getTranslation());
        }
    }

    private class WordAdapter extends RecyclerView.Adapter<WordHolder>{
        private List<Word> mWords;

        public WordAdapter(List<Word> words) {
            mWords = words;
        }

        public List<Word> getWords() {
            return mWords;
        }

        public void setWords(List<Word> words) {
            mWords = words;
        }

        @Override
        public int getItemCount() {
            return mWords.size();
        }

        @NonNull
        @Override
        public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_row,parent,false);
            return new WordHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordHolder holder, int position) {
            Word word = mWords.get(position);
            holder.bindWord(word);
        }

    }


//    private void updateUI() {
//        List<Word> words = mRepository.getList();
//
//        if (mAdapter == null) {
//            mAdapter = new CrimeAdapter(crimes);
//            mRecyclerView.setAdapter(mAdapter);
//        } else {
//            mAdapter.setCrimes(crimes);
//            mAdapter.notifyDataSetChanged();
//        }
//
//        updateSubtitle();
//    }


}