package pl.com.wordsweb.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.AllLanguageAdapter;
import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.ui.activities.MainActivity;

import static android.text.TextUtils.isEmpty;

public class AddNewListFragment extends Fragment {
    private static String LANGUAGE_BUNDLE = "language_bundle";
    ActionBar actionBar;
    private ArrayList<Language> allLanguages;
    private View fragmentView;
    private FloatingActionButton fab;

    public AddNewListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_add_new_list, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            allLanguages = (ArrayList<Language>) bundle.getSerializable(LANGUAGE_BUNDLE);
            initUI(allLanguages);
        }
        ((MainActivity) getActivity()).disableNavi();
        setHasOptionsMenu(true);
        actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        ((MainActivity) getActivity()).setTitle(getString(R.string.title_add_new_list));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


        return fragmentView;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).disableNavi();
        ((MainActivity) getActivity()).setTitle(getString(R.string.title_add_new_list));
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do something useful
                ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
                return (true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((MainActivity) getActivity()).enableNavi();

    }

    private void initUI(ArrayList<Language> allLanguages) {

        /* layout */
        LinearLayout layout = (LinearLayout) fragmentView.findViewById(R.id.add_new_list_card_layout);
        layout.bringToFront();

        /* spinners */
        final AllLanguageAdapter allLanguageAdapter = new AllLanguageAdapter(getContext(), allLanguages);
        final Spinner spinnerFirst = (Spinner) fragmentView.findViewById(R.id.add_lesson_first_language_spinner);
        spinnerFirst.setAdapter(allLanguageAdapter);
        final Spinner spinnerSecond = (Spinner) fragmentView.findViewById(R.id.add_lesson_second_language_spinner);
        spinnerSecond.setAdapter(allLanguageAdapter);

        spinnerFirst.bringToFront();
        spinnerSecond.bringToFront();

        /* edit text */
        final EditText lessonName = (android.widget.EditText) fragmentView.findViewById(R.id.add_lesson_name);

        /* fab */
        fab = ((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (!isEmpty(lessonName.getText().toString())) {


                    Language lessonFirstLanguage = allLanguageAdapter.getItem(spinnerFirst.getSelectedItemPosition());
                    Language lessonSecondLanguage = allLanguageAdapter.getItem(spinnerSecond.getSelectedItemPosition());
                    final LessonList currentLessonList = new LessonList(lessonName.getText().toString(),
                            lessonFirstLanguage, lessonSecondLanguage, 0);
                    ((MainActivity) getActivity()).postLessonList(currentLessonList);
                    getFragmentManager().popBackStack();

                }
            }
        });
    }
}
