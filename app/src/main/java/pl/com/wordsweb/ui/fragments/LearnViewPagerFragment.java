package pl.com.wordsweb.ui.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.LearnViewPagerAdapter;
import pl.com.wordsweb.entities.quizzes.Mode;
import pl.com.wordsweb.entities.quizzes.Quiz;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.Constants.QUIZ_ARGUMENT;
import static pl.com.wordsweb.config.Constants.QUIZ_MODE_WRITE;

public class LearnViewPagerFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LearnViewPagerAdapter learnViewPagerAdapter;

    public LearnViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_learn_view_pager, container, false);
        tabLayout = ((MainActivity) getActivity()).getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(3);
        learnViewPagerAdapter = new LearnViewPagerAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(learnViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        final FloatingActionButton fab = (FloatingActionButton) ((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_forward_black_24px));
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        //set title
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_learn));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                switch (position) {
                    case 0:
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_forward_black_24px));
                        fab.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                viewPager.setCurrentItem(1);
                            }
                        });
                        break;
                    case 1:
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_forward_black_24px));
                        fab.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                viewPager.setCurrentItem(2);
                            }
                        });
                        break;
                    case 2:
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done_24dp));

                        fab.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //TODO dodać asynchroniczność
                                final Mode mode = DbProvider.getMode(getContext());
                                if (mode != null) {
                                    if (mode.isFlashcard() || mode.isWrite()) {
                                        System.out.println(DbProvider.getDirections(getContext()));
                                        Quiz quiz = new Quiz(DbProvider.getLists(getContext()), DbProvider.getLevels(getContext()), DbProvider.getDirections(getContext()));
                                        Bundle arg = new Bundle();
                                        arg.putSerializable(QUIZ_ARGUMENT, quiz);
                                        if (mode.isWrite()) {
                                            arg.putBoolean(QUIZ_MODE_WRITE, true);
                                        } else {
                                            arg.putBoolean(QUIZ_MODE_WRITE, false);
                                        }
                                        ((MainActivity) getActivity()).openFragmentWithBack(new LearnFragment(), arg);
                                    }
                                }
                            }
                        });


                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        tabLayout.setVisibility(View.VISIBLE);
    }
}
