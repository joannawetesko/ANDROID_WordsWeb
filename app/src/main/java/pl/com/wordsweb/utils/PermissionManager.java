package pl.com.wordsweb.utils;

import java.util.ArrayList;

import pl.com.wordsweb.entities.comments.CheckListObject;
import pl.com.wordsweb.entities.comments.GetCheckListResponse;

/**
 * Created by ewelinabukowska on 08.01.2017.
 */

public class PermissionManager {
    public static boolean checkCanListXontentAdd(int listId, GetCheckListResponse getCheckListResponse) {
        ArrayList<CheckListObject> objects = getCheckListResponse.getContent();
        for (CheckListObject object : objects) {
            if (object.getContent().getListId() == listId) {
                if (object.getContent().isCanAddContent()) {
                    return true;
                }
            }
        }
        return false;
    }
}
