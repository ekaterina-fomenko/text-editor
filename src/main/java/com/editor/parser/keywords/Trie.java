package com.editor.parser.keywords;

import com.editor.model.rope.RopeIterator;
import com.editor.parser.SyntaxParser;
import com.editor.parser.SyntaxType;
import com.editor.system.Constants;

import java.util.HashMap;
import java.util.Map;
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

    private final Map<Integer, TokenType> keywordsIndexesSet = new HashMap<>();
    private RopeIterator iterator;
    private int startLine;
    private int endLine;
    private Character currentChar;
    TrieNode root = new TrieNode();


    public void setIterator(RopeIterator iterator) {
        this.iterator = iterator;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public boolean isEmpty() {
        return root.children.size() == 0;
    }

    public void put(String string) {
        TrieNode node = root;
        for (char ch : string.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                node.children.put(ch, new TrieNode());
            }
            node = node.children.get(ch);
        }
        node.isLeaf = true;
    }

    public boolean find(String string) {
        TrieNode node = root;
        for (char ch : string.toLowerCase().toCharArray()) {
            if (!node.children.containsKey(ch)) {
                return false;
            } else {
                node = node.children.get(ch);
            }
        }
        if (node.isLeaf) {
            return true;
        }
        return false;
    }

    public Map<Integer, TokenType> getKeywordsIndexes(int startLineInd, int endLineInd) {
        startLine = startLineInd;
        endLine = endLineInd;
        while (iterator.hasNext() && startLine < endLine) {
            currentChar = iterator.next();
            scanSymbol();
        }
        return keywordsIndexesSet;
    }

    private void moveIterator() {
        if (!isAtEnd()) {
            currentChar = iterator.next();
        }
    }

    private boolean match(char expected) {
        currentChar = iterator.next();
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
        return !iterator.hasNext();
    }

    private void string() {
        int startIndex = iterator.getPos();
        moveIterator();
        // Only for single line strings for now
        while (currentChar != '"' && !isAtEndOfLine()) {
            if (currentChar == '\n') {
                startLine++;
            }
            moveIterator();
        }
        Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, iterator.getPos()).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.STRING));
        keywordsIndexesSet.putAll(keywordIndexes);
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
        int startIndex = iterator.getPos();

        // See if the identifier is a reserved word.
        while (!node.isLeaf) {
            if (!node.children.containsKey(currentChar) || !isAlpha(currentChar)) {
                break;
            } else {
                node = node.children.get(currentChar);
                moveIterator();
            }
        }
        if (node.isLeaf && !isAlpha(currentChar)) {
            Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, iterator.getPos()).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.RESERVED_WORD));
            keywordsIndexesSet.putAll(keywordIndexes);
            return;
        }

        while (isAlpha(currentChar) || isDigit(currentChar)) {
            moveIterator();
        }
    }

    void number() {
        while (isDigit(currentChar)) {
            keywordsIndexesSet.put(iterator.getPos(), TokenType.DIGIT);
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
                moveIterator();
                break;
            case '}':
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
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                startLine++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(currentChar)) {
                    number();
                } else if (isAlpha(currentChar)) {
                    identifier();
                }
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
        int startIndex = iterator.getPos() - commentSymbolsNumber;
        // A comment goes until the end of the line.
        while (currentChar != '\n' && !isAtEndOfLine()) {
            moveIterator();
        }
        Map<Integer, TokenType> keywordIndexes = IntStream.rangeClosed(startIndex, iterator.getPos()).boxed().collect(Collectors.toMap(Function.identity(), v -> TokenType.COMMENT));
        keywordsIndexesSet.putAll(keywordIndexes);
    }
}
