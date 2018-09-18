package pl.com.wordsweb.event;

import java.util.ArrayList;

import pl.com.wordsweb.entities.quizzes.OpenQuizzesList;
import pl.com.wordsweb.entities.quizzes.QuizToLearn;

/**
 * Created by wewe on 26.12.16.
 */

public class OnRestListQuizzesToLearnGetEvent {
    private ArrayList<QuizToLearn> quizToLearn;

    public OnRestListQuizzesToLearnGetEvent(OpenQuizzesList quizToLearnArrayList) {
        this.quizToLearn = quizToLearnArrayList.getOpenQuizzes();
        this.quizToLearn.addAll(quizToLearnArrayList.getFinishedQuizzes());
    }

    public ArrayList<QuizToLearn> getAllQuizToLearn() {
        return quizToLearn;
    }
}
