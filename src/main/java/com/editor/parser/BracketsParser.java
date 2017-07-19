package com.editor.parser;

import com.editor.model.TextEditorModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class BracketsParser {

    public static final char OPENED_BRACE = '{';
    public static final char CLOSED_BRACE = '}';
    public static final char OPENED_ROUND_BRACKET = '(';
    public static final char CLOSED_ROUND_BRACKET = ')';
    public static final char OPENED_SQUARE_BRACKET = '[';
    public static final char CLOSED_SQUARE_BRACKET = ']';

    public boolean hasPair = false;

    public static Set<Character> setOfOpenedBrackets;
    public static Set<Character> setOfClosedBrackets;

    public BracketsParser() {
        setOfOpenedBrackets = new HashSet<>();
        setOfClosedBrackets = new HashSet<>();

        setOfOpenedBrackets.add(OPENED_BRACE);
        setOfOpenedBrackets.add(OPENED_ROUND_BRACKET);
        setOfOpenedBrackets.add(OPENED_SQUARE_BRACKET);

        setOfOpenedBrackets.add(CLOSED_BRACE);
        setOfOpenedBrackets.add(CLOSED_ROUND_BRACKET);
        setOfOpenedBrackets.add(CLOSED_SQUARE_BRACKET);
    }

    private static List<CommonSyntaxHighlight> getBracketsHighlighting(TextEditorModel model) {

        List<StringBuilder> lines = model.getLineBuilders();

        Stack<CommonSyntaxHighlight> stackOfBraces = new Stack<>();
        Stack<CommonSyntaxHighlight> stackOfRoundBrackets = new Stack<>();
        Stack<CommonSyntaxHighlight> stackOfSquareBrackets = new Stack<>();

        List<CommonSyntaxHighlight> result = new java.util.ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            StringBuilder line = lines.get(i);

            for (int j = 0; j < line.length(); j++) {
                char ch = line.charAt(j);

                if (setOfOpenedBrackets.contains(ch)) {
                    switch (ch) {
                        case '{':
                            stackOfBraces.add(new CommonSyntaxHighlight(i, j, -1));
                            break;
                        case '(':
                            stackOfRoundBrackets.add(new CommonSyntaxHighlight(i, j, -1));
                            break;
                        case '[':
                            stackOfSquareBrackets.add(new CommonSyntaxHighlight(i, j, -1));
                            break;
                    }
                } else if (setOfClosedBrackets.contains(ch)) {
                    switch (ch) {
                        case '}':
                            processClosedBracket(stackOfBraces, result, i, ch);
                        case ')':
                            processClosedBracket(stackOfRoundBrackets, result, i, ch);
                            break;
                        case ']':
                            processClosedBracket(stackOfSquareBrackets, result, i, ch);
                    }
                }
            }
        }
        result.addAll(stackOfBraces);
        result.addAll(stackOfRoundBrackets);
        result.addAll(stackOfSquareBrackets);
        return result;
    }

    private static void processClosedBracket(Stack<CommonSyntaxHighlight> stack, List<CommonSyntaxHighlight> result, int row, int column) {
        if (stack.isEmpty()) {
            result.add(new CommonSyntaxHighlight(row, -1, column));
        } else {
            CommonSyntaxHighlight bracket = stack.pop();
            bracket.setEndIndex(column);
            bracket.setRowEndIndex(row);
            result.add(bracket);
        }
    }
}
