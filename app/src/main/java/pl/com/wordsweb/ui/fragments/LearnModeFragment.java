package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import pl.com.wordsweb.R;
import pl.com.wordsweb.entities.quizzes.Mode;
import pl.com.wordsweb.storage.DbProvider;


public class LearnModeFragment extends Fragment {

    private View fragmentView;
    private CheckBox writeChB;
    private CheckBox flashcardsChB;
    private String directions;

    public LearnModeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_mode, container, false);
        initUI();

        return fragmentView;
    }

    private void initUI() {

        flashcardsChB = (CheckBox) fragmentView.findViewById(R.id.mode_flashcards_checkbox);
        writeChB = (CheckBox) fragmentView.findViewById(R.id.mode_write_checkbox);

        final CheckBox bothChB = (CheckBox) fragmentView.findViewById(R.id.direction_both_checkbox);
        final CheckBox upChB = (CheckBox) fragmentView.findViewById(R.id.direction_up_checkbox);
        final CheckBox downChB = (CheckBox) fragmentView.findViewById(R.id.direction_down_checkbox);


        bothChB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean x) {
                if (x) {
                    bothChB.setChecked(true);
                    upChB.setChecked(false);
                    downChB.setChecked(false);
                    directions = "BOTH";
                    DbProvider.addDirections(getContext(), directions);
                } else {
                    flashcardsChB.setChecked(false);
                    writeChB.setChecked(true);
                }
            }
        });

        upChB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean x) {
                if (x) {
                    bothChB.setChecked(false);
                    upChB.setChecked(true);
                    downChB.setChecked(false);
                    directions = "FIRST";
                    DbProvider.addDirections(getContext(), directions);
                } else {
                    flashcardsChB.setChecked(false);
                    writeChB.setChecked(true);
                }
            }
        });

        downChB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean x) {
                if (x) {
                    bothChB.setChecked(false);
                    upChB.setChecked(false);
                    downChB.setChecked(true);
                    directions = "SECOND";
                    DbProvider.addDirections(getContext(), directions);
                } else {
                    flashcardsChB.setChecked(false);
                    writeChB.setChecked(true);
                }
            }
        });


        flashcardsChB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    flashcardsChB.setChecked(true);
                    writeChB.setChecked(false);
                    saveCheckedModeToDb();
                } else {
                    flashcardsChB.setChecked(false);
                    writeChB.setChecked(true);
                }
            }
        });

        writeChB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    flashcardsChB.setChecked(false);
                    writeChB.setChecked(true);
                    saveCheckedModeToDb();
                } else {
                    flashcardsChB.setChecked(true);
                    writeChB.setChecked(false);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (getContext() != null) {
                saveCheckedModeToDb();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCheckedModeFromDb();
    }

    private void saveCheckedModeToDb() {
        DbProvider.addMode(getContext(), new Mode(flashcardsChB.isChecked(), writeChB.isChecked()));

    }

    private void getCheckedModeFromDb() {
        Mode mode = DbProvider.getMode(getContext());
        if (mode != null) {
            flashcardsChB.setChecked(mode.isFlashcard());
            writeChB.setChecked(mode.isWrite());
        }
    }


}
