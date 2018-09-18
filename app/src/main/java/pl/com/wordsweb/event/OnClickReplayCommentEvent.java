package pl.com.wordsweb.event;

import pl.com.wordsweb.entities.comments.Comment;

/**
 * Created by Wewe on 2017-01-08.
 */

public class OnClickReplayCommentEvent {
    Comment comment;

    public OnClickReplayCommentEvent(Comment comment) {
        this.comment = comment;
    }

    public Comment getComment() {
        return comment;
    }
}
