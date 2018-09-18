package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.LearnAllListsAdapter;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.Language;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.event.OnRestLessonListGetEvent;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.ui.activities.MainActivity;


public class LearnListFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String LESSONLIST_ID_BUNDLE = "lessonlist_id_bundle";
    ArrayList<LessonList> allLessonsLists;
    ArrayList<Language> allLanguages;
    Integer[] listsChecked;
    ArrayList<Integer> selectedLessonListsIds = new ArrayList<Integer>();
    private String mParam1;
    private String mParam2;
    private ListView allLessonsListView;
    private LearnAllListsAdapter adapter;
    private View rootView;

    public LearnListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_learn_list, container, false);
        //sharedPreferencesProvider = new SharedPreferencesProvider(getContext());
        allLessonsLists = new ArrayList<>();
        allLanguages = new ArrayList<>();
        initUI();
        getCheckedListFromDb();
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
    public void onPause() {
        super.onPause();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (getContext() != null) {

                saveCheckedListToDb();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        getCheckedListFromDb();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void RestOnLessonListGetEvent(OnRestLessonListGetEvent event) {
        allLessonsLists.clear();
        allLessonsLists.addAll(event.getLessonList());
        adapter.setLessonList(allLessonsLists);
        adapter.notifyDataSetChanged();
    }


   private void initUI() { initLists();}

    private void initLists() {
        allLessonsListView = (ListView) rootView.findViewById(R.id.learn_lesson_listview);
        adapter = new LearnAllListsAdapter(getContext());
        registerForContextMenu(allLessonsListView);
        allLessonsListView.setAdapter(adapter);
        allLessonsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                LessonList clickedLessonList = adapter.getItem(position);
                CheckBox cb = (CheckBox) v.findViewById(R.id.learn_lists_listelement_checkbox);
                cb.setChecked(!cb.isChecked());

                if(cb.isChecked())
                {
                    if(!selectedLessonListsIds.contains(clickedLessonList.getId()))
                    {
                        selectedLessonListsIds.add(clickedLessonList.getId());
                    }
                }
                else
                {
                    if(selectedLessonListsIds.contains(clickedLessonList.getId()))
                    {
                        selectedLessonListsIds.remove(selectedLessonListsIds.indexOf(clickedLessonList.getId()));
                    }
                }

                //Bundle args = new Bundle();
                //args.putInt(LESSONLIST_ID_BUNDLE, clickedLessonList.getId());
                //((MainActivity) getActivity()).openFragmentWithBack(new CurrentLessonListFragment(), args);
            }
        });

    }

    private void getCheckedListFromDb() {
        listsChecked = DbProvider.getLists(getContext());

        //TODO: zaznaczyc checkboxy na podstawie listy

    }

    public void saveCheckedListToDb() {

        listsChecked = new Integer[selectedLessonListsIds.size()];
        listsChecked = selectedLessonListsIds.toArray(listsChecked);
        DbProvider.addLists(getContext(), listsChecked);
    }
}