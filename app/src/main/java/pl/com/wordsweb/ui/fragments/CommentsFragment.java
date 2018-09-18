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
import pl.com.wordsweb.event.OnClickReplayCommentEvent;
import pl.com.wordsweb.event.OnRestCommentGetEvent;
import pl.com.wordsweb.event.OnRestCommentSendEvent;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.Constants.COMMENT;
import static pl.com.wordsweb.config.Constants.COMMENTS;
import static pl.com.wordsweb.config.Constants.LESSON_LIST;
import static pl.com.wordsweb.config.Constants.LIST_ELEMENT;
import static pl.com.wordsweb.ui.activities.BaseActivity.TAG_COMMENT_ADD;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {

    private LessonList lessonList;
    private List<CommentObject> commentObjects;
    private ListView commentsView;
    private TextView emptyTV;
    private CommentsAdapter commentsAdapter;
    private View rootView;

    private FloatingActionButton fab;
    public CommentsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).showFab();
        ((MainActivity) getActivity()).disableNavi();
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getArguments().containsKey(LESSON_LIST)) {
            lessonList = (LessonList) getArguments().get(LESSON_LIST);
        }
        fab = ((MainActivity) getActivity()).getFab();
        fab.setImageResource(R.drawable.ic_add_24dp);
        ((MainActivity) getActivity()).getComments(LIST_ELEMENT, Integer.toString(lessonList.getId()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (lessonList != null) {
            ((MainActivity) getActivity()).setTitle(lessonList.getName());
        }
        rootView = inflater.inflate(R.layout.fragment_comments, container, false);
        initUI();
        initComments();
        return rootView;
    }

    private void setEmptyView(List<CommentObject> items) {
        if (items.size() == 0) {
            commentsView.setVisibility(View.GONE);
            emptyTV.setVisibility(View.VISIBLE);
        }
        else {
            commentsView.setVisibility(View.VISIBLE);
            emptyTV.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
        ((MainActivity) getActivity()).enableNavi();
        ((MainActivity) getActivity()).showFab();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getComments(LIST_ELEMENT, Integer.toString(lessonList.getId()));
        ((MainActivity) getActivity()).showFab();
        fab.setImageResource(R.drawable.ic_add_24dp);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnClickReplayCommentEvent(OnClickReplayCommentEvent event) {
        Comment comment = event.getComment();
        Bundle args = new Bundle();
        args.putSerializable(COMMENT, comment);
        ((MainActivity) getActivity()).openFragmentWithBack(new AnswersFragment(), args, TAG_COMMENT_ADD);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestCommentGetEvent(OnRestCommentGetEvent event) {
        commentObjects.clear();
        commentObjects.addAll(event.getGetCommentResponse().getContent());
        commentsAdapter.notifyDataSetChanged();
        setEmptyView(commentObjects);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestCommentSendEvent(OnRestCommentSendEvent event) {
        ((MainActivity) getActivity()).getComments(LIST_ELEMENT, Integer.toString(lessonList.getId()));
    }

    private void initComments() {
        commentObjects = new ArrayList<>();
        commentsView = (ListView) rootView.findViewById(R.id.comments_listview);
        commentsAdapter = new CommentsAdapter(commentObjects, getContext(), COMMENTS);
        commentsView.setAdapter(commentsAdapter);
    }

    private void initUI() {
        commentObjects = new ArrayList<>();
        emptyTV = (TextView) rootView.findViewById(R.id.empty_list_tv);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putInt(LESSON_LIST, lessonList.getId());
                ((MainActivity) getActivity()).openFragmentWithBack(new AddNewCommentFragment(), args, TAG_COMMENT_ADD);
            }
        });
    }
}
