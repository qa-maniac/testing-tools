package tools.text;

import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.spelling.SpellingCheckRule;

import java.io.IOException;
import java.util.List;


public class Spelling {

    private JLanguageTool tool;


    public Spelling(Language language) {
        tool = new JLanguageTool(language);
    }


    public String check(String text, IgnoredSpelling... ignoredSpellings) {

        assert ignoredSpellings.length <= 1;

        List<RuleMatch> matches = null;
        String result = "";

        if (ignoredSpellings.length > 0) {
            for (Rule rule : tool.getAllActiveRules()) {
                if (rule instanceof SpellingCheckRule) {
                    if (ignoredSpellings[0].getRulesToIgnore() != null) {
                        for (String ruleItem : ignoredSpellings[0].getRulesToIgnore()) {
                            tool.disableRule(ruleItem);
                        }
                    }
                    if (ignoredSpellings[0].getWordsToIgnore() != null) {
                        List<String> ignoringWords = ignoredSpellings[0].getWordsToIgnore();
                        ((SpellingCheckRule) rule).addIgnoreTokens(ignoringWords);
                    }
                }
            }
        }

        try {
            matches = tool.check(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (matches != null) {
            for (RuleMatch match : matches) {
                result += text.substring(match.getFromPos(), match.getToPos()) + ":\t" + match.getMessage() + ";\n";
            }
        }

        if (result.equals(""))
            return "OK";
        else return "\n[Mistake around word : mistake description]\n" + result;
    }
}