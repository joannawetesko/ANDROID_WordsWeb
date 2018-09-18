package pl.com.wordsweb.entities.comments;

/**
 * Created by ewelinabukowska on 08.01.2017.
 */

public class GetCheckListContent {
    private int listId;
    private boolean canDelete;
    private boolean canDeleteContent;
    private boolean canAddContent;
    private boolean isOwner;
    private boolean canEdit;

    public GetCheckListContent() {
    }

    public int getListId() {
        return listId;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public boolean isCanDeleteContent() {
        return canDeleteContent;
    }

    public boolean isCanAddContent() {
        return canAddContent;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public boolean isCanEdit() {
        return canEdit;
    }
}
