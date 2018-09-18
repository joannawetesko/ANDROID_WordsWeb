package pl.com.wordsweb.ui.fragments;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

import pl.com.wordsweb.R;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.quizzes.Expression;
import pl.com.wordsweb.entities.quizzes.Pair;
import pl.com.wordsweb.entities.quizzes.Quiz;
import pl.com.wordsweb.entities.quizzes.QuizAnswer;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;
import pl.com.wordsweb.event.OnRestQuizAnswerSendEvent;
import pl.com.wordsweb.event.OnRestQuizToLearnGetEvent;
import pl.com.wordsweb.storage.DbProvider;
import pl.com.wordsweb.ui.activities.MainActivity;

import static pl.com.wordsweb.config.Constants.BASE_PHOTO_URL;
import static pl.com.wordsweb.config.Constants.PHOTO_FILE_EXTENSION;
import static pl.com.wordsweb.config.Constants.QUIZ_ARGUMENT;
import static pl.com.wordsweb.config.Constants.QUIZ_ID;
import static pl.com.wordsweb.config.Constants.QUIZ_MODE_WRITE;
import static pl.com.wordsweb.config.Constants.QUIZ_SEND_ANSWER_ERROR;
import static pl.com.wordsweb.config.Constants.QUIZ_TO_LEARN_ARGUMENT;

/**
 * A simple {@link Fragment} subclass.
 */
public class LearnFragment extends Fragment {


    int quizCounter = 0;
    boolean isBackVisible = false;
    private View rootView;
    private TextView learnCurrentPhraseFront;
    private TextView learnCurrentExampleFront;
    private TextView learnCurrentListNameFront;
    private TextView learnCurrentLevelFront;
    private TextView learnCurrentPhraseBack;
    private TextView learnCurrentExampleBack;
    private TextView learnCurrentListNameBack;
    private TextView learnCurrentLevelBack;
    private FloatingActionButton noBtn;
    private FloatingActionButton yesBtn;
    private Button currentAnswerCheckBtn;
    private EditText currentAnswerET;
    private ProgressBar progressBar;
    private QuizToLearn quizToLearn;
    private ArrayList<Pair> quizPairs;
    private String quizInProgressId;
    private String directions;
    private boolean isWriteMode = false;
    private Boolean isPhraseOne = null;

    public LearnFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        directions = DbProvider.getDirections(getContext());

