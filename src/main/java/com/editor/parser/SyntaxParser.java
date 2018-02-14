package com.editor.parser;

import com.editor.model.TextEditorModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class helps highlight syntax and line comments of languages
 */
public class SyntaxParser {

    private static SyntaxType CurrentSyntax = SyntaxType.TEXT;
    private static String CurrentSyntaxRegex = Regexp.PLAIN_TEXT_WORDS;

    public static SyntaxType getCurrentSyntax() {
        return CurrentSyntax;
    }

    public static void setCurrentSyntax(SyntaxType syntax) {
        CurrentSyntax = syntax;

        switch (syntax) {
            case TEXT:
                CurrentSyntaxRegex = Regexp.PLAIN_TEXT_WORDS;
                break;
            case JAVASCRIPT:
                CurrentSyntaxRegex = Regexp.JS_RESERVED_WORDS;
                break;
            case HASKELL:
                CurrentSyntaxRegex = Regexp.HASKELL_RESERVED_WORDS;
                break;
            case ERLANG:
                CurrentSyntaxRegex = Regexp.ERLANG_RESERVED_WORDS;
                break;
        }
    }

    public static void setCurrentSyntaxByFileExtension(String extension) {
        for (SyntaxType syntax : SyntaxType.values()) {
            if (syntax.getFileExtension().equals(extension)) {
                setCurrentSyntax(syntax);
                break;
            } else {
                setCurrentSyntax(SyntaxType.TEXT);
            }
        }
    }

    public static boolean isTextSyntax() {
        return getCurrentSyntax() == SyntaxType.TEXT;
    }

    public List<CommonSyntaxHighlight> getReservedWordsHighlightByIndexes(TextEditorModel model, int startRow, int endRow) {
        List<CommonSyntaxHighlight> reservedWordsHighlights = new ArrayList<>();
        if (!isTextSyntax()) {
            List<StringBuilder> lineBuilders = model.getLineBuilders();
            for (int i = startRow; i <= endRow; i++) {
                StringBuilder stringBuilder = lineBuilders.get(i);
                String input = stringBuilder.toString();
                Matcher matcher = Pattern.compile(Regexp.BEFORE_REGEX + CurrentSyntaxRegex + Regexp.AFTER_REGEX).matcher(input);
                while (matcher.find()) {
                    reservedWordsHighlights.add(new CommonSyntaxHighlight(i, matcher.start(), matcher.end() - 1));

                }
            }
            reservedWordsHighlights.sort(Comparator.comparing(CommonSyntaxHighlight::getRowIndex).thenComparing(CommonSyntaxHighlight::getStartIndex));
        }
        return reservedWordsHighlights;
    }

    public List<CommentsHighlight> getLineCommentsHighlight(TextEditorModel model, int startRow, int endRow) {
        List<CommentsHighlight> commentsHighlights = new ArrayList<>();
        if (CurrentSyntax != SyntaxType.TEXT) {
            String lineCommentSymbols = CurrentSyntax.getLineComments();
            List<StringBuilder> lineBuilders = model.getLineBuilders();
            for (int i = startRow; i <= endRow; i++) {
                StringBuilder stringBuilder = lineBuilders.get(i);
                String input = stringBuilder.toString();
                int firstIndex = input.indexOf(lineCommentSymbols);
                if (firstIndex != -1) {
                    commentsHighlights.add(new CommentsHighlight(i, firstIndex, stringBuilder.length()));
                }
            }
            commentsHighlights.sort(Comparator.comparing(CommentsHighlight::getRowIndex));
        }
        return commentsHighlights;
    }
}
