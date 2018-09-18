package pl.com.wordsweb.entities.comments;

/**
 * Created by Wewe on 2017-01-08.
 */

public class CommentObject {
    private Comment comment;
    private EvaluationContext evaluationContext;
    private AnswersContext answersContext;

    public CommentObject() {
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public EvaluationContext getEvaluationContext() {
        return evaluationContext;
    }

    public void setEvaluationContext(EvaluationContext evaluationContext) {
        this.evaluationContext = evaluationContext;
    }

    public AnswersContext getAnswersContext() {
        return answersContext;
    }

    public void setAnswersContext(AnswersContext answersContext) {
        this.answersContext = answersContext;
    }
}
