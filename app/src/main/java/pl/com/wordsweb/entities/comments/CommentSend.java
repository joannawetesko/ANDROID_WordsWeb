package pl.com.wordsweb.entities.comments;

/**
 * Created by Wewe on 2017-01-08.
 */

public class CommentSend {
    private String elementType;
    private String elementId;
    private String content;

    public CommentSend() {
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType(String elementType) {
        this.elementType = elementType;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
