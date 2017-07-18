package com.editor.parser;

import com.editor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class helps highlight the syntax of a JavaScript
 */
public class SyntaxParser {

    private static String SYNTAX = Regexp.JS_RESERVED_WORDS;

    public static  void setSyntax(Syntax syntax) {
        switch (syntax) {
            case TEXT:
                SYNTAX = Regexp.PLAIN_TEXT_WORDS;
                break;
            case JAVASCRIPT:
                SYNTAX = Regexp.JS_RESERVED_WORDS;
                break;
            case HASKELL:
                SYNTAX = Regexp.HASKELL_RESERVED_WORDS;
                break;
            case ERLANG:
                SYNTAX = Regexp.ERLANG_RESERVED_WORDS;
                break;
        }

    }

    public List<CommonSyntaxHighlight> makeHighlights(String input) {
        List<CommonSyntaxHighlight> highlights = new ArrayList<>();
        return highlights;
    }

    public List<CommonSyntaxHighlight> getReservedWordsHighlight2(TextEditorModel model) {
        List<CommonSyntaxHighlight> reservedWordsHighlights = new ArrayList<>();
        ArrayList<StringBuilder> lineBuilders = model.getLineBuilders();
        for (int i = 0; i < lineBuilders.size(); i++) {
            StringBuilder stringBuilder = lineBuilders.get(i);
            String input = stringBuilder.toString();
            Matcher matcher = Pattern.compile(Regexp.BEFORE_REGEX + SYNTAX + Regexp.AFTER_REGEX).matcher(input);
            while (matcher.find()) {
                reservedWordsHighlights.add(new CommonSyntaxHighlight(i, matcher.start(), matcher.end() - 1));

            }
            reservedWordsHighlights.sort(Comparator.comparing(CommonSyntaxHighlight::getRowIndex).thenComparing(CommonSyntaxHighlight::getStartIndex));
        }
        return reservedWordsHighlights;
    }
}
