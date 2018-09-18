package pl.com.wordsweb.event;

/**
 * Created by wewe on 12.11.16.
 */

public class OnRestQuizAnswerSendEvent {
    private boolean responseOk;

    public OnRestQuizAnswerSendEvent(boolean responseOk) {
        this.responseOk = responseOk;
    }

    public boolean isResponseOk() {
        return responseOk;
    }
}
