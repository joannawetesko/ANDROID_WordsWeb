package pl.com.wordsweb.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import pl.com.wordsweb.R;
import pl.com.wordsweb.storage.DbProvider;


public class LearnDirectionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View fragmentView;

    private String directions;
    public LearnDirectionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LearnDirectionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LearnDirectionsFragment newInstance(String param1, String param2) {
        LearnDirectionsFragment fragment = new LearnDirectionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        getCheckedDirectionsFromDb();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (getContext() != null) {

                saveCheckedDirectionsToDb();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_learn_directions, container, false);
        initUI();
        return fragmentView;
    }

    private void initUI() {

        TextView listTitleBoth = (TextView) fragmentView.findViewById(R.id.direction_both_text_view);
        TextView listTitleUp = (TextView) fragmentView.findViewById(R.id.direction_up_text_view);
        TextView listTitleDown = (TextView) fragmentView.findViewById(R.id.direction_down_text_view);
        Typeface roboto = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
        listTitleBoth.setTypeface(roboto);
        listTitleUp.setTypeface(roboto);
        listTitleDown.setTypeface(roboto);

        CheckBox upChB = (CheckBox) fragmentView.findViewById(R.id.direction_up_checkbox);
        CheckBox downChB = (CheckBox) fragmentView.findViewById(R.id.direction_down_checkbox);

        upChB.setEnabled(false);
        downChB.setEnabled(false);

        getCheckedDirectionsFromDb();
    }

    private void getCheckedDirectionsFromDb() {
        directions = DbProvider.getDirections(getContext());

        if (directions != null) {
            CheckBox bothChB = (CheckBox) fragmentView.findViewById(R.id.direction_both_checkbox);
            CheckBox upChB = (CheckBox) fragmentView.findViewById(R.id.direction_up_checkbox);
            CheckBox downChB = (CheckBox) fragmentView.findViewById(R.id.direction_down_checkbox);

            switch(directions) {
                case "BOTH":
                    bothChB.setChecked(true);
                case "FIRST":
                    upChB.setChecked(true);
                case "SECOND":
                    downChB.setChecked(true);
            }
        }
    }

    private void saveCheckedDirectionsToDb() {

        CheckBox bothChB = (CheckBox) fragmentView.findViewById(R.id.direction_both_checkbox);
        CheckBox upChB = (CheckBox) fragmentView.findViewById(R.id.direction_up_checkbox);
        CheckBox downChB = (CheckBox) fragmentView.findViewById(R.id.direction_down_checkbox);

        if(bothChB.isChecked()) {
            directions = "BOTH";
            upChB.setChecked(false);
            downChB.setChecked(false);
        }

        /*if (upChB.isChecked()) {
            //NOT SUPPORTED YET
        }

        if (downChB.isChecked()) {
            //NOT SUPPORTED YET
        }*/

        DbProvider.addDirections(getContext(), directions);
    }
}
