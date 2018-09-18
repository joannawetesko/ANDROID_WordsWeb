package pl.com.wordsweb.entities.comments;

/**
 * Created by ewelinabukowska on 08.01.2017.
 */

public class CheckListObject {
    private String id;
    private int userId;
    private GetCheckListContent content;

    public CheckListObject() {
    }

    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public GetCheckListContent getContent() {
        return content;
    }
}
