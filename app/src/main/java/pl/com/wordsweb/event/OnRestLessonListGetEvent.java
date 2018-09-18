package pl.com.wordsweb.event;

import java.util.ArrayList;

import pl.com.wordsweb.entities.LessonList;

/**
 * Created by wewe on 29.10.16.
 */

public class OnRestLessonListGetEvent {
    private ArrayList<LessonList> lessonList;

    public OnRestLessonListGetEvent(ArrayList<LessonList> lessonList) {
        this.lessonList = lessonList;
    }

    public ArrayList<LessonList> getLessonList() {
        return this.lessonList;
    }


}

