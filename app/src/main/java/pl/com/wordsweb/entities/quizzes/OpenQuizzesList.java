package pl.com.wordsweb.entities.quizzes;

import java.util.ArrayList;

/**
 * Created by wewe on 26.12.16.
 */

public class OpenQuizzesList {
    ArrayList<QuizToLearn> openQuizzes;
    ArrayList<QuizToLearn> finishedQuizzes;

    OpenQuizzesList(ArrayList<QuizToLearn> openQuizzes, ArrayList<QuizToLearn> finishedQuizzes) {
        this.openQuizzes = openQuizzes;
        this.finishedQuizzes = finishedQuizzes;
    }

    public ArrayList<QuizToLearn> getOpenQuizzes() {
        return openQuizzes;
    }

    public ArrayList<QuizToLearn> getFinishedQuizzes() {
        return finishedQuizzes;
    }
}
