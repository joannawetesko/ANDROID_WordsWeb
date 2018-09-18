package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;

/**
 * Created by wewe on 12.11.16.
 */
public class QuizBasicDto implements Serializable {

    private String id;
    private int[] levels;
    private int[] listIds;
    private boolean isFinished;
    private String direction;
    private String type;

    public QuizBasicDto(String id, int[] levels, int[] listIds, boolean isFinished, String direction, String type) {
        this.id = id;
        this.levels = levels;
        this.listIds = listIds;
        this.isFinished = isFinished;
        this.direction = direction;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public int[] getLevels() {
        return levels;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public String getDirection() {
        return direction;
    }

    public String getType() {
        return type;
    }

    public int[] getListIds() {
        return listIds;
    }
}
