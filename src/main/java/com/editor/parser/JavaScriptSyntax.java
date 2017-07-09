package main.java.com.editor.parser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class helps highlight the syntax of a JavaScript
 */
public class JavaScriptSyntax {
    //TODO: move to separate file
    public static final String RESERVED_WORDS = "var|new|class|this|super|implements|extends|interface|import|function|public|default|private|protected|abstract|static|final|finally|void|null|int|double|long|byte|native|char|float|short|enum|synchronized|true|false|volatile|goto|while|throws|boolean|export|in|try|catch|delete|throw|with|while|for|continue|return|else|typOf|instanceOf|break|do|if|package|transient";

    public List<CommonSyntaxHighlight> makeHighlights(String input) {
        List<CommonSyntaxHighlight> highlights = new ArrayList<>();
       // highlights.addAll(getReservedWordsHighlight(input));
        return highlights;
    }

    public Map<Integer,CommonSyntaxHighlight> getReservedWordsHighlight(String input) {
        Map<Integer, CommonSyntaxHighlight> reservedWordsHighlights = new TreeMap<>();
        Matcher matcher = Pattern.compile(CommonRegex.BEFORE_REGEX + RESERVED_WORDS + CommonRegex.AFTER_REGEX).matcher(input);
        while(matcher.find()){
            int startIndex = matcher.start();
            reservedWordsHighlights.put(startIndex, new CommonSyntaxHighlight(matcher.start(), Color.PINK,matcher.end()));

        }
        return reservedWordsHighlights;
    }
}
