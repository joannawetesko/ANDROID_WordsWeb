package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.storage.DbProvider;

public class LearnLevelsFragment extends Fragment {
    /* Tutaj byłoby fajnie, gdyby zapisywało sie do db zaraz po zaznaczeniu i wymusic ze mozna tylko jedno zaznaczyc,
    inaczej pojdzie  null, bo przycisk jest wywoływany w LearnViewPagerAdapter*/

    private Integer[] listLevels;
    private View fragmentView;

    CheckBox ch1;
    CheckBox ch2;
    CheckBox ch3;
    CheckBox ch4;
    CheckBox ch5;
    CheckBox ch6;
    CheckBox ch7;
    CheckBox ch8;
    CheckBox ch9;

    public LearnLevelsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_learn_levels, container, false);
        initUI();
        getCheckedLevelsFromDb();

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCheckedLevelsFromDb();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (getContext() != null) {

                saveCheckedLevelsToDb();
            }
        }
    }

    private void initUI() {

        ch1 = (CheckBox) fragmentView.findViewById(R.id.levels_1);
        ch2 = (CheckBox) fragmentView.findViewById(R.id.levels_2);
        ch3 = (CheckBox) fragmentView.findViewById(R.id.levels_3);
        ch4 = (CheckBox) fragmentView.findViewById(R.id.levels_4);
        ch5 = (CheckBox) fragmentView.findViewById(R.id.levels_5);
        ch6 = (CheckBox) fragmentView.findViewById(R.id.levels_6);
        ch7 = (CheckBox) fragmentView.findViewById(R.id.levels_7);
        ch8 = (CheckBox) fragmentView.findViewById(R.id.levels_8);
        ch9 = (CheckBox) fragmentView.findViewById(R.id.levels_9);

        ch1.setChecked(false);
        ch2.setChecked(false);
        ch3.setChecked(false);
        ch4.setChecked(false);
        ch5.setChecked(false);
        ch6.setChecked(false);
        ch7.setChecked(false);
        ch8.setChecked(false);
        ch9.setChecked(false);
    }

    private void getCheckedLevelsFromDb() {
        listLevels = DbProvider.getLevels(getContext());

        if (listLevels != null) {
            for (Integer number: listLevels) {

                switch(number) {
                    case 1:
                        ch1.setChecked(true);
                    case 2:
                        ch2.setChecked(true);
                    case 3:
                        ch3.setChecked(true);
                    case 4:
                        ch4.setChecked(true);
                    case 5:
                        ch5.setChecked(true);
                    case 6:
                        ch6.setChecked(true);
                    case 7:
                        ch7.setChecked(true);
                    case 8:
                        ch8.setChecked(true);
                    case 9:
                        ch9.setChecked(true);
                }
            }
        }
    }

    private void saveCheckedLevelsToDb() {
        ArrayList<Integer> lLevels = new ArrayList<Integer>();

        if (ch1.isChecked()) {
            lLevels.add(1);
        }
        if (ch2.isChecked()) {
            lLevels.add(2);
        }
        if (ch3.isChecked()) {
            lLevels.add(3);
        }
        if (ch4.isChecked()) {
            lLevels.add(4);
        }
        if (ch5.isChecked()) {
            lLevels.add(5);
        }
        if (ch6.isChecked()) {
            lLevels.add(6);
        }
        if (ch7.isChecked()) {
            lLevels.add(7);
        }
        if (ch8.isChecked()) {
            lLevels.add(8);
        }
        if (ch9.isChecked()) {
            lLevels.add(9);
        }

        listLevels = new Integer[lLevels.size()];
        listLevels = lLevels.toArray(listLevels);

        DbProvider.addLevels(getContext(), listLevels);
    }
}
