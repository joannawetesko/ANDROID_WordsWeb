package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by wewe on 06.11.16.
 */

public class QuizToLearn implements Serializable {

    private QuizBasicDto quizBasicDto;
    private long startDate;
    private long endDate = -1;
    private ArrayList<VocabularyList> vocabularyLists;
    private Result result;
    private ArrayList<Pair> pairs;

    public QuizToLearn(QuizBasicDto quizBasicDto, long startDate, long endDate, ArrayList<VocabularyList> vocabularyLists, Result result, ArrayList<Pair> pairs) {
        this.quizBasicDto = quizBasicDto;
        this.startDate = startDate;
        this.endDate = endDate;
        this.vocabularyLists = vocabularyLists;
        this.result = result;
        this.pairs = pairs;
    }

    public QuizBasicDto getQuizBasicDto() {
        return quizBasicDto;
    }

    public long getStartDate() {
        return startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public ArrayList<VocabularyList> getVocabularyLists() {
        return vocabularyLists;
    }

    public Result getResult() {
        return result;
    }

    public ArrayList<Pair> getPairs() {
        return pairs;
    }
}
