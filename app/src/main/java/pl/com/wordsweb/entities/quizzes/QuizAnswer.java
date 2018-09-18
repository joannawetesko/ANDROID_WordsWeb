package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;

/**
 * Created by wewe on 12.11.16.
 */

public class QuizAnswer implements Serializable {

    private Boolean correctness;
    private int expressionId;
    private int pairId;

    public QuizAnswer(Boolean correctness, int expressionId, int pairId) {
        this.correctness = correctness;
        this.expressionId = expressionId;
        this.pairId = pairId;
    }

    public Boolean getCorrectness() {
        return correctness;
    }

    public int getExpressionId() {
        return expressionId;
    }

    public int getPairId() {
        return pairId;
    }
}
