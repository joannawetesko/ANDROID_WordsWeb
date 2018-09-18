package pl.com.wordsweb.event;

import pl.com.wordsweb.entities.comments.GetCheckListResponse;

/**
 * Created by ewelinabukowska on 08.01.2017.
 */

public class OnCheckUserEvent {
    private GetCheckListResponse checkListResponse;

    public OnCheckUserEvent(GetCheckListResponse checkListResponse) {
        this.checkListResponse = checkListResponse;
    }

    public GetCheckListResponse getCheckListResponse() {
        return checkListResponse;
    }
}
