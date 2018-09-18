package pl.com.wordsweb.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.com.wordsweb.R;
import pl.com.wordsweb.entities.comments.CommentSend;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.Constants.COMMENT_ELEMENT;
import static pl.com.wordsweb.config.Constants.LESSON_LIST;
import static pl.com.wordsweb.config.Constants.LIST_ELEMENT;
import static pl.com.wordsweb.ui.fragments.AnswersFragment.COMMENT_ID;

public class AddNewCommentFragment extends Fragment {

    private View rootView;
    private FloatingActionButton fab;
    private EditText contentTV;
    private Integer listId = -1;
    private String commentId = "";

    public AddNewCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(LESSON_LIST)) {
            listId = getArguments().getInt(LESSON_LIST);
        }
        if (getArguments().containsKey(COMMENT_ID)) {
            commentId = getArguments().getString(COMMENT_ID);
        }
        fab = ((MainActivity) getActivity()).getFab();
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (listId != -1) {
                    CommentSend commentSend = new CommentSend();
                    commentSend.setContent(contentTV.getText().toString());
                    commentSend.setElementType(LIST_ELEMENT);
                    commentSend.setElementId(listId.toString());
                    ((MainActivity) getActivity()).postComment(commentSend);
                    getFragmentManager().popBackStack();
                } else if (!commentId.equalsIgnoreCase("")) {
                    CommentSend commentSend = new CommentSend();
                    commentSend.setContent(contentTV.getText().toString());
                    commentSend.setElementType(COMMENT_ELEMENT);
                    commentSend.setElementId(commentId);
                    ((MainActivity) getActivity()).postComment(commentSend);
                    getFragmentManager().popBackStack();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_new_comment, container, false);
        initUI();
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_add_24dp));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void initUI() {

        /* title */
        if (!commentId.equals("")) {
            TextView titleTV = (TextView) rootView.findViewById(R.id.add_new_comment_title);
            titleTV.setText("New answer");
        }

        /* layout */
        final LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.add_new_comment_card_layout);
        layout.bringToFront();

        /* content */
        contentTV = (EditText) rootView.findViewById(R.id.add_new_comment_content);
    }

}
