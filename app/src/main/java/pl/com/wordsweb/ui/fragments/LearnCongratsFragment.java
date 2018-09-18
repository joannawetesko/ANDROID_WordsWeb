package pl.com.wordsweb.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import pl.com.wordsweb.R;
import pl.com.wordsweb.config.AppSettings;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;
import pl.com.wordsweb.event.OnBackStackEvent;
import pl.com.wordsweb.event.OnRestQuizToLearnGetEvent;
import pl.com.wordsweb.ui.activities.MainActivity;

import static android.view.View.VISIBLE;
import static pl.com.wordsweb.config.Constants.QUIZ_ID;
import static pl.com.wordsweb.config.Constants.QUIZ_TO_LEARN_ARGUMENT;


public class LearnCongratsFragment extends Fragment {

    View rootView;
    QuizToLearn quizWithResult;
    TextView rightAnswersTV;
    TextView wrongAnswersTV;
    TextView rightAnswersDescriptionTV;
    TextView wrongAnswersDescriptionTV;
    private String TAG = "LEarnCongratsFragment";

    public LearnCongratsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String quizId = getArguments().getString(QUIZ_ID);
            if (quizId != null) ((MainActivity) getActivity()).getQuiz(quizId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_congrats_learn, container, false);
        initUI();

        return rootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnRestQuizToLearnGetEvent(OnRestQuizToLearnGetEvent event) {
        quizWithResult = event.getQuizToLearn();
        Log.d("Result ", quizWithResult.getResult().getCorrect() + " ");
        // trzeba pomyslec o zablokowaniu przyciskow i wstrzymaniu ich do pobrania danych
        // Generalnie wszedzie bedzie trzeba takie rzeczy posprawdzac jak jzu skonczymy

        rightAnswersTV.setText(Integer.toString(quizWithResult.getResult().getCorrect()));
        wrongAnswersTV.setText(Integer.toString(quizWithResult.getResult().getIncorrect()));
        rightAnswersDescriptionTV.setVisibility(VISIBLE);
        wrongAnswersDescriptionTV.setVisibility(VISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBackPressedEvent(OnBackStackEvent event) {
        Log.d(TAG, "onBackPressed ");
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
        ((MainActivity) getActivity()).hideFab();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((MainActivity) getActivity()).showFab();
    }


    private void initUI() {

        /* card */
        TableLayout layout = (TableLayout) rootView.findViewById(R.id.congrats_card_layout);
        layout.bringToFront();

        /* numbers */
        rightAnswersTV = (TextView) rootView.findViewById(R.id.right_answers_text_view);
        wrongAnswersTV = (TextView) rootView.findViewById(R.id.wrong_answers_text_view);
        rightAnswersDescriptionTV = (TextView) rootView.findViewById(R.id.right_answers_description_text_view);
        wrongAnswersDescriptionTV = (TextView) rootView.findViewById(R.id.wrong_answers_description_text_view);

        /* buttons */
        Button goBackBtn = (Button) rootView.findViewById(R.id.congrats_button_go_back);

        Button quizBtn = (Button) rootView.findViewById(R.id.congrats_button_quiz);

        Button againBtn = (Button) rootView.findViewById(R.id.congrats_button_again);

        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getActivity().getSupportFragmentManager().popBackStack();
                // TODO do naprawy sypie sie
                ((MainActivity) getActivity()).openFragment(new LearnViewPagerFragment());
            }
        });

        quizBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openFragment(new MyQuizzesListFragment());
            }
        });

        againBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quizWithResult != null && quizWithResult.getPairs() != null) {
                    if (quizWithResult.getPairs().size() > 0) {
                        Bundle arg = new Bundle();
                        arg.putSerializable(QUIZ_TO_LEARN_ARGUMENT, quizWithResult);
                        ((MainActivity) getActivity()).openFragment(new LearnFragment(), arg);
                    } else {
                        //TODO komunikat ze nie ma pytan do powtorki
                        Toast.makeText(getContext(), "You finished this test. No phrases available!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }
}

