package pl.com.wordsweb.event;

import pl.com.wordsweb.entities.comments.GetCommentResponse;

/**
 * Created by Wewe on 2017-01-08.
 */

public class OnRestCommentGetEvent {
    private GetCommentResponse getCommentResponse;

    public OnRestCommentGetEvent(GetCommentResponse getCommentResponse) {
        this.getCommentResponse = getCommentResponse;
    }

    public GetCommentResponse getGetCommentResponse() {
        return getCommentResponse;
    }
}
