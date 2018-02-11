package com.editor.parser.keywords;

import com.editor.model.rope.RopeIterator;
import com.editor.parser.CommonSyntaxHighlight;
import com.editor.system.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Prefix Tree for constructing reserved words
 */
public class Trie {

    static class TrieNode {

        Map<Character, TrieNode> children = new TreeMap<>();
        boolean isLeaf;
    }

    private final List<CommonSyntaxHighlight> keywordsIndexesList = new ArrayList<>();
    private RopeIterator iterator;
    private int startLine;
    private Character currentChar;
    TrieNode root = new TrieNode();


    public void setIterator(RopeIterator iterator) {
        this.iterator = iterator;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
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

    public List<CommonSyntaxHighlight> getKeywordsIndexes(int startLine, int endLine) {
        TrieNode node = root;
        while (iterator.hasNext() && startLine < endLine) {
            currentChar = iterator.next();
            scanSymbol();
        }
        return keywordsIndexesList;
    }

    private void moveIterator() {
        currentChar = iterator.next();
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

    private boolean isAtEnd() {
        return currentChar == Constants.NEW_LINE_CHAR;
    }

    private void string() {
        while (currentChar != '"' && !isAtEnd()) {
            if (currentChar == '\n') {
                startLine++;
            }
            currentChar = iterator.next();
        }
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
            CommonSyntaxHighlight keywordIndexes = new CommonSyntaxHighlight(startIndex, iterator.getPos());
            keywordsIndexesList.add(keywordIndexes);
            return;
        }

        while (isAlpha(currentChar) || isDigit(currentChar)) {
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
            case '-':
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
                if (match('/')) {
                    // A comment goes until the end of the line.
                    while (currentChar != '\n' && !isAtEnd()) {
                        moveIterator();
                    }
                } else {
                    moveIterator();
                }
                break;
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
                    moveIterator();
                } else if (isAlpha(currentChar)) {
                    identifier();
                }
                break;
        }
    }
}
