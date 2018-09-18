package pl.com.wordsweb.event;

import pl.com.wordsweb.entities.LessonList;

/**
 * Created by wewe on 29.10.16.
 */

public class OnRestCurrentLessonListGetEvent {

    private LessonList currentLessonList;

    public OnRestCurrentLessonListGetEvent(LessonList currentLessonList) {
        this.currentLessonList = currentLessonList;
    }

    public LessonList getCurrentLessonList() {
        return this.currentLessonList;
    }
}
