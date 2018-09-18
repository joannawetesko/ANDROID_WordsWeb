package pl.com.wordsweb.entities.quizzes;

import java.io.Serializable;

/**
 * Created by wewe on 12.11.16.
 */
public class VocabularyList implements Serializable {
    private int id;
    private String name;

    public VocabularyList(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
