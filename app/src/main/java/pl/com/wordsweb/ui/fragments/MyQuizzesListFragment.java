package pl.com.wordsweb.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.MyQuizzesListAdapter;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;
import pl.com.wordsweb.event.OnRestListQuizzesToLearnGetEvent;
import pl.com.wordsweb.ui.activities.MainActivity;
import pl.com.wordsweb.ui.utils.OnQuizClickListener;
import pl.com.wordsweb.ui.utils.QuizDividerItemDecoration;

import static pl.com.wordsweb.config.AppSettings.bus;
import static pl.com.wordsweb.config.Constants.QUIZ_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyQuizzesListFragment extends Fragment {


    private ArrayList<QuizToLearn> myQuizzes;
    private RecyclerView rvMyQuizzes;
    private MyQuizzesListAdapter myQuizzesListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView emptyTV;
    private boolean isRefreshed = false;
    public MyQuizzesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my_quizzes, container, false);
        rvMyQuizzes = (RecyclerView) rootView.findViewById(R.id.rvQuizzes);
        ((MainActivity) getActivity()).setTitle(getString(R.string.hamburger_menu_my_quizzes));
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        ((MainActivity) getActivity()).hideFab();
        ((MainActivity) getActivity()).getQuizzes();
        emptyTV = (TextView) rootView.findViewById(R.id.empty_list_tv);
        myQuizzes = new ArrayList<>();
        myQuizzesListAdapter = new MyQuizzesListAdapter(getContext(), myQuizzes, new OnQuizClickListener() {
            @Override
            public void onQuizClick(QuizToLearn quiz) {
                if (!quiz.getQuizBasicDto().isFinished()) {
                    Bundle arg = new Bundle();
                    arg.putString(QUIZ_ID, quiz.getQuizBasicDto().getId());
                    ((MainActivity) getActivity()).openFragment(new LearnFragment(), arg);
                }
            }
        });
        rvMyQuizzes.setAdapter(myQuizzesListAdapter);
        rvMyQuizzes.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMyQuizzes.addItemDecoration(new QuizDividerItemDecoration(getContext()));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                ((MainActivity) getActivity()).getQuizzes();
                isRefreshed = true;
            }
        });

        return rootView;
    }

    private void setEmptyView(ArrayList<QuizToLearn> items) {
        if (items.isEmpty()) {
            rvMyQuizzes.setVisibility(View.GONE);
            emptyTV.setVisibility(View.VISIBLE);
        }
        else {
            rvMyQuizzes.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFab();
        ((MainActivity) getActivity()).setTitle(getString(R.string.hamburger_menu_my_quizzes));
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestListQuizzesToLearnGetEvent(OnRestListQuizzesToLearnGetEvent event) {
        myQuizzes.clear();
        myQuizzes.addAll(event.getAllQuizToLearn());
        myQuizzesListAdapter.notifyDataSetChanged();
        setEmptyView(myQuizzes);
        if (isRefreshed) {

            swipeRefreshLayout.setRefreshing(false);
            isRefreshed = false;
        }
    }
}
