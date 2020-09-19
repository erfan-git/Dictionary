package com.example.dictionary.controller;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.dictionary.R;
import com.example.dictionary.dataBase.IRepository;
import com.example.dictionary.dataBase.WordRepository;
import com.example.dictionary.model.Word;

import java.util.List;


public class DictionaryFragment extends Fragment {
    public static final int SPAN_COUNT = 1;
    public static final String BUNDLE_IS_SUBTITLE_VISIBLE = "isSubtitleVisible";
    public static final int REQUEST_CODE_DATE_PICKER = 0;
    public static final String DIALOG_FRAGMENT_TAG = "Dialog";
    public static final String TAG = "DIC";
    private RecyclerView mRecyclerView;
    private IRepository mRepository;
    private WordAdapter mAdapter;
    private boolean mIsSubtitleVisible = false;
    private ImageView mImageViewEmptyList;
    private SearchView mSearch;
    private EditText mEditTextSearch;

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
        setHasOptionsMenu(true);

        mRepository = WordRepository.getInstance(getContext());
        mRepository.insert(new Word("Hello", "سلام"));

        if (savedInstanceState != null) {
            mIsSubtitleVisible = savedInstanceState.getBoolean(BUNDLE_IS_SUBTITLE_VISIBLE, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        findViews(view);
        initViews();

        mImageViewEmptyList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailWordDialog detailWordDialogFragment = DetailWordDialog.newInstance();

                //create parent-child relations between CrimeDetailFragment-DatePickerFragment
                detailWordDialogFragment.setTargetFragment(DictionaryFragment.this, REQUEST_CODE_DATE_PICKER);

                detailWordDialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                mAdapter.notifyDataSetChanged();
                initUI();
            }
        });

        return view;
    }

    private void findViews(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerView);
        mImageViewEmptyList = view.findViewById(R.id.imageView_empty);
        mSearch = view.findViewById(R.id.searchView);
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
        Log.d(TAG,"initUi");
        List<Word> words = mRepository.getList();
        if (mAdapter == null) {
            mAdapter = new WordAdapter(words);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setWords(words);
            mAdapter.notifyDataSetChanged();
        }
        handleEmptyList();
        updateSubtitle();
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
                    DetailWordDialog detailWordDialogFragment = DetailWordDialog.newInstance(mWord);

                    //create parent-child relations between CrimeDetailFragment-DatePickerFragment
                    detailWordDialogFragment.setTargetFragment(DictionaryFragment.this, REQUEST_CODE_DATE_PICKER);

                    detailWordDialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                    mAdapter.notifyDataSetChanged();
                    initUI();
                }
            });
        }

        public void bindWord(Word word) {
            mWord = word;
            mBaseWord.setText(mWord.getBaseWord());
            mTranslation.setText(mWord.getTranslation());
        }
    }

    private class WordAdapter extends RecyclerView.Adapter<WordHolder> {
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_row, parent, false);
            return new WordHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull WordHolder holder, int position) {
            Word word = mWords.get(position);
            holder.bindWord(word);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        initUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);

        MenuItem item = menu.findItem(R.id.show_numbers);
        updateMenuItemSubtitle(item);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_word:
                DetailWordDialog detailWordDialogFragment = DetailWordDialog.newInstance();

                //create parent-child relations between CrimeDetailFragment-DatePickerFragment
                detailWordDialogFragment.setTargetFragment(DictionaryFragment.this, REQUEST_CODE_DATE_PICKER);

                detailWordDialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                mAdapter.notifyDataSetChanged();
                initUI();
                return true;
            case R.id.show_numbers:
                mIsSubtitleVisible = !mIsSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.delete_all_word:
                showQuestionDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateMenuItemSubtitle(@NonNull MenuItem item) {
        if (mIsSubtitleVisible)
            item.setTitle(R.string.hide_numbers);
        else
            item.setTitle(R.string.show_numbers);
    }

    private void updateSubtitle() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        String stringWords = getString(R.string.subtitle, mRepository.getList().size());

        if (!mIsSubtitleVisible)
            stringWords = null;

        activity.getSupportActionBar().setSubtitle(stringWords);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(BUNDLE_IS_SUBTITLE_VISIBLE, mIsSubtitleVisible);
    }

    private void handleEmptyList() {
        if (mRepository.getList().size() == 0) {
            mImageViewEmptyList.setVisibility(View.VISIBLE);
            mImageViewEmptyList.bringToFront();
        } else
            mImageViewEmptyList.setVisibility(View.GONE);
    }

    private void showQuestionDialog() {

        new AlertDialog.Builder(getActivity())
                .setTitle("Are you sure ?")
                .setPositiveButton(R.string.yess, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRepository.clear();
                        initUI();
                    }
                })
                .setNegativeButton(R.string.noo, null)
                .show();

    }


    private void setSearchView() {

        mSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                mAdapter.getFilter().filter(newText);
                return true;
            }
        });


        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                setKey();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}