package pl.com.wordsweb.entities;

import java.io.Serializable;

/**
 * Created by jwetesko on 02.05.16.
 */
public class PhraseInfo implements Serializable {

    private Integer id;
    private Integer phraseUseId;
    private String partOfSpeach;
    private Boolean isIdiom;
    private String description;

    public PhraseInfo(String partOfSpeach, Boolean isIdiom, String description) {
        this.id = null;
        this.phraseUseId = null;
        this.partOfSpeach = partOfSpeach;
        this.isIdiom = isIdiom;
        this.description = description;
    }

    public PhraseInfo() {
    }

    public int getId() { return this.id; }

    public void setId(int id) { this.id = id; }

    public int getPhraseUseId() {
        return this.phraseUseId;
    }

    public void setPhraseUseId(int phraseUseId) { this.phraseUseId = phraseUseId; }

    public String getPartOfSpeach() {
        return this.partOfSpeach;
    }

    public void setPartOfSpeach(String partOfSpeach) { this.partOfSpeach = partOfSpeach; }

    public Boolean getIsIdiom() {
        return this.isIdiom;
    }

    public void setIsIdiom(Boolean isIdiom) { this.isIdiom = isIdiom; }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) { this.description = description; }

}
