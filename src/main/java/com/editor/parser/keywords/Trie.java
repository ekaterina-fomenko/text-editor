package com.editor.parser.keywords;

import com.editor.model.rope.Rope;
import com.editor.parser.SyntaxParser;
import com.editor.parser.SyntaxType;
import com.editor.system.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Prefix Tree for constructing reserved words
 */
public class Trie {
    static class TrieNode {

        Map<Character, TrieNode> children = new TreeMap<>();
        boolean isLeaf;
    }

    private Stack<BracketInfo> openBracketsStack;
    private Map<Integer, TokenType> keywordsIndexesMap;
    private Map<Integer, BracketInfo> bracketsIndexesMap;
    private char currentChar;
    private int currentIndex;
    private TrieNode root = new TrieNode();
    private int currentRopeIndex;
    private Rope rope;

    public Map<Integer, BracketInfo> getBracketsIndexesMap() {
        return bracketsIndexesMap;
    }

    public boolean isEmpty() {
        return root.children.size() == 0;
    }

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

    public Map<Integer, TokenType> getKeywordsIndexes(Rope visibleRope) {
        this.currentRopeIndex = currentRopeIndex;
        // todo: move to constructor
        keywordsIndexesMap = new HashMap<>();
        bracketsIndexesMap = new HashMap<>();
        openBracketsStack = new Stack<>();
        rope = visibleRope;
        currentIndex = 0;
        while (currentIndex < rope.getLength()) {
            currentChar = rope.charAt(currentIndex);
            scanSymbol();
            // if (isAtEndOfLine()) {
            //     break;
            // }
            if (isAtEnd()) {
                break;
            }
        }
        return keywordsIndexesMap;
    }

    private char moveIterator() {
        if (!isAtEnd()) {
            currentIndex++;
            currentChar = rope.charAt(currentIndex);
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
        return currentIndex >= rope.getLength() - 1;
    }

    private void string() {
        int startIndex = currentIndex;
        moveIterator();
        // Only for single line strings for now
        while (currentChar != '"' && !isAtEndOfLine()) {
            moveIterator();
        }
        Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, currentIndex).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.STRING));
        keywordsIndexesMap.putAll(keywordIndexes);
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
        if (node.isLeaf && !isAlpha(currentChar)) {
            Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, currentIndex - 1).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.RESERVED_WORD));
            keywordsIndexesMap.putAll(keywordIndexes);
            return;
        }

        while (isAlpha(currentChar) || isDigit(currentChar)) {
            moveIterator();
        }
    }

    void number() {
        while (isDigit(currentChar)) {
            keywordsIndexesMap.put(currentIndex, TokenType.DIGIT);
            moveIterator();
        }
    }

    private void scanSymbol() {
        switch (currentChar) {
            case '(':
                moveIterator();
                break;
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
            case ',':
                moveIterator();
                break;
            case '.':
                moveIterator();
                break;
            case '+':
                moveIterator();
                break;
            case ';':
                moveIterator();
                break;
            case '*':
                moveIterator();
                break;
            case '!':
                moveIterator();
                break;
            case '=':
                moveIterator();
                break;
            case '<':
                moveIterator();
                break;
            case '>':
                moveIterator();
                break;
            //for js only
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
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                moveIterator();
                break;

            case '\n':
                //startLine++;  --- need for cases with several lines
                moveIterator();
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
        return SyntaxParser.getCurrentSyntax() == SyntaxType.JAVASCRIPT;
    }

    private boolean isHsSyntax() {
        return SyntaxParser.getCurrentSyntax() == SyntaxType.HASKELL;
    }

    private boolean isErSyntax() {
        return SyntaxParser.getCurrentSyntax() == SyntaxType.ERLANG;
    }

    private void comment(int commentSymbolsNumber) {
        int startIndex = currentIndex - commentSymbolsNumber;
        // A comment goes until the end of the line.
        while (currentChar != '\n' && !isAtEndOfLine()) {
            moveIterator();
        }
        Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, currentIndex).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.COMMENT));
        keywordsIndexesMap.putAll(keywordIndexes);
    }

    private void findOpenedPair() {
        ;
        if (!openBracketsStack.isEmpty()) {
            BracketInfo bracketInfo = openBracketsStack.pop();
            bracketInfo.setEndInd(currentIndex);
            bracketsIndexesMap.put(bracketInfo.getStartInd(), bracketInfo);
            bracketsIndexesMap.put(bracketInfo.getEndInd(), bracketInfo);
        }

    }

    private void pushBracketToStack() {
        BracketInfo bracketInfo = new BracketInfo(currentChar, currentIndex);
        openBracketsStack.push(bracketInfo);
    }
}
