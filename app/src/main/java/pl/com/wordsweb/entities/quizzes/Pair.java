package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;

/**
 * Created by wewe on 12.11.16.
 */

public class Pair implements Serializable {
    private int id;
    private VocabularyList vocabularyList;
    private Expression expressionOne;
    private Expression expressionTwo;

    public Pair(int id, VocabularyList vocabularyList, Expression expressionOne, Expression expressionTwo) {
        this.id = id;
        this.vocabularyList = vocabularyList;
        this.expressionOne = expressionOne;
        this.expressionTwo = expressionTwo;
    }

    public int getId() {
        return id;
    }

    public VocabularyList getVocabularyList() {
        return vocabularyList;
    }

    public Expression getExpressionOne() {
        return expressionOne;
    }

    public Expression getExpressionTwo() {
        return expressionTwo;
    }
}
