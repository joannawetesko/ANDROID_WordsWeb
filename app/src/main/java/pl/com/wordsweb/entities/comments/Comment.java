package pl.com.wordsweb.entities.comments;

import java.io.Serializable;

/**
 * Created by Wewe on 2016-12-30.
 */

public class Comment implements Serializable {
    private String id;
    private Author author;
    private String content;
    private long creationDate;

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