        if (getArguments() != null) {
            if (getArguments().containsKey(QUIZ_MODE_WRITE)) {
                isWriteMode = getArguments().getBoolean(QUIZ_MODE_WRITE);
            }
            if (getArguments().getSerializable(QUIZ_ARGUMENT) != null) {
                // first start of quiz
                Quiz quiz = (Quiz) getArguments().getSerializable(QUIZ_ARGUMENT);
                System.out.println(quiz);
                System.out.println(directions);
                if (quiz != null) ((MainActivity) getActivity()).postQuiz(quiz);

            } else if (getArguments().getSerializable(QUIZ_TO_LEARN_ARGUMENT) != null) {
                //click again on CongratsFragment
                quizToLearn = (QuizToLearn) getArguments().getSerializable(QUIZ_TO_LEARN_ARGUMENT);
                quizPairs = quizToLearn.getPairs();
                quizInProgressId = quizToLearn.getQuizBasicDto().getId();
                quizCounter = 0;
            } else if (getArguments().getString(QUIZ_ID) != null) {
                String quizId = getArguments().getString(QUIZ_ID);
                ((MainActivity) getActivity()).getQuiz(quizId);
            }

        } else {
            //selected list are empty or error
            //TODO show empty view ( user information)
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isWriteMode) {
            rootView = inflater.inflate(R.layout.fragment_learn_write, container, false);
            initUIWrite();
            if (quizPairs != null) fillTextViewsWrite();
        } else {
            rootView = inflater.inflate(R.layout.fragment_learn_flashcards, container, false);
            initUIFlipCards();
            if (quizPairs != null) fillTextViewsFlashcards();
        }
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_progressBar);
        if (quizToLearn == null) progressBar.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).hideFab();


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideFab();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).showFab();
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
    public void onBackPressedEvent() {
        getActivity().getSupportFragmentManager().popBackStack();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestQuizToLearnGetEvent(OnRestQuizToLearnGetEvent event) {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        quizToLearn = event.getQuizToLearn();
        quizPairs = quizToLearn.getPairs();
        quizInProgressId = quizToLearn.getQuizBasicDto().getId();
        if (isWriteMode) {
            fillTextViewsWrite();
        } else {

            fillTextViewsFlashcards();
        }
        quizCounter = 0;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestQuizAnswerAccepted(OnRestQuizAnswerSendEvent event) {
        if (event.isResponseOk()) {
            quizCounter++;

            if (quizCounter < quizPairs.size()) {
                if (isWriteMode) {
                    fillTextViewsWrite();
                }
                else {
                    fillTextViewsFlashcards();
                }
            }
            else {
                Bundle arg = new Bundle();
                arg.putString(QUIZ_ID, quizInProgressId);
                ((MainActivity) getActivity()).openFragmentWithBack(new LearnCongratsFragment(), arg);
            }

        } else {
            Toast toast = Toast.makeText(getContext(), QUIZ_SEND_ANSWER_ERROR, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void performFlip() {

        AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flip_right_out);
        AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.flip_left_in);

        LinearLayout front = (LinearLayout) rootView.findViewById(R.id.flip_card_layout_front);
        LinearLayout back = (LinearLayout) rootView.findViewById(R.id.flip_card_layout_back);

        if(!isBackVisible){
            setRightOut.setTarget(front);
            setLeftIn.setTarget(back);
            setRightOut.start();
            setLeftIn.start();
            isBackVisible = true;
        }
        else{
            setRightOut.setTarget(back);
            setLeftIn.setTarget(front);
            setRightOut.start();
            setLeftIn.start();
            isBackVisible = false;
        }
    }


    private void initUIWrite() {

        /* card */
        final TableLayout layout = (TableLayout) rootView.findViewById(R.id.learn_card_layout);
        layout.bringToFront();

        /* textviews */
        learnCurrentPhraseFront = (TextView) rootView.findViewById(R.id.learn_current_phrase_front);
        learnCurrentExampleFront = (TextView) rootView.findViewById(R.id.learn_current_example_front);
        learnCurrentLevelFront = (TextView) rootView.findViewById(R.id.learn_current_list_level_front);
        learnCurrentListNameFront = (TextView) rootView.findViewById(R.id.learn_current_list_name_front);

        /*edittext*/
        currentAnswerET = (EditText) rootView.findViewById(R.id.current_answer_edit_text);

        /*button*/
        currentAnswerCheckBtn = (Button) rootView.findViewById(R.id.current_answer_check_button);

        currentAnswerCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String currentAnswer = currentAnswerET.getText().toString();

                if (directions.equals("FIRST")) {
                    writeFirst(currentAnswer);
                }
                else if (directions.equals("SECOND")) {
                    writeSecond(currentAnswer);
                }
                else if (directions.equals("BOTH")) {
                    writeBoth(currentAnswer);
                }
            }
        });
    }

    private void writeFirst(String currentAnswer) {
        if (currentAnswer.equals(quizPairs.get(quizCounter).getExpressionTwo().getPhrase())) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Great!", Toast.LENGTH_SHORT);
            toast.show();
            sendAnswer(true, quizPairs.get(quizCounter).getExpressionOne().getId(), quizPairs.get(quizCounter).getId());
        }
        else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Sorry, bad answer :(", Toast.LENGTH_SHORT);
            toast.show();
            sendAnswer(false, quizPairs.get(quizCounter).getExpressionOne().getId(), quizPairs.get(quizCounter).getId());
        }
    }

    private void writeSecond(String currentAnswer) {

        if (currentAnswer.equals(quizPairs.get(quizCounter).getExpressionOne().getPhrase())) {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Great!", Toast.LENGTH_SHORT);
            toast.show();
            sendAnswer(true, quizPairs.get(quizCounter).getExpressionTwo().getId(), quizPairs.get(quizCounter).getId());
        }
        else {
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Sorry, bad answer :(", Toast.LENGTH_SHORT);
            toast.show();
            sendAnswer(false, quizPairs.get(quizCounter).getExpressionTwo().getId(), quizPairs.get(quizCounter).getId());
        }
    }

    private void writeBoth(String currentAnswer) {

        if (isPhraseOne == true) {
            writeFirst(currentAnswer);
        }
        else if (isPhraseOne == false) {
            writeSecond(currentAnswer);
        }
    }



    private void initUIFlipCards() {
        /* card */
        final TableLayout layout = (TableLayout) rootView.findViewById(R.id.learn_card_layout);
        layout.bringToFront();

        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                performFlip();
            }
        });

        /* textviews */
        learnCurrentPhraseFront = (TextView) rootView.findViewById(R.id.learn_current_phrase_front);
        learnCurrentExampleFront = (TextView) rootView.findViewById(R.id.learn_current_example_front);
        learnCurrentLevelFront = (TextView) rootView.findViewById(R.id.learn_current_list_level_front);
        learnCurrentListNameFront = (TextView) rootView.findViewById(R.id.learn_current_list_name_front);
        learnCurrentExampleBack = (TextView) rootView.findViewById(R.id.learn_current_example_back);
        learnCurrentLevelBack = (TextView) rootView.findViewById(R.id.learn_current_list_level_back);
        learnCurrentPhraseBack = (TextView) rootView.findViewById(R.id.learn_current_phrase_back);
        learnCurrentListNameBack = (TextView) rootView.findViewById(R.id.learn_current_list_name_back);

        /* buttons */
        noBtn = (FloatingActionButton) rootView.findViewById(R.id.fab_learn_no);
        yesBtn = (FloatingActionButton) rootView.findViewById(R.id.fab_learn_yes);

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBackVisible) {
                    performFlip();
                }
                if (directions != null) {
                    if (directions.equals("FIRST")) {
                        sendAnswer(false, quizPairs.get(quizCounter).getExpressionOne().getId(), quizPairs.get(quizCounter).getId());
                    } else if (directions.equals("SECOND")) {
                        sendAnswer(false, quizPairs.get(quizCounter).getExpressionTwo().getId(), quizPairs.get(quizCounter).getId());
                    } else if (directions.equals("BOTH")) {
                        if (isPhraseOne == true) {
                            sendAnswer(false, quizPairs.get(quizCounter).getExpressionOne().getId(), quizPairs.get(quizCounter).getId());
                        } else if (isPhraseOne == false) {
                            sendAnswer(false, quizPairs.get(quizCounter).getExpressionTwo().getId(), quizPairs.get(quizCounter).getId());
                        }
                    }
                }
            }
        });

        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isBackVisible) {
                    performFlip();
                }

                if (directions.equals("FIRST")) {
                    sendAnswer(true, quizPairs.get(quizCounter).getExpressionOne().getId(), quizPairs.get(quizCounter).getId());
                }
                else if (directions.equals("SECOND")) {
                    sendAnswer(true, quizPairs.get(quizCounter).getExpressionTwo().getId(), quizPairs.get(quizCounter).getId());
                }
                else if (directions.equals("BOTH")) {
                    if (isPhraseOne == true) {
                        sendAnswer(true, quizPairs.get(quizCounter).getExpressionOne().getId(), quizPairs.get(quizCounter).getId());
                    }
                    else if (isPhraseOne == false) {
                        sendAnswer(true, quizPairs.get(quizCounter).getExpressionTwo().getId(), quizPairs.get(quizCounter).getId());
                    }
                }

            }
        });
    }

    private void sendAnswer(boolean isCorrect, int expressionId, int pairId) {
        QuizAnswer quizAnswer = new QuizAnswer(isCorrect, expressionId, pairId);
        ((MainActivity) getActivity()).sendQuizAnswer(quizAnswer, quizInProgressId);
    }

    private void fillTextViewsFlashcards() {

        if (quizCounter > quizPairs.size())
        {
            quizCounter = 0;
        }

        noBtn.setVisibility(View.VISIBLE);
        yesBtn.setVisibility(View.VISIBLE);

       if (directions.equals("FIRST")) {
            initFront(quizPairs.get(quizCounter).getExpressionOne());
            initBack(quizPairs.get(quizCounter).getExpressionTwo());

            loadImage(quizPairs.get(quizCounter).getExpressionOne().getLanguage().getFlag(), R.id.learn_current_flag_front);
            loadImage(quizPairs.get(quizCounter).getExpressionTwo().getLanguage().getFlag(), R.id.learn_current_flag_back);
       }
       else if (directions.equals("SECOND")) {

            initFront(quizPairs.get(quizCounter).getExpressionTwo());
            initBack(quizPairs.get(quizCounter).getExpressionOne());

            loadImage(quizPairs.get(quizCounter).getExpressionTwo().getLanguage().getFlag(), R.id.learn_current_flag_front);
            loadImage(quizPairs.get(quizCounter).getExpressionOne().getLanguage().getFlag(), R.id.learn_current_flag_back);
        }
        else if (directions.equals("BOTH")) {

           Random rn = new Random();
           int answer = rn.nextInt(10) + 1;

           if (answer % 2 == 0) {

               isPhraseOne = true;
               initFront(quizPairs.get(quizCounter).getExpressionOne());
               initBack(quizPairs.get(quizCounter).getExpressionTwo());

               loadImage(quizPairs.get(quizCounter).getExpressionOne().getLanguage().getFlag(), R.id.learn_current_flag_front);
               loadImage(quizPairs.get(quizCounter).getExpressionTwo().getLanguage().getFlag(), R.id.learn_current_flag_back);

           } else {

               isPhraseOne = false;
               initFront(quizPairs.get(quizCounter).getExpressionTwo());
               initBack(quizPairs.get(quizCounter).getExpressionOne());

               loadImage(quizPairs.get(quizCounter).getExpressionTwo().getLanguage().getFlag(), R.id.learn_current_flag_front);
               loadImage(quizPairs.get(quizCounter).getExpressionOne().getLanguage().getFlag(), R.id.learn_current_flag_back);
           }
       }
    }

    private void fillTextViewsWrite() {

        if (quizCounter >= quizPairs.size())
        {
            quizCounter = 0;
        }

        currentAnswerCheckBtn.setVisibility(View.VISIBLE);
        currentAnswerET.setText("");

        if (directions.equals("FIRST")) {
            initFront(quizPairs.get(quizCounter).getExpressionOne());
            loadImage(quizPairs.get(quizCounter).getExpressionOne().getLanguage().getFlag(), R.id.learn_current_flag_front);
        } else if (directions.equals("SECOND")) {

            initFront(quizPairs.get(quizCounter).getExpressionTwo());
            loadImage(quizPairs.get(quizCounter).getExpressionTwo().getLanguage().getFlag(), R.id.learn_current_flag_front);
        } else if (directions.equals("BOTH")) {

            Random rn = new Random();
            int answer = rn.nextInt(10) + 1;

            if (answer % 2 == 0) {

                isPhraseOne = true;
                initFront(quizPairs.get(quizCounter).getExpressionOne());
                loadImage(quizPairs.get(quizCounter).getExpressionOne().getLanguage().getFlag(), R.id.learn_current_flag_front);

            } else {

                isPhraseOne = false;
                initFront(quizPairs.get(quizCounter).getExpressionTwo());
                loadImage(quizPairs.get(quizCounter).getExpressionTwo().getLanguage().getFlag(), R.id.learn_current_flag_front);
            }
        }
    }

    private void loadImage(String flagLink, int viewId) {

        final ImageView imageView = (ImageView) rootView.findViewById(viewId);
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

    private void initFront(Expression x) {
        learnCurrentPhraseFront.setText(x.getPhrase());
        learnCurrentExampleFront.setText(x.getExample());
        learnCurrentListNameFront.setText(quizPairs.get(quizCounter).getVocabularyList().getName());
        learnCurrentLevelFront.setText(Integer.toString(x.getKnowledgeLevel()));
    }

    private void initBack(Expression x) {
        learnCurrentPhraseBack.setText(x.getPhrase());
        learnCurrentExampleBack.setText(x.getExample());
        learnCurrentListNameBack.setText(quizPairs.get(quizCounter).getVocabularyList().getName());
        learnCurrentLevelBack.setText(Integer.toString(x.getKnowledgeLevel()));
    }
}
