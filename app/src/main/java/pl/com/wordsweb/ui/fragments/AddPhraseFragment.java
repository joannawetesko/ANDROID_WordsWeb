package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.AutocompletePhraseAdapter;
import pl.com.wordsweb.adapters.AutocompletePhraseUsesAdapter;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Example;
import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.entities.Phrase;
import pl.com.wordsweb.entities.PhraseInfo;
import pl.com.wordsweb.entities.PhraseUse;
import pl.com.wordsweb.entities.Token;
import pl.com.wordsweb.event.OnMustSaveAddPhraseEvent;
import pl.com.wordsweb.event.OnSavedAddPhraseEvent;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.ui.utils.DelayAutoCompleteTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.com.wordsweb.config.AppSettings.bus;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPhraseFragment extends Fragment {

    public static final String LANGUAGE = "language_id";
    public static final String LANGUAGE_NUMBER = "language_number";
    DelayAutoCompleteTextView phraseTextView;
    private View rootView;
    private int languageId;
    private int languageNumber;
    private int selectedPhraseId = -1;
    private ArrayList<PhraseUse> firstPhraseUses;
    private ArrayList<PhraseUse> secondPhraseUses;
    private ImageButton newExampleBtn;
    private AutocompletePhraseUsesAdapter firstPhraseUsesSpinnerAdapter;
    private AutocompletePhraseUsesAdapter secondPhraseUsesSpinnerAdapter;
    private Language language;
    private Button saveButton;
    private AutoCompleteTextView phraseUsesTextView;
    private AutoCompleteTextView partoOfSpeachTextView;
    private AutoCompleteTextView detailsTextView;
    private CheckBox idiomCheckBox;
    private Spinner phraseUseSpinner;
    private String selectedPhraseContent = "";

    public AddPhraseFragment() {
        // Required empty public constructor
    }

    public static AddPhraseFragment newInstance(Language language, int languageNumber) {
        AddPhraseFragment fragment = new AddPhraseFragment();
        Bundle args = new Bundle();
        args.putSerializable(AddPhraseFragment.LANGUAGE, language);
        args.putInt(LANGUAGE_NUMBER, languageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(LANGUAGE)) {
            language = (Language) getArguments().getSerializable(LANGUAGE);
            languageId = language.getId();
        }
        if (getArguments().containsKey(LANGUAGE_NUMBER)) {
            languageNumber = getArguments().getInt(LANGUAGE_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_phrase, container, false);
        unitUI();
        return rootView;
    }

    private void fulfillView(AutocompletePhraseUsesAdapter adapter) {
        phraseUseSpinner.setAdapter(adapter);
        if (firstPhraseUses != null) {
            selectPhraseUsesView(firstPhraseUses);
        } else if (secondPhraseUses != null) {
            selectPhraseUsesView(secondPhraseUses);
        }
    }

    private void setOtherViews(PhraseUse phraseUses) {
        detailsTextView.setText(phraseUses.getPhraseInfo().getDescription());
        partoOfSpeachTextView.setText(phraseUses.getPhraseInfo().getPartOfSpeach());
        idiomCheckBox.setChecked(phraseUses.getPhraseInfo().getIsIdiom());
        lockOtherViews();
    }

    private void lockOtherViews() {
        detailsTextView.setFocusable(false);
        detailsTextView.setEnabled(false);
        partoOfSpeachTextView.setFocusable(false);
        partoOfSpeachTextView.setEnabled(false);
        idiomCheckBox.setEnabled(false);
    }

    private void unlockOtherViews() {
        detailsTextView.setFocusableInTouchMode(true);
        detailsTextView.setFocusable(true);
        detailsTextView.setEnabled(true);
        partoOfSpeachTextView.setFocusableInTouchMode(true);
        partoOfSpeachTextView.setFocusable(true);
        partoOfSpeachTextView.setEnabled(true);
        partoOfSpeachTextView.setWidth(260);
        idiomCheckBox.setEnabled(true);
        detailsTextView.getText().clear();
        partoOfSpeachTextView.getText().clear();
        idiomCheckBox.setChecked(false);
    }

    private void selectPhraseUsesView(ArrayList<PhraseUse> phraseUses) {
        if (phraseUses.size() > 0) {
            turnOnPhraseUseSpinner();
        } else {
            turnOffPhraseUseSpinner();
        }
    }

    private void turnOnPhraseUseSpinner() {
        phraseUseSpinner.setVisibility(View.VISIBLE);
        phraseUsesTextView.setVisibility(View.GONE);
        newExampleBtn.setVisibility(View.VISIBLE);
    }

    private void turnOffPhraseUseSpinner() {
        phraseUseSpinner.setVisibility(View.GONE);
        phraseUsesTextView.setVisibility(View.VISIBLE);
        newExampleBtn.setVisibility(View.GONE);
        unlockOtherViews();
    }

    private void unitUI() {
        phraseTextView = (DelayAutoCompleteTextView) rootView.findViewById(R.id.phrase_language);
        newExampleBtn = (ImageButton) rootView.findViewById(R.id.add_new_phrase_new_example_fields);
        newExampleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnOffPhraseUseSpinner();
            }
        });
        phraseUseSpinner = (Spinner) rootView.findViewById(R.id.phrase_uses_spinner);
        phraseUseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (languageNumber == 1) {
                    setOtherViews(firstPhraseUsesSpinnerAdapter.getItem(position));
                } else if (languageNumber == 2) {
                    setOtherViews(secondPhraseUsesSpinnerAdapter.getItem(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        phraseUsesTextView = (AutoCompleteTextView) rootView.findViewById(R.id.example_language_1);
        partoOfSpeachTextView = (AutoCompleteTextView) rootView.findViewById(R.id.phrase_details_pos);
        detailsTextView = (AutoCompleteTextView) rootView.findViewById(R.id.phrase_details_desc);
        idiomCheckBox = (CheckBox) rootView.findViewById(R.id.pair_idiom_checkbox);
        phraseTextView.setThreshold(1);
        phraseTextView.setAdapter(new AutocompletePhraseAdapter(getContext(), languageId));
        phraseTextView.setLoadingIndicator(
                (android.widget.ProgressBar) rootView.findViewById(R.id.pb_loading_indicator));
        phraseTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Phrase phrase = (Phrase) adapterView.getItemAtPosition(position);
                phraseTextView.setText(phrase.getContent());
                selectedPhraseId = phrase.getId();
                selectedPhraseContent = phrase.getContent();
                getPhraseUses(selectedPhraseId, languageId);
            }
        });
        phraseTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (before == 1 && count == 0 && s.length() == 0) {
                    if (languageNumber == 1 && firstPhraseUsesSpinnerAdapter != null) {
                        firstPhraseUsesSpinnerAdapter.clear();
                        turnOffPhraseUseSpinner();
                    } else if (languageNumber == 2 && secondPhraseUsesSpinnerAdapter != null) {
                        secondPhraseUsesSpinnerAdapter.clear();
                        turnOffPhraseUseSpinner();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (getContext() != null) {
                savePhraseToDb();
                Log.d("AddPhraseFragment ", "Save to db, lang " + languageNumber);
            }
        }
    }

    public void savePhraseToDb() {
        PhraseUse phrase = createPhraseToSave();
        if (languageNumber == 1) {
            DbProvider.addPhraseOne(getContext(), phrase);
        } else if (languageNumber == 2) {
            DbProvider.addPhraseTwo(getContext(), phrase);
        }
    }

    private PhraseUse createPhraseToSave() {
        PhraseUse phraseUse = new PhraseUse();
        ArrayList<Example> exampleArrayList = new ArrayList<Example>();
        boolean phraseExist = false;
        if (selectedPhraseId != -1 && !selectedPhraseContent.equalsIgnoreCase("")) {
            if (phraseTextView.getText().toString().trim().equalsIgnoreCase(selectedPhraseContent)) {
                phraseUse.setPhrase(new Phrase(phraseTextView.getText().toString(), selectedPhraseId));
                phraseExist = true;
            } else {
                phraseUse.setPhrase(new Phrase(phraseTextView.getText().toString()));
            }
        } else {
            phraseUse.setPhrase(new Phrase(phraseTextView.getText().toString()));
        }
        if (phraseUseSpinner.getVisibility() == View.VISIBLE) {
            PhraseUse phraseUseSpinnerSelectedItem = (PhraseUse) phraseUseSpinner.getSelectedItem();
            exampleArrayList.add(phraseUseSpinnerSelectedItem.getExamples().get(0));
            phraseUse.setExamples(exampleArrayList);
            phraseUse.setId(phraseUseSpinnerSelectedItem.getId());
        } else if (phraseUsesTextView.getVisibility() == View.VISIBLE) {
            if (phraseExist) {
                exampleArrayList.add(new Example(phraseUsesTextView.getText().toString()));
                phraseUse.setExamples(exampleArrayList);
            } else {
                phraseUse.setPhrase(new Phrase(phraseTextView.getText().toString()));
                exampleArrayList.add(new Example(phraseUsesTextView.getText().toString()));
                phraseUse.setExamples(exampleArrayList);
            }
        }
        PhraseInfo phraseInfo = new PhraseInfo(detailsTextView.getText().toString(), idiomCheckBox.isChecked(), partoOfSpeachTextView.getText().toString());
        phraseUse.setPhraseInfo(phraseInfo);
        phraseUse.setLanguage(language);
        return phraseUse;
    }

    private void getPhraseUses(int currentPhraseId, final int languageId) {
        SharedPreferencesProvider sharedPreferencesProvider = new SharedPreferencesProvider(getContext());
        if (sharedPreferencesProvider.checkToken()) {
            Token token = sharedPreferencesProvider.getAcessToken();
            Call<ArrayList<PhraseUse>> call = AppSettings.phraseApi.getPhraseUses(currentPhraseId, languageId, token.getToken());
            Log.d("BODY", call.toString());
            call.enqueue(new Callback<ArrayList<PhraseUse>>() {
                @Override
                public void onResponse(Call<ArrayList<PhraseUse>> call, Response<ArrayList<PhraseUse>> response) {
                    if (response.isSuccessful()) {
                        Log.d("CallBack", " response is " + new Gson().toJson(response.body()));
                        if (languageNumber == 1) {
                            firstPhraseUses = response.body();
                            firstPhraseUsesSpinnerAdapter = new AutocompletePhraseUsesAdapter(getActivity(), firstPhraseUses);
                            fulfillView(firstPhraseUsesSpinnerAdapter);
                        } else {
                            secondPhraseUses = response.body();
                            secondPhraseUsesSpinnerAdapter = new AutocompletePhraseUsesAdapter(getActivity(), secondPhraseUses);
                            fulfillView(secondPhraseUsesSpinnerAdapter);
                        }
                    } else {
                        Log.d("CallBack", " response is " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<PhraseUse>> call, Throwable t) {
                    Log.d("CallBack", " Throwable is " + t);
                }
            });
        } else {
            Log.d("GET LIST", "Token NOT exists");
        }
    }

    @Subscribe
    public void OnMustSaveAddPhraseEvent(OnMustSaveAddPhraseEvent event) {
        savePhraseToDb();
        if (languageNumber == 2) {
            bus.post(new OnSavedAddPhraseEvent());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        bus.unregister(this);
    }
}
