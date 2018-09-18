package pl.com.wordsweb.event;

import pl.com.wordsweb.entities.LessonList;

/**
 * Created by Wewe on 2016-12-30.
 */

public class OnCommentClickButtonEvent {
    private LessonList lessonList;

    public OnCommentClickButtonEvent(LessonList lessonList) {
        this.lessonList = lessonList;
    }

    public LessonList getLessonList() {
        return lessonList;
    }
}
