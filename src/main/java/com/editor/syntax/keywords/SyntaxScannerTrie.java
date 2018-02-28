package com.editor.syntax.keywords;

import com.editor.model.rope.Rope;
import com.editor.syntax.SyntaxSetter;
import com.editor.syntax.SyntaxType;
import com.editor.system.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Prefix Tree for register list of keywords.
 * Also helps to find reserved words in rope.
 */
public class SyntaxScannerTrie {
    static class TrieNode {

        Map<Character, TrieNode> children = new TreeMap<>();
        boolean isLeaf;
    }

    private Stack<PairedBracketsInfo> openBracketsStack;

    //Store keywords indexes
    private Map<Integer, TokenType> keywordsIndexesMap;

    //Store paired brackets indexes
    private Map<Integer, PairedBracketsInfo> bracketsIndexesMap;
    private char currentChar;
    private int currentIndex;
    private int currentRopeIndex;
    private Rope rope;
    private TrieNode root;

    SyntaxScannerTrie() {
        root = new TrieNode();
    }

    private void clearAll() {
        currentIndex = 0;
        keywordsIndexesMap = new HashMap<>();
        bracketsIndexesMap = new HashMap<>();
        openBracketsStack = new Stack<>();
    }

    public Map<Integer, PairedBracketsInfo> getBracketsIndexesMap() {
        return bracketsIndexesMap;
    }

    public boolean isEmpty() {
        return root.children.size() == 0;
    }

    // Using for register keywords of language
    void registerReservedWord(String string) {
        TrieNode node = root;
        for (char ch : string.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        node.isLeaf = true;
    }

    public Map<Integer, TokenType> getKeywordsIndexes(Rope visibleRope, int currentRopeIndex) {
        clearAll();
        this.currentRopeIndex = currentRopeIndex;
        rope = visibleRope;
        while (currentIndex < rope.getLength()) {
            currentChar = rope.charAt(currentIndex);
            scanSymbol();
            if (isAtEnd()) {
                break;
            }
        }
        return keywordsIndexesMap;
    }

    private char moveIterator() {
        if (!isAtEnd()) {
            currentIndex++;
            currentRopeIndex++;
            if (!isAtEnd()) {
                currentChar = rope.charAt(currentIndex);
            }
        }

        return currentChar;
    }

    private boolean match(char expected) {
        currentChar = moveIterator();
        if (currentChar == Constants.NEW_LINE_CHAR) {
            return false;
        }
        if (currentChar != expected) {
            return false;
        }
        return true;
    }

    private boolean isAtEndOfLine() {
        return currentChar == Constants.NEW_LINE_CHAR;
    }

    private boolean isAtEnd() {
        return currentIndex >= rope.getLength();
    }

    private void string() {
        int startIndex = currentIndex;
        moveIterator();
        // Only for single line strings for now
        while (currentChar != '"' && !isAtEndOfLine() && !isAtEnd()) {
            moveIterator();
        }
        Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, currentIndex).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.STRING));
        keywordsIndexesMap.putAll(keywordIndexes);
        moveIterator();
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z');
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void identifier() {
        TrieNode node = root;
        int startIndex = currentIndex;

        // See if identifier is a reserved word
        while (!node.isLeaf) {
            if (!node.children.containsKey(currentChar) || !isAlpha(currentChar)) {
                break;
            } else {
                node = node.children.get(currentChar);
                moveIterator();
            }
        }
        if (node.isLeaf && (!isAlpha(currentChar) || ((isAlpha(currentChar)) && isAtEnd()))) {
            Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, currentIndex - 1).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.RESERVED_WORD));
            keywordsIndexesMap.putAll(keywordIndexes);
            return;
        }

        while ((isAlpha(currentChar) || isDigit(currentChar)) && !isAtEnd()) {
            moveIterator();
        }
    }

    void number() {
        while (isDigit(currentChar) && !isAtEnd()) {
            keywordsIndexesMap.put(currentIndex, TokenType.DIGIT);
            moveIterator();
        }
    }

    private void scanSymbol() {
        switch (currentChar) {
            //add highlighting
            case '(':
            case ')':
                moveIterator();
                break;
            case '{':
                pushBracketToStack();
                moveIterator();
                break;
            case '}':
                findOpenedPair();
                moveIterator();
                break;
            case '/':
                if (isJsSyntax() && match('/')) {
                    comment(2);
                } else {
                    moveIterator();
                }
                break;
            case '-':
                if (isHsSyntax() && match('-')) {
                    comment(2);
                } else {
                    moveIterator();
                }
                break;
            case '%':
                if (isErSyntax()) {
                    comment(1);
                } else {
                    moveIterator();
                }
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(currentChar)) {
                    number();
                    break;
                } else if (isAlpha(currentChar)) {
                    identifier();
                    break;
                }
                moveIterator();
                break;
        }
    }

    private boolean isJsSyntax() {
        return SyntaxSetter.getCurrentSyntax() == SyntaxType.JAVASCRIPT;
    }

    private boolean isHsSyntax() {
        return SyntaxSetter.getCurrentSyntax() == SyntaxType.HASKELL;
    }

    private boolean isErSyntax() {
        return SyntaxSetter.getCurrentSyntax() == SyntaxType.ERLANG;
    }

    private void comment(int commentSymbolsNumber) {
        int startIndex = currentIndex - commentSymbolsNumber;
        // A comment goes until the end of the line
        while (!isAtEndOfLine() && !isAtEnd()) {
            moveIterator();
        }
        Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, currentIndex).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.COMMENT));
        keywordsIndexesMap.putAll(keywordIndexes);
    }

    private void findOpenedPair() {
        if (!openBracketsStack.isEmpty()) {
            PairedBracketsInfo bracketInfo = openBracketsStack.pop();
            bracketInfo.setEndInd(currentRopeIndex);
            bracketsIndexesMap.put(bracketInfo.getStartInd(), bracketInfo);
            bracketsIndexesMap.put(bracketInfo.getEndInd(), bracketInfo);
        }

    }

    private void pushBracketToStack() {
        PairedBracketsInfo bracketInfo = new PairedBracketsInfo(currentRopeIndex);
        openBracketsStack.push(bracketInfo);
    }
}
