package pl.com.wordsweb.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.com.wordsweb.R;
import pl.com.wordsweb.ui.activities.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainPageFragment extends Fragment {

    View fragmentView;

    public MainPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).hideFab();
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_main_page, container, false);
        initUI();

        return fragmentView;
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).hideFab();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFab();
    }

    private void initUI() {

        Button myListsBtn = (Button) fragmentView.findViewById(R.id.main_my_lists_button);
        Button learnBtn = (Button) fragmentView.findViewById(R.id.main_learn_button);
        Button listsBtn = (Button) fragmentView.findViewById(R.id.main_lists_button);
        Button quizzesBtn = (Button) fragmentView.findViewById(R.id.main_quizzes_button);
        Button logOutBtn = (Button) fragmentView.findViewById(R.id.main_log_out_button);

        myListsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFragment(new MyLessonListFragment());
                ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_list));
                ((MainActivity) getActivity()).getNavigationView().setCheckedItem(R.id.nav_my_manage_page);
            }
        });

        learnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFragment(new LearnViewPagerFragment());
                ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_learn));
                ((MainActivity) getActivity()).getNavigationView().setCheckedItem(R.id.nav_learn_page);
            }
        });

        listsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFragment(new LessonListFragment());
                ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_list));
                ((MainActivity) getActivity()).getNavigationView().setCheckedItem(R.id.nav_manage_page);
            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).logout();
            }
        });

        quizzesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFragment(new MyQuizzesListFragment());
                ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_quizzes));
                ((MainActivity) getActivity()).getNavigationView().setCheckedItem(R.id.nav_my_quizz);

            }
        });

    }
}
