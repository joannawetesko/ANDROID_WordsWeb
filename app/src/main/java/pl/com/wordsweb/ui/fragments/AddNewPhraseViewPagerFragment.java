package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.Subscribe;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.AddPhraseViewPagerAdapter;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.entities.Pair;
import pl.com.wordsweb.entities.PhraseUse;
import pl.com.wordsweb.event.OnMustSaveAddPhraseEvent;
import pl.com.wordsweb.event.OnRestPairPostEvent;
import pl.com.wordsweb.event.OnSavedAddPhraseEvent;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.AppSettings.bus;
import static pl.com.wordsweb.config.Constants.CURRENT_LESSONS_LIST;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewPhraseViewPagerFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AddPhraseViewPagerAdapter addPhraseViewPagerAdapter;
    private LessonList currentLessonList;

    public AddNewPhraseViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(CURRENT_LESSONS_LIST)) {
                currentLessonList = (LessonList) getArguments().getSerializable(CURRENT_LESSONS_LIST);

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_new_phrase_view_pager, container, false);

        tabLayout = ((MainActivity) getActivity()).getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        addPhraseViewPagerAdapter = new AddPhraseViewPagerAdapter(getChildFragmentManager(), getContext(), currentLessonList.getFirstLanguage(), currentLessonList.getSecondLanguage());
        viewPager.setAdapter(addPhraseViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        final FloatingActionButton fab = (FloatingActionButton) ((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_arrow_forward_black_24px));
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        ((MainActivity) getActivity()).setTitle(currentLessonList.getName());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
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
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_done_24dp));
                        fab.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                ((MainActivity) getActivity()).showProgressBar();
                                AppSettings.bus.post(new OnMustSaveAddPhraseEvent());

                            }
                        });
                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return rootView;
    }

    @Subscribe
    public void OnSavedAddPhraseEvent(OnSavedAddPhraseEvent event) {
        PhraseUse phraseUseOne = DbProvider.getPhraseOne(getContext());
        PhraseUse phraseUseTwo = DbProvider.getPhraseTwo(getContext());
        ((MainActivity) getActivity()).hideProgressBar();
        if (phraseUseOne != null && phraseUseTwo != null) {
            Pair pair = new Pair(phraseUseOne, phraseUseTwo);
            Log.d("FLOAT", "Click:" + pair.toString());
            ((MainActivity) getActivity()).postPair(currentLessonList.getId(), pair);
        }
    }

    @Subscribe
    public void OnRestPairPostEvent(OnRestPairPostEvent event) {
        ((MainActivity) getActivity()).getSupportFragmentManager().popBackStack();
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
