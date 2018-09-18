package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.AllLessonsListAdapter;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;
import pl.com.wordsweb.event.OnCommentClickButtonEvent;
import pl.com.wordsweb.event.OnRestLessonListGetEvent;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.Constants.LESSON_LIST;
import static pl.com.wordsweb.ui.activities.BaseActivity.TAG_COMMENT;

public class LessonListFragment extends Fragment {


    private static String LANGUAGE_BUNDLE = "language_bundle";
    private static String LESSONLIST_ID_BUNDLE = "lessonlist_id_bundle";
    protected FloatingActionButton fab;
    private ArrayList<LessonList> allLessonsLists;
    private ListView allLessonsListView;
    private LessonList currentLessonList;
    private ArrayList<Language> allLanguages;
    private AllLessonsListAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyTV;
    //wziac z main activity
    private SharedPreferencesProvider sharedPreferencesProvider;
    //wziac z main activity
    private View rootView;


    public LessonListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lesson_list, container, false);
        sharedPreferencesProvider = new SharedPreferencesProvider(getContext());
        ((MainActivity) getActivity()).showFab();
        fab = ((MainActivity) getActivity()).getFab();
        allLessonsLists = new ArrayList<>();
        allLanguages = new ArrayList<>();
        initUI();
        allLanguages.addAll(DbProvider.getLanguage(getContext()));
        ((MainActivity) getActivity()).getLessonLists(true);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        AppSettings.bus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        AppSettings.bus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_list));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RestOnLessonListGetEvent(OnRestLessonListGetEvent event) {
        allLessonsLists.clear();
        allLessonsLists.addAll(event.getLessonList());
        adapter.setLessonList(allLessonsLists);
        adapter.notifyDataSetChanged();
        setEmptyView(allLessonsLists);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnCommentClickButtonEvent(OnCommentClickButtonEvent event) {
        Bundle args = new Bundle();
        args.putSerializable(LESSON_LIST, event.getLessonList());
        ((MainActivity) getActivity()).openFragmentWithBack(new CommentsFragment(), args, TAG_COMMENT);

    }

    private void setUpOnRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).getLessonLists(false);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setUpOnScrollListener() {
        allLessonsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (allLessonsListView == null || allLessonsListView.getChildCount() == 0) ? 0 : allLessonsListView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled((topRowVerticalPosition >= 0));
            }
        });
    }

    private void setEmptyView(ArrayList<LessonList> items) {
        if (items.isEmpty()) {
            allLessonsListView.setVisibility(View.GONE);
            emptyTV.setVisibility(View.VISIBLE);
        }
        else {
            allLessonsListView.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.GONE);
        }
    }

    private void initUI() {
        emptyTV = (TextView) rootView.findViewById(R.id.empty_list_item);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_progressBar);
        fab.setVisibility(View.GONE);
        initLists();
        setUpOnRefreshListener();
        setUpOnScrollListener();

    }

    private void initLists() {
        allLessonsListView = (ListView) rootView.findViewById(R.id.lesson_list_view);
        adapter = new AllLessonsListAdapter(getContext());
        registerForContextMenu(allLessonsListView);
        allLessonsListView.setAdapter(adapter);
        allLessonsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                LessonList clickedLessonList = adapter.getItem(position);

                Bundle args = new Bundle();
                args.putInt(LESSONLIST_ID_BUNDLE, clickedLessonList.getId());
                ((MainActivity) getActivity()).openFragmentWithBack(new CurrentLessonListFragment(), args);
            }
        });

    }

}
