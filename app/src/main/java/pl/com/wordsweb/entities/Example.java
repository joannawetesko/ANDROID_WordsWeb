package pl.com.wordsweb.entities;

import java.io.Serializable;

/**
 * Created by jwetesko on 02.05.16.
 */
public class Example implements Serializable {

    private Integer id;
    private String content;

    public Example(String content) {
        this.id = null;
        this.content = content;
    }

    public Example() {
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) { this.content = content; }
}
