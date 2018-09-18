package pl.com.wordsweb.event;

import pl.com.wordsweb.entities.quizzes.QuizToLearn;

/**
 * Created by wewe on 12.11.16.
 */

public class OnRestQuizToLearnGetEvent {
    private QuizToLearn quizToLearn;

    public OnRestQuizToLearnGetEvent(QuizToLearn quizToLearn) {
        this.quizToLearn = quizToLearn;
    }

    public QuizToLearn getQuizToLearn() {
        return quizToLearn;
    }
}
