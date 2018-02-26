package com.editor.parser;

/**
 * This class provides different syntax for text-editor.
 * Also provides file extensions specific for each syntax.
 */

public enum SyntaxType {

    TEXT(""),
    JAVASCRIPT("js"),
    HASKELL("hs"),
    ERLANG("erl");

    private String fileExtension;

    SyntaxType(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
