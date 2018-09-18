package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import pl.com.wordsweb.event.OnCommentClickButtonEvent;
import pl.com.wordsweb.event.OnRestLessonListGetEvent;
import pl.com.wordsweb.event.OnRestNewLessonListSendEvent;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.storage.SharedPreferencesProvider;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.Constants.LESSON_LIST;


public class MyLessonListFragment extends Fragment {

    public static String PRIVATE_LIST = "private_list";
    private static String LANGUAGE_BUNDLE = "language_bundle";
    private static String LESSONLIST_ID_BUNDLE = "lessonlist_id_bundle";
    protected FloatingActionButton fab;
    private ArrayList<LessonList> myLessonsLists;
    private ListView allLessonsListView;
    private LessonList currentLessonList;
    private ArrayList<Language> allLanguages;
    private AllLessonsListAdapter adapter;
    private ProgressBar progressBar;
    private TextView emptyTextView;
    private SwipeRefreshLayout swipeRefreshLayout;
    //wziac z main activity
    private SharedPreferencesProvider sharedPreferencesProvider;
    //wziac z main activity
    private View rootView;


    public MyLessonListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_lesson_list, container, false);
        sharedPreferencesProvider = new SharedPreferencesProvider(getContext());
        emptyTextView = (TextView) rootView.findViewById(R.id.empty_list_item);
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_my_list));
        ((MainActivity) getActivity()).showFab();
        fab = ((MainActivity) getActivity()).getFab();
        myLessonsLists = new ArrayList<>();
        allLanguages = new ArrayList<>();
        initUI();
        allLanguages.addAll(DbProvider.getLanguage(getContext()));
        ((MainActivity) getActivity()).getMyLessonLists(true);
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
        ((MainActivity) getActivity()).setTitle(getResources().getString(R.string.title_module_my_list));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RestOnLessonListGetEvent(OnRestLessonListGetEvent event) {
        myLessonsLists.clear();
        myLessonsLists.addAll(event.getLessonList());
        if (myLessonsLists.size() == 0) {
            setEmptyView();
        } else {
            hideEmptyView();
        }
        adapter.setLessonList(myLessonsLists);
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestNewLessonListSendEvent(OnRestNewLessonListSendEvent event) {
        ((MainActivity) getActivity()).getMyLessonLists(true);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnCommentClickButtonEvent(OnCommentClickButtonEvent event) {
        Bundle args = new Bundle();
        args.putSerializable(LESSON_LIST, event.getLessonList());
        ((MainActivity) getActivity()).openFragmentWithBack(new CommentsFragment(), args);

    }


    //Delete post on long click of list element
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.current_element_action_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_delete:
                LessonList selectedLessonList = adapter.getItem(info.position);
                ((MainActivity) getActivity()).deleteList(selectedLessonList.getId());

                break;
        }
        return true;
    }


    private void setUpOnRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).getMyLessonLists(false);
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


    void setUpActivityFloatingActionButton() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(LANGUAGE_BUNDLE, allLanguages);
                ((MainActivity) getActivity()).openFragmentWithBack(new AddNewListFragment(), bundle);
            }
        });
    }


    private void initUI() {
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_progressBar);
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add_24dp));
        setUpActivityFloatingActionButton();
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
                args.putBoolean(PRIVATE_LIST, true);
                ((MainActivity) getActivity()).openFragmentWithBack(new CurrentLessonListFragment(), args);
            }
        });

    }

    private void setEmptyView() {
        emptyTextView.setVisibility(View.VISIBLE);
        allLessonsListView.setVisibility(View.GONE);
    }

    private void hideEmptyView() {
        emptyTextView.setVisibility(View.GONE);
        allLessonsListView.setVisibility(View.VISIBLE);
    }

}
