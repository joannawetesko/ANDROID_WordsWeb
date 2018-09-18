package pl.com.wordsweb.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.AllLessonPairAdapter;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.entities.Pair;
import pl.com.wordsweb.entities.comments.GetCheckListResponse;
import pl.com.wordsweb.event.OnCheckUserEvent;
import pl.com.wordsweb.event.OnRestCurrentLessonListGetEvent;
import pl.com.wordsweb.event.OnRestNewLessonListSendEvent;
import pl.com.wordsweb.ui.activities.MainActivity;
import pl.com.wordsweb.utils.PermissionManager;

import static pl.com.wordsweb.config.AppSettings.bus;
import static pl.com.wordsweb.config.Constants.BASE_PHOTO_URL;
import static pl.com.wordsweb.config.Constants.CURRENT_LESSONS_LIST;
import static pl.com.wordsweb.config.Constants.PHOTO_FILE_EXTENSION;
import static pl.com.wordsweb.ui.fragments.MyLessonListFragment.PRIVATE_LIST;

public class CurrentLessonListFragment extends Fragment {

    private static String LESSONLIST_ID_BUNDLE = "lessonlist_id_bundle";
    private View rootView;
    private LessonList currentLessonList;
    private ArrayList<Pair> allLessonPairs;
    private ListView allLessonPairsListView;
    private AllLessonPairAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int lessonListId;
    private boolean privateLists;
    private TextView lessonTitle;
    private TextView emptyTextView;
    private boolean fabOnClickEnabled = false;
    private FloatingActionButton fab;

    public CurrentLessonListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_current_lesson_list, container, false);
        emptyTextView = (TextView) rootView.findViewById(R.id.empty_list_item);
        Bundle bundle = getArguments();
        if (bundle != null) {
            lessonListId = bundle.getInt(LESSONLIST_ID_BUNDLE);
            ((MainActivity) getActivity()).getCurrentLessonList(true, lessonListId);
            if (bundle.containsKey(PRIVATE_LIST)) {
                privateLists = bundle.getBoolean(PRIVATE_LIST);
            }
        }
        lessonTitle = (TextView) rootView.findViewById(R.id.current_lesson_list_title);
        initUI();
        allLessonPairsListView = (ListView) rootView.findViewById(R.id.phrase_list_view);
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

    @Override
    public void onResume() {
        super.onResume();
        if (fab != null) {
            fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_24dp));
        }
        if (currentLessonList != null) {
            ((MainActivity) getActivity()).setTitle(currentLessonList.getName());
        }
    }

    private void initUI() {
        fab = ((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_24dp));
        if (privateLists) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (fabOnClickEnabled) {
                        ((MainActivity) getActivity()).checkUserList();
                    }
                }
            });
        } else {
            fab.setVisibility(View.GONE);
        }
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        setUpOnRefreshListener();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setUpOnRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).getCurrentLessonList(false, lessonListId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpOnScrollListener() {
        allLessonPairsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (allLessonPairsListView == null || allLessonPairsListView.getChildCount() == 0) ? 0 : allLessonPairsListView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled((topRowVerticalPosition >= 0));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RestOnLessonListGetEvent(OnRestCurrentLessonListGetEvent event) {
        currentLessonList = event.getCurrentLessonList();
        if (currentLessonList.getPairs().size() == 0) {
            setEmptyView();
        } else {
            hideEmptyView();
        }
        ((MainActivity) getActivity()).setTitle(currentLessonList.getName());
        bindData();
        adapter.setPairsList(currentLessonList.getPairs());
        Log.d("Object of adapter", " " + adapter.getCount());
        adapter.notifyDataSetChanged();
        fabOnClickEnabled = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestNewLessonListSendEvent(OnRestNewLessonListSendEvent event) {
        ((MainActivity) getActivity()).getCurrentLessonList(true, lessonListId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnCheckUserEvent(OnCheckUserEvent event) {
        final GetCheckListResponse getCheckListResponse = event.getCheckListResponse();
        if (PermissionManager.checkCanListXontentAdd(currentLessonList.getId(), getCheckListResponse)) {
            openFragment(new AddNewPhraseViewPagerFragment());
        } else {
            ((MainActivity) getActivity()).showSnackbar(getResources().getString(R.string.not_allowed));
        }
    }

    private void bindData() {
        if (currentLessonList != null) {
            loadImage(currentLessonList.getFirstLanguage().getFlag(), rootView, R.id.firstLanguageImageView);
            loadImage(currentLessonList.getSecondLanguage().getFlag(), rootView, R.id.secondLanguageImageView);
            lessonTitle.setText(currentLessonList.getName());
            allLessonPairs = currentLessonList.getPairs();
            adapter = new AllLessonPairAdapter(getContext(), allLessonPairs);
            allLessonPairsListView.setAdapter(adapter);
            allLessonPairsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Pair currentPair = (Pair) allLessonPairsListView.getAdapter().getItem(position);
                    final TextView example1 = (TextView) v.findViewById(R.id.phrase_list_element_example_1);
                    final TextView example2 = (TextView) v.findViewById(R.id.phrase_list_element_example_2);
                    final TextView phrase1 = (TextView) v.findViewById(R.id.phrase_list_element_phrase_1);
                    final TextView phrase2 = (TextView) v.findViewById(R.id.phrase_list_element_phrase_2);
                    final TableRow exampleRow = (TableRow) v.findViewById(R.id.example_row);
                    if (exampleRow.getVisibility() == View.INVISIBLE || exampleRow.getVisibility() == View.GONE) {
                        exampleRow.setBackgroundColor((getResources().getColor(R.color.colorPrimaryDark)));
                        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
                        exampleRow.setVisibility(View.VISIBLE);
                        exampleRow.startAnimation(a);
                        example1.setVisibility(View.VISIBLE);
                        phrase1.setTextColor(getResources().getColor(R.color.colorAccent));
                        example1.setText(currentPair.getPhraseUseOne().getExamples().get(0).getContent());
                        example2.setVisibility(View.VISIBLE);
                        phrase2.setTextColor(getResources().getColor(R.color.colorAccent));
                        example2.setText(currentPair.getPhraseUseTwo().getExamples().get(0).getContent());
                    } else {
                        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up_up);
                        a.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                phrase1.setTextColor(getResources().getColor(R.color.colorSecondaryText));
                                phrase2.setTextColor(getResources().getColor(R.color.colorDivider));
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                exampleRow.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                //NONE
                            }
                        });
                        exampleRow.startAnimation(a);
                    }
                }
            });
            setUpOnScrollListener();
        }
    }

    public void loadImage(String flagLink, View lessonListRow, int viewId) {
        final ImageView imageView = (ImageView) lessonListRow.findViewById(viewId);
        imageView.setImageBitmap(null);
        if (flagLink != null) {
            Uri uri = Uri.parse(BASE_PHOTO_URL + flagLink + PHOTO_FILE_EXTENSION);
            AppSettings.requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    // SVG cannot be serialized so it's not worth to cache it
                    .load(uri)
                    .into(imageView);
        }
    }

    void openFragment(Fragment fragment) {
        Bundle args = new Bundle();
        args.putSerializable(CURRENT_LESSONS_LIST, currentLessonList);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment.setArguments(args);
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void setEmptyView() {
        emptyTextView.setVisibility(View.VISIBLE);
        allLessonPairsListView.setVisibility(View.GONE);
    }

    private void hideEmptyView() {
        emptyTextView.setVisibility(View.GONE);
        allLessonPairsListView.setVisibility(View.VISIBLE);
    }
}
