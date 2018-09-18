package pl.com.wordsweb.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jwetesko on 02.05.16.
 */
public class PhraseUse implements Serializable {

    private Integer id;
    private Language language;
    private Phrase phrase;
    private PhraseInfo phraseInfo;
    private ArrayList<Example> examples;

    public PhraseUse(Language language, Phrase phrase, PhraseInfo phraseInfo, ArrayList<Example> examples) {
        this.id = null;
        this.language = language;
        this.phrase = phrase;
        this.phraseInfo = phraseInfo;
        this.examples = examples;
    }

    public PhraseUse() {
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) { this.language = language; }

    public Phrase getPhrase() {
        return this.phrase;
    }

    public void setPhrase(Phrase phrase) { this.phrase = phrase; }

    public PhraseInfo getPhraseInfo() {
        return this.phraseInfo;
    }

    public void setPhraseInfo(PhraseInfo phraseInfo) { this.phraseInfo = phraseInfo; }

    public ArrayList<Example> getExamples() {
        return this.examples;
    }

    public void setExamples(ArrayList<Example> examples) { this.examples = examples; }

}