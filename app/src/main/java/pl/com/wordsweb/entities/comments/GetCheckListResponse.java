package pl.com.wordsweb.entities.comments;

import java.util.ArrayList;

/**
 * Created by ewelinabukowska on 08.01.2017.
 */

public class GetCheckListResponse {

    private ArrayList<CheckListObject> content;
    private boolean last;
    private int totalElements;
    private int totalPages;
    private int size;
    private int number;
    private boolean first;
    private int numberOfElements;

    public GetCheckListResponse() {
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public boolean isLast() {
        return last;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getSize() {
        return size;
    }

    public int getNumber() {
        return number;
    }

    public boolean isFirst() {
        return first;
    }

    public ArrayList<CheckListObject> getContent() {
        return content;
    }
}
