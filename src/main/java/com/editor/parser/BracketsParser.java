package com.editor.parser;

import com.editor.model.TextEditorModel;

import java.util.*;

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

    public List<BracketModel> getBracketsHighlighting(TextEditorModel model) {

        List<StringBuilder> lines = model.getLineBuilders();

        Stack<BracketModel> stackOfBraces = new Stack<>();
        Stack<BracketModel> stackOfRoundBrackets = new Stack<>();
        Stack<BracketModel> stackOfSquareBrackets = new Stack<>();

        List<BracketModel> result = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            StringBuilder line = lines.get(i);

            for (int j = 0; j < line.length(); j++) {
                char ch = line.charAt(j);

                if (setOfOpenedBrackets.contains(ch)) {
                    switch (ch) {
                        case '{':
                            stackOfBraces.add(new BracketModel(i, j, -1));
                            break;
                        case '(':
                            stackOfRoundBrackets.add(new BracketModel(i, j, -1));
                            break;
                        case '[':
                            stackOfSquareBrackets.add(new BracketModel(i, j, -1));
                            break;
                    }
                } else if (setOfClosedBrackets.contains(ch)) {
                    switch (ch) {
                        case '}':
                            processClosedBracket(stackOfBraces, result, i, ch);
                            break;
                        case ')':
                            processClosedBracket(stackOfRoundBrackets, result, i, ch);
                            break;
                        case ']':
                            processClosedBracket(stackOfSquareBrackets, result, i, ch);
                            break;
                    }
                }
            }
        }
        result.addAll(stackOfBraces);
        result.addAll(stackOfRoundBrackets);
        result.addAll(stackOfSquareBrackets);
        result.sort(Comparator.comparing(BracketModel::getRowFirstBracket).thenComparing(BracketModel::getFirstIndex));
        return result;
    }

    private static void processClosedBracket(Stack<BracketModel> stack, List<BracketModel> result, int row, int column) {
        if (stack.isEmpty()) {
            result.add(new BracketModel(row, column, -1));
        } else {
            BracketModel bracket = stack.pop();
            bracket.setSecondIndex(column);
            bracket.setRowSecondBracket(row);
            result.add(bracket);
        }
    }
}
