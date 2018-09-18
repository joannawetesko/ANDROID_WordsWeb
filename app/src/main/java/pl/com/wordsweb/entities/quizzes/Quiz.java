package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;

/**
 * Created by wewe on 06.11.16.
 */

public class Quiz implements Serializable {

    private Integer[] listIds;
    private Integer[] levels;
    private String direction;
    // change when api will support more type
    private String type = "SIMPLE";

    public Quiz(Integer[] listIds, Integer[] levels, String direction) {
        this.listIds = listIds;
        this.levels = levels;
        this.direction = direction;
    }

    public Integer[] getListIds() {
        return listIds;
    }

    public Integer[] getLevels() {
        return levels;
    }

    public String getDirection() {
        return direction;
    }
}
