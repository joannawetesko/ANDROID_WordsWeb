package pl.com.wordsweb.entities.quizzes;

/**
 * Created by wewe on 10.12.16.
 */

public class Mode {
    private boolean flashcard = false;
    private boolean write = false;

    public Mode() {

    }

    public Mode(boolean flashcard, boolean write) {
        this.flashcard = flashcard;
        this.write = write;
    }

    public boolean isFlashcard() {
        return flashcard;
    }

    public boolean isWrite() {
        return write;
    }
}
