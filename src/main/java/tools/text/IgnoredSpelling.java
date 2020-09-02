package tools.text;

import java.util.List;


public class IgnoredSpelling {

    private List<String> wordsToIgnore;
    private List<String> rulesToIgnore;


    public IgnoredSpelling(List<String> wordsToIgnore, List<String> rulesToIgnore) {
        this.wordsToIgnore = wordsToIgnore;
        this.rulesToIgnore = rulesToIgnore;
    }

    public List<String> getWordsToIgnore() {
        return wordsToIgnore;
    }

    public List<String> getRulesToIgnore() {
        return rulesToIgnore;
    }
}