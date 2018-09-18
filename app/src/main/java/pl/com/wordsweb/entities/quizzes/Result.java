package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;

/**
 * Created by wewe on 12.11.16.
 */
public class Result implements Serializable {
    private int correct;
    private int incorrect;

    public Result(int correct, int incorrect) {
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }
}
