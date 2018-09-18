package pl.com.wordsweb.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import pl.com.wordsweb.R;
import pl.com.wordsweb.adapters.CommentsAdapter;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.LessonList;
import pl.com.wordsweb.entities.comments.Comment;
import pl.com.wordsweb.entities.comments.CommentObject;
import pl.com.wordsweb.event.OnRestCommentGetEvent;
import pl.com.wordsweb.event.OnRestCommentSendEvent;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.Constants.ANSWERS;
import static pl.com.wordsweb.config.Constants.COMMENT;
import static pl.com.wordsweb.config.Constants.COMMENT_ELEMENT;
import static pl.com.wordsweb.ui.activities.BaseActivity.TAG_COMMENT_ADD;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswersFragment extends Fragment {

    public static final String COMMENT_ID = "comment_id";
    private LessonList lessonList;
    private Comment comment;
    private TextView emptyTV;
    private List<CommentObject> answers;
    private ListView answersView;
    private CommentsAdapter commentsAdapter;
    private View rootView;

    private FloatingActionButton fab;
    public AnswersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).showFab();
        ((MainActivity) getActivity()).disableNavi();
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getArguments().containsKey(COMMENT)) {
            comment = (Comment) getArguments().get(COMMENT);
            ((MainActivity) getActivity()).setTitle(comment.getContent());
        }
        fab = ((MainActivity) getActivity()).getFab();
        ((MainActivity) getActivity()).getComments(COMMENT_ELEMENT, comment.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (lessonList != null) {
            ((MainActivity) getActivity()).setTitle(lessonList.getName());
        }
        rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        initUI();
        answers = new ArrayList<>();

        initAnswers();
        return rootView;
    }

    private void setEmptyView(List<CommentObject> items) {
        if (items.isEmpty()) {
            answersView.setVisibility(View.GONE);
            emptyTV.setVisibility(View.VISIBLE);
        }
        else {
            answersView.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if (getFragmentManager().getBackStackEntryCount() > 0)
                    getFragmentManager().popBackStack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).showFab();
        ((MainActivity) getActivity()).getComments(COMMENT_ELEMENT, comment.getId());
    }

    private void initAnswers() {
        answersView = (ListView) rootView.findViewById(R.id.comments_listview);
        commentsAdapter = new CommentsAdapter(answers, getContext(), ANSWERS);
        answersView.setAdapter(commentsAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        AppSettings.bus.register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestCommentSendEvent(OnRestCommentSendEvent event) {
        ((MainActivity) getActivity()).getComments(COMMENT_ELEMENT, comment.getId());
    }


    @Override
    public void onStop() {
        super.onStop();
        AppSettings.bus.unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestCommentGetEvent(OnRestCommentGetEvent event) {
        answers.clear();
        answers.addAll(event.getGetCommentResponse().getContent());
        commentsAdapter.notifyDataSetChanged();
        setEmptyView(answers);
    }



    private void initUI() {
        emptyTV = (TextView) rootView.findViewById(R.id.empty_list_tv);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putString(COMMENT_ID, comment.getId());
                ((MainActivity) getActivity()).openFragmentWithBack(new AddNewCommentFragment(), args, TAG_COMMENT_ADD);
            }
        });
    }
}
