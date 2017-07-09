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

    public List<CommonSyntaxHighlight> makeHighlights(String input) {
        List<CommonSyntaxHighlight> highlights = new ArrayList<>();
       // highlights.addAll(getReservedWordsHighlight(input));
        return highlights;
    }

    public Map<Integer,CommonSyntaxHighlight> getReservedWordsHighlight(String input) {
        Map<Integer, CommonSyntaxHighlight> reservedWordsHighlights = new TreeMap<>();
        Matcher matcher = Pattern.compile(Regex.BEFORE_REGEX + Regex.JS_RESERVED_WORDS + Regex.AFTER_REGEX).matcher(input);
        while(matcher.find()){
            int startIndex = matcher.start();
            reservedWordsHighlights.put(startIndex, new CommonSyntaxHighlight(matcher.start(), Color.PINK,matcher.end()));

        }
        return reservedWordsHighlights;
    }
}
