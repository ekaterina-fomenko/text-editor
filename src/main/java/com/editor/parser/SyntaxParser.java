package com.editor.parser;

/**
 * This class helps highlight syntax and line comments of languages
 */
public class SyntaxParser {

    private static SyntaxType CurrentSyntax = SyntaxType.TEXT;

    public static SyntaxType getCurrentSyntax() {
        return CurrentSyntax;
    }

    public static void setCurrentSyntax(SyntaxType syntax) {
        CurrentSyntax = syntax;
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
}
