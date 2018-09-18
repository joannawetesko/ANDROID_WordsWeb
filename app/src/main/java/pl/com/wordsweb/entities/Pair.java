package pl.com.wordsweb.entities;

import java.io.Serializable;

/**
 * Created by jwetesko on 02.05.16.
 */
public class Pair implements Serializable {

    private Integer id;
    private PhraseUse phraseUseOne;
    private PhraseUse phraseUseTwo;

    public Pair(PhraseUse phraseUseOne, PhraseUse phraseUseTwo) {
        this.id = null;
        this.phraseUseOne = phraseUseOne;
        this.phraseUseTwo = phraseUseTwo;
    }


    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public PhraseUse getPhraseUseOne() {
        return this.phraseUseOne;
    }

    public void setPhraseUseOne(PhraseUse phraseUseOne) { this.phraseUseOne = phraseUseOne; }

    public PhraseUse getPhraseUseTwo() {
        return this.phraseUseTwo;
    }

    public void setPhraseUseTwo(PhraseUse phraseUseTwo) { this.phraseUseTwo = phraseUseTwo; }

    @Override
    public String toString() {
        return "Pair{" +
                "id=" + id +
                ", phraseUseOne=" + phraseUseOne +
                ", phraseUseTwo=" + phraseUseTwo +
                '}';
    }
}
