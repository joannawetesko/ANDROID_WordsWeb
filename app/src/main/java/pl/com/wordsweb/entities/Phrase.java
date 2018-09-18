package pl.com.wordsweb.entities;

import java.io.Serializable;

/**
 * Created by jwetesko on 02.05.16.
 */
public class Phrase implements Serializable {

    private Integer id;
    private String content;

    public Phrase(String content) {
        this.id = null;
        this.content = content;
    }

    public Phrase(String content, int id) {
        this.id = id;
        this.content = content;
    }

    public Phrase() {
    }
    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return this.getContent();
    }

}
