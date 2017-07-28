package com.editor.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * This class provide map of all kinds of brackets
 */
public class Brackets {
    private final Map<Character, Boolean> bracketsDirection = new HashMap<>();
    private final Map<Character, Character> bracketsOpposite = new HashMap<>();

    {
        bracketsDirection.put('(', true);
        bracketsDirection.put(')', false);

        bracketsDirection.put('{', true);
        bracketsDirection.put('}', false);

        bracketsDirection.put('[', true);
        bracketsDirection.put(']', false);

        putCrossOpposite('(', ')');
        putCrossOpposite('[', ']');
        putCrossOpposite('{', '}');
    }

    private void putCrossOpposite(char front, char opposite) {
        bracketsOpposite.put(front, opposite);
        bracketsOpposite.put(opposite, front);
    }

    public Map<Character, Boolean> getBracketsDirection() {
        return bracketsDirection;
    }

    public Map<Character, Character> getBracketsOpposite() {
        return bracketsOpposite;
    }
}
